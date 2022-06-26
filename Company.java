package com.ap.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company_master")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "comp_name")
	private String compName;

	@Column(name = "comp_email_id")
	private String compEmailId;

	@Column(name = "comp_url")
	private String compUrl;

	@Column(name = "comp_iec")
	private String compIec;

	@Column(name = "comp_pan")
	private String compPan;

	@Column(name = "comp_tin")
	private String compTin;

	@Column(name = "comp_incorporation_number")
	private String compIncorporationNumber;

	@Column(name = "comp_type")
	private String compType;

	@Column(name = "industry_type")
	private String industryType;

	@Column(name = "addr_line_1")
	private String addrLine1;

	@Column(name = "addr_line_2")
	private String addrLine2;

	@Column(name = "addr_line_3")
	private String addrLine3;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "country")
	private String country;

	@Column(name = "pin_code")
	private String pinCode;

	@Column(name = "prod_site_addr")
	private String prodSiteAddr;

	@Column(name = "cont_person_name")
	private String contPersonName;

	@Column(name = "cont_person_email")
	private String contPersonEmail;

	@Column(name = "cont_person_phone")
	private String contPersonPhone;

	@Column(name = "moowr_number")
	private String moowrNumber;

	@Column(name = "gst_production_plant")
	private String gstProductionPlant;

	@Column(name = "hsn_number")
	private String hsnNumber;
	
	@Column(name = "active")
	private boolean active;

	public Company() {
		super();
	}

	/**
	 * Used to load user data from file to object.
	 * 
	 * @param compName
	 * @param compEmailId
	 * @param compUrl
	 * @param compIec
	 * @param compPan
	 * @param compTin
	 * @param compIncorporationNumber
	 * @param compType
	 * @param industryType
	 * @param addrLine1
	 * @param addrLine2
	 * @param addrLine3
	 * @param city
	 * @param state
	 * @param country
	 * @param pinCode
	 * @param prodSiteAddr
	 * @param contPersonName
	 * @param contPersonEmail
	 * @param contPersonPhone
	 * @param moowrNumber
	 * @param gstProductionPlant
	 */
	public Company(String compName, 
			String compEmailId, 
			String compUrl, 
			String compIec, 
			String compPan, 
			String compTin,
			String compIncorporationNumber, 
			String compType, 
			String industryType, 
			String addrLine1, 
			String addrLine2,
			String addrLine3, 
			String city, 
			String state, 
			String country,
			String pinCode, 
			String prodSiteAddr,
			String contPersonName, 
			String contPersonEmail, 
			String contPersonPhone, 
			String moowrNumber,
			String gstProductionPlant,
			String hsnNumber) {
		super();
		this.compName = compName;
		this.compEmailId = compEmailId;
		this.compUrl = compUrl;
		this.compIec = compIec;
		this.compPan = compPan;
		this.compTin = compTin;
		this.compIncorporationNumber = compIncorporationNumber;
		this.compType = compType;
		this.industryType = industryType;
		this.addrLine1 = addrLine1;
		this.addrLine2 = addrLine2;
		this.addrLine3 = addrLine3;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pinCode = pinCode;
		this.prodSiteAddr = prodSiteAddr;
		this.contPersonName = contPersonName;
		this.contPersonEmail = contPersonEmail;
		this.contPersonPhone = contPersonPhone;
		this.moowrNumber = moowrNumber;
		this.gstProductionPlant = gstProductionPlant;
		this.hsnNumber = hsnNumber;
	}
	
	public Company(Long id, String username, String password, Long roleId,
			String compName, String compEmailId, String compUrl, String compIec, String compPan, String compTin,
			String compIncorporationNumber, String compType, String industryType, String addrLine1, String addrLine2,
			String addrLine3, String city, String state, String country, String pinCode, String prodSiteAddr,
			String contPersonName, String contPersonEmail, String contPersonPhone, String moowrNumber,
			String gstProductionPlant, String hsnNumber) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.roleId = roleId;
		this.compName = compName;
		this.compEmailId = compEmailId;
		this.compUrl = compUrl;
		this.compIec = compIec;
		this.compPan = compPan;
		this.compTin = compTin;
		this.compIncorporationNumber = compIncorporationNumber;
		this.compType = compType;
		this.industryType = industryType;
		this.addrLine1 = addrLine1;
		this.addrLine2 = addrLine2;
		this.addrLine3 = addrLine3;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pinCode = pinCode;
		this.prodSiteAddr = prodSiteAddr;
		this.contPersonName = contPersonName;
		this.contPersonEmail = contPersonEmail;
		this.contPersonPhone = contPersonPhone;
		this.moowrNumber = moowrNumber;
		this.gstProductionPlant = gstProductionPlant;
		this.hsnNumber = hsnNumber;
	}
	
	public Company(String username, String password, Long roleId,
			String compName, String compEmailId, String compUrl, String compIec, String compPan, String compTin,
			String compIncorporationNumber, String compType, String industryType, String addrLine1, String addrLine2,
			String addrLine3, String city, String state, String country, String pinCode, String prodSiteAddr,
			String contPersonName, String contPersonEmail, String contPersonPhone, String moowrNumber,
			String gstProductionPlant, String hsnNumber) {
		
		super();
		
		this.username = username;
		this.password = password;
		this.roleId = roleId;
		
		this.compName = compName;
		this.compEmailId = compEmailId;
		this.compUrl = compUrl;
		this.compIec = compIec;
		this.compPan = compPan;
		this.compTin = compTin;
		this.compIncorporationNumber = compIncorporationNumber;
		this.compType = compType;
		this.industryType = industryType;
		
		this.addrLine1 = addrLine1;
		this.addrLine2 = addrLine2;
		this.addrLine3 = addrLine3;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pinCode = pinCode;
		this.prodSiteAddr = prodSiteAddr;
		
		this.contPersonName = contPersonName;
		this.contPersonEmail = contPersonEmail;
		this.contPersonPhone = contPersonPhone;
		
		this.moowrNumber = moowrNumber;
		this.gstProductionPlant = gstProductionPlant;
		this.hsnNumber = hsnNumber;
	}

	public Company(Long id, String username, String password, Long roleId) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.roleId = roleId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompEmailId() {
		return compEmailId;
	}

	public void setCompEmailId(String compEmailId) {
		this.compEmailId = compEmailId;
	}

	public String getCompUrl() {
		return compUrl;
	}

	public void setCompUrl(String compUrl) {
		this.compUrl = compUrl;
	}

	public String getCompIec() {
		return compIec;
	}

	public void setCompIec(String compIec) {
		this.compIec = compIec;
	}

	public String getCompPan() {
		return compPan;
	}

	public void setCompPan(String compPan) {
		this.compPan = compPan;
	}

	public String getCompTin() {
		return compTin;
	}

	public void setCompTin(String compTin) {
		this.compTin = compTin;
	}

	public String getCompIncorporationNumber() {
		return compIncorporationNumber;
	}

	public void setCompIncorporationNumber(String compIncorporationNumber) {
		this.compIncorporationNumber = compIncorporationNumber;
	}

	public String getCompType() {
		return compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public String getAddrLine3() {
		return addrLine3;
	}

	public void setAddrLine3(String addrLine3) {
		this.addrLine3 = addrLine3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getProdSiteAddr() {
		return prodSiteAddr;
	}

	public void setProdSiteAddr(String prodSiteAddr) {
		this.prodSiteAddr = prodSiteAddr;
	}

	public String getContPersonName() {
		return contPersonName;
	}

	public void setContPersonName(String contPersonName) {
		this.contPersonName = contPersonName;
	}

	public String getContPersonEmail() {
		return contPersonEmail;
	}

	public void setContPersonEmail(String contPersonEmail) {
		this.contPersonEmail = contPersonEmail;
	}

	public String getContPersonPhone() {
		return contPersonPhone;
	}

	public void setContPersonPhone(String contPersonPhone) {
		this.contPersonPhone = contPersonPhone;
	}

	public String getMoowrNumber() {
		return moowrNumber;
	}

	public void setMoowrNumber(String moowrNumber) {
		this.moowrNumber = moowrNumber;
	}

	public String getGstProductionPlant() {
		return gstProductionPlant;
	}

	public void setGstProductionPlant(String gstProductionPlant) {
		this.gstProductionPlant = gstProductionPlant;
	}

	public String getHsnNumber() {
		return hsnNumber;
	}

	public void setHsnNumber(String hsnNumber) {
		this.hsnNumber = hsnNumber;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", roleId=" + roleId + ", compName=" + compName + ", compEmailId="
				+ compEmailId + ", compUrl=" + compUrl + ", compIec=" + compIec + ", compPan=" + compPan + ", compTin="
				+ compTin + ", compIncorporationNumber=" + compIncorporationNumber + ", compType=" + compType
				+ ", industryType=" + industryType + ", addrLine1=" + addrLine1 + ", addrLine2=" + addrLine2
				+ ", addrLine3=" + addrLine3 + ", city=" + city + ", state=" + state + ", country=" + country
				+ ", pinCode=" + pinCode + ", prodSiteAddr=" + prodSiteAddr + ", contPersonName=" + contPersonName
				+ ", contPersonEmail=" + contPersonEmail + ", contPersonPhone=" + contPersonPhone + ", moowrNumber="
				+ moowrNumber + ", gstProductionPlant=" + gstProductionPlant + ", hsnNumber=" + hsnNumber + "]";
	}

}
