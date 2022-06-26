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

import com.ap.demo.model.Purchase;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static String INSERT1 = "INSERT INTO purchase_master (username,comp_iec,type_of_purchase,part_number,part_desc,uom,pur_batch_number,grr_qty,grr_no,pur_inv_no,pur_inv_date,in_bond_boe_num,in_bond_boe_date,port_of_import,bottle_seal_number,insurance_details,per_unit_cost,total_pur_value,duty_bcd_rate,duty_bcd_amt,duty_cess_bcd_amt,duty_cess_bcd_rate,duty_gst_rate,duty_gst_amt,duty_cess_gst_rate,duty_cess_gst_amt,duty_other_rate,duty_other_amt,total_custom_duty,total_gst,vehicle_reg_num,warehouse_date,eway_bill_num,eway_bill_date,created_date,updated_date) "
			+ "VALUES (:username,:comp_iec,:type_of_purchase,:part_number,:part_desc,:uom,:pur_batch_number,:grr_qty,:grr_no,:pur_inv_no,:pur_inv_date,:in_bond_boe_num,:in_bond_boe_date,:port_of_import,:bottle_seal_number,:insurance_details,:per_unit_cost,:total_pur_value,:duty_bcd_rate,:duty_bcd_amt,:duty_cess_bcd_amt,:duty_cess_bcd_rate,:duty_gst_rate,:duty_gst_amt,:duty_cess_gst_rate,:duty_cess_gst_amt,:duty_other_rate,:duty_other_amt,:total_custom_duty,:total_gst,:vehicle_reg_num,:warehouse_date,:eway_bill_num,:eway_bill_date,NOW(),NOW())";
	
	private static String INSERT = "INSERT INTO purchase_master (username,comp_iec,type_of_purchase,part_number,part_desc,uom,pur_batch_number,grr_qty,grr_no,pur_inv_no,pur_inv_date,in_bond_boe_num,in_bond_boe_date,port_of_import,bottle_seal_number,insurance_details,per_unit_cost,total_pur_value,duty_bcd_rate,duty_bcd_amt,duty_cess_bcd_amt,duty_cess_bcd_rate,duty_gst_rate,duty_gst_amt,duty_cess_gst_rate,duty_cess_gst_amt,duty_other_rate,duty_other_amt,total_custom_duty,total_gst,vehicle_reg_num,warehouse_date,eway_bill_num,eway_bill_date,hsn_number,balance_duty,memo_duty,waive_off_duty,paid_duty,available_qty,created_date,updated_date) "
			+ "VALUES (:username,:comp_iec,:type_of_purchase,:part_number,:part_desc,:uom,:pur_batch_number,:grr_qty,:grr_no,:pur_inv_no,:pur_inv_date,:in_bond_boe_num,:in_bond_boe_date,:port_of_import,:bottle_seal_number,:insurance_details,:per_unit_cost,:total_pur_value,:duty_bcd_rate,:duty_bcd_amt,:duty_cess_bcd_amt,:duty_cess_bcd_rate,:duty_gst_rate,:duty_gst_amt,:duty_cess_gst_rate,:duty_cess_gst_amt,:duty_other_rate,:duty_other_amt,:total_custom_duty,:total_gst,:vehicle_reg_num,:warehouse_date,:eway_bill_num,:eway_bill_date,:hsn_number,:balance_duty,0,0,0,:grr_qty,NOW(),NOW())";

	private static String UPDATE = "UPDATE purchase_master SET " 
			+ "comp_iec = :comp_iec, "
			+ "type_of_purchase = :type_of_purchase, " 
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
			+ "balance_duty = :balance_duty"
			+ "memo_duty = :memo_duty"
			+ "waived_off_duty = :waived_off_duty"
			+ "paid_duty = :paid_duty"
			+ "available_qty = :available_qty"
			+ "updated_date = NOW() " 
			+ "WHERE id = :id";

	private static String SELECT = "SELECT * FROM purchase_master";
	private static String DELETE = "DELETE FROM purchase_master WHERE id = :id";

	@Override
	public Purchase findById(Long purchaseId) {
		return (Purchase) namedParameterJdbcTemplate.queryForObject("select * from purchase_master where id = :id",
				new MapSqlParameterSource("id", purchaseId), (rs, rowNum) -> purchaseMapper);

	}

	@Override
	public boolean delete(Long purchaseId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", purchaseId);
		int status = namedParameterJdbcTemplate.update(DELETE, namedParameters);
		return status != 0;
	}

	@Override
	public long save(Purchase purchase) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("username", purchase.getUsername());
		params.put("comp_iec", purchase.getCompIec());
		params.put("part_desc", purchase.getPartDesc());
		params.put("part_number", purchase.getPartNumber());
		params.put("type_of_purchase", purchase.getTypeOfPurchase());
		params.put("uom", purchase.getUom());
		
		params.put("pur_batch_number", purchase.getPurBatchNumber());
		params.put("grr_qty", purchase.getGrrQty());
		params.put("grr_no", purchase.getGrrNo());
		params.put("pur_inv_no", purchase.getPurInvNo());
		params.put("pur_inv_date", purchase.getPurInvDate());
		params.put("in_bond_boe_num", purchase.getInBondBoeNum());
		params.put("in_bond_boe_date", purchase.getInBondBoeDate());
		params.put("port_of_import", purchase.getPortOfImport());
		params.put("bottle_seal_number", purchase.getBottleSealNumber());
		params.put("insurance_details", purchase.getInsuranceDetails());
		params.put("per_unit_cost", purchase.getPerUnitCost());
		params.put("total_pur_value", purchase.getTotalPurValue());
		params.put("duty_bcd_rate", purchase.getDutyBcdRate());
		params.put("duty_bcd_amt", purchase.getDutyBcdAmt());
		params.put("duty_cess_bcd_amt", purchase.getDutyCessBcdAmt());
		params.put("duty_cess_bcd_rate", purchase.getDutyCessBcdRate());
		params.put("duty_gst_rate", purchase.getDutyGstRate());
		params.put("duty_gst_amt", purchase.getDutyGstAmt());

		params.put("duty_cess_gst_rate", purchase.getDutyCessGstRate());
		params.put("duty_cess_gst_amt", purchase.getDutyCessGstAmt());
		params.put("duty_other_rate", purchase.getDutyOtherRate());
		params.put("duty_other_amt", purchase.getDutyOtherAmt());
		params.put("total_custom_duty", purchase.getTotalCustomDuty());
		params.put("total_gst", purchase.getTotalGst());
		params.put("vehicle_reg_num", purchase.getVehicleRegNum());
		params.put("warehouse_date", purchase.getWarehouseDate());
		params.put("eway_bill_num", purchase.getEwayBillNum());
		params.put("eway_bill_date", purchase.getEwayBillDate());
		params.put("hsn_number", purchase.getHsnNumber());
		
		params.put("balance_duty", purchase.getTotalCustomDuty());
		
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(INSERT, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public boolean update(Purchase purchase) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", purchase.getId());
		//params.put("username", purchase.getUsername());
		params.put("comp_iec", purchase.getCompIec());
		params.put("part_desc", purchase.getPartDesc());
		//params.put("part_number", purchase.getPartNumber());
		params.put("type_of_purchase", purchase.getTypeOfPurchase());
		params.put("uom", purchase.getUom());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		long updateCount = namedParameterJdbcTemplate.update(UPDATE, sqlParameterSource);
		return updateCount != 0;
	}

	@Override
	public List<Purchase> findAll() {
		return namedParameterJdbcTemplate.query(SELECT, purchaseMapper);
	}

	private RowMapper<Purchase> purchaseMapper = new RowMapper<Purchase>() {

		@Override
		public Purchase mapRow(ResultSet rs, int num) throws SQLException {
			Purchase purchase = new Purchase();
			purchase.setId(rs.getLong("id"));
			purchase.setUsername(rs.getString("username"));
			purchase.setCompIec(rs.getString("comp_iec"));
			purchase.setPartDesc(rs.getString("part_desc"));
			purchase.setPartNumber(rs.getString("part_number"));
			purchase.setTypeOfPurchase(rs.getString("type_of_purchase"));
			purchase.setUom(rs.getString("uom"));

			purchase.setPurBatchNumber(rs.getString("pur_batch_number"));
			purchase.setGrrQty(rs.getString("grr_qty"));
			purchase.setGrrNo(rs.getString("grr_no"));
			purchase.setPurInvNo(rs.getString("pur_inv_no"));
			purchase.setPurInvDate(rs.getDate("pur_inv_date"));
			purchase.setInBondBoeNum(rs.getString("in_bond_boe_num"));
			purchase.setInBondBoeDate(rs.getDate("in_bond_boe_date"));
			purchase.setPortOfImport(rs.getString("port_of_import"));
			purchase.setBottleSealNumber(rs.getString("bottle_seal_number"));
			purchase.setInsuranceDetails(rs.getString("insurance_details"));
			
			purchase.setPerUnitCost(rs.getString("per_unit_cost"));
			purchase.setTotalPurValue(rs.getString("total_pur_value"));
			purchase.setDutyBcdRate(rs.getString("duty_bcd_rate"));
			purchase.setDutyBcdAmt(rs.getString("duty_bcd_amt"));
			purchase.setDutyCessBcdAmt(rs.getString("duty_cess_bcd_amt"));
			purchase.setDutyCessBcdRate(rs.getString("duty_cess_bcd_rate"));
			purchase.setDutyGstRate(rs.getString("duty_gst_rate"));
			purchase.setDutyGstAmt(rs.getString("duty_gst_amt"));
			purchase.setDutyCessGstRate(rs.getString("duty_cess_gst_rate"));
			purchase.setDutyCessGstAmt(rs.getString("duty_cess_gst_amt"));
			purchase.setDutyOtherRate(rs.getString("duty_other_rate"));
			purchase.setDutyOtherAmt(rs.getString("duty_other_amt"));
			purchase.setTotalCustomDuty(rs.getString("total_custom_duty"));
			purchase.setTotalGst(rs.getString("total_gst"));
			
			purchase.setVehicleRegNum(rs.getString("vehicle_reg_num"));
			purchase.setWarehouseDate(rs.getDate("warehouse_date"));
			purchase.setEwayBillNum(rs.getString("eway_bill_num"));
			purchase.setEwayBillDate(rs.getDate("eway_bill_date"));
			purchase.setHsnNumber(rs.getString("hsn_number"));
			
			purchase.setCreatedDate(rs.getString("created_date"));
			purchase.setUpdateDate(rs.getString("updated_date"));
			
			purchase.setBalanceDuty(rs.getString("balance_duty"));
			
			return purchase;
		}

	};

	@Override
	public boolean isPurchasePresent(Purchase purchase) {
		int count = (int) namedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) FROM purchase_master WHERE pur_batch_number = :pur_batch_number",
				new MapSqlParameterSource("pur_batch_number", purchase.getPurBatchNumber()), Integer.class);
		
		return (count > 0) ? true : false;
	}
	
	@Override
	public Purchase findByPurchaseBatchNumber(String purBatchNumber) {
		return (Purchase) namedParameterJdbcTemplate.queryForObject("SELECT * FROM purchase_master WHERE pur_batch_number = :pur_batch_number",
				new MapSqlParameterSource("pur_batch_number", purBatchNumber), purchaseMapper);
	}
}
