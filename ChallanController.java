package com.ap.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ap.demo.model.Challan;
import com.ap.demo.model.Item;
import com.ap.demo.model.Purchase;
import com.ap.demo.respository.ChallanRepository;
import com.ap.demo.respository.CompanyRepository;
import com.ap.demo.respository.ItemRepository;
import com.ap.demo.respository.PurchaseRepository;
import com.ap.demo.utils.Constants;

@Controller
public class ChallanController {

	private static final Logger logger = LoggerFactory.getLogger(ChallanController.class);

	@Autowired
	private ChallanRepository challanRepository;

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Value("${app.temp.upload.dir}")
	public String appTempUploadDir;

	@GetMapping("/challan-master")
	public String showMasterPage() {
		logger.info("Showing master page.");
		return "challan-master";
	}

	@ResponseBody
	@GetMapping("/challans")
	public List<Challan> getChallans() {
		logger.info("Fetching all challans details.");
		return challanRepository.findAll();
	}

	@PostMapping("/remove-challan")
	@ResponseBody
	public boolean remove(@RequestParam(name = "id") Long challanId) {
		logger.info("Processing request to remove challan. Challan Id: {}", challanId);
		return challanRepository.delete(challanId);
	}

	@GetMapping("/add-challan")
	public String showAddChallanForm(Model model) {
		logger.info("Showing add challan page.");
		model.addAttribute("challan", new Challan());
		return "add-challan";
	}
	
	@PostMapping("/add-challan")
//	public String showAddChallanForm(@RequestParam("username") String username,
//			@RequestParam("compIec") String compIec, @RequestParam("purBatchNumber") String purBatchNumber,
//			@RequestParam("challanNumber") String challanNumber, @RequestParam("challanDate") String challanDate,
//			@RequestParam("challanAmount") String challanAmount) {

	public String showAddChallanForm(Challan challan) {
//		logger.info("Handling update challan request. {}, {}, {}, {}, {}, {}, {}", username, compIec, purBatchNumber,
//				challanNumber, challanDate, challanAmount);
		
		logger.info("Handling update challan request. {}, {}, {}, {}, {}, {}, {}", challan.getChallanNumber());

		challanRepository.save(challan);
		return "challan-master";
	}
	
	@PostMapping("/add-challan-entry")
	@ResponseBody
	public String addChallanForm(@RequestParam("compIec") String compIec, @RequestParam("purBatchNumber") String purBatchNumber,
			@RequestParam("challanNumber") String challanNumber, @RequestParam("challanDate") String challanDate,
			@RequestParam("challanAmount") String challanAmount) {

		logger.info("Handling update challan request. {}, {}, {}, {}, {}, {}, {}, {}", compIec, purBatchNumber,
				challanNumber, challanDate, challanAmount);

		Challan challan = new Challan();
		challan.setCompIec(compIec);
		challan.setPurBatchNumber(purBatchNumber);
		challan.setChallanNumber(challanNumber);
		challan.setChallanDate(challanDate);
		challan.setChallanAmount(challanAmount);

		challanRepository.save(challan);
		return "added";
	}
	
	@PostMapping("/get-challan-entries")
	@ResponseBody
	public List<Challan> getChallanEnties(@RequestParam("compIec") String compIec, @RequestParam("purBatchNumber") String purBatchNumber) {

		logger.info("Handling get challan entries request for company IEC: {}, Purchase batch number: {}", compIec, purBatchNumber);


		List<Challan> challans = challanRepository.findAllChallans(compIec, purBatchNumber);
		return challans;
	}
	
	@GetMapping("/update-challan")
	public String showUpdateChallanForm(@RequestParam("id") Long challanId) {
		logger.info("Showing update challan page.");
		challanRepository.findById(challanId);
		return "update-challan";
	}

