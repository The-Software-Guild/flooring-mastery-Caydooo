package com.flooring.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.flooring.dao.IDao;
import com.flooring.exception.InvalidNewOrderDateException;
import com.flooring.exception.NoOrdersForDateException;
import com.flooring.model.Order;
import com.flooring.model.Product;
import com.flooring.model.TaxData;

public class FlooringService implements IService {

	@Autowired
	private IDao dao;
	private List<TaxData> taxes;
	private List<Product> products;
	private Map<String, List<Order>> orders;
	private static final int MIN_YEAR = 1900;
	private static final int MAX_YEAR = 2100;
	private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	@Override
	public List<Order> retrieveOrders(String orderDate) {
		String day = orderDate.substring(0, 2);
		String month = orderDate.substring(3, 5);
		String year = orderDate.substring(6, 10);
		orderDate = month.concat(day).concat(year);
		List<Order> orderList = new ArrayList<Order>();
		
		try {
			orderList = dao.retrieveOrders(orderDate);
			if(orderList.size() == 0) {
				NoOrdersForDateException nofde = new NoOrdersForDateException("No orders found for that date!");
				throw nofde;
			}
		} catch (NoOrdersForDateException nofde) {
			String msg = nofde.getMessage();
			System.out.println("  *\n  * Error: " + msg);
			return null;
		}
			
		return orderList;
	}

	@Override
	public List<TaxData> retrieveTaxes() {
		taxes = dao.retrieveTaxes();
		return taxes;
	}

	@Override
	public List<Product> retrieveProducts() {
		products = dao.retrieveProducts();
		return products;
	}

	@Override
	public Order createNewOrder(Integer newOrderNumber, String customerName, String state, String prodType, BigDecimal area) {
		Order order = new Order(newOrderNumber, customerName, state, prodType, area);
		calcDetailedInfo(order);
		
		return order;
	}

	@Override
	public void editOrder(Order order, String orderDate) {
		String day = orderDate.substring(0, 2);
		String month = orderDate.substring(3, 5);
		String year = orderDate.substring(6, 10);
		orderDate = month.concat(day).concat(year);
		dao.modifyOrder(order, orderDate);
	}

	@Override
	public void removeOrder(Order order, String orderDate) {
		String day = orderDate.substring(0, 2);
		String month = orderDate.substring(3, 5);
		String year = orderDate.substring(6, 10);
		orderDate = month.concat(day).concat(year);
		dao.deleteOrder(order, orderDate);
	}

	@Override
	public Order findOrder(String orderDate, Integer orderNumber) {
		Order o = null;

		if(validateOrderDate(orderDate, false)) {	
			List<Order> orders = retrieveOrders(orderDate);
			if(orders != null) {
				o = validateOrderNumber(orders, orderNumber);
				return o;
			}	
		}

		return null;
	}

	@Override
	public void saveOrder(Order order, String orderDate) {
		boolean keyFound = false;
		String day = orderDate.substring(0, 2);
		String month = orderDate.substring(3, 5);
		String year = orderDate.substring(6, 10);
		orderDate = month.concat(day).concat(year);
		
		if(orders == null) {
			orders = new HashMap<>();
			List<Order> o = new ArrayList<>();
			o.add(order);
			orders.put(orderDate, o);
		} else {
			Set<String> orderSet = orders.keySet();
			Iterator<String> iter = orderSet.iterator();
			
			while(iter.hasNext()) {
				String key = iter.next();
				if(key.equals(orderDate)) {
					keyFound = true;
					List<Order> orderList = orders.get(key);
					orderList.add(order);
				}		
			}
			
			if(keyFound == false) {
				List<Order> orderList = new ArrayList<>();
				orderList.add(order);
				orders.put(orderDate, orderList);
			}
		}
		
	}

