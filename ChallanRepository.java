package com.ap.demo.respository;

import java.util.List;

import com.ap.demo.model.Challan;

public interface ChallanRepository {

	public long save(Challan challan);

	public boolean update(Challan challan);
	
	public boolean delete(Long challanId);
	
	public List<Challan> findAll();
	
	public List<Challan> findAllChallans(String companyIec, String purBatchNumber);
	
	public Challan findById(Long challanId);
}
