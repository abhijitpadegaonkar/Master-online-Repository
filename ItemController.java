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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ap.demo.model.Company;
import com.ap.demo.model.Item;
import com.ap.demo.respository.CompanyRepository;
import com.ap.demo.respository.ItemRepository;
import com.ap.demo.utils.Constants;

@Controller
public class ItemController {

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Value("${app.temp.upload.dir}")
	public String appTempUploadDir;

	@Value("${max.file.upload.size}")
	public long maxFileUploadSize;

	@GetMapping("/item-master")
	public String showMasterPage() {
		logger.info("Showing master page.");
		return "item-master";
	}

	@ResponseBody
	@GetMapping("/items")
	public List<Item> getItems() {
		logger.info("Fetching all items details.");
		return itemRepository.findAll();
	}

	@PostMapping("/remove-item")
	@ResponseBody
	public boolean remove(@RequestParam(name = "id") Long itemId) {
		logger.info("Processing request to remove item. Item Id: {}", itemId);
		return itemRepository.delete(itemId);
	}

	@GetMapping("/update-item")
	public String showUpdateItemForm(@RequestParam("id") Long itemId) {
		logger.info("Showing update item page.");
		itemRepository.findById(itemId);
		return "update-item";
	}

	@PostMapping("/update-item")
	@ResponseBody
	public String showUpdateItemForm(@RequestParam("id") Long itemId, @RequestParam("username") String username,
			@RequestParam("compIec") String compIec, @RequestParam("partNumber") String partNumber,
			@RequestParam("typeOfPurchase") String typeOfPurchase, @RequestParam("partDesc") String partDesc,
			@RequestParam("uom") String uom, @RequestParam("hsnNumber") String hsnNumber,
			@RequestParam(name = "createdDate", required = false) String createdDate,
			@RequestParam(name = "updateDate", required = false) String updateDate) {

		logger.info("Handling update item request. {}, {}, {}, {}, {}, {}, {}, {}", username, compIec, partNumber,
				typeOfPurchase, partDesc, uom, createdDate, updateDate);

		Item item = new Item();
		item.setId(itemId);
		item.setUsername(username);
		item.setCompIec(compIec);
		item.setPartDesc(partDesc);
		item.setPartNumber(partNumber);
		item.setTypeOfPurchase(typeOfPurchase);
		item.setUom(uom);
		item.setHsnNumber(hsnNumber);

		itemRepository.update(item);
		return "update-item";
	}

	@GetMapping("/add-items")
	public String showAddItemsForm() {
		logger.info("Showing add items page.");
		return "add-items";
	}

