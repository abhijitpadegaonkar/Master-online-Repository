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

import com.ap.demo.controller.ItemController;
import com.ap.demo.model.Item;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

	private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static String INSERT_ITEM = "INSERT INTO item_master (username,comp_iec,part_number,type_of_purchase,part_desc,uom,hsn_number,created_date,updated_date) "
			+ "VALUES (:username,:comp_iec,:part_number,:type_of_purchase,:part_desc,:uom,:hsn_number,NOW(),NOW())";

	private static String UPDATE_ITEM = "UPDATE item_master SET " 
			+ "comp_iec = :comp_iec, "
			+ "type_of_purchase = :type_of_purchase, " 
			+ "part_desc = :part_desc, "
			+ "uom = :uom, " 
			+ "hsn_number = :hsn_number, " 
			+ "updated_date = NOW() " 
			+ "WHERE id = :id";

	private static String GET_ALL_ITEM = "SELECT * FROM item_master";
	private static String DELETE_ITEM = "DELETE FROM item_master WHERE id = :id";

	@Override
	public Item findById(Long itemId) {
		return (Item) namedParameterJdbcTemplate.queryForObject("select * from item_master where id = :id",
				new MapSqlParameterSource("id", itemId), (rs, rowNum) -> itemMapper);

	}

	@Override
	public boolean delete(Long itemId) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", itemId);
		int status = namedParameterJdbcTemplate.update(DELETE_ITEM, namedParameters);
		return status != 0;
	}

	@Override
	public long save(Item item) {

		logger.info("Adding item. Part Number: ", item.getPartNumber());
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("username", item.getUsername());
		params.put("comp_iec", item.getCompIec());
		params.put("part_desc", item.getPartDesc());
		params.put("part_number", item.getPartNumber());
		params.put("type_of_purchase", item.getTypeOfPurchase());
		params.put("uom", item.getUom());
		params.put("hsn_number", item.getHsnNumber());
		params.put("created_date", item.getCreatedDate());
		params.put("updated_date", item.getUpdateDate());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(INSERT_ITEM, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public boolean update(Item item) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", item.getId());
		//params.put("username", item.getUsername());
		params.put("comp_iec", item.getCompIec());
		params.put("part_desc", item.getPartDesc());
		//params.put("part_number", item.getPartNumber());
		params.put("type_of_purchase", item.getTypeOfPurchase());
		params.put("uom", item.getUom());
		params.put("hsn_number", item.getHsnNumber());

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
		long updateCount = namedParameterJdbcTemplate.update(UPDATE_ITEM, sqlParameterSource);
		return updateCount != 0;
	}

	@Override
	public List<Item> findAll() {
		return namedParameterJdbcTemplate.query(GET_ALL_ITEM, itemMapper);
	}

	private RowMapper<Item> itemMapper = new RowMapper<Item>() {

		@Override
		public Item mapRow(ResultSet rs, int num) throws SQLException {
			Item item = new Item();
			item.setId(rs.getLong("id"));
			item.setUsername(rs.getString("username"));
			item.setCompIec(rs.getString("comp_iec"));
			item.setPartDesc(rs.getString("part_desc"));
			item.setPartNumber(rs.getString("part_number"));
			item.setTypeOfPurchase(rs.getString("type_of_purchase"));
			item.setUom(rs.getString("uom"));
			item.setHsnNumber(rs.getString("hsn_number"));
			item.setCreatedDate(rs.getString("created_date"));
			item.setUpdateDate(rs.getString("updated_date"));

			return item;
		}
	};

	@Override
	public Item findByPartNumber(String partNumber) {
		return (Item) namedParameterJdbcTemplate.queryForObject("SELECT * FROM item_master WHERE part_number = :part_number",
				new MapSqlParameterSource("part_number", partNumber), itemMapper);
	}
	
	@Override
	public Item findByPartNumberAndIec(String partNumber, String iec) {
		Item item = null;
		
		try {	
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("comp_iec", iec);
			params.put("part_number", partNumber);
			
			item = namedParameterJdbcTemplate.queryForObject("SELECT * FROM item_master WHERE part_number = :part_number AND comp_iec = :comp_iec",
					params, itemMapper);
		} catch (EmptyResultDataAccessException e) {
			logger.warn("Company not find item. Part number: {}, Company IEC: {}", partNumber, iec);
		}
		
		return item;
	}
	
	@Override
	public boolean isItemPresent(Item item) {
		int count = (int) namedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) FROM item_master WHERE part_number = :part_number",
				new MapSqlParameterSource("part_number", item.getPartNumber()), Integer.class);
		
		return (count > 0) ? true : false;
	}

}
