package com.ap.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item_master")
public class Item {

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

	@Column(name = "hsn_number")
	private String hsnNumber;
	
	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_date")
	private String updateDate;

	public Item() {
		super();
	}
	
	public Item(String username, String compIec, String partNumber, String typeOfPurchase, String partDesc, String uom, String hsnNumber, 
			String createdDate, String updateDate) {
		super();
		this.username = username;
		this.compIec = compIec;
		this.partNumber = partNumber;
		this.typeOfPurchase = typeOfPurchase;
		this.partDesc = partDesc;
		this.uom = uom;
		this.hsnNumber = hsnNumber;
		this.createdDate = createdDate;
		this.updateDate = updateDate;
	}
	
	public Item(String username, String compIec, String partNumber, String typeOfPurchase, String partDesc, String uom, String hsnNumber) {
		super();
		this.username = username;
		this.compIec = compIec;
		this.partNumber = partNumber;
		this.partDesc = partDesc;
		this.typeOfPurchase = typeOfPurchase;
		this.uom = uom;
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
		return "Item [id=" + id + ", username=" + username + ", compIec=" + compIec + ", partNumber=" + partNumber
				+ ", typeOfPurchase=" + typeOfPurchase + ", partDesc=" + partDesc + ", uom=" + uom + ", hsnNumber=" + hsnNumber + ", createdDate="
				+ createdDate + ", updateDate=" + updateDate + "]";
	}

}
