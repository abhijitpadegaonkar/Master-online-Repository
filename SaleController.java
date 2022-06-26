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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
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

import com.ap.demo.model.Item;
import com.ap.demo.model.Purchase;
import com.ap.demo.model.Sale;
import com.ap.demo.respository.CompanyRepository;
import com.ap.demo.respository.ItemRepository;
import com.ap.demo.respository.PurchaseRepository;
import com.ap.demo.respository.SaleRepository;
import com.ap.demo.utils.Constants;

@Controller
public class SaleController {

	private static final Logger logger = LoggerFactory.getLogger(SaleController.class);

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Value("${app.temp.upload.dir}")
	public String appTempUploadDir;

	@GetMapping("/sale-master")
	public String showMasterPage() {
		logger.info("Showing master page.");
		return "sale-master";
	}

	@ResponseBody
	@GetMapping("/sales")
	public List<Sale> getSales() {
		logger.info("Fetching all sales details.");
		return saleRepository.findAll();
	}

	@PostMapping("/remove-sale")
	@ResponseBody
	public boolean remove(@RequestParam(name = "id") Long saleId) {
		logger.info("Processing request to remove sale. Sale Id: {}", saleId);
		return saleRepository.delete(saleId);
	}

	@GetMapping("/update-sale")
	public String showUpdateSaleForm(@RequestParam("id") Long saleId) {
		logger.info("Showing update sale page.");
		saleRepository.findById(saleId);
		return "update-sale";
	}

	@PostMapping("/update-sale")
	@ResponseBody
	public String showUpdateSaleForm(@RequestParam("id") Long saleId, @RequestParam("username") String username,
			@RequestParam("compIec") String compIec, @RequestParam("partNumber") String partNumber,
			@RequestParam("typeOfSale") String typeOfSale, @RequestParam("partDesc") String partDesc,
			@RequestParam("uom") String uom, @RequestParam(name = "createdDate", required = false) String createdDate,
			@RequestParam(name = "updateDate", required = false) String updateDate) {

		logger.info("Handling update sale request. {}, {}, {}, {}, {}, {}, {}", username, compIec, partNumber, partDesc, uom, createdDate, updateDate);

		Sale sale = new Sale();
		sale.setId(saleId);
		sale.setUsername(username);
		sale.setCompIec(compIec);
		sale.setPartDesc(partDesc);
		sale.setPartNumber(partNumber);
		sale.setUom(uom);

		saleRepository.update(sale);
		return "update-sale";
	}

	@GetMapping("/add-sales")
	public String showAddSalesForm() {
		logger.info("Showing add sales page.");
		return "add-sales";
	}

