package com.ap.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bill_master")
public class Bill {

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

	@Column(name = "rec_wastage")
	private String recWastage;

	@Column(name = "irrec_wastage")
	private String irrecWastage;

	@Column(name = "net_qty")
	private String netQty;

	@Column(name = "source_of_material")
	private String sourceOfMaterial;

	@Column(name = "type_of_material")
	private String typeOfMaterial;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "pur_batch_number")
	private String purBatchNumber;

	@Column(name = "prod_batch_number")
	private String prodBatchNumber;

	@Column(name = "hsn_number")
	private String hsnNumber;
	
	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_date")
	private String updateDate;

	public Bill() {
		super();
	}

	public Bill(String username, String compIec, String partNumber, String partDesc, String uom, String recWastage,
			String irrecWastage, String netQty, String sourceOfMaterial, String typeOfMaterial, String remarks,
			String purBatchNumber, String prodBatchNumber, String hsnNumber) {
		super();
		this.username = username;
		this.compIec = compIec;
		this.partNumber = partNumber;
		this.partDesc = partDesc;
		this.uom = uom;
		this.recWastage = recWastage;
		this.irrecWastage = irrecWastage;
		this.netQty = netQty;
		this.sourceOfMaterial = sourceOfMaterial;
		this.typeOfMaterial = typeOfMaterial;
		this.remarks = remarks;
		this.purBatchNumber = purBatchNumber;
		this.prodBatchNumber = prodBatchNumber;
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

	public String getRecWastage() {
		return recWastage;
	}

	public void setRecWastage(String recWastage) {
		this.recWastage = recWastage;
	}

	public String getIrrecWastage() {
		return irrecWastage;
	}

	public void setIrrecWastage(String irrecWastage) {
		this.irrecWastage = irrecWastage;
	}

	public String getNetQty() {
		return netQty;
	}

	public void setNetQty(String netQty) {
		this.netQty = netQty;
	}

	public String getSourceOfMaterial() {
		return sourceOfMaterial;
	}

	public void setSourceOfMaterial(String sourceOfMaterial) {
		this.sourceOfMaterial = sourceOfMaterial;
	}

	public String getTypeOfMaterial() {
		return typeOfMaterial;
	}

	public void setTypeOfMaterial(String typeOfMaterial) {
		this.typeOfMaterial = typeOfMaterial;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getHsnNumber() {
		return hsnNumber;
	}

	public void setHsnNumber(String hsnNumber) {
		this.hsnNumber = hsnNumber;
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
		return "Bill [username=" + username + ", compIec=" + compIec + ", partNumber=" + partNumber + ", partDesc="
				+ partDesc + ", uom=" + uom + ", recWastage=" + recWastage + ", irrecWastage=" + irrecWastage
				+ ", netQty=" + netQty + ", sourceOfMaterial=" + sourceOfMaterial + ", typeOfMaterial=" + typeOfMaterial
				+ ", remarks=" + remarks + ", purBatchNumber=" + purBatchNumber + ", prodBatchNumber=" + prodBatchNumber
				 + ", hsnNumber=" + hsnNumber
				+ "]";
	}

}
