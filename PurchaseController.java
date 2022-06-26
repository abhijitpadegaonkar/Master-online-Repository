package com.ap.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
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

import com.ap.demo.model.Item;
import com.ap.demo.model.Purchase;
import com.ap.demo.respository.CompanyRepository;
import com.ap.demo.respository.ItemRepository;
import com.ap.demo.respository.PurchaseRepository;
import com.ap.demo.utils.Constants;

@Controller
public class PurchaseController {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Value("${app.temp.upload.dir}")
	public String appTempUploadDir;

	@GetMapping("/purchase-master")
	public String showMasterPage() {
		logger.info("Showing master page.");
		return "purchase-master";
	}

	@ResponseBody
	@GetMapping("/purchases")
	public List<Purchase> getPurchases() {
		logger.info("Fetching all purchases details.");
		return purchaseRepository.findAll();
	}

	@PostMapping("/remove-purchase")
	@ResponseBody
	public boolean remove(@RequestParam(name = "id") Long purchaseId) {
		logger.info("Processing request to remove purchase. Purchase Id: {}", purchaseId);
		return purchaseRepository.delete(purchaseId);
	}

	@GetMapping("/update-purchase")
	public String showUpdatePurchaseForm(@RequestParam("id") Long purchaseId) {
		logger.info("Showing update purchase page.");
		purchaseRepository.findById(purchaseId);
		return "update-purchase";
	}

	@PostMapping("/update-purchase")
	@ResponseBody
	public String showUpdatePurchaseForm(@RequestParam("id") Long purchaseId, @RequestParam("username") String username,
			@RequestParam("compIec") String compIec, @RequestParam("partNumber") String partNumber,
			@RequestParam("typeOfPurchase") String typeOfPurchase, @RequestParam("partDesc") String partDesc,
			@RequestParam("uom") String uom, @RequestParam(name = "createdDate", required = false) String createdDate,
			@RequestParam(name = "updateDate", required = false) String updateDate) {

		logger.info("Handling update purchase request. {}, {}, {}, {}, {}, {}, {}, {}", username, compIec, partNumber,
				typeOfPurchase, partDesc, uom, createdDate, updateDate);

		Purchase purchase = new Purchase();
		purchase.setId(purchaseId);
		purchase.setUsername(username);
		purchase.setCompIec(compIec);
		purchase.setPartDesc(partDesc);
		purchase.setPartNumber(partNumber);
		purchase.setTypeOfPurchase(typeOfPurchase);
		purchase.setUom(uom);

		purchaseRepository.update(purchase);
		return "update-purchase";
	}

	@GetMapping("/add-purchases")
	public String showAddPurchasesForm() {
		logger.info("Showing add purchases page.");
		return "add-purchases";
	}