	@PostMapping("/update-challan")
	@ResponseBody
	public String showUpdateChallanForm(@RequestParam("id") Long challanId, @RequestParam("username") String username,
			@RequestParam("compIec") String compIec, @RequestParam("purBatchNumber") String purBatchNumber,
			@RequestParam("challanNumber") String challanNumber, @RequestParam("challanDate") String challanDate,
			@RequestParam("challanAmount") String challanAmount) {

		logger.info("Handling update challan request. {}, {}, {}, {}, {}, {}, {}, {}", compIec, purBatchNumber,
				challanNumber, challanDate, challanAmount);

		Challan challan = new Challan();
		challan.setId(challanId);
		challan.setCompIec(compIec);
		challan.setPurBatchNumber(purBatchNumber);
		challan.setChallanNumber(challanNumber);
		challan.setChallanDate(challanDate);
		challan.setChallanAmount(challanAmount);

		challanRepository.update(challan);
		return "update-challan";
	}

	@GetMapping("/add-challans")
	public String showAddChallansForm() {
		logger.info("Showing add challans page.");
		return "add-challans";
	}

	@PostMapping("/add-challans")
	public String addChallans(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		logger.info("Handling challans creation via file upload request. Filename: {}", file.getOriginalFilename());

		List<Challan> challans;
		try {

			if (file.isEmpty() || file.getSize() > 2097152) {
				logger.info("Uploaded file is empty or exceeds 2MB max size allowed. Filename: {}",
						file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload a file. Max 2MB file size is allowed.");
				return "redirect:add-challans";
			}

			if (!file.getOriginalFilename().toLowerCase().endsWith(".xls")) {
				logger.info("Uploaded file is not a .xls file. Filename: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload .xls file.");
				return "redirect:add-challans";
			}

			Path copyLocation = Paths.get(appTempUploadDir + File.separator + UUID.randomUUID().toString()
					+ StringUtils.cleanPath(file.getOriginalFilename()));
			try {
				Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				logger.info("Unable to save file: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error",
						"Unable to save file: " + file.getOriginalFilename() + ". Please try again.");
				return "redirect:add-challans";
			}

			challans = parseChallansDataFromXlsFile(copyLocation.toFile());
			List<String> errors = new ArrayList<String>();
			List<String> infos = new ArrayList<String>();
			errors = validateChallansData(challans, "comp1");
			
			if (errors.isEmpty()) {
				for (Challan challan : challans) {
					System.out.println("Saving " + challan.getPurBatchNumber());
					try {
						challanRepository.save(challan);
					} catch (DuplicateKeyException e) {
						logger.error("Cannot add challan: {}, Some of follwing data is duplicate: Purchase Batch Number.",
								challan.getPurBatchNumber(), e);
						errors.add("Cannot add challan: " + challan.getPurBatchNumber()
								+ ". Some of follwing data is duplicate: Purchase Batch Number.");
					}
				}
			}
			
			if (!infos.isEmpty()) {
				redirectAttributes.addFlashAttribute("infos", infos);
			}
			
			if (!errors.isEmpty()) {
				redirectAttributes.addFlashAttribute("errors", errors);
				return "redirect:/add-challans";
			}
		} catch (Exception e) {
			logger.error("Unable to upload file: {}", file.getOriginalFilename(), e);
			redirectAttributes.addFlashAttribute("error", "Unable to upload " + file.getOriginalFilename());
			return "redirect:/add-challans";
		}

		logger.info("Created challans via file upload: {}", file.getOriginalFilename());
		redirectAttributes.addFlashAttribute("info",
				"Created " + challans.size() + " challans via " + file.getOriginalFilename() + " file upload.");
		return "redirect:/add-challans";
	}
	
	private List<Challan> parseChallansDataFromXlsFile(File file) {

		Workbook workbook = null;
		// Create the Workbook
		try {
			workbook = WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | IOException e) {
			logger.error("Unable to process add challans file. Filename: {} ", file.getName(), e);
		}

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Getting number of columns in the Sheet
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		DataFormatter formatter = new DataFormatter();
		logger.info("Add challans file has {} sheets and sheet 0 has {} columns", workbook.getNumberOfSheets(),
				noOfColumns);

		// header list
		List<String> headers = new ArrayList<>();
		headers.add("compIec");
		headers.add("purBatchNumber");
		headers.add("challanNumber");
		headers.add("challanDate");
		headers.add("challanAmount");

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
						Cell celltmp = cell;
						columnValue = formatter.formatCellValue(celltmp);
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
		List<Challan> challans = loadChallanData(sheetData);

		// Closing the workbook
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return challans;
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
	
	private List<Challan> loadChallanData(List<Map<String, String>> sheetData) {

		List<Challan> challans = new ArrayList<Challan>();
		for (Map<String, String> row : sheetData) {
			Challan challan = null;
			if (row != null) {
				challan = new Challan(row.get("compIec"),
						row.get("purBatchNumber"),
						row.get("challanNumber"),
						row.get("challanDate"),
						row.get("challanAmount"));
			}
			
			challans.add(challan);
		}
		return challans;
	}
	
	private List<String> validateChallansData(List<Challan> challans, String username) {
		List<String> validationErrors = new ArrayList<String>();
		int rowCounter = 1;
		for (Challan challan : challans) {
			rowCounter++;
			List<String> errors = new ArrayList<String>();
			if (challan == null) {
				validationErrors.add("Row: " + rowCounter + " - No item data in file.");
				continue;
			}

			if (challan.getCompIec() == null || challan.getCompIec().equals("")) {
				errors.add("Row: " + rowCounter + " - IEC is required in Challan data.");
			} else {
				if (!Constants.validateByRegex(Constants.IEC_REGEX, challan.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. IEC can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (challan.getPurBatchNumber() == null || challan.getPurBatchNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase batch number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.PURCHASE_BATCH_NUMBER_REGEX, challan.getPurBatchNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Purchase batch number. Purchase batch number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (challan.getChallanNumber() == null || challan.getChallanNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase batch number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.PURCHASE_BATCH_NUMBER_REGEX, challan.getChallanNumber())) {
					errors.add("Row: " + rowCounter + " - Invalid Purchase batch number. Purchase batch number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (challan.getChallanDate() == null || challan.getChallanDate().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase batch number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.DATE_REGEX, challan.getChallanDate())) {
					errors.add("Row: " + rowCounter + " - Invalid Purchase batch number. Purchase batch number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (challan.getChallanAmount() == null || challan.getChallanAmount().equals("")) {
				errors.add("Row: " + rowCounter + " - Purchase batch number is required in Purchase data.");
			} else {
				if (!Constants.validateByRegex(Constants.DATE_REGEX, challan.getChallanAmount())) {
					errors.add("Row: " + rowCounter + " - Invalid Purchase batch number. Purchase batch number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			// TODO add after login integrated
//			if (challan.getUsername() == null || challan.getUsername().equals("")) {
//				errors.add("Row: " + rowCounter + " - Username is required in Item data.");
//			} else {
//				if (!Constants.validateByRegex(Constants.USERNAME_REGEX, challan.getUsername())) {
//					errors.add("Row: " + rowCounter
//							+ " - Invalid Username. Username can contain alphanumeric characters and can be at max 10 characters in length.");
//				}
//			}
//
//			if (errors.isEmpty()) {
//				String iec = companyRepository.getIecByUsername(challan.getUsername());
//				if (iec == null || iec == "" || !iec.equalsIgnoreCase(challan.getCompIec())) {
//					errors.add("Row: " + rowCounter
//							+ " - Invalid IEC. Provided IEC is not registered against the company.");
//				}
//			}
			
			if (errors.isEmpty()) {
				Purchase purchase = purchaseRepository.findByPurchaseBatchNumber(challan.getPurBatchNumber());
				if (purchase == null || purchase.getPartNumber() == null || purchase.getPartNumber().equals("")) {
					errors.add("Row: " + rowCounter
							+ " - Invalid purchase batch number. No purchase with this purchase batch number is registered.");
				} else {
					
					if (!purchase.getCompIec().equalsIgnoreCase(challan.getCompIec())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid part number. Provided part number should be same as that of registered for the purchase batch.");
					}
				}
			}

			validationErrors.addAll(errors);
		}

		return validationErrors;
	}
}