	@Override
	public void calcDetailedInfo(Order newOrder) {
		for(TaxData td : taxes) {
			if(td.getStateAbbreviation().equals(newOrder.getState())){
				newOrder.setTaxRate(td.getTaxRate());
			}
		}
		for(Product p : products) {
			if(p.getProductType().equals(newOrder.getProductType())) {
				newOrder.setCostPerSquareFoot(p.getCostPerSquareFoot());
				newOrder.setLabourCostPerSquareFoot(p.getLabourCostPerSquareFoot());
			}
		}
		
		newOrder.calcMaterialCost();
		newOrder.calcLabourCost();
		newOrder.calcTax();
		newOrder.calcTotal();
	}

	
	@Override
	public boolean validateOrderDate(String orderDate, boolean newOrder) {
		try {
			int day = Integer.parseInt(orderDate.substring(0,2));
			int month = Integer.parseInt(orderDate.substring(3,5));
			int year = Integer.parseInt(orderDate.substring(6,10));
			LocalDate ld = LocalDate.parse(orderDate, PATTERN);
			LocalDate today = LocalDate.now();
			
			if(newOrder) {
				if(Period.between(today, ld).isNegative()) {
					System.out.println("period: " + Period.between(today, ld));
					InvalidNewOrderDateException inode = new InvalidNewOrderDateException("Cannot create a date in the past!");
					throw inode;
				}
			}
			
			if(year < MIN_YEAR || year > MAX_YEAR) {
				return false;
			}
			if(month < 1 || month > 12) {
				return false;
			}
			if(day < 1 || day > 31) {
				return false;
			}
				
			//check for February
			if(month == 2) {
				if(((year % 4 == 0) && (year % 100 == 0)) || (year % 400 == 0)) {
					return (day <= 29);
				} else {
					return (day <= 28);
				}
			}
			
			//check for months with 30 days
			if(month == 4 || month == 6 || month == 9 || month == 11) {
				return (day <= 30);
			}
			
		} catch(InvalidNewOrderDateException inode) {
			String msg = inode.getMessage();
			System.out.println("  * Error: " + msg);
			return false;
		} catch(NumberFormatException e) {
			System.out.println("  * Error: Please enter a correctly formatted date following the pattern 'dd-MM-yyyy'.");
			return false;
			
		} catch (DateTimeParseException e) {
			System.out.println("  * Error: This is not a valid date!");
			return false;
		}
		
		return true;
	}

	@Override
	public Order validateOrderNumber(List<Order> orders, Integer orderNumber) {
		for(Order o : orders) {
			if(o.getOrderNumber() == orderNumber) {
				return o;
			}
		}
		System.out.println("  *\n  * Error: No order number found for that date!");
		return null;
	}

	@Override
	public boolean validateCustomerName(String name) {
		if(name.equals("")) {
			System.out.println("  * Error: Name cannot be blank!");
			return false;
		}

		int length = name.length();
		for(int i = 0; i < length; i++) {
			if((Character.isLetterOrDigit(name.charAt(i)) == false)) {
				if(name.charAt(i) != ',' && name.charAt(i) != '.' && !Character.isWhitespace(name.charAt(i))) {
					System.out.println("  * Error: Name contains invalid characters!");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean validateState(String state) {
		for(TaxData td : taxes) {
			if(td.getStateAbbreviation().equalsIgnoreCase(state)) {
				return true;
			}
		}

		System.out.println("  * Error: This is not a state in the list!");
		return false;
	}

	@Override
	public boolean validateProdType(String product) {
		for(Product p : products) {
			if(p.getProductType().equalsIgnoreCase(product)) {
				return true;
			}
		}

		System.out.println("  * Error: This is not a product in the list!");
		return false;
	}

	@Override
	public boolean validateArea(BigDecimal area) {
		if(area.compareTo(new BigDecimal("100")) >= 0) {
			return true;
		}
		
		System.out.println("  * Error: The flooring area needs to be positive and larger than 100 sq ft!");
		return false;
	}

	@Override
	public Integer getMaxOrderNumber() {
		Integer maxNum = dao.getMaxOrderNumber();
		return maxNum;
	}

	@Override
	public boolean exportAllData() {	
		if(dao.exportAllData(orders)) {
			return true;
		}
		
		return false;
	}

	@Override
	public void backupData() {
		dao.backupData();
		
	}
}
