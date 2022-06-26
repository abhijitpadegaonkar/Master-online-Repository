package com.ap.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ap.demo.model.Bill;
import com.ap.demo.model.Company;
import com.ap.demo.model.Item;
import com.ap.demo.model.Purchase;
import com.ap.demo.respository.BillRepository;
import com.ap.demo.respository.CompanyRepository;
import com.ap.demo.respository.ItemRepository;
import com.ap.demo.respository.PurchaseRepository;
import com.ap.demo.utils.Constants;

@Controller
public class BillController {

	private static final Logger logger = LoggerFactory.getLogger(BillController.class);

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Value("${app.temp.upload.dir}")
	public String appTempUploadDir;

	@GetMapping("/bill-master")
	public String showMasterPage() {
		logger.info("Showing master page.");
		return "bill-master";
	}

	@ResponseBody
	@GetMapping("/bills")
	public List<Bill> getBills() {
		logger.info("Fetching all bills details.");
		return billRepository.findAll();
	}

	@PostMapping("/remove-bill")
	@ResponseBody
	public boolean remove(@RequestParam(name = "id") Long billId) {
		logger.info("Processing request to remove bill. Bill Id: {}", billId);
		return billRepository.delete(billId);
	}

	@GetMapping("/update-bill")
	public String showUpdateBillForm(@RequestParam("id") Long billId) {
		logger.info("Showing update bill page.");
		billRepository.findById(billId);
		return "update-bill";
	}

	@PostMapping("/update-bill")
	@ResponseBody
	public String showUpdateBillForm(@RequestParam("id") Long billId, @RequestParam("username") String username,
			@RequestParam("compIec") String compIec, @RequestParam("partNumber") String partNumber,
			@RequestParam("typeOfBill") String typeOfBill, @RequestParam("partDesc") String partDesc,
			@RequestParam("uom") String uom, @RequestParam(name = "createdDate", required = false) String createdDate,
			@RequestParam(name = "updateDate", required = false) String updateDate) {

		logger.info("Handling update bill request. {}, {}, {}, {}, {}, {}, {}, {}", username, compIec, partNumber,
				typeOfBill, partDesc, uom, createdDate, updateDate);

		Bill bill = new Bill();
		bill.setId(billId);
		bill.setUsername(username);
		bill.setCompIec(compIec);
		bill.setPartDesc(partDesc);
		bill.setPartNumber(partNumber);
		bill.setUom(uom);

		billRepository.update(bill);
		return "update-bill";
	}

	@GetMapping("/add-bills")
	public String showAddBillsForm() {
		logger.info("Showing add bills page.");
		return "add-bills";
	}