	@PostMapping("/add-purchases")
	public String addPurchases(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		logger.info("Handling purchases creation via file upload request. Filename: {}", file.getOriginalFilename());

		List<Purchase> purchases;
		try {

			if (file.isEmpty() || file.getSize() > 2097152) {
				logger.info("Uploaded file is empty or exceeds 2MB max size allowed. Filename: {}",
						file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload a file. Max 2MB file size is allowed.");
				return "redirect:add-purchases";
			}

			if (!file.getOriginalFilename().toLowerCase().endsWith(".xls")) {
				logger.info("Uploaded file is not a .xls file. Filename: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload .xls file.");
				return "redirect:add-purchases";
			}

			Path copyLocation = Paths.get(appTempUploadDir + File.separator + UUID.randomUUID().toString()
					+ StringUtils.cleanPath(file.getOriginalFilename()));
			try {
				Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				logger.info("Unable to save file: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error",
						"Unable to save file: " + file.getOriginalFilename() + ". Please try again.");
				return "redirect:add-purchases";
			}

			purchases = parsePurchasesDataFromXlsFile(copyLocation.toFile());
			List<String> errors = new ArrayList<String>();
			List<String> infos = new ArrayList<String>();
			errors = validatePurchasesData(purchases, "comp1");
			
			if (errors.isEmpty()) {
				for (Purchase purchase : purchases) {
					try {
						if (!purchaseRepository.isPurchasePresent(purchase)) {
							purchaseRepository.save(purchase);
							infos.add("Item added. Purchase batch number: " + purchase.getPurBatchNumber());
						} else {
							errors.add("Cannot add purchase. Purchase already present with this purchase batch number: "
									+ purchase.getPurBatchNumber());
						}
					} catch (DuplicateKeyException e) {
						logger.error("Cannot add purchase: {}, Some of follwing data is duplicate: Purchase Batch Number.",
								purchase.getPurBatchNumber(), e);
						errors.add("Cannot add purchase: " + purchase.getPurBatchNumber()
								+ ". Some of follwing data is duplicate: Purchase Batch Number.");
					}
				}
			}
			
			if (!infos.isEmpty()) {
				redirectAttributes.addFlashAttribute("infos", infos);
			}
			
			if (!errors.isEmpty()) {
				redirectAttributes.addFlashAttribute("errors", errors);
				return "redirect:/add-purchases";
			}
		} catch (Exception e) {
			logger.error("Unable to upload file: {}", file.getOriginalFilename(), e);
			redirectAttributes.addFlashAttribute("error", "Unable to upload " + file.getOriginalFilename());
			return "redirect:/add-purchases";
		}

		logger.info("Created purchases via file upload: {}", file.getOriginalFilename());
		redirectAttributes.addFlashAttribute("info",
				"Created " + purchases.size() + " purchases via " + file.getOriginalFilename() + " file upload.");
		return "redirect:/add-purchases";
	}
	
	private List<Purchase> parsePurchasesDataFromXlsFile(File file) throws ParseException {

		Workbook workbook = null;
		// Create the Workbook
		try {
			workbook = WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | IOException e) {
			logger.error("Unable to process add purchases file. Filename: {} ", file.getName(), e);
		}

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Getting number of columns in the Sheet
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		logger.info("Add purchases file has {} sheets and sheet 0 has {} columns", workbook.getNumberOfSheets(),
				noOfColumns);

		// header list
		List<String> headers = new ArrayList<>();
		headers.add("username");
		headers.add("compIec");
		headers.add("partNumber");
		headers.add("typeOfPurchase");
		headers.add("partDesc");
		headers.add("uom");
		headers.add("purBatchNumber");
		headers.add("grrQty");
		headers.add("grrNo");
		headers.add("purInvNo");
		headers.add("purInvDate");
		headers.add("inBondBoeNum");
		headers.add("inBondBoeDate");
		headers.add("portOfImport");
		headers.add("bottleSealNumber");
		headers.add("insuranceDetails");
		headers.add("perUnitCost");
		headers.add("totalPurValue");
		headers.add("dutyBcdRate");
		headers.add("dutyBcdAmt");
		headers.add("dutyCessBcdAmt");
		headers.add("dutyCessBcdRate");
		headers.add("dutyGstRate");
		headers.add("dutyGstAmt");
		headers.add("dutyCessGstRate");
		headers.add("dutyCessGstAmt");
		headers.add("dutyOtherRate");
		headers.add("dutyOtherAmt");
		headers.add("totalCustomDuty");
		headers.add("totalGst");
		headers.add("vehicleRegNum");
		headers.add("warehouseDate");
		headers.add("ewayBillNum");
		headers.add("ewayBillDate");
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
		List<Purchase> purchases = loadPurchaseData(sheetData);

		// Closing the workbook
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return purchases;
	}
	
	private List<Purchase> loadPurchaseData(List<Map<String, String>> sheetData) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		List<Purchase> purchases = new ArrayList<Purchase>();
		for (Map<String, String> row : sheetData) {
			Purchase purchase = null;
			if (row != null) {
				purchase = new Purchase(row.get("username"),
						row.get("compIec"),
						row.get("partNumber"),
						row.get("typeOfPurchase"),
						row.get("partDesc"),
						row.get("uom"),
						row.get("purBatchNumber"),
						row.get("grrQty"),
						row.get("grrNo"),
						row.get("purInvNo"),
						sdf.parse(row.get("purInvDate")),
						row.get("inBondBoeNum"),
						sdf.parse(row.get("inBondBoeDate")),
						row.get("portOfImport"),
						row.get("bottleSealNumber"),
						row.get("insuranceDetails"),
						row.get("perUnitCost"),
						row.get("totalPurValue"),
						row.get("dutyBcdRate"),
						row.get("dutyBcdAmt"),
						row.get("dutyCessBcdAmt"),
						row.get("dutyCessBcdRate"),
						row.get("dutyGstRate"),
						row.get("dutyGstAmt"),
						row.get("dutyCessGstRate"),
						row.get("dutyCessGstAmt"),
						row.get("dutyOtherRate"),
						row.get("dutyOtherAmt"),
						row.get("totalCustomDuty"),
						row.get("totalGst"),
						row.get("vehicleRegNum"),
						sdf.parse(row.get("warehouseDate")),
						row.get("ewayBillNum"),
						sdf.parse(row.get("ewayBillDate")),
						row.get("hsnNumber"));
			}
			
			purchases.add(purchase);
		}
		return purchases;
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

	private List<String> validatePurchasesData(List<Purchase> purchases, String username) {
		List<String> validationErrors = new ArrayList<String>();
		int rowCounter = 1;
		for (Purchase purchase : purchases) {
			rowCounter++;
			List<String> errors = new ArrayList<String>();
			if (purchase == null) {
				validationErrors.add("Row: " + rowCounter + " - No item data in file.");
				continue;
			}

			if (purchase.getCompIec() == null || purchase.getCompIec().equals("")) {
				errors.add("Row: " + rowCounter + " - IEC is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.IEC_REGEX, purchase.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. IEC can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (purchase.getPartNumber() == null || purchase.getPartNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Part number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.PART_NUMBER_REGEX, purchase.getPartNumber())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Number. Part Number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (purchase.getPartDesc() == null || purchase.getPartDesc().equals("")) {
				errors.add("Row: " + rowCounter + " - Part description is required in Purchase data");
			} else {
				if (!Constants.validateByRegex(Constants.PART_DESCRIPTION_REGEX, purchase.getPartDesc())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Description. Part Description can contain alphanumeric and space characters and can be at max 40 characters in length.");
				}
			}

			if (purchase.getTypeOfPurchase() == null || purchase.getTypeOfPurchase().equals("")) {
				errors.add("Row: " + rowCounter + " - Type of Purchase is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.TYPE_OF_PURCHASE_REGEX, purchase.getTypeOfPurchase())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Type of purchase. Type of purchase can be either import or domestic.");
				}
			}

			if (purchase.getUom() == null || purchase.getUom().equals("")) {
				errors.add("Row: " + rowCounter + " - UOM is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.UOM_REGEX, purchase.getUom())) {
					errors.add("Row: " + rowCounter + " - Invalid UOM. UOM can be either kg ot piece.");
				}
			}

			if (purchase.getPurBatchNumber() == null || purchase.getPurBatchNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase batch number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.PURCHASE_BATCH_NUMBER_REGEX, purchase.getPurBatchNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Purchase batch number. Purchase batch number can be either kg ot piece.");
				}
			}
			
			if (purchase.getGrrQty() == null || purchase.getGrrQty().equals("")) {
				errors.add("Row: " + rowCounter + " - GRR Quantity is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.QUANTITY_REGEX, purchase.getGrrQty())) {
					errors.add("Row: " + rowCounter + " - Invalid GRR Quantity. GRR Quantity can contain numeric characters and can be max 10 characters in length.");
				}
			}
			
			if (purchase.getGrrNo() == null || purchase.getGrrNo().equals("")) {
				errors.add("Row: " + rowCounter + " - GRR Number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.GRR_NUMBER_REGEX, purchase.getGrrNo())) {
					errors.add("Row: " + rowCounter + " - Invalid GRR Number. GRR Number can contain alphanumeric characters and can be max 8 charaters in length.");
				}
			}
			
			if (purchase.getPurInvNo() == null || purchase.getPurInvNo().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase invoice number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.PURCHASE_INVOICE_NUMBER_REGEX, purchase.getPurInvNo())) {
					errors.add("Row: " + rowCounter + " - Invalid Purchase invoice number. Purchase invoice number can contain alphanumeric characters and can be max 3 charaters in length.");
				}
			}
			
