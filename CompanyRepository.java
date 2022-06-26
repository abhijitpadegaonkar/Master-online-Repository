package com.ap.demo.respository;

import java.util.List;

import com.ap.demo.model.Company;

public interface CompanyRepository {

	public Company findByUsername(String username);
	
	public Company findById(Long id);
	
	public Company getCompanyDetailsByUsername(String username);
	
	public long save(Company user);

	public List<Company> findAll();
	
	public boolean delete(Long companyId);
	
	public boolean enable(Long companyId);
	
	public boolean disable(Long companyId);
	
	public boolean update(Company company);

	public List<Company> findAllActive();

	public String getIecByUsername(String username);
	
	boolean isCompanyPresent(Company company);

}
