package com.ap.demo.respository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ap.demo.model.Challan;

@Repository
public class ChallanRepositoryImpl implements ChallanRepository {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static String INSERT = "INSERT INTO challan_master (comp_iec,pur_batch_number,challan_number,challan_date,challan_amount,created_date,updated_date) "
			+ "VALUES (:comp_iec,:pur_batch_number,:challan_number,:challan_date,:challan_amount,NOW(),NOW())";
	
	private static String UPDATE = "UPDATE challan_master SET " 
			+ "comp_iec = :comp_iec, "
			+ "pur_batch_number = :pur_batch_number, "
			+ "challan_number = :challan_number, "
			+ "challan_date = :challan_date, "
			+ "challan_amount = :challan_amount, "
			+ "updated_date = NOW() " 
			+ "WHERE id = :id";

	private static String SELECT = "SELECT * FROM challan_master";
	private static String DELETE = "DELETE FROM challan_master WHERE id = :id";
	private static String CHALLAN_ENTRIES = "SELECT * FROM challan_master WHERE comp_iec = :comp_iec AND pur_batch_number = :pur_batch_number";

	@Override
	public Challan findById(Long challanId) {
		return (Challan) namedParameterJdbcTemplate.queryForObject("select * from challan_master where id = :id",
				new MapSqlParameterSource("id", challanId), (rs, rowNum) -> challanMapper);

	}

	@Override
	public boolean delete(Long challanId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", challanId);
		int status = namedParameterJdbcTemplate.update(DELETE, namedParameters);
		return status != 0;
	}

	@Override
	public long save(Challan challan) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("comp_iec", challan.getCompIec());
		params.put("pur_batch_number", challan.getPurBatchNumber());
		params.put("challan_number", challan.getChallanNumber());
		//params.put("challan_date", challan.getChallanDate());
		params.put("challan_date", new Date());
		params.put("challan_amount", challan.getChallanAmount());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(INSERT, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public boolean update(Challan challan) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", challan.getId());
		params.put("comp_iec", challan.getCompIec());
		params.put("pur_batch_number", challan.getPurBatchNumber());
		params.put("challan_number", challan.getChallanNumber());
		params.put("challan_date", challan.getChallanDate());
		params.put("challan_amount", challan.getChallanAmount());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		long updateCount = namedParameterJdbcTemplate.update(UPDATE, sqlParameterSource);
		return updateCount != 0;
	}

	@Override
	public List<Challan> findAll() {
		return namedParameterJdbcTemplate.query(SELECT, challanMapper);
	}

	@Override
	public List<Challan> findAllChallans(String companyIec, String purBatchNumber) {
		//return namedParameterJdbcTemplate.query(CHALLAN_ENTRIES, challanMapper);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comp_iec", companyIec);
		params.put("pur_batch_number", purBatchNumber);
		
		return namedParameterJdbcTemplate.query(CHALLAN_ENTRIES,
				new MapSqlParameterSource(params), challanMapper);
	}
	
	private RowMapper<Challan> challanMapper = new RowMapper<Challan>() {

		@Override
		public Challan mapRow(ResultSet rs, int num) throws SQLException {
			Challan challan = new Challan();
			challan.setId(rs.getLong("id"));
			challan.setCompIec(rs.getString("comp_iec"));
			challan.setPurBatchNumber(rs.getString("pur_batch_number"));
			
			challan.setChallanNumber(rs.getString("challan_number"));
			challan.setChallanDate(rs.getString("challan_date"));
			challan.setChallanAmount(rs.getString("challan_amount"));

			challan.setCreatedDate(rs.getString("created_date"));
			challan.setUpdateDate(rs.getString("updated_date"));
			
			return challan;
		}

	};

}
