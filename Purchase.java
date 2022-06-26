package com.ap.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "purchase_master")
public class Purchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "comp_iec")
	private String compIec;

	@Column(name = "part_number")
	private String partNumber;

	@Column(name = "type_of_purchase")
	private String typeOfPurchase;

	@Column(name = "part_desc")
	private String partDesc;

	@Column(name = "uom")
	private String uom;

	@Column(name = "pur_batch_number")
	private String purBatchNumber;

	@Column(name = "grr_qty")
	private String grrQty;

	@Column(name = "grr_no")
	private String grrNo;

	@Column(name = "pur_inv_no")
	private String purInvNo;

	@Column(name = "pur_inv_date")
	private Date purInvDate;

	@Column(name = "in_bond_boe_num")
	private String inBondBoeNum;

	@Column(name = "in_bond_boe_date")
	private Date inBondBoeDate;

	@Column(name = "port_of_import")
	private String portOfImport;

	@Column(name = "bottle_seal_number")
	private String bottleSealNumber;

	@Column(name = "insurance_details")
	private String insuranceDetails;

	@Column(name = "per_unit_cost")
	private String perUnitCost;

	@Column(name = "total_pur_value")
	private String totalPurValue;

	@Column(name = "duty_bcd_rate")
	private String dutyBcdRate;

	@Column(name = "duty_bcd_amt")
	private String dutyBcdAmt;

	@Column(name = "duty_cess_bcd_amt")
	private String dutyCessBcdAmt;

	@Column(name = "duty_cess_bcd_rate")
	private String dutyCessBcdRate;

	@Column(name = "duty_gst_rate")
	private String dutyGstRate;

	@Column(name = "duty_gst_amt")
	private String dutyGstAmt;

	@Column(name = "duty_cess_gst_rate")
	private String dutyCessGstRate;

	@Column(name = "duty_cess_gst_amt")
	private String dutyCessGstAmt;

	@Column(name = "duty_other_rate")
	private String dutyOtherRate;

	@Column(name = "duty_other_amt")
	private String dutyOtherAmt;

	@Column(name = "total_custom_duty")
	private String totalCustomDuty;

	@Column(name = "total_gst")
	private String totalGst;

	@Column(name = "vehicle_reg_num")
	private String vehicleRegNum;

	@Column(name = "warehouse_date")
	private Date warehouseDate;

	@Column(name = "eway_bill_num")
	private String ewayBillNum;

	@Column(name = "eway_bill_date")
	private Date ewayBillDate;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_date")
	private String updateDate;
	
	@Column(name = "hsn_number")
	private String hsnNumber;
	
	@Column(name = "duty_paid_amount")
	private String dutyPaidAmount;
	
	@Column(name = "balance_duty")
	private String balanceDuty;

	public Purchase() {
		super();
	}

	public Purchase(String username, String compIec, String partNumber, String typeOfPurchase, String partDesc,
			String uom, String purBatchNumber, String grrQty, String grrNo, String purInvNo, Date purInvDate,
			String inBondBoeNum, String inBondBoeDate, String portOfImport, String bottleSealNumber,
			String insuranceDetails, String perUnitCost, String totalPurValue, String dutyBcdRate, String dutyBcdAmt,
			String dutyCessBcdAmt, String dutyCessBcdRate, String dutyGstRate, String dutyGstAmt,
			String dutyCessGstRate, String dutyCessGstAmt, String dutyOtherRate, String dutyOtherAmt,
			String totalCustomDuty, String totalGst, String vehicleRegNum, String warehouseDate, String ewayBillNum,
			String ewayBillDate, String createdDate, String updateDate) {
		super();
		this.username = username;
		this.compIec = compIec;
		this.partNumber = partNumber;
		this.typeOfPurchase = typeOfPurchase;
		this.partDesc = partDesc;
		this.uom = uom;
		this.purBatchNumber = purBatchNumber;
		this.grrQty = grrQty;
		this.grrNo = grrNo;
		this.purInvNo = purInvNo;
		this.purInvDate = purInvDate;
		this.inBondBoeNum = inBondBoeNum;
		//this.inBondBoeDate = inBondBoeDate;
		this.portOfImport = portOfImport;
		this.bottleSealNumber = bottleSealNumber;
		this.insuranceDetails = insuranceDetails;
		this.perUnitCost = perUnitCost;
		this.totalPurValue = totalPurValue;
		this.dutyBcdRate = dutyBcdRate;
		this.dutyBcdAmt = dutyBcdAmt;
		this.dutyCessBcdAmt = dutyCessBcdAmt;
		this.dutyCessBcdRate = dutyCessBcdRate;
		this.dutyGstRate = dutyGstRate;
		this.dutyGstAmt = dutyGstAmt;
		this.dutyCessGstRate = dutyCessGstRate;
		this.dutyCessGstAmt = dutyCessGstAmt;
		this.dutyOtherRate = dutyOtherRate;
		this.dutyOtherAmt = dutyOtherAmt;
		this.totalCustomDuty = totalCustomDuty;
		this.totalGst = totalGst;
		this.vehicleRegNum = vehicleRegNum;
		//this.warehouseDate = warehouseDate;
		this.ewayBillNum = ewayBillNum;
		//this.ewayBillDate = ewayBillDate;
		this.createdDate = createdDate;
		this.updateDate = updateDate;
	}

	public Purchase(String username, String compIec, String partNumber, String typeOfPurchase, String partDesc,
			String uom, String purBatchNumber, String grrQty, String grrNo, String purInvNo, Date purInvDate,
			String inBondBoeNum, Date inBondBoeDate, String portOfImport, String bottleSealNumber,
			String insuranceDetails, String perUnitCost, String totalPurValue, String dutyBcdRate, String dutyBcdAmt,
			String dutyCessBcdAmt, String dutyCessBcdRate, String dutyGstRate, String dutyGstAmt,
			String dutyCessGstRate, String dutyCessGstAmt, String dutyOtherRate, String dutyOtherAmt,
			String totalCustomDuty, String totalGst, String vehicleRegNum, Date warehouseDate, String ewayBillNum,
			Date ewayBillDate, String hsnNumber) {
		super();
		this.username = username;
		this.compIec = compIec;
		this.partNumber = partNumber;
		this.typeOfPurchase = typeOfPurchase;
		this.partDesc = partDesc;
		this.uom = uom;
		this.purBatchNumber = purBatchNumber;
		this.grrQty = grrQty;
		this.grrNo = grrNo;
		this.purInvNo = purInvNo;
		this.purInvDate = purInvDate;
		this.inBondBoeNum = inBondBoeNum;
		this.inBondBoeDate = inBondBoeDate;
		this.portOfImport = portOfImport;
		this.bottleSealNumber = bottleSealNumber;
		this.insuranceDetails = insuranceDetails;
		this.perUnitCost = perUnitCost;
		this.totalPurValue = totalPurValue;
		this.dutyBcdRate = dutyBcdRate;
		this.dutyBcdAmt = dutyBcdAmt;
		this.dutyCessBcdAmt = dutyCessBcdAmt;
		this.dutyCessBcdRate = dutyCessBcdRate;
		this.dutyGstRate = dutyGstRate;
		this.dutyGstAmt = dutyGstAmt;
		this.dutyCessGstRate = dutyCessGstRate;
		this.dutyCessGstAmt = dutyCessGstAmt;
		this.dutyOtherRate = dutyOtherRate;
		this.dutyOtherAmt = dutyOtherAmt;
		this.totalCustomDuty = totalCustomDuty;
		this.totalGst = totalGst;
		this.vehicleRegNum = vehicleRegNum;
		this.warehouseDate = warehouseDate;
		this.ewayBillNum = ewayBillNum;
		this.ewayBillDate = ewayBillDate;
		this.hsnNumber = hsnNumber;
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

	public String getTypeOfPurchase() {
		return typeOfPurchase;
	}

	public void setTypeOfPurchase(String typeOfPurchase) {
		this.typeOfPurchase = typeOfPurchase;
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

	public String getPurBatchNumber() {
		return purBatchNumber;
	}

	public void setPurBatchNumber(String purBatchNumber) {
		this.purBatchNumber = purBatchNumber;
	}

	public String getGrrQty() {
		return grrQty;
	}

	public void setGrrQty(String grrQty) {
		this.grrQty = grrQty;
	}

	public String getGrrNo() {
		return grrNo;
	}

	public void setGrrNo(String grrNo) {
		this.grrNo = grrNo;
	}

	public String getPurInvNo() {
		return purInvNo;
	}

	public void setPurInvNo(String purInvNo) {
		this.purInvNo = purInvNo;
	}

	public Date getPurInvDate() {
		return purInvDate;
	}

	public void setPurInvDate(Date purInvDate) {
		this.purInvDate = purInvDate;
	}

	public String getInBondBoeNum() {
		return inBondBoeNum;
	}

	public void setInBondBoeNum(String inBondBoeNum) {
		this.inBondBoeNum = inBondBoeNum;
	}

	public Date getInBondBoeDate() {
		return inBondBoeDate;
	}

	public void setInBondBoeDate(Date inBondBoeDate) {
		this.inBondBoeDate = inBondBoeDate;
	}

	public String getPortOfImport() {
		return portOfImport;
	}

	public void setPortOfImport(String portOfImport) {
		this.portOfImport = portOfImport;
	}

	public String getBottleSealNumber() {
		return bottleSealNumber;
	}

	public void setBottleSealNumber(String bottleSealNumber) {
		this.bottleSealNumber = bottleSealNumber;
	}

	public String getInsuranceDetails() {
		return insuranceDetails;
	}

	public void setInsuranceDetails(String insuranceDetails) {
		this.insuranceDetails = insuranceDetails;
	}

	public String getPerUnitCost() {
		return perUnitCost;
	}

	public void setPerUnitCost(String perUnitCost) {
		this.perUnitCost = perUnitCost;
	}

	public String getTotalPurValue() {
		return totalPurValue;
	}

	public void setTotalPurValue(String totalPurValue) {
		this.totalPurValue = totalPurValue;
	}

	public String getDutyBcdRate() {
		return dutyBcdRate;
	}

	public void setDutyBcdRate(String dutyBcdRate) {
		this.dutyBcdRate = dutyBcdRate;
	}

	public String getDutyBcdAmt() {
		return dutyBcdAmt;
	}

	public void setDutyBcdAmt(String dutyBcdAmt) {
		this.dutyBcdAmt = dutyBcdAmt;
	}

	public String getDutyCessBcdAmt() {
		return dutyCessBcdAmt;
	}

	public void setDutyCessBcdAmt(String dutyCessBcdAmt) {
		this.dutyCessBcdAmt = dutyCessBcdAmt;
	}

	public String getDutyCessBcdRate() {
		return dutyCessBcdRate;
	}

	public void setDutyCessBcdRate(String dutyCessBcdRate) {
		this.dutyCessBcdRate = dutyCessBcdRate;
	}

	public String getDutyGstRate() {
		return dutyGstRate;
	}

	public void setDutyGstRate(String dutyGstRate) {
		this.dutyGstRate = dutyGstRate;
	}

	public String getDutyGstAmt() {
		return dutyGstAmt;
	}

	public void setDutyGstAmt(String dutyGstAmt) {
		this.dutyGstAmt = dutyGstAmt;
	}

	public String getDutyCessGstRate() {
		return dutyCessGstRate;
	}

	public void setDutyCessGstRate(String dutyCessGstRate) {
		this.dutyCessGstRate = dutyCessGstRate;
	}

	public String getDutyCessGstAmt() {
		return dutyCessGstAmt;
	}

	public void setDutyCessGstAmt(String dutyCessGstAmt) {
		this.dutyCessGstAmt = dutyCessGstAmt;
	}

	public String getDutyOtherRate() {
		return dutyOtherRate;
	}

	public void setDutyOtherRate(String dutyOtherRate) {
		this.dutyOtherRate = dutyOtherRate;
	}

	public String getDutyOtherAmt() {
		return dutyOtherAmt;
	}

	public void setDutyOtherAmt(String dutyOtherAmt) {
		this.dutyOtherAmt = dutyOtherAmt;
	}

	public String getTotalCustomDuty() {
		return totalCustomDuty;
	}

	public void setTotalCustomDuty(String totalCustomDuty) {
		this.totalCustomDuty = totalCustomDuty;
	}

	public String getTotalGst() {
		return totalGst;
	}

	public void setTotalGst(String totalGst) {
		this.totalGst = totalGst;
	}

	public String getVehicleRegNum() {
		return vehicleRegNum;
	}

	public void setVehicleRegNum(String vehicleRegNum) {
		this.vehicleRegNum = vehicleRegNum;
	}

	public Date getWarehouseDate() {
		return warehouseDate;
	}

	public void setWarehouseDate(Date warehouseDate) {
		this.warehouseDate = warehouseDate;
	}

	public String getEwayBillNum() {
		return ewayBillNum;
	}

	public void setEwayBillNum(String ewayBillNum) {
		this.ewayBillNum = ewayBillNum;
	}

	public Date getEwayBillDate() {
		return ewayBillDate;
	}

	public void setEwayBillDate(Date ewayBillDate) {
		this.ewayBillDate = ewayBillDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getHsnNumber() {
		return hsnNumber;
	}

	public void setHsnNumber(String hsnNumber) {
		this.hsnNumber = hsnNumber;
	}

	
	public String getDutyPaidAmount() {
		return dutyPaidAmount;
	}

	public void setDutyPaidAmount(String dutyPaidAmount) {
		this.dutyPaidAmount = dutyPaidAmount;
	}

	public String getBalanceDuty() {
		return balanceDuty;
	}

	public void setBalanceDuty(String balanceDuty) {
		this.balanceDuty = balanceDuty;
	}
	
	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", username=" + username + ", compIec=" + compIec + ", partNumber=" + partNumber
				+ ", typeOfPurchase=" + typeOfPurchase + ", partDesc=" + partDesc + ", uom=" + uom + ", purBatchNumber="
				+ purBatchNumber + ", grrQty=" + grrQty + ", grrNo=" + grrNo + ", purInvNo=" + purInvNo
				+ ", purInvDate=" + purInvDate + ", inBondBoeNum=" + inBondBoeNum + ", inBondBoeDate=" + inBondBoeDate
				+ ", portOfImport=" + portOfImport + ", bottleSealNumber=" + bottleSealNumber + ", insuranceDetails="
				+ insuranceDetails + ", perUnitCost=" + perUnitCost + ", totalPurValue=" + totalPurValue
				+ ", dutyBcdRate=" + dutyBcdRate + ", dutyBcdAmt=" + dutyBcdAmt + ", dutyCessBcdAmt=" + dutyCessBcdAmt
				+ ", dutyCessBcdRate=" + dutyCessBcdRate + ", dutyGstRate=" + dutyGstRate + ", dutyGstAmt=" + dutyGstAmt
				+ ", dutyCessGstRate=" + dutyCessGstRate + ", dutyCessGstAmt=" + dutyCessGstAmt + ", dutyOtherRate="
				+ dutyOtherRate + ", dutyOtherAmt=" + dutyOtherAmt + ", totalCustomDuty=" + totalCustomDuty
				+ ", totalGst=" + totalGst + ", vehicleRegNum=" + vehicleRegNum + ", warehouseDate=" + warehouseDate
				+ ", ewayBillNum=" + ewayBillNum + ", ewayBillDate=" + ewayBillDate + ", createdDate=" + createdDate
				+ ", updateDate=" + updateDate + ", hsnNumber=" + hsnNumber + ", dutyPaidAmount=" + dutyPaidAmount
				+ ", balanceDuty=" + balanceDuty + "]";
	}

}
