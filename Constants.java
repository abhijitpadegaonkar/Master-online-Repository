package com.ap.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

	// Company
	public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{1,10}$";
	public static final String COMP_NAME_REGEX = "^[a-zA-Z0-9]{1,40}$";
	public static final String COMP_URL_REGEX = "^[a-zA-Z0-9\\-._\\/]{1,40}$";
	public static final String COMP_INCORPORATION_NUMBER_REGEX = "^[a-zA-Z0-9]{1,20}$";
	public static final String COMP_TYPE_REGEX = "^(?i)(private|public)$";
	public static final String INDUSTRY_TYPE_REGEX = "^[a-zA-Z0-9]{1,10}$";
	public static final String CONT_PERSON_NAME_REGEX = "^[a-zA-Z0-9 ]{1,40}$";
	
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.@-]{1,40}$";
	public static final String PHONE_REGEX = "^[0-9]{1,10}$";
	public static final String IEC_REGEX = "^[a-zA-Z0-9]{1,20}$";
	public static final String PAN_REGEX = "^[a-zA-Z0-9]{1,20}$";
	public static final String TIN_REGEX = "^[a-zA-Z0-9]{1,20}$";
	public static final String MOOWR_NUMBER_REGEX = "^[a-zA-Z0-9]{1,20}$";
	public static final String GST_NUMBER_REGEX = "^[a-zA-Z0-9]{1,20}$";
	
	public static final String ADDR_LINE_1_REGEX = "^[a-zA-Z0-9 ]{1,40}$";
	public static final String ADDR_LINE_2_REGEX = "^[a-zA-Z0-9 ]{0,40}$";
	public static final String ADDR_LINE_3_REGEX = "^[a-zA-Z0-9 ]{0,40}$";
	public static final String CITY_REGEX = "^[a-zA-Z]{1,20}$";
	public static final String STATE_REGEX = "^[a-zA-Z]{1,20}$";
	public static final String COUNTRY_REGEX = "^[a-zA-Z]{1,15}$";
	public static final String PIN_REGEX = "^[0-9]{6}$";
	
	public static final String PROD_SITE_ADDR_REGEX = "^[a-zA-Z0-9 ]{1,120}$";

	// Item
	public static final String PART_NUMBER_REGEX = "^[a-zA-Z0-9]{1,20}$";
	public static final String PART_DESCRIPTION_REGEX = "^[a-zA-Z0-9 ]{1,40}$";
	public static final String UOM_REGEX = "^(?i)(kg|piece)$";
	public static final String TYPE_OF_PURCHASE_REGEX = "^(?i)(import|domestic)$";
	public static final String TYPE_OF_SALE_REGEX = "^(?i)(export|domestic)$";
	public static final String HSN_REGEX = "^[a-zA-Z0-9]{1,8}$";

	// Purchase
	public static final String PURCHASE_BATCH_NUMBER_REGEX = "^[a-zA-Z0-9]{1,20}$";
	public static final String PRODUCTION_BATCH_NUMBER_REGEX = "^[a-zA-Z0-9]{1,20}$";
	public static final String GRR_NUMBER_REGEX = "^[a-zA-Z0-9]{1,8}$";
	public static final String PURCHASE_INVOICE_NUMBER_REGEX = "^[a-zA-Z0-9]{1,3}$";
	public static final String BOND_BOE_NUMBER_REGEX = "^[a-zA-Z0-9]{1,15}$";
	public static final String PORT_OF_IMPORT_REGEX = "^[a-zA-Z0-9 ]{1,15}$";
	public static final String BOTTLE_SEAL_NUMBER_REGEX = "^[a-zA-Z0-9]{1,15}$";
	public static final String INSURANCE_DETAILS_REGEX = "^[a-zA-Z0-9 ]{1,30}$";
	public static final String VEHICLE_REGISTRATION_NUMBER_REGEX = "^[a-zA-Z0-9]{1,15}$";
	public static final String EWAY_BILL_NUMBER_REGEX = "^[a-zA-Z0-9]{1,10}$";

	// general purpose
	public static final String DATE_REGEX = "^[0-9\\/-]{7,10}$";
	public static final String DATE_TIME_REGEX = "^[0-9\\ /:-]{7,19}$";
	public static final String RATE_REGEX = "^[0-9]{1,2}(?:[.][0-9]{1,2})?$";
	public static final String AMOUNT_REGEX = "^[0-9]{1,8}(?:[.][0-9]{1,2})?$";
	public static final String QUANTITY_REGEX = "^[0-9]{1,10}$";

	// bill
	public static final String SOURCE_OF_MATERIAL_REGEX = "^(?i)(import|domestic)$";
	public static final String TYPE_OF_MATERIAL_REGEX = "^[a-zA-Z0-9]{1,15}$";
	public static final String REMARK_REGEX = "^[a-zA-Z0-9 ]{1,50}$";

	// sale
	public static final String TEMP_INVOICE_NUMBER_REGEX = "^[a-zA-Z0-9]{1,15}$";
	public static final String GST_INVOICE_NUMBER_REGEX = "^[a-zA-Z0-9]{1,15}$";
	public static final String SHB_NUMBER_REGEX = "^[a-zA-Z0-9]{1,15}$";
	public static final String PORT_OF_EXPORT_REGEX = "^[a-zA-Z0-9 ]{1,15}$";
	public static final String ONE_TIME_LOCK_NUMBER_REGEX = "^[a-zA-Z0-9]{1,15}$";
	
	public static boolean validateByRegex(String regex, String str) {
		Pattern p = Pattern.compile(regex);

		if (str == null) {
			return false;
		}

		Matcher m = p.matcher(str);

		return m.matches();
	}

}