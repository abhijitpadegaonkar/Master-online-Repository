package com.ap.demo.respository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.ap.demo.model.Bill;

@Repository
public class BillRepositoryImpl implements BillRepository {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static String INSERT = "INSERT INTO bill_master (username,comp_iec,part_number,part_desc,uom,rec_wastage,irrec_wastage,net_qty,source_of_material,type_of_material,remarks,pur_batch_number,prod_batch_number,hsn_number,created_date,updated_date) "
			+ "VALUES (:username,:comp_iec,:part_number,:part_desc,:uom,:rec_wastage,:irrec_wastage,:net_qty,:source_of_material,:type_of_material,:remarks,:pur_batch_number,:prod_batch_number,:hsn_number,NOW(),NOW())";
	
	private static String UPDATE = "UPDATE bill_master SET " 
			+ "comp_iec = :comp_iec, "
			+ "part_desc = :part_desc, "
			+ "uom = :uom, "
			+ "pur_batch_number = :pur_batch_number, "
			+ "grr_qty = :grr_qty, "
			+ "grr_no = :grr_no, "
			+ "pur_inv_no = :pur_inv_no, "
			+ "pur_inv_date = :pur_inv_date, "
			+ "in_bond_boe_num = :in_bond_boe_num, "
			+ "in_bond_boe_date = :in_bond_boe_date, "
			+ "port_of_import = :port_of_import, "
			+ "bottle_seal_number = :bottle_seal_number, "
			+ "insurance_details = :insurance_details, "
			+ "per_unit_cost = :per_unit_cost, "
			+ "total_pur_value = :total_pur_value, "
			+ "duty_bcd_rate = :duty_bcd_rate, "
			+ "duty_bcd_amt = :duty_bcd_amt, "
			+ "duty_cess_bcd_amt = :duty_cess_bcd_amt, "
			+ "duty_cess_bcd_rate = :duty_cess_bcd_rate, "
			+ "duty_gst_rate = :duty_gst_rate, "
			+ "duty_gst_amt = :duty_gst_amt, "
			+ "duty_cess_gst_rate = :duty_cess_gst_rate, "
			+ "duty_cess_gst_amt = :duty_cess_gst_amt, "
			+ "duty_other_rate = :duty_other_rate, "
			+ "duty_other_amt = :duty_other_amt, "
			+ "total_custom_duty = :total_custom_duty, "
			+ "total_gst = :total_gst, "
			+ "vehicle_reg_num = :vehicle_reg_num, "
			+ "warehouse_date = :warehouse_date, "
			+ "eway_bill_num = :eway_bill_num, "
			+ "eway_bill_date = :eway_bill_date, "
			+ "hsn_number = :hsn_number, "
			+ "updated_date = NOW() " 
			+ "WHERE id = :id";

	private static String SELECT = "SELECT * FROM bill_master";
	private static String DELETE = "DELETE FROM bill_master WHERE id = :id";

	@Override
	public Bill findById(Long billId) {
		return (Bill) namedParameterJdbcTemplate.queryForObject("select * from bill_master where id = :id",
				new MapSqlParameterSource("id", billId), (rs, rowNum) -> billMapper);

	}

	@Override
	public boolean delete(Long billId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", billId);
		int status = namedParameterJdbcTemplate.update(DELETE, namedParameters);
		return status != 0;
	}

	@Override
	public long save(Bill bill) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("username", bill.getUsername());
		params.put("comp_iec", bill.getCompIec());
		params.put("part_desc", bill.getPartDesc());
		params.put("part_number", bill.getPartNumber());
		params.put("uom", bill.getUom());
		
		params.put("rec_wastage", bill.getRecWastage());
		params.put("irrec_wastage", bill.getIrrecWastage());
		params.put("net_qty", bill.getNetQty());
		params.put("source_of_material", bill.getSourceOfMaterial());
		params.put("type_of_material", bill.getTypeOfMaterial());
		params.put("remarks", bill.getRemarks());
		params.put("pur_batch_number", bill.getPurBatchNumber());
		params.put("prod_batch_number", bill.getProdBatchNumber());
		params.put("hsn_number", bill.getHsnNumber());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(INSERT, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public boolean update(Bill bill) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", bill.getId());
		//params.put("username", bill.getUsername());
		params.put("comp_iec", bill.getCompIec());
		params.put("part_desc", bill.getPartDesc());
		//params.put("part_number", bill.getPartNumber());
		params.put("uom", bill.getUom());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		long updateCount = namedParameterJdbcTemplate.update(UPDATE, sqlParameterSource);
		return updateCount != 0;
	}

	@Override
	public List<Bill> findAll() {
		return namedParameterJdbcTemplate.query(SELECT, billMapper);
	}

	private RowMapper<Bill> billMapper = new RowMapper<Bill>() {

		@Override
		public Bill mapRow(ResultSet rs, int num) throws SQLException {
			Bill bill = new Bill();
			bill.setId(rs.getLong("id"));
			bill.setUsername(rs.getString("username"));
			bill.setCompIec(rs.getString("comp_iec"));
			bill.setPartDesc(rs.getString("part_desc"));
			bill.setPartNumber(rs.getString("part_number"));
			bill.setUom(rs.getString("uom"));

			bill.setRecWastage(rs.getString("rec_wastage"));
			bill.setIrrecWastage(rs.getString("irrec_wastage"));
			bill.setNetQty(rs.getString("net_qty"));
			bill.setSourceOfMaterial(rs.getString("source_of_material"));
			bill.setTypeOfMaterial(rs.getString("type_of_material"));
			bill.setRemarks(rs.getString("remarks"));
			bill.setPurBatchNumber(rs.getString("pur_batch_number"));
			bill.setProdBatchNumber(rs.getString("prod_batch_number"));
			bill.setHsnNumber(rs.getString("hsn_number"));
			
			bill.setCreatedDate(rs.getString("created_date"));
			bill.setUpdateDate(rs.getString("updated_date"));
			
			return bill;
		}

	};

}
