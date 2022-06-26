package com.ap.demo.respository;

import java.util.List;

import com.ap.demo.model.Item;
import com.ap.demo.model.Purchase;

public interface PurchaseRepository {

	public long save(Purchase purchase);

	public boolean update(Purchase purchase);
	
	public boolean delete(Long purchaseId);
	
	public List<Purchase > findAll();
	
	public Purchase findById(Long purchaseId);
	
	public boolean isPurchasePresent(Purchase purchase);

	public Purchase findByPurchaseBatchNumber(String purBatchNumber);
}