			if (purchase.getPurInvDate() == null || purchase.getPurInvDate().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase invoice date is required in Purchase data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, purchase.getPurInvDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid Purchase invoice date. Purchase invoice date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}

			if (purchase.getInBondBoeNum() == null || purchase.getInBondBoeNum().equals("")) {
				errors.add("Row: " + rowCounter + " - InBondBoeNum is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.BOND_BOE_NUMBER_REGEX, purchase.getInBondBoeNum())) {
					errors.add("Row: " + rowCounter + " - Invalid BOE number. BOE number can contain alphanumeric characters and can be max 15 charaters in length.");
				}
			}
			
			if (purchase.getInBondBoeDate() == null || purchase.getInBondBoeDate().equals("")) {
				errors.add("Row: " + rowCounter + " - BOE date is required in Purchase data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, purchase.getInBondBoeDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid InBondBoeDate. BOE date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}

			if (purchase.getPortOfImport() == null || purchase.getPortOfImport().equals("")) {
				errors.add("Row: " + rowCounter + " - Port of import is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.PORT_OF_IMPORT_REGEX, purchase.getPortOfImport())) {
					errors.add("Row: " + rowCounter + " - Invalid Port of import. Port of import can contain alphanumeric and space characters and can be max 15 charaters in length.");
				}
			}
			
			if (purchase.getBottleSealNumber() == null || purchase.getBottleSealNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Bottle Seal Number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.BOTTLE_SEAL_NUMBER_REGEX, purchase.getBottleSealNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Bottle Seal Number. Bottle Seal Number can contain alphanumeric characters and can be max 15 charaters in length.");
				}
			}
			
			if (purchase.getInsuranceDetails() == null || purchase.getInsuranceDetails().equals("")) {
				errors.add("Row: " + rowCounter + " - Insurance details is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.INSURANCE_DETAILS_REGEX, purchase.getInsuranceDetails())) {
					errors.add("Row: " + rowCounter + " - Invalid Insurance details. Insurance details can contain alphanumeric and space characters and can be max 30 charaters in length.");
				}
			}
			
			if (purchase.getPerUnitCost() == null || purchase.getPerUnitCost().equals("")) {
				errors.add("Row: " + rowCounter + " - Per unit cost is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, purchase.getPerUnitCost())) {
					errors.add("Row: " + rowCounter + " - Invalid Per unit cost. Per unit cost can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (purchase.getTotalPurValue() == null || purchase.getTotalPurValue().equals("")) {
				errors.add("Row: " + rowCounter + " - Total purchase value is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, purchase.getTotalPurValue())) {
					errors.add("Row: " + rowCounter + " - Invalid Total purchase value. Total purchase value can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (purchase.getDutyBcdRate() == null || purchase.getDutyBcdRate().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty BCD Rate is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.RATE_REGEX, purchase.getDutyBcdRate())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty BCD Rate. Duty BCD Rate can be max 2 digits followed by 2 digits after decimal");
				}
			}
			
			if (purchase.getDutyBcdAmt() == null || purchase.getDutyBcdAmt().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty BCD Amount is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, purchase.getDutyBcdAmt())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty BCD Amount. Duty BCD Amount can be max 8 digits followed by 2 digits after decimal.");
				}
			}

			if (purchase.getDutyCessBcdAmt() == null || purchase.getDutyCessBcdAmt().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty Cess BCD Amount is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, purchase.getDutyCessBcdAmt())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty Cess BCD Amount. Duty Cess BCD Amount can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (purchase.getDutyCessBcdRate() == null || purchase.getDutyCessBcdRate().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty Cess BCD Rate is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.RATE_REGEX, purchase.getDutyCessBcdRate())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty Cess BCD Rate. Duty Cess BCD Rate can be max 2 digits followed by 2 digits after decimal");
				}
			}
			
			if (purchase.getDutyGstRate() == null || purchase.getDutyGstRate().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty GST Rate is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.RATE_REGEX, purchase.getDutyGstRate())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty GST Rate. Duty GST Rate can be max 2 digits followed by 2 digits after decimal");
				}
			}
			
			if (purchase.getDutyGstAmt() == null || purchase.getDutyGstAmt().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty GST Amount is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, purchase.getDutyGstAmt())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty GST Amount. Duty GST Amount can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			
			if (purchase.getDutyCessGstRate() == null || purchase.getDutyCessGstRate().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty Cess GST Rate is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.RATE_REGEX, purchase.getDutyCessGstRate())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty Cess GST Rate. Duty Cess GST Rate can be max 2 digits followed by 2 digits after decimal");
				}
			}
			
			if (purchase.getDutyCessGstAmt() == null || purchase.getDutyCessGstAmt().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty Cess GST Amount is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, purchase.getDutyCessGstAmt())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty Cess GST Amount. Duty Cess GST Amount can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (purchase.getDutyOtherRate() == null || purchase.getDutyOtherRate().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty Other Rate is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.RATE_REGEX, purchase.getDutyOtherRate())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty Other Rate. Duty Other Rate can be max 2 digits followed by 2 digits after decimal");
				}
			}
			
			if (purchase.getDutyOtherAmt() == null || purchase.getDutyOtherAmt().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty Other Amount is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, purchase.getDutyOtherAmt())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty Other Amount. Duty Other Amount can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (purchase.getTotalCustomDuty() == null || purchase.getTotalCustomDuty().equals("")) {
				errors.add("Row: " + rowCounter + " - Total Custom Duty is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, purchase.getTotalCustomDuty())) {
					errors.add("Row: " + rowCounter + " - Invalid Total Custom Duty. Total Custom Duty can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (purchase.getTotalGst() == null || purchase.getTotalGst().equals("")) {
				errors.add("Row: " + rowCounter + " - Total GST is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.RATE_REGEX, purchase.getTotalGst())) {
					errors.add("Row: " + rowCounter + " - Invalid Total GST. Total GST can be max 2 digits followed by 2 digits after decimal");
				}
			}
			
			if (purchase.getVehicleRegNum() == null || purchase.getVehicleRegNum().equals("")) {
				errors.add("Row: " + rowCounter + " - Vehicle registration number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.VEHICLE_REGISTRATION_NUMBER_REGEX, purchase.getVehicleRegNum())) {
					errors.add("Row: " + rowCounter + " - Invalid Vehicle registration number. Vehicle registration number can contain alphanumeric characters and can be max 15 charaters in length.");
				}
			}
			
			if (purchase.getWarehouseDate() == null || purchase.getWarehouseDate().equals("")) {
				errors.add("Row: " + rowCounter + " - Warehouse Date is required in Purchase data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, purchase.getWarehouseDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid Warehouse Date. Warehouse Date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}
			
			if (purchase.getEwayBillNum() == null || purchase.getEwayBillNum().equals("")) {
				errors.add("Row: " + rowCounter + " - Eway Bill Number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.EWAY_BILL_NUMBER_REGEX, purchase.getEwayBillNum())) {
					errors.add("Row: " + rowCounter + " - Invalid Eway Bill Number. Eway Bill Number can contain alphanumeric characters and can be max 10 charaters in length.");
				}
			}
			
			if (purchase.getEwayBillDate() == null || purchase.getEwayBillDate().equals("")) {
				errors.add("Row: " + rowCounter + " - Eway Bill Date is required in Purchase data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, purchase.getEwayBillDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid Eway Bill Date. Eway Bill Date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}
			
			if (purchase.getHsnNumber() == null || purchase.getHsnNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - HSN number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.HSN_REGEX, purchase.getHsnNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid HSN number. HSN number can contain alphanumeric characters and can be max 8 characters in length.");
				}
			}
			
			if (purchase.getUsername() == null || purchase.getUsername().equals("")) {
				errors.add("Row: " + rowCounter + " - Username is required in Item data.");
			} else {
				if (!Constants.validateByRegex(Constants.USERNAME_REGEX, purchase.getUsername())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Username. Username can contain alphanumeric characters and can be at max 10 characters in length.");
				}
			}

			if (errors.isEmpty()) {
				String iec = companyRepository.getIecByUsername(purchase.getUsername());
				if (iec == null || iec == "" || !iec.equalsIgnoreCase(purchase.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. Provided IEC is not registered against the company.");
				}
			}
			
			if (errors.isEmpty()) {
				Item item = itemRepository.findByPartNumberAndIec(purchase.getPartNumber(), purchase.getCompIec());
				if (item == null || item.getPartNumber() == null || item.getPartNumber().equals("")) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Number. No item with this part number is registered for the specifed IEC.");
				} else {
					if (!item.getUom().equalsIgnoreCase(purchase.getUom())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid UOM. Provided UOM should be same as that of registered for the item.");
					}
					
					if (!item.getTypeOfPurchase().equalsIgnoreCase(purchase.getTypeOfPurchase())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid Type of purchase. Provided type of purchase should be same as that of registered for the item.");
					}
					
					if (!item.getHsnNumber().equalsIgnoreCase(purchase.getHsnNumber())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid HSN number. Provided HSN number should be same as that of registered for the item.");
					}
				}
			}

			validationErrors.addAll(errors);
		}

		return validationErrors;
	}

}
