package com.ap.demo.respository;

import java.util.List;

import com.ap.demo.model.Item;

public interface ItemRepository {

	public long save(Item user);

	public boolean update(Item item);
	
	public boolean delete(Long itemId);
	
	public List<Item> findAll();
	
	public Item findById(Long itemId);
	
	public Item findByPartNumber(String partNumber);

	boolean isItemPresent(Item item);

	Item findByPartNumberAndIec(String partNumber, String iec);
}