	@PostMapping("/add-sales")
	public String addSales(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		logger.info("Handling sales creation via file upload request. Filename: {}", file.getOriginalFilename());

		List<Sale> sales;
		try {

			if (file.isEmpty() || file.getSize() > 2097152) {
				logger.info("Uploaded file is empty or exceeds 2MB max size allowed. Filename: {}",
						file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload a file. Max 2MB file size is allowed.");
				return "redirect:add-sales";
			}

			if (!file.getOriginalFilename().toLowerCase().endsWith(".xls")) {
				logger.info("Uploaded file is not a .xls file. Filename: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload .xls file.");
				return "redirect:add-sales";
			}

			Path copyLocation = Paths.get(appTempUploadDir + File.separator + UUID.randomUUID().toString()
					+ StringUtils.cleanPath(file.getOriginalFilename()));
			try {
				Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				logger.info("Unable to save file: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error",
						"Unable to save file: " + file.getOriginalFilename() + ". Please try again.");
				return "redirect:add-sales";
			}

			sales = parseSalesDataFromXlsFile(copyLocation.toFile());
			List<String> errors = new ArrayList<String>();
			List<String> infos = new ArrayList<String>();
			errors = validateSalesData(sales, "comp1");
			
			if (errors.isEmpty()) {
				for (Sale sale : sales) {
					System.out.println("Saving " + sale.getPurBatchNumber());
					try {
						saleRepository.save(sale);
					} catch (DuplicateKeyException e) {
						logger.error("Cannot add sale: {}, Some of follwing data is duplicate: Sale Batch Number.",
								sale.getPurBatchNumber(), e);
						errors.add("Cannot add sale: " + sale.getPurBatchNumber()
								+ ". Some of follwing data is duplicate: Sale Batch Number.");
					}
				}
			}

			if (!infos.isEmpty()) {
				redirectAttributes.addFlashAttribute("infos", infos);
			}
			
			if (!errors.isEmpty()) {
				redirectAttributes.addFlashAttribute("errors", errors);
				return "redirect:/add-sales";
			}
		} catch (Exception e) {
			logger.error("Unable to upload file: {}", file.getOriginalFilename(), e);
			redirectAttributes.addFlashAttribute("error", "Unable to upload " + file.getOriginalFilename());
			return "redirect:/add-sales";
		}

		logger.info("Created sales via file upload: {}", file.getOriginalFilename());
		redirectAttributes.addFlashAttribute("info",
				"Created " + sales.size() + " sales via " + file.getOriginalFilename() + " file upload.");
		return "redirect:/add-sales";
	}

	private List<Sale> parseSalesDataFromXlsFile(File file) throws ParseException {

		Workbook workbook = null;
		// Create the Workbook
		try {
			workbook = WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | IOException e) {
			logger.error("Unable to process add sales file. Filename: {} ", file.getName(), e);
		}

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Getting number of columns in the Sheet
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		logger.info("Add sales file has {} sheets and sheet 0 has {} columns", workbook.getNumberOfSheets(),
				noOfColumns);

		// header list
		List<String> headers = new ArrayList<>();
		headers.add("username"); 
		headers.add("compIec");
		headers.add("partNumber");
		headers.add("partDesc");
		headers.add("uom");
		headers.add("typeOfSale");
		headers.add("quantity");
		headers.add("hsnNumber");
		headers.add("tempInvoiceNumber");
		headers.add("tempInvoiceDate");
		headers.add("gstInvoiceNumber");
		headers.add("gstInvoiceDate");
		headers.add("shbNumber");
		headers.add("shbDate");
		headers.add("portOfExport");
		headers.add("detailsOfInsurance");
		headers.add("perUnitAssessibleValue");
		headers.add("assessibleValue");
		headers.add("dutyGst");
		headers.add("dutyCessOnGst");
		headers.add("vehicleRegNumber");
		headers.add("oneTimeLockNumber");
		headers.add("goodsRemovalTs");
		headers.add("ewayBillNumber");
		headers.add("ewayBillDate");
		headers.add("remark");
		headers.add("purBatchNumber");
		headers.add("prodBatchNumber");
		headers.add("exBondBoeNo");
		headers.add("exBondBoeDate");
		

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
		List<Sale> sales = loadSaleData(sheetData);

		// Closing the workbook
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sales;
	}
	
	private List<Sale> loadSaleData(List<Map<String, String>> sheetData) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		List<Sale> sales = new ArrayList<Sale>();
		for (Map<String, String> row : sheetData) {
			Sale sale = null;
			if (row != null) {
				sale = new Sale(row.get("username"), 
						row.get("compIec"),
						row.get("partNumber"),
						row.get("partDesc"),
						row.get("uom"),
						row.get("typeOfSale"),
						row.get("quantity"),
						row.get("hsnNumber"),
						row.get("tempInvoiceNumber"),
						sdf.parse(row.get("tempInvoiceDate")),
						row.get("gstInvoiceNumber"),
						sdf.parse(row.get("gstInvoiceDate")),
						row.get("shbNumber"),
						sdf.parse(row.get("shbDate")),
						row.get("portOfExport"),
						row.get("detailsOfInsurance"),
						row.get("perUnitAssessibleValue"),
						row.get("assessibleValue"),
						row.get("dutyGst"),
						row.get("dutyCessOnGst"),
						row.get("vehicleRegNumber"),
						row.get("oneTimeLockNumber"),
						sdf.parse(row.get("goodsRemovalTs")),
						row.get("ewayBillNumber"),
						sdf.parse(row.get("ewayBillDate")),
						row.get("remark"),
						row.get("purBatchNumber"),
						row.get("prodBatchNumber"),
						row.get("exBondBoeNo"),
						sdf.parse(row.get("exBondBoeDate")));
				
			}
			
			sales.add(sale);
		}
		return sales;
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
	
	private List<String> validateSalesData(List<Sale> sales, String username) {
		List<String> validationErrors = new ArrayList<String>();
		int rowCounter = 1;
		for (Sale sale : sales) {
			rowCounter++;
			List<String> errors = new ArrayList<String>();
			if (sale == null) {
				validationErrors.add("Row: " + rowCounter + " - No item data in file.");
				continue;
			}

			if (sale.getCompIec() == null || sale.getCompIec().equals("")) {
				errors.add("Row: " + rowCounter + " - IEC is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.IEC_REGEX, sale.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. IEC can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (sale.getPartNumber() == null || sale.getPartNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Part number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.PART_NUMBER_REGEX, sale.getPartNumber())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Number. Part Number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (sale.getPartDesc() == null || sale.getPartDesc().equals("")) {
				errors.add("Row: " + rowCounter + " - Part description is required in Sale data");
			} else {
				if (!Constants.validateByRegex(Constants.PART_DESCRIPTION_REGEX, sale.getPartDesc())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Description. Part Description can contain alphanumeric and space characters and can be at max 40 characters in length.");
				}
			}

			if (sale.getTypeOfSale() == null || sale.getTypeOfSale().equals("")) {
				errors.add("Row: " + rowCounter + " - Type of Sale is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.TYPE_OF_SALE_REGEX, sale.getTypeOfSale())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Type of Sale. Type of Sale can be either export or domestic.");
				}
			}

			if (sale.getUom() == null || sale.getUom().equals("")) {
				errors.add("Row: " + rowCounter + " - UOM is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.UOM_REGEX, sale.getUom())) {
					errors.add("Row: " + rowCounter + " - Invalid UOM. UOM can be either kg ot piece.");
				}
			}

			if (sale.getQuantity() == null || sale.getQuantity().equals("")) {
				errors.add("Row: " + rowCounter + " - Quantity is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.QUANTITY_REGEX, sale.getQuantity())) {
					errors.add("Row: " + rowCounter + " - Invalid Quantity. Quantity can contain numeric characters and can be max 10 characters in length.");
				}
			}
			
			if (sale.getHsnNumber() == null || sale.getHsnNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - HSN number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.HSN_REGEX, sale.getHsnNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid HSN number. HSN number can contain alphanumeric characters and can be max 8 characters in length.");
				}
			}
			
			if (sale.getTempInvoiceNumber() == null || sale.getTempInvoiceNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Temporary invoice number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.TEMP_INVOICE_NUMBER_REGEX, sale.getTempInvoiceNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Temporary invoice number. Temporary invoice number can contain alphanumeric characters and can be max 15 charaters in length.");
				}
			}
			
			if (sale.getTempInvoiceDate() == null || sale.getTempInvoiceDate().equals("")) {
				errors.add("Row: " + rowCounter + " - Temporary invoice date is required in Sale data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, sale.getTempInvoiceDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid Temporary invoice date. Temporary invoice date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}

			if (sale.getGstInvoiceNumber() == null || sale.getGstInvoiceNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - GST invoice number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.GST_INVOICE_NUMBER_REGEX, sale.getGstInvoiceNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid GST invoice number. GST invoice number can contain alphanumeric characters and can be max 15s charaters in length.");
				}
			}
			
			if (sale.getGstInvoiceDate() == null || sale.getGstInvoiceDate().equals("")) {
				errors.add("Row: " + rowCounter + " - GST invoice date is required in Sale data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, sale.getGstInvoiceDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid GST invoice date. GST invoice date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}
			
			if (sale.getShbNumber() == null || sale.getShbNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - SHB number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.SHB_NUMBER_REGEX, sale.getShbNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid SHB number. SHB number can contain alphanumeric characters and can be max 15 charaters in length.");
				}
			}
			
			if (sale.getShbDate() == null || sale.getShbDate().equals("")) {
				errors.add("Row: " + rowCounter + " - SHB date is required in Sale data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, sale.getShbDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid SHB date. SHB date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}
			
			if (sale.getPortOfExport() == null || sale.getPortOfExport().equals("")) {
				errors.add("Row: " + rowCounter + " - Port of export is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.PORT_OF_EXPORT_REGEX, sale.getPortOfExport())) {
					errors.add("Row: " + rowCounter + " - Invalid Port of export. Port of export can contain alphanumeric and space characters and can be max 20 charaters in length.");
				}
			}
			
			if (sale.getDetailsOfInsurance() == null || sale.getDetailsOfInsurance().equals("")) {
				errors.add("Row: " + rowCounter + " - Insurance details is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.INSURANCE_DETAILS_REGEX, sale.getDetailsOfInsurance())) {
					errors.add("Row: " + rowCounter + " - Invalid Insurance details. Insurance details can contain alphanumeric and space characters and can be max 30 charaters in length.");
				}
			}
			
			if (sale.getPerUnitAssessibleValue() == null || sale.getPerUnitAssessibleValue().equals("")) {
				errors.add("Row: " + rowCounter + " - Per unit assessible value is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, sale.getPerUnitAssessibleValue())) {
					errors.add("Row: " + rowCounter + " - Invalid Per unit assessible value. Per unit assessible value can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (sale.getAssessibleValue() == null || sale.getAssessibleValue().equals("")) {
				errors.add("Row: " + rowCounter + " - Assessible value is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, sale.getAssessibleValue())) {
					errors.add("Row: " + rowCounter + " - Invalid Assessible value. Assessible value can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (sale.getDutyGst() == null || sale.getDutyGst().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty GST is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, sale.getDutyGst())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty GST. Duty GST can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (sale.getDutyCessOnGst() == null || sale.getDutyCessOnGst().equals("")) {
				errors.add("Row: " + rowCounter + " - Duty Cess on GST is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.AMOUNT_REGEX, sale.getDutyCessOnGst())) {
					errors.add("Row: " + rowCounter + " - Invalid Duty Cess on GST. Duty Cess on GST can be max 8 digits followed by 2 digits after decimal.");
				}
			}
			
			if (sale.getVehicleRegNumber() == null || sale.getVehicleRegNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Vehicle registration number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.VEHICLE_REGISTRATION_NUMBER_REGEX, sale.getVehicleRegNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Vehicle registration number. Vehicle registration number can contain alphanumeric characters and can be max 15 charaters in length.");
				}
			}
			
			if (sale.getOneTimeLockNumber() == null || sale.getOneTimeLockNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - One time lock number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.ONE_TIME_LOCK_NUMBER_REGEX, sale.getOneTimeLockNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid One time lock number. One time lock number can contain alphanumeric characters and can be max 15 charaters in length.");
				}
			}
			
			if (sale.getGoodsRemovalTs() == null || sale.getGoodsRemovalTs().equals("")) {
				errors.add("Row: " + rowCounter + " - Goods removal date is required in Sale data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_TIME_REGEX, sale.getGoodsRemovalTs())) {
//					errors.add("Row: " + rowCounter + " - Invalid Goods removal date. Goods removal date should be in DD-MM-YYYY hh:mm:ss format. For example: 15-01-2022 18:45:55");
//				}
			}
			
			if (sale.getEwayBillNumber() == null || sale.getEwayBillNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Eway Bill Number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.EWAY_BILL_NUMBER_REGEX, sale.getEwayBillNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Eway Bill Number. Eway Bill Number can contain alphanumeric characters and can be max 10 charaters in length.");
				}
			}
			
			if (sale.getEwayBillDate() == null || sale.getEwayBillDate().equals("")) {
				errors.add("Row: " + rowCounter + " - Eway Bill Date is required in Sale data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, sale.getEwayBillDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid Eway Bill Date. Eway Bill Date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}
			
			if (sale.getRemark() == null || sale.getRemark().equals("")) {
				errors.add("Row: " + rowCounter + " - Remark is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.REMARK_REGEX, sale.getRemark())) {
					errors.add("Row: " + rowCounter + " - Invalid Remark. Remark  can contain alphanumeric and space characters and can be max 50 characters in length.");
				}
			}
			
			if (sale.getPurBatchNumber() == null || sale.getPurBatchNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase batch number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.PURCHASE_BATCH_NUMBER_REGEX, sale.getPurBatchNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Purchase batch number. Purchase batch number can be either kg ot piece.");
				}
			}
			
			if (sale.getProdBatchNumber() == null || sale.getProdBatchNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Production batch number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.PRODUCTION_BATCH_NUMBER_REGEX, sale.getProdBatchNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Production batch number. Production batch number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (sale.getHsnNumber() == null || sale.getHsnNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - HSN number is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.HSN_REGEX, sale.getHsnNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid HSN number. HSN number can contain alphanumeric characters and can be max 8 characters in length.");
				}
			}
			
			if (sale.getExBondBoeNo() == null || sale.getExBondBoeNo().equals("")) {
				errors.add("Row: " + rowCounter + " - BOE number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.BOND_BOE_NUMBER_REGEX, sale.getExBondBoeNo())) {
					errors.add("Row: " + rowCounter + " - Invalid BOE number. BOE number can contain alphanumeric characters and can be max 15 charaters in length.");
				}
			}
			
			if (sale.getExBondBoeDate() == null || sale.getExBondBoeDate().equals("")) {
				errors.add("Row: " + rowCounter + " - BOE date is required in Purchase data.");
			} else {
//				if (!Constants.validateByRegex(Constants.DATE_REGEX, sale.getExBondBoeDate())) {
//					errors.add("Row: " + rowCounter + " - Invalid BOE. BOE date should be in DD-MM-YYYY format. For example: 15-01-2022");
//				}
			}
			
			if (sale.getUsername() == null || sale.getUsername().equals("")) {
				errors.add("Row: " + rowCounter + " - Username is required in Sale data.");
			} else {
				if (!Constants.validateByRegex(Constants.USERNAME_REGEX, sale.getUsername())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Username. Username can contain alphanumeric characters and can be at max 10 characters in length.");
				}
			}

			if (errors.isEmpty()) {
				String iec = companyRepository.getIecByUsername(sale.getUsername());
				if (iec == null || iec == "" || !iec.equalsIgnoreCase(sale.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. Provided IEC is not registered against the company.");
				}
			}
			
			if (errors.isEmpty()) {
				Item item = itemRepository.findByPartNumberAndIec(sale.getPartNumber(), sale.getCompIec());
				if (item == null || item.getPartNumber() == null || item.getPartNumber().equals("")) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Number. No item with this part number is registered.");
				} else {
					if (!item.getUom().equalsIgnoreCase(sale.getUom())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid UOM. Provided UOM should be same as that of registered for the item.");
					}
					
					if (!item.getHsnNumber().equalsIgnoreCase(sale.getHsnNumber())) {
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