	@PostMapping("/add-bills")
	public String addBills(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		logger.info("Handling bills creation via file upload request. Filename: {}", file.getOriginalFilename());

		List<Bill> bills;
		try {

			if (file.isEmpty() || file.getSize() > 2097152) {
				logger.info("Uploaded file is empty or exceeds 2MB max size allowed. Filename: {}",
						file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload a file. Max 2MB file size is allowed.");
				return "redirect:add-bills";
			}

			if (!file.getOriginalFilename().toLowerCase().endsWith(".xls")) {
				logger.info("Uploaded file is not a .xls file. Filename: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload .xls file.");
				return "redirect:add-bills";
			}

			Path copyLocation = Paths.get(appTempUploadDir + File.separator + UUID.randomUUID().toString()
					+ StringUtils.cleanPath(file.getOriginalFilename()));
			try {
				Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				logger.info("Unable to save file: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error",
						"Unable to save file: " + file.getOriginalFilename() + ". Please try again.");
				return "redirect:add-bills";
			}

			bills = parseBillsDataFromXlsFile(copyLocation.toFile());
			List<String> errors = new ArrayList<String>();
			List<String> infos = new ArrayList<String>();
			errors = validateBillsData(bills, "comp1");
			
			if (errors.isEmpty()) {
				for (Bill bill : bills) {
					System.out.println("Saving " + bill.getPurBatchNumber());
					try {
						billRepository.save(bill);
					} catch (DuplicateKeyException e) {
						logger.error("Cannot add bill: {}, Some of follwing data is duplicate: Production Batch Number.",
								bill.getProdBatchNumber(), e);
						errors.add("Cannot add bill: " + bill.getProdBatchNumber()
								+ ". Some of follwing data is duplicate: Production Batch Number.");
					}
				}
			}
			
			if (!infos.isEmpty()) {
				redirectAttributes.addFlashAttribute("infos", infos);
			}
			
			if (!errors.isEmpty()) {
				redirectAttributes.addFlashAttribute("errors", errors);
				return "redirect:/add-bills";
			}
		} catch (Exception e) {
			logger.error("Unable to upload file: {}", file.getOriginalFilename(), e);
			redirectAttributes.addFlashAttribute("error", "Unable to upload " + file.getOriginalFilename());
			return "redirect:/add-bills";
		}

		logger.info("Created bills via file upload: {}", file.getOriginalFilename());
		redirectAttributes.addFlashAttribute("info",
				"Created " + bills.size() + " bills via " + file.getOriginalFilename() + " file upload.");
		return "redirect:/add-bills";
	}
	
	private List<Bill> parseBillsDataFromXlsFile(File file) {

		Workbook workbook = null;
		// Create the Workbook
		try {
			workbook = WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | IOException e) {
			logger.error("Unable to process add bills file. Filename: {} ", file.getName(), e);
		}

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Getting number of columns in the Sheet
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		logger.info("Add bills file has {} sheets and sheet 0 has {} columns", workbook.getNumberOfSheets(),
				noOfColumns);

		// header list
		List<String> headers = new ArrayList<>();
		
		headers.add("username");
		headers.add("compIec");
		headers.add("partNumber");
		headers.add("partDesc");
		headers.add("uom");
		headers.add("recWastage");
		headers.add("irrecWastage"); 
		headers.add("netQty"); 
		headers.add("sourceOfMaterial"); 
		headers.add("typeOfMaterial"); 
		headers.add("remarks");
		headers.add("purBatchNumber"); 
		headers.add("prodBatchNumber");
		headers.add("hsnNumber");

		int begin = sheet.getFirstRowNum();
		int end = sheet.getLastRowNum();
		logger.info("Sheet has first row num: {}, last: {}", begin, end);
		List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
		boolean skipHeader = true;

		// loop each row
		for (int rowNum = begin; rowNum <= end; rowNum++) {
			Row row = sheet.getRow(rowNum);

			// Skip fist row as header
			if (skipHeader) {
				skipHeader = false;
				continue;
			}

			// identify empty row and add error for each empty row
			if (isEmptyRow(row)) {
				sheetData.add(null);
				logger.info("Empty row found. Row index: {}", rowNum);
				continue;
			}

			String columnName = null;
			String columnValue = null;
			Map<String, String> rowData = new LinkedHashMap<String, String>();

			// loop each cell in current row
			for (int i = 0; i < headers.size(); i++) {
				columnName = headers.get(i);
				Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

				switch (cell.getCellType()) {
				case STRING:
					columnValue = cell.getStringCellValue();
					break;
				case NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						columnValue = df.format(date);
					} else {
						cell.setCellType(CellType.STRING);
						columnValue = cell.getStringCellValue();
					}
					break;
				case BOOLEAN:
					columnValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case BLANK:
					columnValue = "";
					break;
				case _NONE:
					columnValue = null;
					break;
				case ERROR:
					columnValue = String.valueOf(cell.getErrorCellValue());
					break;
				case FORMULA:
					switch (cell.getCachedFormulaResultType()) {
					case NUMERIC:
					case STRING:
						cell.setCellType(CellType.STRING);
						columnValue = cell.getStringCellValue();
						break;
					default:
						break;
					}
				default:
					columnValue = "";
				}

				// add extracted cell data to row map
				rowData.put(columnName, columnValue);
			}

			// add extracted row data to sheep map
			sheetData.add(rowData);
		}

		// load extracted data into data objects
		List<Bill> bills = loadBillData(sheetData);

		// Closing the workbook
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bills;
	}

