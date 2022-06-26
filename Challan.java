package com.ap.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bill_master")
public class Challan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "comp_iec")
	private String compIec;

	@Column(name = "pur_batch_number")
	private String purBatchNumber;

	@Column(name = "challan_number")
	private String challanNumber;

	@Column(name = "challan_date")
	private String challanDate;

	@Column(name = "challan_amount")
	private String challanAmount;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_date")
	private String updateDate;

	public Challan() {
		super();
	}

	public Challan(String compIec, String purBatchNumber, String challanNumber, String challanDate,
			String challanAmount) {
		super();
		this.compIec = compIec;
		this.purBatchNumber = purBatchNumber;
		this.challanNumber = challanNumber;
		this.challanDate = challanDate;
		this.challanAmount = challanAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompIec() {
		return compIec;
	}

	public void setCompIec(String compIec) {
		this.compIec = compIec;
	}

	public String getPurBatchNumber() {
		return purBatchNumber;
	}

	public void setPurBatchNumber(String purBatchNumber) {
		this.purBatchNumber = purBatchNumber;
	}

	public String getChallanNumber() {
		return challanNumber;
	}

	public void setChallanNumber(String challanNumber) {
		this.challanNumber = challanNumber;
	}

	public String getChallanDate() {
		return challanDate;
	}

	public void setChallanDate(String challanDate) {
		this.challanDate = challanDate;
	}

	public String getChallanAmount() {
		return challanAmount;
	}

	public void setChallanAmount(String challanAmount) {
		this.challanAmount = challanAmount;
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
		return "Challan [id=" + id + ", compIec=" + compIec + ", purBatchNumber=" + purBatchNumber + ", challanNumber="
				+ challanNumber + ", challanDate=" + challanDate + ", challanAmount=" + challanAmount + ", createdDate="
				+ createdDate + ", updateDate=" + updateDate + "]";
	}

}
