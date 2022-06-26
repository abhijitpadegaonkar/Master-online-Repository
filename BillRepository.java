package com.ap.demo.respository;

import java.util.List;

import com.ap.demo.model.Bill;

public interface BillRepository {

	public long save(Bill bill);

	public boolean update(Bill bill);
	
	public boolean delete(Long billId);
	
	public List<Bill > findAll();
	
	public Bill findById(Long billId);
}