	@PostMapping("/add-items")
	public String addItems(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		logger.info("Handling items creation via file upload request. Filename: {}", file.getOriginalFilename());

		List<Item> items;
		try {

			if (file.isEmpty() || file.getSize() > maxFileUploadSize) {
				logger.info("Uploaded file is empty or exceeds 2MB max size allowed. Filename: {}",
						file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload a file. Max 2MB file size is allowed.");
				return "redirect:add-items";
			}

			if (! (file.getOriginalFilename().toLowerCase().endsWith(".xls") || file.getOriginalFilename().toLowerCase().endsWith(".xlsx"))) {
				logger.info("Uploaded file is not a .xls or .xlsx file. Filename: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload .xls or .xlsx file.");
				return "redirect:add-items";
			}

			Path copyLocation = Paths.get(appTempUploadDir + File.separator + UUID.randomUUID().toString()
					+ StringUtils.cleanPath(file.getOriginalFilename()));
			try {
				Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				logger.info("Unable to save file: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error",
						"Unable to save file: " + file.getOriginalFilename() + ". Please try again.");
				return "redirect:add-items";
			}

			items = parseItemsDataFromXlsFile(copyLocation.toFile());
			List<String> errors = new ArrayList<String>();
			List<String> infos = new ArrayList<String>();
			errors = validateItemsData(items, "comp1");

			if (errors.isEmpty()) {
				for (Item item : items) {
					try {
						if (!itemRepository.isItemPresent(item)) {
							itemRepository.save(item);
							infos.add("Item added. Part number: " + item.getPartNumber());
						} else {
							errors.add("Cannot add item. Item already present with this part number: "
									+ item.getPartNumber());
						}
					} catch (DuplicateKeyException e) {
						logger.error("Cannot add item. Item already present with this part number: {}",
								item.getPartNumber(), e);
						errors.add(
								"Cannot add item. Item already present with this part number: " + item.getPartNumber());
					}
				}
			}

			if (!infos.isEmpty()) {
				redirectAttributes.addFlashAttribute("infos", infos);
			}

			if (!errors.isEmpty()) {
				redirectAttributes.addFlashAttribute("errors", errors);
				return "redirect:/add-items";
			}
		} catch (Exception e) {
			logger.error("Unable to upload file: {}", file.getOriginalFilename(), e);
			redirectAttributes.addFlashAttribute("error", "Unable to upload " + file.getOriginalFilename());
			return "redirect:/add-items";
		}

		logger.info("Created items via file upload: {}", file.getOriginalFilename());
		redirectAttributes.addFlashAttribute("info",
				"Created " + items.size() + " items via " + file.getOriginalFilename() + " file upload.");
		return "redirect:/add-items";
	}

	private List<String> validateItemsData(List<Item> items, String username) {
		List<String> validationErrors = new ArrayList<String>();
		int rowCounter = 1;
		for (Item item : items) {
			rowCounter++;
			List<String> errors = new ArrayList<String>();
			if (item == null) {
				errors.add("Row: " + rowCounter + " - No item data in file.");
				continue;
			}

			if (item.getCompIec() == null || item.getCompIec().equals("")) {
				errors.add("Row: " + rowCounter + " - IEC is required in Item data.");
			} else {
				if (!Constants.validateByRegex(Constants.IEC_REGEX, item.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. IEC can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (item.getHsnNumber() == null || item.getHsnNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - HSN number is required in Item data");
			} else {
				if (!Constants.validateByRegex(Constants.HSN_REGEX, item.getHsnNumber())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid HSN. HSN can contain alphanumeric characters and can be at max 8 characters in length.");
				}
			}

			if (item.getPartNumber() == null || item.getPartNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Part number is required in Item data.");
			} else {
				if (!Constants.validateByRegex(Constants.PART_NUMBER_REGEX, item.getPartNumber())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Number. Part Number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}

			if (item.getPartDesc() == null || item.getPartDesc().equals("")) {
				errors.add("Row: " + rowCounter + " - Part description is required in Item data");
			} else {
				if (!Constants.validateByRegex(Constants.PART_DESCRIPTION_REGEX, item.getPartDesc())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Part Description. Part Description can contain alphanumeric and space characters and can be at max 40 characters in length.");
				}
			}

			if (item.getTypeOfPurchase() == null || item.getTypeOfPurchase().equals("")) {
				errors.add("Row: " + rowCounter + " - Type of Purchase is required in Item data.");
			} else {
				if (!Constants.validateByRegex(Constants.TYPE_OF_PURCHASE_REGEX, item.getTypeOfPurchase())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Type of purchase. Type of purchase can be either import or domestic.");
				}
			}

			if (item.getUom() == null || item.getUom().equals("")) {
				errors.add("Row: " + rowCounter + " - UOM is required in Item data.");
			} else {
				if (!Constants.validateByRegex(Constants.UOM_REGEX, item.getUom())) {
					errors.add("Row: " + rowCounter + " - Invalid UOM. UOM can be either kg ot piece.");
				}
			}

			if (item.getUsername() == null || item.getUsername().equals("")) {
				errors.add("Row: " + rowCounter + " - Username is required in Item data.");
			} else {
				if (!Constants.validateByRegex(Constants.USERNAME_REGEX, item.getUsername())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Username. Username can contain alphanumeric characters and can be at max 10 characters in length.");
				}
			}

			if (errors.isEmpty()) {
				Company company = companyRepository.getCompanyDetailsByUsername(item.getUsername());
				if (company == null) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company. Company username is not registered against the company.");
				} else {
					if (company.getCompIec() == null || company.getCompIec() == "" || !company.getCompIec().equalsIgnoreCase(item.getCompIec())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid IEC. Provided IEC is not registered against the company.");
					}
					
					if (company.getHsnNumber() == null || company.getHsnNumber() == "" || !company.getHsnNumber().equalsIgnoreCase(item.getHsnNumber())) {
						errors.add("Row: " + rowCounter
								+ " - Invalid HSN. Provided HSN is not registered against the company.");
					}
				}
				
			}

			validationErrors.addAll(errors);
		}

		return validationErrors;
	}

	private List<Item> parseItemsDataFromXlsFile(File file) {

		Workbook workbook = null;
		// Create the Workbook
		try {
			workbook = WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | IOException e) {
			logger.error("Unable to process add items file. Filename: {} ", file.getName(), e);
		}

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Getting number of columns in the Sheet
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		DataFormatter formatter = new DataFormatter();
		logger.info("Add items file has {} sheets and sheet 0 has {} columns", workbook.getNumberOfSheets(),
				noOfColumns);

		// header list
		List<String> headers = new ArrayList<>();
		headers.add("username");
		headers.add("compIec");
		headers.add("partNumber");
		headers.add("typeOfPurchase");
		headers.add("partDesc");
		headers.add("uom");
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
		List<Item> items = loadItemData(sheetData);

		// Closing the workbook
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return items;
	}

	private List<Item> loadItemData(List<Map<String, String>> sheetData) {

		List<Item> items = new ArrayList<Item>();
		for (Map<String, String> row : sheetData) {
			Item item = null;
			if (row != null) {
				item = new Item(row.get("username"), row.get("compIec"), row.get("partNumber"),
						row.get("typeOfPurchase"), row.get("partDesc"), row.get("uom"), row.get("hsnNumber"));
			}

			items.add(item);
		}
		return items;
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

}
