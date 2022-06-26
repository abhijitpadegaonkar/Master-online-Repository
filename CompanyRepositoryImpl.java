package com.ap.demo.respository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ap.demo.model.Company;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

	private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryImpl.class);

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static String INSERT_COMPANY = "INSERT INTO company_master(username, password, role_id, comp_name, comp_email_id, comp_url, comp_iec, comp_pan, comp_tin, comp_incorporation_number, comp_type, industry_type, addr_line_1, addr_line_2, addr_line_3, city, state, country, pin_code, prod_site_addr, cont_person_name, cont_person_email, cont_person_phone, moowr_number, gst_production_plant, hsn_number, active) "
			+ "VALUES (:username, :password, :role_id, :comp_name, :comp_email_id, :comp_url, :comp_iec, :comp_pan, :comp_tin, :comp_incorporation_number, :comp_type, :industry_type, :addr_line_1, :addr_line_2, :addr_line_3, :city, :state, :country, :pin_code, :prod_site_addr, :cont_person_name, :cont_person_email, :cont_person_phone, :moowr_number, :gst_production_plant, :hsn_number, true)";

	private static String UPDATE_COMPANY = "UPDATE company_master SET " + "comp_name = :comp_name, "
			+ "comp_email_id = :comp_email_id, " + "comp_url = :comp_url, " + "comp_iec = :comp_iec, "
			+ "comp_pan = :comp_pan, " + "comp_tin = :comp_tin, "
			+ "comp_incorporation_number = :comp_incorporation_number, " + "comp_type = :comp_type, "
			+ "industry_type = :industry_type, " + "addr_line_1 = :addr_line_1, " + "addr_line_2 = :addr_line_2, "
			+ "addr_line_3 = :addr_line_3, " + "city = :city, " + "state = :state, " + "country = :country, "
			+ "pin_code = :pin_code, " + "prod_site_addr = :prod_site_addr, " + "cont_person_name = :cont_person_name, "
			+ "cont_person_email = :cont_person_email, " + "cont_person_phone = :cont_person_phone, "
			+ "moowr_number = :moowr_number, " + "gst_production_plant = :gst_production_plant, "
			+ "hsn_number = :hsn_number " + "WHERE id = :id";

	private static String GET_ALL_COMPANIES = "SELECT * FROM company_master";
	private static String GET_ACTIVE_COMPANIES = "SELECT * FROM company_master WHERE active = true";
	private static String DELETE_COMPANY = "DELETE FROM company_master WHERE id = :id";
	private static String ENABLE_COMPANY = "UPDATE company_master SET active = true WHERE id = :id";
	private static String DISABLE_COMPANY = "UPDATE company_master SET active = false WHERE id = :id";

	@Override
	public Company findByUsername(String username) {
		return namedParameterJdbcTemplate.queryForObject("select * from company_master where username = :username",
				new MapSqlParameterSource("username", username), (rs, rowNum) -> new Company(rs.getLong("id"),
						rs.getString("username"), rs.getString("password"), rs.getLong("role_id")));

	}

	@Override
	public String getIecByUsername(String username) {
		String iec = null;
		try {
			iec = (String) namedParameterJdbcTemplate.queryForObject(
					"SELECT comp_iec FROM company_master WHERE username = :username",
					new MapSqlParameterSource("username", username), String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.warn("Company not found for username: {}", username);
		}

		return iec;
	}

	@Override
	public Company findById(Long companyId) {
		return (Company) namedParameterJdbcTemplate.queryForObject("SELECT * FROM company_master WHERE id = :id",
				new MapSqlParameterSource("id", companyId), (rs, rowNum) -> companyMapper);

	}
	
	@Override
	public Company getCompanyDetailsByUsername(String username) {
		return (Company) namedParameterJdbcTemplate.queryForObject("SELECT * FROM company_master WHERE username = :username",
				new MapSqlParameterSource("username", username), companyMapper);

	}

	/**
	 * Deletes company record by company id
	 * 
	 * @param companyId
	 * @return true if deleted, false otherwise
	 */
	@Override
	public boolean delete(Long companyId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", companyId);
		int status = namedParameterJdbcTemplate.update(DELETE_COMPANY, namedParameters);
		return status != 0;
	}

	@Override
	public boolean enable(Long companyId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", companyId);
		int status = namedParameterJdbcTemplate.update(ENABLE_COMPANY, namedParameters);
		return status != 0;
	}

	@Override
	public boolean disable(Long companyId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", companyId);
		int status = namedParameterJdbcTemplate.update(DISABLE_COMPANY, namedParameters);
		return status != 0;
	}

	@Override
	public long save(Company company) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", company.getRoleId());
		params.put("username", company.getUsername());
		params.put("password", company.getPassword());

		params.put("comp_name", company.getCompName());
		params.put("comp_email_id", company.getCompEmailId());
		params.put("comp_url", company.getCompUrl());
		params.put("comp_iec", company.getCompIec());
		params.put("comp_pan", company.getCompPan());
		params.put("comp_tin", company.getCompTin());
		params.put("comp_incorporation_number", company.getCompIncorporationNumber());
		params.put("comp_type", company.getCompType());
		params.put("industry_type", company.getIndustryType());

		params.put("addr_line_1", company.getAddrLine1());
		params.put("addr_line_2", company.getAddrLine2());
		params.put("addr_line_3", company.getAddrLine3());
		params.put("city", company.getCity());
		params.put("state", company.getState());
		params.put("country", company.getCountry());
		params.put("pin_code", company.getPinCode());
		params.put("prod_site_addr", company.getProdSiteAddr());

		params.put("cont_person_name", company.getContPersonName());
		params.put("cont_person_email", company.getContPersonEmail());
		params.put("cont_person_phone", company.getContPersonPhone());

		params.put("moowr_number", company.getMoowrNumber());
		params.put("gst_production_plant", company.getGstProductionPlant());
		params.put("hsn_number", company.getHsnNumber());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(INSERT_COMPANY, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public boolean update(Company company) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", company.getId());
//		params.put("username", company.getUsername());

		params.put("comp_name", company.getCompName());
		params.put("comp_email_id", company.getCompEmailId());
		params.put("comp_url", company.getCompUrl());
		params.put("comp_iec", company.getCompIec());
		params.put("comp_pan", company.getCompPan());
		params.put("comp_tin", company.getCompTin());
		params.put("comp_incorporation_number", company.getCompIncorporationNumber());
		params.put("comp_type", company.getCompType());
		params.put("industry_type", company.getIndustryType());

		params.put("addr_line_1", company.getAddrLine1());
		params.put("addr_line_2", company.getAddrLine2());
		params.put("addr_line_3", company.getAddrLine3());
		params.put("city", company.getCity());
		params.put("state", company.getState());
		params.put("country", company.getCountry());
		params.put("pin_code", company.getPinCode());
		params.put("prod_site_addr", company.getProdSiteAddr());

		params.put("cont_person_name", company.getContPersonName());
		params.put("cont_person_email", company.getContPersonEmail());
		params.put("cont_person_phone", company.getContPersonPhone());

		params.put("moowr_number", company.getMoowrNumber());
		params.put("gst_production_plant", company.getGstProductionPlant());
		params.put("hsn_number", company.getHsnNumber());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		long updateCount = namedParameterJdbcTemplate.update(UPDATE_COMPANY, sqlParameterSource);
		return updateCount != 0;
	}

	@Override
	public List<Company> findAll() {
		return namedParameterJdbcTemplate.query(GET_ALL_COMPANIES, companyMapper);
	}

	@Override
	public List<Company> findAllActive() {
		return namedParameterJdbcTemplate.query(GET_ACTIVE_COMPANIES, companyMapper);
	}

	private RowMapper<Company> companyMapper = new RowMapper<Company>() {

		@Override
		public Company mapRow(ResultSet rs, int num) throws SQLException {
			Company company = new Company();
			company.setId(rs.getLong("id"));
			company.setRoleId(rs.getLong("role_id"));
			company.setUsername(rs.getString("username"));

			company.setCompName(rs.getString("comp_name"));
			company.setCompEmailId(rs.getString("comp_email_id"));
			company.setCompUrl(rs.getString("comp_url"));
			company.setCompIec(rs.getString("comp_iec"));
			company.setCompPan(rs.getString("comp_pan"));
			company.setCompTin(rs.getString("comp_tin"));
			company.setCompIncorporationNumber(rs.getString("comp_incorporation_number"));
			company.setCompType(rs.getString("comp_type"));
			company.setIndustryType(rs.getString("industry_type"));

			company.setAddrLine1(rs.getString("addr_line_1"));
			company.setAddrLine2(rs.getString("addr_line_2"));
			company.setAddrLine3(rs.getString("addr_line_3"));
			company.setCity(rs.getString("city"));
			company.setState(rs.getString("state"));
			company.setCountry(rs.getString("country"));
			company.setPinCode(rs.getString("pin_code"));
			company.setProdSiteAddr(rs.getString("prod_site_addr"));

			company.setContPersonName(rs.getString("cont_person_name"));
			company.setContPersonEmail(rs.getString("cont_person_email"));
			company.setContPersonPhone(rs.getString("cont_person_phone"));

			company.setMoowrNumber(rs.getString("moowr_number"));
			company.setGstProductionPlant(rs.getString("gst_production_plant"));
			company.setHsnNumber(rs.getString("hsn_number"));
			
			company.setActive(rs.getBoolean("active"));
			return company;
		}

	};

	public Company mapRowData(ResultSet rs) throws SQLException {
		Company company = new Company();
		company.setId(rs.getLong("id"));
		company.setRoleId(rs.getLong("role_id"));
		company.setUsername(rs.getString("username"));

		company.setCompName(rs.getString("comp_name"));
		company.setCompEmailId(rs.getString("comp_email_id"));
		company.setCompUrl(rs.getString("comp_url"));
		company.setCompIec(rs.getString("comp_iec"));
		company.setCompPan(rs.getString("comp_pan"));
		company.setCompTin(rs.getString("comp_tin"));
		company.setCompIncorporationNumber(rs.getString("comp_incorporation_number"));
		company.setCompType(rs.getString("comp_type"));
		company.setIndustryType(rs.getString("industry_type"));

		company.setAddrLine1(rs.getString("addr_line_1"));
		company.setAddrLine2(rs.getString("addr_line_2"));
		company.setAddrLine3(rs.getString("addr_line_3"));
		company.setCity(rs.getString("city"));
		company.setState(rs.getString("state"));
		company.setCountry(rs.getString("country"));
		company.setPinCode(rs.getString("pin_code"));
		company.setProdSiteAddr(rs.getString("prod_site_addr"));

		company.setContPersonName(rs.getString("cont_person_name"));
		company.setContPersonEmail(rs.getString("cont_person_email"));
		company.setContPersonPhone(rs.getString("cont_person_phone"));

		company.setMoowrNumber(rs.getString("moowr_number"));
		company.setGstProductionPlant(rs.getString("gst_production_plant"));
		company.setHsnNumber(rs.getString("hsn_number"));

		company.setActive(rs.getBoolean("active"));
		return company;
	}

	@Override
	public boolean isCompanyPresent(Company company) {
		int count = (int) namedParameterJdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM company_master WHERE comp_name = :comp_name",
				new MapSqlParameterSource("comp_name", company.getCompName()), Integer.class);

		return (count > 0) ? true : false;
	}

}
