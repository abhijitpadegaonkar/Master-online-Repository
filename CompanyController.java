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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ap.demo.model.Company;
import com.ap.demo.respository.CompanyRepository;
import com.ap.demo.utils.Constants;
import com.ap.demo.utils.GeneralUtility;

@Controller
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    
	@Autowired
	private CompanyRepository companyRepository;

	@Value("${app.temp.upload.dir}")
	public String appTempUploadDir;

	/**
	 * Shows Company master page.
	 */
	@GetMapping("/company-master")
	public String showCompanyMasterPage() {
		logger.info("Showing company master page.");
		return "company-master";
	}

	/**
	 * Fetches Company records to be used by DataTable or AJAX.
	 */
	@ResponseBody
	@GetMapping("/companies")
	public List<Company> getCompanies() {
		logger.info("Fetching all companies details.");
		return companyRepository.findAll();
	}

	@PostMapping("/remove-company")
	@ResponseBody
	public boolean removeCompany(@RequestParam(name = "id") Long companyId) {
		logger.info("Processing request to remove company. Company Id: {}", companyId);
		return companyRepository.delete(companyId);
	}
	
	@PostMapping("/enable-company")
	@ResponseBody
	public boolean enableCompany(@RequestParam(name = "id") Long companyId) {
		logger.info("Processing request to enable company. Company Id: {}", companyId);
		return companyRepository.enable(companyId);
	}
	
	@PostMapping("/disable-company")
	@ResponseBody
	public boolean disableCompany(@RequestParam(name = "id") Long companyId) {
		logger.info("Processing request to disable company. Company Id: {}", companyId);
		return companyRepository.disable(companyId);
	}
	
	/**
	 * Shows Update Companies with upload file page
	 */
	@GetMapping("/update-company")
	public String showUpdateCompanyForm(@RequestParam("id") Long companyId, Company user) {
		logger.info("Showing update company page.");
		companyRepository.findById(companyId);
		return "update-company";
	}
	
	@PostMapping("/update-company")
	@ResponseBody
	public String showUpdateCompanyForm(
			@RequestParam("id") Long companyId,
			@RequestParam("username") String username,
			@RequestParam("compName") String compName,
			@RequestParam("compEmailId") String compEmailId,
			@RequestParam("compUrl") String compUrl,
			@RequestParam("compIec") String compIec,
			@RequestParam("compPan") String compPan,
			@RequestParam("compTin") String compTin,
			@RequestParam("compIncorporationNumber") String compIncorporationNumber,
			@RequestParam("compType") String compType,
			@RequestParam("industryType") String industryType,
			@RequestParam("addrLine1") String addrLine1,
			@RequestParam("addrLine2") String addrLine2,
			@RequestParam("addrLine3") String addrLine3,
			@RequestParam("city") String city,
			@RequestParam("state") String state,
			@RequestParam("country") String country,
			@RequestParam("pinCode") String pinCode,
			@RequestParam("prodSiteAddr") String prodSiteAddr,
			@RequestParam("contPersonName") String contPersonName,
			@RequestParam("contPersonEmail") String contPersonEmail,
			@RequestParam("contPersonPhone") String contPersonPhone,
			@RequestParam("moowrNumber") String moowrNumber,
			@RequestParam("gstProductionPlant") String gstProductionPlant,
			@RequestParam("hsnNumber") String hsnNumber
			) {
		
		logger.info(
				"Handling update company request. {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} ",
				companyId, compName, compEmailId, compUrl, compIec, compPan, compTin, compIncorporationNumber, compType,
				industryType, addrLine1, addrLine2, addrLine3, city, state, country, pinCode, prodSiteAddr,
				contPersonName, contPersonEmail, contPersonPhone, moowrNumber, gstProductionPlant);
		
		
		Company company = new Company();

		company.setId(companyId);
		company.setUsername(username);
		company.setCompName(compName);
		company.setCompEmailId(compEmailId);
		company.setCompUrl(compUrl);
		company.setCompIec(compIec);
		company.setCompPan(compPan);
		company.setCompTin(compTin);
		company.setCompIncorporationNumber(compIncorporationNumber);
		company.setCompType(compType);
		company.setIndustryType(industryType);

		company.setAddrLine1(addrLine1);
		company.setAddrLine2(addrLine2);
		company.setAddrLine3(addrLine3);
		company.setCity(city);
		company.setState(state);
		company.setCountry(country);
		company.setPinCode(pinCode);
		company.setProdSiteAddr(prodSiteAddr);
		
		company.setContPersonName(contPersonName);
		company.setContPersonEmail(contPersonEmail);
		company.setContPersonPhone(contPersonPhone);
		
		company.setMoowrNumber(moowrNumber);
		company.setGstProductionPlant(gstProductionPlant);
		company.setHsnNumber(hsnNumber);
		
		companyRepository.update(company);
		return "update-company";
	}
	
	/**
	 * Shows Add Companies with upload file page
	 */
	@GetMapping("/add-companies")
	public String showAddCompaniesForm() {
		logger.info("Showing add companies page.");
		return "add-companies";
	}

	/**
	 * Processes bulk companies creation via file upload.
	 */
	@PostMapping("/add-companies")
	public String addCompanies(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		
		logger.info("Handling companies creation via file upload request. Filename: {}", file.getOriginalFilename());

		List<Company> companies;
		try {

			if (file.isEmpty() || file.getSize() > 2097152) {
				logger.info("Uploaded file is empty or exceeds 2MB max size allowed. Filename: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload a file. Max 2MB file size is allowed.");
				return "redirect:add-companies";
			}

			if (! (file.getOriginalFilename().toLowerCase().endsWith(".xls") || file.getOriginalFilename().toLowerCase().endsWith(".xlsx"))) {
				logger.info("Uploaded file is not a .xls or .xlsx file. Filename: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error", "Please upload .xls or .xlsx file.");
				return "redirect:add-companies";
			}

			Path copyLocation = Paths.get(appTempUploadDir + File.separator + UUID.randomUUID().toString()
					+ StringUtils.cleanPath(file.getOriginalFilename()));
			try {
				Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				logger.info("Unable to save file: {}", file.getOriginalFilename());
				redirectAttributes.addFlashAttribute("error",
						"Unable to save file: " + file.getOriginalFilename() + ". Please try again.");
				return "redirect:add-companies";
			}

			companies = parseCompaniesDataFromXlsFile(copyLocation.toFile());
			List<String> errors = new ArrayList<String>();
			List<String> infos = new ArrayList<String>();
			errors = validateCompanyData(companies, "comp1");
			if (errors.isEmpty()) {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				for (Company company : companies) {
					company.setUsername(GeneralUtility.generateUsername(company.getCompName()));
					company.setPassword(passwordEncoder.encode(GeneralUtility.shuffleData(company.getUsername())));
					System.out.println("Saving " + company.getCompName());
					logger.info("Saving company. Company Name: {}, Generated Username: {}", company.getCompName(), company.getUsername());
					try {
						if (!companyRepository.isCompanyPresent(company)) {
							companyRepository.save(company);
							infos.add("Company added. Company Name: " + company.getCompName() + ", Generated Username: " + company.getUsername());
							logger.info("Company added. Company Name: {}, Generated Username: {}", company.getCompName(), company.getUsername());
						} else {
							errors.add("Cannot add company. Company already present with this company name: "
									+ company.getCompName());
							logger.warn("Cannot add company. Company already present. Company Name: {}", company.getCompName());
						}
					} catch (DuplicateKeyException e) {
						errors.add("Cannot add company: " + company.getCompName() +  ". Some of follwing data is duplicate: IEC or PAN or TIN or Incorporation Number or GSP Production Plant or Moowr Number.");
						logger.error("Cannot add company. Some of follwing data is duplicate: IEC or PAN or TIN or Incorporation Number or GSP Production Plant or Moowr Number. Company Name : {}", company.getCompName(), e);
					}
				}
			}
			
			if (!infos.isEmpty()) {
				redirectAttributes.addFlashAttribute("infos", infos);
			}
			
			if(!errors.isEmpty()) {
				redirectAttributes.addFlashAttribute("errors", errors);
				return "redirect:/add-companies";
			}
		} catch (Exception e) {
			logger.error("Unable to upload file: {}", file.getOriginalFilename(), e);
			redirectAttributes.addFlashAttribute("error", "Unable to upload " + file.getOriginalFilename());
			return "redirect:/add-companies";
		}
		
		logger.info("Created companies via file upload: {}", file.getOriginalFilename());
		redirectAttributes.addFlashAttribute("info", "Created " + companies.size() + " companies via " + file.getOriginalFilename() + " file upload.");
		return "redirect:/add-companies";
	}
	
	public List<Company> parseCompaniesDataFromXlsFile(File file) {

		Workbook workbook = null;
		// Create the Workbook
		try {
			workbook = WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | IOException e) {
			logger.error("Unable to process add company file. Filename: {} ", file.getName(), e);
		}

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Getting number of columns in the Sheet
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		logger.info("Add companies file has {} sheets and sheet 0 has {} columns", workbook.getNumberOfSheets(),
				noOfColumns);

		// header list
		List<String> headers = new ArrayList<>();
		headers.add("compName");
		headers.add("compEmailId");
		headers.add("compUrl");
		headers.add("compIec");
		headers.add("compPan");
		headers.add("compTin");
		headers.add("compIncorporationNumber");
		headers.add("compType");
		headers.add("industryType");
		headers.add("addrLine1");
		headers.add("addrLine2");
		headers.add("addrLine3");
		headers.add("city");
		headers.add("state");
		headers.add("country");
		headers.add("pinCode");
		headers.add("prodSiteAddr");
		headers.add("contPersonName");
		headers.add("contPersonEmail");
		headers.add("contPersonPhone");
		headers.add("moowrNumber");
		headers.add("gstProductionPlant");
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
		List<Company> companies = loadCompanyData(sheetData);

		// Closing the workbook
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return companies;
	}

	private List<String> validateCompanyData(List<Company> companies, String username) {
		List<String> validationErrors = new ArrayList<String>();
		int rowCounter = 1;
		for (Company company : companies) {
			rowCounter++;
			List<String> errors = new ArrayList<String>();
			if (company == null) {
				errors.add("Row: " + rowCounter + " - No company data in file.");
				continue;
			}

			if (company.getCompIec() == null || company.getCompIec().equals("")) {
				errors.add("Row: " + rowCounter + " - IEC is required in Item data.");
			} else {
				if (!Constants.validateByRegex(Constants.IEC_REGEX, company.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid IEC. IEC can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getCompName() == null || company.getCompName().equals("")) {
				errors.add("Row: " + rowCounter + " - Company name is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.COMP_NAME_REGEX, company.getCompName())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company name. Company name can contain alphanumeric characters and can be at max 40 characters in length.");
				}
			}

			if (company.getCompEmailId() == null || company.getCompEmailId().equals("")) {
				errors.add("Row: " + rowCounter + " - Company email is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.EMAIL_REGEX, company.getCompEmailId())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company email. Company email can contain alphanumeric and _ - . @ characters and can be at max 40 characters in length.");
				}
			}
			
			if (company.getCompUrl() == null || company.getCompUrl().equals("")) {
				errors.add("Row: " + rowCounter + " - Company URL is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.COMP_URL_REGEX, company.getCompUrl())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company URL. Company URL can contain alphanumeric and _ - . / characters and can be at max 40 characters in length.");
				}
			}
			
			if (company.getCompIec() == null || company.getCompIec().equals("")) {
				errors.add("Row: " + rowCounter + " - Company IEC is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.IEC_REGEX, company.getCompIec())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company IEC. Company IEC can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getCompPan() == null || company.getCompPan().equals("")) {
				errors.add("Row: " + rowCounter + " - Company PAN is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.PAN_REGEX, company.getCompPan())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company PAN. Company PAN can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getCompTin() == null || company.getCompTin().equals("")) {
				errors.add("Row: " + rowCounter + " - Company TIN is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.TIN_REGEX, company.getCompTin())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company TIN. Company TIN can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getCompIncorporationNumber() == null || company.getCompIncorporationNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - Company Incorporation Number is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.COMP_INCORPORATION_NUMBER_REGEX, company.getCompIncorporationNumber())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company Incorporation Number. Company Incorporation Number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getCompType() == null || company.getCompType().equals("")) {
				errors.add("Row: " + rowCounter + " - Company Type is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.COMP_TYPE_REGEX, company.getCompType())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company Type. Company Type can be private or public.");
				}
			}
			
			if (company.getIndustryType() == null || company.getIndustryType().equals("")) {
				errors.add("Row: " + rowCounter + " - Company Industry Type is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.INDUSTRY_TYPE_REGEX, company.getIndustryType())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company Industry Type. Company Industry Type can be private or public.");
				}
			}
			
			if (company.getAddrLine1() == null || company.getAddrLine1().equals("")) {
				errors.add("Row: " + rowCounter + " - Company Address Line 1 is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.ADDR_LINE_1_REGEX, company.getAddrLine1())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Company Address Line 1. Company Address Line 1 can contain alphanumeric characters and can be at max 40 characters in length.");
				}
			}
			
			if (!Constants.validateByRegex(Constants.ADDR_LINE_2_REGEX, company.getAddrLine2())) {
				errors.add("Row: " + rowCounter
						+ " - Invalid Company Address Line 2. Company Address Line 2 can contain alphanumeric characters and can be at max 40 characters in length.");
			}
			
			if (!Constants.validateByRegex(Constants.ADDR_LINE_3_REGEX, company.getAddrLine3())) {
				errors.add("Row: " + rowCounter
						+ " - Invalid Company Address Line 3. Company Address Line 3 can contain alphanumeric characters and can be at max 40 characters in length.");
			}
			
			if (company.getCity() == null || company.getCity().equals("")) {
				errors.add("Row: " + rowCounter + " - City is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.CITY_REGEX, company.getCity())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid City. City can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getState() == null || company.getState().equals("")) {
				errors.add("Row: " + rowCounter + " - State is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.STATE_REGEX, company.getState())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid State. State can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getCountry() == null || company.getCountry().equals("")) {
				errors.add("Row: " + rowCounter + " - Country is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.COUNTRY_REGEX, company.getCountry())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Country. Country can contain alphanumeric characters and can be at max 15 characters in length.");
				}
			}
			
			if (company.getPinCode() == null || company.getPinCode().equals("")) {
				errors.add("Row: " + rowCounter + " - PIN is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.PIN_REGEX, company.getPinCode())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid PIN. PIN can be 6 digit number.");
				}
			}
			
			if (company.getProdSiteAddr() == null || company.getProdSiteAddr().equals("")) {
				errors.add("Row: " + rowCounter + " - Production Site Address is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.PROD_SITE_ADDR_REGEX, company.getProdSiteAddr())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Production Site Address. Production Site Address can contain alphanumeric characters and can be at max 120 characters in length.");
				}
			}

			if (company.getContPersonName() == null || company.getContPersonName().equals("")) {
				errors.add("Row: " + rowCounter + " - Contact person name is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.CONT_PERSON_NAME_REGEX, company.getContPersonName())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Contact person name. Contact person name can contain alphanumeric characters and can be at max 40 characters in length.");
				}
			}
			
			if (company.getContPersonEmail() == null || company.getContPersonEmail().equals("")) {
				errors.add("Row: " + rowCounter + " - Contact person email is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.EMAIL_REGEX, company.getContPersonEmail())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Contact person email. Contact person email can contain alphanumeric characters and can be at max 40 characters in length.");
				}
			}
			
			if (company.getContPersonPhone() == null || company.getContPersonPhone().equals("")) {
				errors.add("Row: " + rowCounter + " - Contact person phone is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.PHONE_REGEX, company.getContPersonPhone())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Contact person phone. Contact person phone can be 10 digit number.");
				}
			}
			
			if (company.getMoowrNumber() == null || company.getMoowrNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - MOOWR Number is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.MOOWR_NUMBER_REGEX, company.getMoowrNumber())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid MOOWR Number. MOOWR Number can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getGstProductionPlant() == null || company.getGstProductionPlant().equals("")) {
				errors.add("Row: " + rowCounter + " - Production plant GST is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.GST_NUMBER_REGEX, company.getGstProductionPlant())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid Production plant GST. Production plant GST can contain alphanumeric characters and can be at max 20 characters in length.");
				}
			}
			
			if (company.getHsnNumber() == null || company.getHsnNumber().equals("")) {
				errors.add("Row: " + rowCounter + " - HSN is required in company data.");
			} else {
				if (!Constants.validateByRegex(Constants.HSN_REGEX, company.getHsnNumber())) {
					errors.add("Row: " + rowCounter
							+ " - Invalid HSN. HSN can contain alphanumeric characters and can be at max 8 characters in length.");
				}
			}
			
			validationErrors.addAll(errors);
		}

		return validationErrors;
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
	
	/**
	 * Load parsed company data in Company objects.
	 * 
	 * @param sheetData parsed company data from file
	 * @return list of Company objects
	 */
	private List<Company> loadCompanyData(List<Map<String, String>> sheetData) {

		List<Company> companies = new ArrayList<Company>();
		for (Map<String, String> row : sheetData) {
			Company company = null;
			if (row != null) {
				company = new Company(
						row.get("compName"),
						row.get("compEmailId"),
						row.get("compUrl"),
						row.get("compIec"),
						row.get("compPan"),
						row.get("compTin"),
						row.get("compIncorporationNumber"),
						row.get("compType"),
						row.get("industryType"),
						row.get("addrLine1"),
						row.get("addrLine2"),
						row.get("addrLine3"),
						row.get("city"),
						row.get("state"),
						row.get("country"),
						row.get("pinCode"),
						row.get("prodSiteAddr"),
						row.get("contPersonName"),
						row.get("contPersonEmail"),
						row.get("contPersonPhone"),
						row.get("moowrNumber"),
						row.get("gstProductionPlant"),
						row.get("hsnNumber"));
			}
			
			companies.add(company);
		}
		return companies;
	}
}
