package com.ap.demo.respository;

import java.util.List;

import com.ap.demo.model.Sale;

public interface SaleRepository {

	public long save(Sale sale);

	public boolean update(Sale sale);
	
	public boolean delete(Long saleId);
	
	public List<Sale > findAll();
	
	public Sale findById(Long saleId);
}
