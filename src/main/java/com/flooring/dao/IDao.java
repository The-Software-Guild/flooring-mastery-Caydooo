package com.flooring.dao;

import java.util.List;
import java.util.Map;

import com.flooring.model.Order;
import com.flooring.model.Product;
import com.flooring.model.TaxData;

public interface IDao {

	public List<Order> retrieveOrders(String orderDate);
	public List<TaxData> retrieveTaxes();
	public List<Product> retrieveProducts();
	
	public void persistOrders(String orderDate, List<Order> orders);
	
	public void modifyOrder(Order order, String orderDate);
	public void deleteOrder(Order order, String orderDate);
	
	public Integer getMaxOrderNumber();

	public boolean exportAllData(Map<String, List<Order>> orders);
	public void backupData();
}
