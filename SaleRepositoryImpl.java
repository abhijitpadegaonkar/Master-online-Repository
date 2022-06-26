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

import com.ap.demo.model.Sale;

@Repository
public class SaleRepositoryImpl implements SaleRepository {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static String INSERT1 = "INSERT INTO sale_master (username,comp_iec,type_of_sale,part_number,part_desc,quantity,uom,hsn_number,temp_invoice_number,temp_invoice_date,gst_invoice_number,gst_invoice_date,shb_number,shb_date,port_of_export,details_of_insurance,per_unit_assessible_value,assessible_value,duty_gst,duty_cess_on_gst,vehicle_reg_number,one_time_lock_number,goods_removal_ts,eway_bill_number,eway_bill_date,remark,pur_batch_number,prod_batch_number,created_date,updated_date) "
			+ "VALUES (:username,:comp_iec,:type_of_sale,:part_number,:part_desc,:quantity,:uom,:hsn_number,:temp_invoice_number,:temp_invoice_date,:gst_invoice_number,:gst_invoice_date,:shb_number,:shb_date,:port_of_export,:details_of_insurance,:per_unit_assessible_value,:assessible_value,:duty_gst,:duty_cess_on_gst,:vehicle_reg_number,:one_time_lock_number,:goods_removal_ts,:eway_bill_number,:eway_bill_date,:remark,:pur_batch_number,:prod_batch_number,NOW(),NOW())";
	
	private static String INSERT = "INSERT INTO sale_master (username,comp_iec,type_of_sale,part_number,part_desc,quantity,uom,hsn_number,temp_invoice_number,temp_invoice_date,gst_invoice_number,gst_invoice_date,shb_number,shb_date,port_of_export,details_of_insurance,per_unit_assessible_value,assessible_value,duty_gst,duty_cess_on_gst,vehicle_reg_number,one_time_lock_number,goods_removal_ts,eway_bill_number,eway_bill_date,remark,pur_batch_number,prod_batch_number,ex_bond_boe_no,ex_bond_boe_date,created_date,updated_date) "
			+ "VALUES (:username,:comp_iec,:type_of_sale,:part_number,:part_desc,:quantity,:uom,:hsn_number,:temp_invoice_number,:temp_invoice_date,:gst_invoice_number,:gst_invoice_date,:shb_number,:shb_date,:port_of_export,:details_of_insurance,:per_unit_assessible_value,:assessible_value,:duty_gst,:duty_cess_on_gst,:vehicle_reg_number,:one_time_lock_number,:goods_removal_ts,:eway_bill_number,:eway_bill_date,:remark,:pur_batch_number,:prod_batch_number,:ex_bond_boe_no,:ex_bond_boe_date,NOW(),NOW())";
	
	private static String UPDATE = "UPDATE sale_master SET " 
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
			+ "updated_date = NOW() " 
			+ "WHERE id = :id";

	private static String SELECT = "SELECT * FROM sale_master";
	private static String DELETE = "DELETE FROM sale_master WHERE id = :id";

	@Override
	public Sale findById(Long saleId) {
		return (Sale) namedParameterJdbcTemplate.queryForObject("select * from sale_master where id = :id",
				new MapSqlParameterSource("id", saleId), (rs, rowNum) -> saleMapper);

	}

	@Override
	public boolean delete(Long saleId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", saleId);
		int status = namedParameterJdbcTemplate.update(DELETE, namedParameters);
		return status != 0;
	}

