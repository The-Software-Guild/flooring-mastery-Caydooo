package com.flooring.view;

import java.util.List;

import com.flooring.model.Order;

public interface IView {

	public int printMainMenu();
	
	public void displayOrders(String orderDate, List<Order> orders);
	public void displayData(List<?> list, String type);
	
	public String userQueries(String msg, int i);
	public String[] findOrder();
	
	public boolean addOrderSummary(Order order);
	public boolean editOrderSummary(Order oldOrder, Order newOrder);
	public boolean removeOrderSummary(Order order);
	
	public void exportData();
	public void exportDataSummary();
	
	public void exitMenu();
}
