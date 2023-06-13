package com.flooring.service;

import java.math.BigDecimal;
import java.util.List;

import com.flooring.model.Order;
import com.flooring.model.Product;
import com.flooring.model.TaxData;

public interface IService {

	public List<Order> retrieveOrders(String orderDate);
	public List<TaxData> retrieveTaxes();
	public List<Product> retrieveProducts();
	
	public Order createNewOrder(Integer newOrderNumber, String customerName, String state, String prodType, BigDecimal area);
	public void editOrder(Order order, String orderDate);
	public void removeOrder(Order order, String orderDate);
	
	public Order findOrder(String orderDate, Integer orderNumber);
	public void saveOrder(Order order, String orderDate);
	public void calcDetailedInfo(Order newOrder);
	
	public boolean validateOrderDate(String orderDate, boolean newOrder);	
	public Order validateOrderNumber(List<Order> orders, Integer orderNumber);	
	public boolean validateCustomerName(String name);	
	public boolean validateState(String state);
	public boolean validateProdType(String product);	
	public boolean validateArea(BigDecimal area);
	
	public Integer getMaxOrderNumber();
	
	public boolean exportAllData();
	public void backupData();

}
