package com.ap.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sale_master")
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "comp_iec")
	private String compIec;

	@Column(name = "part_number")
	private String partNumber;

	@Column(name = "part_desc")
	private String partDesc;

	@Column(name = "uom")
	private String uom;

	@Column(name = "type_of_sale")
	private String typeOfSale;

	@Column(name = "quantity")
	private String quantity;

	@Column(name = "hsn_number")
	private String hsnNumber;

	@Column(name = "temp_invoice_number")
	private String tempInvoiceNumber;

	@Column(name = "temp_invoice_date")
	private Date tempInvoiceDate;

	@Column(name = "gst_invoice_number")
	private String gstInvoiceNumber;

	@Column(name = "gst_invoice_date")
	private Date gstInvoiceDate;

	@Column(name = "shb_number")
	private String shbNumber;

	@Column(name = "shb_date")
	private Date shbDate;

	@Column(name = "port_of_export")
	private String portOfExport;

	@Column(name = "details_of_insurance")
	private String detailsOfInsurance;

	@Column(name = "per_unit_assessible_value")
	private String perUnitAssessibleValue;

	@Column(name = "assessible_value")
	private String assessibleValue;

	@Column(name = "duty_gst")
	private String dutyGst;

	@Column(name = "duty_cess_on_gst")
	private String dutyCessOnGst;

	@Column(name = "vehicle_reg_number")
	private String vehicleRegNumber;

	@Column(name = "one_time_lock_number")
	private String oneTimeLockNumber;

	@Column(name = "goods_removal_ts")
	private Date goodsRemovalTs;

	@Column(name = "eway_bill_number")
	private String ewayBillNumber;

	@Column(name = "eway_bill_date")
	private Date ewayBillDate;

	@Column(name = "remark")
	private String remark;

	@Column(name = "pur_batch_number")
	private String purBatchNumber;

	@Column(name = "prod_batch_number")
	private String prodBatchNumber;
	
	@Column(name = "ex_bond_boe_no")
	private String exBondBoeNo;
	
	@Column(name = "ex_bond_boe_date")
	private Date exBondBoeDate;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_date")
	private String updateDate;

	public Sale() {
		super();
	}

	public Sale(String username, String compIec, String partNumber, String partDesc, String uom, String typeOfSale,
			String quantity, String hsnNumber, String tempInvoiceNumber, String tempInvoiceDate, String gstInvoiceNumber,
			String gstInvoiceDate, String shbNumber, String shbDate, String portOfExport, String detailsOfInsurance,
			String perUnitAssessibleValue, String assessibleValue, String dutyGst, String dutyCessOnGst,
			String vehicleRegNumber, String oneTimeLockNumber, String goodsRemovalTs,
			String ewayBillNumber, String ewayBillDate, String remark, String purBatchNumber, String prodBatchNumber, String exBondBoeNo, String exBondBoeDate) {
		super();
		this.username = username;
		this.compIec = compIec;
		this.partNumber = partNumber;
		this.partDesc = partDesc;
		this.uom = uom;
		this.typeOfSale = typeOfSale;
		this.quantity = quantity;
		this.hsnNumber = hsnNumber;
		this.tempInvoiceNumber = tempInvoiceNumber;
		//this.tempInvoiceDate = tempInvoiceDate;
		this.gstInvoiceNumber = gstInvoiceNumber;
		//this.gstInvoiceDate = gstInvoiceDate;
		this.shbNumber = shbNumber;
		//this.shbDate = shbDate;
		this.portOfExport = portOfExport;
		this.detailsOfInsurance = detailsOfInsurance;
		this.perUnitAssessibleValue = perUnitAssessibleValue;
		this.assessibleValue = assessibleValue;
		this.dutyGst = dutyGst;
		this.dutyCessOnGst = dutyCessOnGst;
		this.vehicleRegNumber = vehicleRegNumber;
		this.oneTimeLockNumber = oneTimeLockNumber;
		//this.goodsRemovalTs = goodsRemovalTs;
		this.ewayBillNumber = ewayBillNumber;
		//this.ewayBillDate = ewayBillDate;
		this.remark = remark;
		this.purBatchNumber = purBatchNumber;
		this.prodBatchNumber = prodBatchNumber;
		this.exBondBoeNo = exBondBoeNo;
		//this.exBondBoeDate = exBondBoeDate;
	}
	
	public Sale(String username, String compIec, String partNumber, String partDesc, String uom, String typeOfSale,
			String quantity, String hsnNumber, String tempInvoiceNumber, Date tempInvoiceDate, String gstInvoiceNumber,
			Date gstInvoiceDate, String shbNumber, Date shbDate, String portOfExport, String detailsOfInsurance,
			String perUnitAssessibleValue, String assessibleValue, String dutyGst, String dutyCessOnGst,
			String vehicleRegNumber, String oneTimeLockNumber, Date goodsRemovalTs,
			String ewayBillNumber, Date ewayBillDate, String remark, String purBatchNumber, String prodBatchNumber, String exBondBoeNo, Date exBondBoeDate) {
		super();
		this.username = username;
		this.compIec = compIec;
		this.partNumber = partNumber;
		this.partDesc = partDesc;
		this.uom = uom;
		this.typeOfSale = typeOfSale;
		this.quantity = quantity;
		this.hsnNumber = hsnNumber;
		this.tempInvoiceNumber = tempInvoiceNumber;
		this.tempInvoiceDate = tempInvoiceDate;
		this.gstInvoiceNumber = gstInvoiceNumber;
		this.gstInvoiceDate = gstInvoiceDate;
		this.shbNumber = shbNumber;
		this.shbDate = shbDate;
		this.portOfExport = portOfExport;
		this.detailsOfInsurance = detailsOfInsurance;
		this.perUnitAssessibleValue = perUnitAssessibleValue;
		this.assessibleValue = assessibleValue;
		this.dutyGst = dutyGst;
		this.dutyCessOnGst = dutyCessOnGst;
		this.vehicleRegNumber = vehicleRegNumber;
		this.oneTimeLockNumber = oneTimeLockNumber;
		this.goodsRemovalTs = goodsRemovalTs;
		this.ewayBillNumber = ewayBillNumber;
		this.ewayBillDate = ewayBillDate;
		this.remark = remark;
		this.purBatchNumber = purBatchNumber;
		this.prodBatchNumber = prodBatchNumber;
		this.exBondBoeNo = exBondBoeNo;
		this.exBondBoeDate = exBondBoeDate;
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

	public String getCompIec() {
		return compIec;
	}

	public void setCompIec(String compIec) {
		this.compIec = compIec;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartDesc() {
		return partDesc;
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getTypeOfSale() {
		return typeOfSale;
	}

	public void setTypeOfSale(String typeOfSale) {
		this.typeOfSale = typeOfSale;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getHsnNumber() {
		return hsnNumber;
	}

	public void setHsnNumber(String hsnNumber) {
		this.hsnNumber = hsnNumber;
	}

	public String getTempInvoiceNumber() {
		return tempInvoiceNumber;
	}

	public void setTempInvoiceNumber(String tempInvoiceNumber) {
		this.tempInvoiceNumber = tempInvoiceNumber;
	}

	public Date getTempInvoiceDate() {
		return tempInvoiceDate;
	}

	public void setTempInvoiceDate(Date tempInvoiceDate) {
		this.tempInvoiceDate = tempInvoiceDate;
	}

	public String getGstInvoiceNumber() {
		return gstInvoiceNumber;
	}

	public void setGstInvoiceNumber(String gstInvoiceNumber) {
		this.gstInvoiceNumber = gstInvoiceNumber;
	}

	public Date getGstInvoiceDate() {
		return gstInvoiceDate;
	}

	public void setGstInvoiceDate(Date gstInvoiceDate) {
		this.gstInvoiceDate = gstInvoiceDate;
	}

	public String getShbNumber() {
		return shbNumber;
	}

	public void setShbNumber(String shbNumber) {
		this.shbNumber = shbNumber;
	}

	public Date getShbDate() {
		return shbDate;
	}

	public void setShbDate(Date shbDate) {
		this.shbDate = shbDate;
	}

	public String getPortOfExport() {
		return portOfExport;
	}

	public void setPortOfExport(String portOfExport) {
		this.portOfExport = portOfExport;
	}

	public String getDetailsOfInsurance() {
		return detailsOfInsurance;
	}

	public void setDetailsOfInsurance(String detailsOfInsurance) {
		this.detailsOfInsurance = detailsOfInsurance;
	}

	public String getPerUnitAssessibleValue() {
		return perUnitAssessibleValue;
	}

	public void setPerUnitAssessibleValue(String perUnitAssessibleValue) {
		this.perUnitAssessibleValue = perUnitAssessibleValue;
	}

	public String getAssessibleValue() {
		return assessibleValue;
	}

	public void setAssessibleValue(String assessibleValue) {
		this.assessibleValue = assessibleValue;
	}

	public String getDutyGst() {
		return dutyGst;
	}

	public void setDutyGst(String dutyGst) {
		this.dutyGst = dutyGst;
	}

	public String getDutyCessOnGst() {
		return dutyCessOnGst;
	}

	public void setDutyCessOnGst(String dutyCessOnGst) {
		this.dutyCessOnGst = dutyCessOnGst;
	}

	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	public String getOneTimeLockNumber() {
		return oneTimeLockNumber;
	}

	public void setOneTimeLockNumber(String oneTimeLockNumber) {
		this.oneTimeLockNumber = oneTimeLockNumber;
	}

	public Date getGoodsRemovalTs() {
		return goodsRemovalTs;
	}

	public void setGoodsRemovalTs(Date goodsRemovalTs) {
		this.goodsRemovalTs = goodsRemovalTs;
	}

	public String getEwayBillNumber() {
		return ewayBillNumber;
	}

	public void setEwayBillNumber(String ewayBillNumber) {
		this.ewayBillNumber = ewayBillNumber;
	}

	public Date getEwayBillDate() {
		return ewayBillDate;
	}

	public void setEwayBillDate(Date ewayBillDate) {
		this.ewayBillDate = ewayBillDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPurBatchNumber() {
		return purBatchNumber;
	}

	public void setPurBatchNumber(String purBatchNumber) {
		this.purBatchNumber = purBatchNumber;
	}

	public String getProdBatchNumber() {
		return prodBatchNumber;
	}

	public void setProdBatchNumber(String prodBatchNumber) {
		this.prodBatchNumber = prodBatchNumber;
	}

	public String getExBondBoeNo() {
		return exBondBoeNo;
	}

	public void setExBondBoeNo(String exBondBoeNo) {
		this.exBondBoeNo = exBondBoeNo;
	}

	public Date getExBondBoeDate() {
		return exBondBoeDate;
	}

	public void setExBondBoeDate(Date exBondBoeDate) {
		this.exBondBoeDate = exBondBoeDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "Sales [id=" + id + ", username=" + username + ", compIec=" + compIec + ", partNumber=" + partNumber
				+ ", partDesc=" + partDesc + ", uom=" + uom + ", typeOfSale=" + typeOfSale + ", quantity=" + quantity
				+ ", hsnNo=" + hsnNumber + ", tempInvoiceNumber=" + tempInvoiceNumber + ", tempInvoiceDate="
				+ tempInvoiceDate + ", gstInvoiceNumber=" + gstInvoiceNumber + ", gstInvoiceDate=" + gstInvoiceDate
				+ ", shbNumber=" + shbNumber + ", shbDate=" + shbDate + ", portOfExport=" + portOfExport
				+ ", detailsOfInsurance=" + detailsOfInsurance + ", perUnitAssessibleValue=" + perUnitAssessibleValue
				+ ", assessibleValue=" + assessibleValue + ", dutyGst=" + dutyGst + ", dutyCessOnGst=" + dutyCessOnGst
				+ ", vehicleRegNumber=" + vehicleRegNumber + ", oneTimeLockNumber=" + oneTimeLockNumber
				+ ", goodsRemovalDate=" + goodsRemovalTs
				+ ", ewayBillNumber=" + ewayBillNumber + ", ewayBillDate=" + ewayBillDate + ", remark=" + remark
				+ ", purBatchNumber=" + purBatchNumber + ", prodBatchNumber=" + prodBatchNumber + ", exBondBoeNo=" + exBondBoeNo + ", exBondBoeDate=" + exBondBoeDate + ", createdDate="
				+ createdDate + ", updateDate=" + updateDate + "]";
	}

	
}