	@Override
	public long save(Sale sale) {

		Map<String, Object> params = new HashMap<String, Object>();
		System.out.println(sale);
		params.put("username", sale.getUsername());
		params.put("comp_iec", sale.getCompIec());
		params.put("part_number", sale.getPartNumber());
		params.put("part_desc", sale.getPartDesc());
		params.put("uom", sale.getUom());

		params.put("type_of_sale", sale.getTypeOfSale());
		params.put("quantity", sale.getQuantity());
		params.put("hsn_number", sale.getHsnNumber());
		params.put("temp_invoice_number", sale.getTempInvoiceNumber());
		params.put("temp_invoice_date", sale.getTempInvoiceDate());
		params.put("gst_invoice_number", sale.getGstInvoiceNumber());
		params.put("gst_invoice_date", sale.getGstInvoiceDate());
		params.put("shb_number", sale.getShbNumber());
		params.put("shb_date", sale.getShbDate());
		params.put("port_of_export", sale.getPortOfExport());
		params.put("details_of_insurance", sale.getDetailsOfInsurance());
		params.put("per_unit_assessible_value", sale.getPerUnitAssessibleValue());
		params.put("assessible_value", sale.getAssessibleValue());

		params.put("duty_gst", sale.getDutyGst());
		params.put("duty_cess_on_gst", sale.getDutyCessOnGst());
		params.put("vehicle_reg_number", sale.getVehicleRegNumber());
		params.put("one_time_lock_number", sale.getOneTimeLockNumber());
		params.put("goods_removal_ts", sale.getGoodsRemovalTs());
		params.put("eway_bill_number", sale.getEwayBillNumber());
		params.put("eway_bill_date", sale.getEwayBillDate());
		params.put("remark", sale.getRemark());

		params.put("pur_batch_number", sale.getPurBatchNumber());
		params.put("prod_batch_number", sale.getProdBatchNumber());
		
		params.put("ex_bond_boe_no", sale.getExBondBoeNo());
		params.put("ex_bond_boe_date", sale.getExBondBoeDate());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(INSERT, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public boolean update(Sale sale) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", sale.getId());
		//params.put("username", sale.getUsername());
		params.put("comp_iec", sale.getCompIec());
		params.put("part_desc", sale.getPartDesc());
		//params.put("part_number", sale.getPartNumber());
		params.put("uom", sale.getUom());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		long updateCount = namedParameterJdbcTemplate.update(UPDATE, sqlParameterSource);
		return updateCount != 0;
	}

	@Override
	public List<Sale> findAll() {
		return namedParameterJdbcTemplate.query(SELECT, saleMapper);
	}

	private RowMapper<Sale> saleMapper = new RowMapper<Sale>() {

		@Override
		public Sale mapRow(ResultSet rs, int num) throws SQLException {
			Sale sale = new Sale();
			sale.setId(rs.getLong("id"));
			sale.setUsername(rs.getString("username"));
			sale.setCompIec(rs.getString("comp_iec"));
			sale.setPartDesc(rs.getString("part_desc"));
			sale.setPartNumber(rs.getString("part_number"));
			sale.setUom(rs.getString("uom"));
			
			sale.setTypeOfSale(rs.getString("type_of_sale"));
			sale.setQuantity(rs.getString("quantity"));
			sale.setHsnNumber(rs.getString("hsn_number"));
			sale.setTempInvoiceNumber(rs.getString("temp_invoice_number"));
			sale.setTempInvoiceDate(rs.getDate("temp_invoice_date"));
			sale.setGstInvoiceNumber(rs.getString("gst_invoice_number"));
			sale.setGstInvoiceDate(rs.getDate("gst_invoice_date"));
			sale.setShbNumber(rs.getString("shb_number"));
			sale.setShbDate(rs.getDate("shb_date"));
			sale.setPortOfExport(rs.getString("port_of_export"));
			sale.setDetailsOfInsurance(rs.getString("details_of_insurance"));
			sale.setPerUnitAssessibleValue(rs.getString("per_unit_assessible_value"));
			sale.setAssessibleValue(rs.getString("assessible_value"));
			sale.setDutyGst(rs.getString("duty_gst"));
			sale.setDutyCessOnGst(rs.getString("duty_cess_on_gst"));
			sale.setVehicleRegNumber(rs.getString("vehicle_reg_number"));
			sale.setOneTimeLockNumber(rs.getString("one_time_lock_number"));
			
			sale.setGoodsRemovalTs(rs.getDate("goods_removal_ts"));
			sale.setEwayBillNumber(rs.getString("eway_bill_number"));
			sale.setEwayBillDate(rs.getDate("eway_bill_date"));
			sale.setRemark(rs.getString("remark"));
			sale.setPurBatchNumber(rs.getString("pur_batch_number"));
			sale.setProdBatchNumber(rs.getString("prod_batch_number"));
			
			sale.setRemark(rs.getString("remark"));
			sale.setPurBatchNumber(rs.getString("pur_batch_number"));
			sale.setProdBatchNumber(rs.getString("prod_batch_number"));
			
			sale.setExBondBoeNo(rs.getString("ex_bond_boe_no"));
			sale.setExBondBoeDate(rs.getDate("ex_bond_boe_date"));
			
			sale.setCreatedDate(rs.getString("created_date"));
			sale.setUpdateDate(rs.getString("updated_date"));
			
			return sale;
		}

	};

}