	private boolean isEmptyRow(Row row) {
		boolean isEmptyRow = true;
		if (row != null && row.getLastCellNum() > 0) {
			for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
				Cell cell = row.getCell(cellNum);
				if (cell != null && cell.getCellType() != CellType.BLANK) {
					isEmptyRow = false;
					break;
				}
			}
		}
		return isEmptyRow;
	}
	
	private List<Bill> loadBillData(List<Map<String, String>> sheetData) {

		List<Bill> bills = new ArrayList<Bill>();
		for (Map<String, String> row : sheetData) {
			Bill bill = null;
			if (row != null) {
				bill = new Bill(row.get("username"),
						row.get("compIec"),
						row.get("partNumber"),
						row.get("partDesc"),
						row.get("uom"),
						row.get("recWastage"),
						row.get("irrecWastage"), 
						row.get("netQty"), 
						row.get("sourceOfMaterial"), 
						row.get("typeOfMaterial"), 
						row.get("remarks"),
						row.get("purBatchNumber"),
						row.get("prodBatchNumber"),
						row.get("hsnNumber"));
			}
			
			bills.add(bill);
		}
		return bills;
	}
	
	private List<String> validateBillsData(List<Bill> bills, String username) {
		List<String> validationErrors = new ArrayList<String>();
		int rowCounter = 1;
		for (Bill bill : bills) {
			rowCounter++;
			List<String> errors = new ArrayList<String>();
			if (bill == null) {
				validationErrors.add("Row: " + rowCounter + " - No item data in file.");
				continue;
			}

			if (bill.getCompIec() == null || bill.getCompIec().equals("")) {
				errors.add("Row: " + rowCounter + " - IEC is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.IEC_REGEX, bill.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. IEC can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (bill.getPartNumber() == null || bill.getPartNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Part number is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.PART_NUMBER_REGEX, bill.getPartNumber())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Number. Part Number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (bill.getPartDesc() == null || bill.getPartDesc().equals("")) {
				errors.add("Row: " + rowCounter + " - Part description is required in Bill data");
			} else {
				if (!Constants.validateByRegex(Constants.PART_DESCRIPTION_REGEX, bill.getPartDesc())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Description. Part Description can contain alphanumeric and space characters and can be at max 40 characters in length.");
				}
			}

			if (bill.getUom() == null || bill.getUom().equals("")) {
				errors.add("Row: " + rowCounter + " - UOM is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.UOM_REGEX, bill.getUom())) {
					errors.add("Row: " + rowCounter + " - Invalid UOM. UOM can be either kg ot piece.");
				}
			}

			if (bill.getRecWastage() == null || bill.getRecWastage().equals("")) {
				errors.add("Row: " + rowCounter + " - Recoverable Wastage is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.QUANTITY_REGEX, bill.getRecWastage())) {
					errors.add("Row: " + rowCounter + " - Invalid Recoverable Wastage. Recoverable Wastage can contain numeric characters and can be max 10 characters in length.");
				}
			}
			
			if (bill.getIrrecWastage() == null || bill.getIrrecWastage().equals("")) {
				errors.add("Row: " + rowCounter + " - Irrecoverable Wastage is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.QUANTITY_REGEX, bill.getIrrecWastage())) {
					errors.add("Row: " + rowCounter + " - Invalid Irrecoverable Wastage. Irrecoverable Wastage can contain numeric characters and can be max 10 characters in length.");
				}
			}

			if (bill.getNetQty() == null || bill.getNetQty().equals("")) {
				errors.add("Row: " + rowCounter + " - Net Quantity is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.QUANTITY_REGEX, bill.getNetQty())) {
					errors.add("Row: " + rowCounter + " - Invalid Net Quantity. Net Quantity can contain numeric characters and can be max 10 characters in length.");
				}
			}
			
			if (bill.getSourceOfMaterial() == null || bill.getSourceOfMaterial().equals("")) {
				errors.add("Row: " + rowCounter + " - Source of material is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.SOURCE_OF_MATERIAL_REGEX, bill.getSourceOfMaterial())) {
					errors.add("Row: " + rowCounter + " - Invalid Source of material. Source of material can be either import or domestic.");
				}
			}
			
			if (bill.getTypeOfMaterial() == null || bill.getTypeOfMaterial().equals("")) {
				errors.add("Row: " + rowCounter + " - Type of material is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.TYPE_OF_MATERIAL_REGEX, bill.getTypeOfMaterial())) {
					errors.add("Row: " + rowCounter + " - Invalid Type of material. Type of material can contain alphanumeric characters and can be max 15 characters in length.");
				}
			}
			
			if (bill.getRemarks() == null || bill.getRemarks().equals("")) {
				errors.add("Row: " + rowCounter + " - Remark is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.REMARK_REGEX, bill.getRemarks())) {
					errors.add("Row: " + rowCounter + " - Invalid Remark. Remark  can contain alphanumeric and space characters and can be max 50 characters in length.");
				}
			}

			if (bill.getPurBatchNumber() == null || bill.getPurBatchNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase batch number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.PURCHASE_BATCH_NUMBER_REGEX, bill.getPurBatchNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Purchase batch number. Purchase batch number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (bill.getProdBatchNumber() == null || bill.getProdBatchNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Production batch number is required in Bill data.");
			} else {
				if (!Constants.validateByRegex(Constants.PRODUCTION_BATCH_NUMBER_REGEX, bill.getProdBatchNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Production batch number. Production batch number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (bill.getUsername() == null || bill.getUsername().equals("")) {
				errors.add("Row: " + rowCounter + " - Username is required in Item data.");
			} else {
				if (!Constants.validateByRegex(Constants.USERNAME_REGEX, bill.getUsername())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Username. Username can contain alphanumeric characters and can be at max 10 characters in length.");
				}
			}

			if (errors.isEmpty()) {
				String iec = companyRepository.getIecByUsername(bill.getUsername());
				if (iec == null || iec == "" || !iec.equalsIgnoreCase(bill.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. Provided IEC is not registered against the company.");
				}
			}
			
			if (errors.isEmpty()) {
				Item item = itemRepository.findByPartNumberAndIec(bill.getPartNumber(), bill.getCompIec());
				if (item == null || item.getPartNumber() == null || item.getPartNumber().equals("")) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Number. No item with this part number is registered for specified IEC.");
				} else {
					
					if (!item.getUom().equalsIgnoreCase(bill.getUom())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid UOM. Provided UOM should be same as that of registered for the item.");
					}
					
				}
				
				Company company = companyRepository.getCompanyDetailsByUsername(bill.getUsername());
				if (company == null) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company. Company username is not registered against the company.");
				} else {
					if (company.getCompIec() == null || company.getCompIec() == "" || !company.getCompIec().equalsIgnoreCase(bill.getCompIec())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid IEC. Provided IEC is not registered against the company.");
					}
				}
				
				Purchase purchase = purchaseRepository.findByPurchaseBatchNumber(bill.getPurBatchNumber());
				if (purchase == null || purchase.getPartNumber() == null || purchase.getPartNumber().equals("")) {
					errors.add("Row: " + rowCounter
							+ " - Invalid purchase batch number. No purchase with this purchase batch number is registered.");
				} else {
					
					if (!purchase.getPartNumber().equalsIgnoreCase(bill.getPartNumber())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid part number. Provided part number should be same as that of registered for the purchase batch.");
					}
					
					if (!purchase.getUom().equalsIgnoreCase(bill.getUom())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid UOM. Provided UOM should be same as that of registered for the purchase batch.");
					}
				}
			}

			validationErrors.addAll(errors);
		}

		return validationErrors;
	}
}
