package com.flooring.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.flooring.model.Order;
import com.flooring.model.Product;
import com.flooring.model.TaxData;

public class FlooringDao implements IDao {
	
	private String orderFileStart = null;
	private String orderFileEnd = null;
	private String orderFileName = null;
	private String taxFileName = null;
	private String productFileName = null;
	private String ordersDirectory = null;
	private String backupDataFileName = null;
	private BufferedReader bReader = null;
	private BufferedWriter bWriter = null;
	private String titles = null;
	
	public FlooringDao() {
		orderFileStart = "C:\\C353\\Assignments\\flooring_mastery\\SampleFileData\\Orders\\Orders_";
		orderFileEnd = ".txt";
		ordersDirectory = "C:\\C353\\Assignments\\flooring_mastery\\SampleFileData\\Orders";
		taxFileName = "C:\\C353\\Assignments\\flooring_mastery\\SampleFileData\\Data\\Taxes.txt";
		productFileName = "C:\\C353\\Assignments\\flooring_mastery\\SampleFileData\\Data\\Products.txt";
		backupDataFileName = "C:\\C353\\Assignments\\flooring_mastery\\SampleFileData\\Backup\\DataExport.txt";
	}
	
	@Override
	public List<Order> retrieveOrders(String orderDate) {
		List<String> ordersList = null;
		List<Order> orders = new ArrayList<Order>();
		orderFileName = orderFileStart.concat(orderDate.concat(orderFileEnd));
		
		try {
			bReader = new BufferedReader( new FileReader(orderFileName));
			titles = bReader.readLine();
			String line = bReader.readLine();
			
			while(line != null) {
				String[] orderArr = line.split(",");
				ordersList = new ArrayList<String>(Arrays.asList(orderArr));
				
				//checking if customer name has a ',' in it
				while (ordersList.size() >= 13) {
					String name = ordersList.get(1).concat(",").concat(ordersList.get(2));
					ordersList.remove(1);
					ordersList.remove(1);
					ordersList.add(1, name);
				}
				
				orderArr = ordersList.toArray(new String[0]);
				
				Integer orderNumber = Integer.parseInt(orderArr[0]);
				String customerName = orderArr[1];
				String state = orderArr[2];
				BigDecimal taxRate = new BigDecimal(orderArr[3]);
				String productType = orderArr[4];
				BigDecimal area = new BigDecimal(orderArr[5]);
				BigDecimal costPerSquareFoot = new BigDecimal(orderArr[6]);
				BigDecimal labourCostPerSquareFoot = new BigDecimal(orderArr[7]);
				BigDecimal materialCost = new BigDecimal(orderArr[8]);
				BigDecimal labourCost = new BigDecimal(orderArr[9]);
				BigDecimal tax = new BigDecimal(orderArr[10]);
				BigDecimal total = new BigDecimal(orderArr[11]);
				
				Order o = new Order(orderNumber, customerName, state, taxRate, productType, area, costPerSquareFoot, labourCostPerSquareFoot, materialCost, labourCost, tax, total);
				orders.add(o);
				line = bReader.readLine();
			}
			
			bReader.close();
			
		} catch(IOException e) {
			
		} 
		
		return orders;
	}

	@Override
	public List<TaxData> retrieveTaxes() {
		List<TaxData> taxes = new ArrayList<TaxData>();
		
		try {
			bReader = new BufferedReader( new FileReader(taxFileName));
			titles = bReader.readLine();
			String line = bReader.readLine();
			
			while(line != null) {
				String[] taxArr = line.split(",");
				
				String stateAbbreviation = taxArr[0];
				String stateName = taxArr[1];
				BigDecimal taxRate = new BigDecimal(taxArr[2]);
				
				TaxData td = new TaxData(stateAbbreviation, stateName, taxRate);
				taxes.add(td);
				line = bReader.readLine();
			}
			
			bReader.close();
			
		} catch(IOException e) {
			
		}
		return taxes;
	}

	@Override
	public List<Product> retrieveProducts() {
		List<Product> products = new ArrayList<Product>();
		
		try {
			bReader = new BufferedReader( new FileReader(productFileName));
			titles = bReader.readLine();
			String line = bReader.readLine();
			
			while(line != null) {
				String[] prodArr = line.split(",");
				
				String productType = prodArr[0];
				BigDecimal costPerSquareFoot = new BigDecimal(prodArr[1]);
				BigDecimal labourCostPerSquareFoot = new BigDecimal(prodArr[2]);
				
				Product p = new Product(productType, costPerSquareFoot, labourCostPerSquareFoot);
				products.add(p);
				line = bReader.readLine();
			}
			
			bReader.close();
			
		} catch(IOException e) {
			
		}
		return products;
	}
	
	@Override
	public void persistOrders(String orderDate, List<Order> orders) {
		orderFileName = orderFileStart.concat(orderDate.concat(orderFileEnd));
		
		try {
			bWriter = new BufferedWriter( new FileWriter(orderFileName));
			bWriter.write(titles + "\r\n");
			
			String toWrite;
			
			// order objects have 12 values that need to be written to file
			for(Order o : orders) {
				toWrite = (o.getOrderNumber().toString()).concat(",");
				toWrite = toWrite.concat(o.getCustomerName()).concat(",");
				toWrite = toWrite.concat(o.getState()).concat(",");
				toWrite = toWrite.concat(o.getTaxRate().toString()).concat(",");
				toWrite = toWrite.concat(o.getProductType()).concat(",");
				toWrite = toWrite.concat(o.getArea().toString()).concat(",");
				toWrite = toWrite.concat(o.getCostPerSquareFoot().toString()).concat(",");
				toWrite = toWrite.concat(o.getLabourCostPerSquareFoot().toString()).concat(",");
				toWrite = toWrite.concat(o.getMaterialCost().toString()).concat(",");
				toWrite = toWrite.concat(o.getLabourCost().toString()).concat(",");
				toWrite = toWrite.concat(o.getTax().toString()).concat(",");
				toWrite = toWrite.concat(o.getTotal().toString());
				
				bWriter.write(toWrite + "\r\n");
			}
			
			bWriter.flush();
			bWriter.close();
			
		} catch(IOException e) {
			System.out.println("Error with file");
		}
	}
	
	@Override
	public void modifyOrder(Order order, String orderDate) {
		List<Order> orders = retrieveOrders(orderDate);
		for(int i = 0; i < orders.size(); i++) {
			if(orders.get(i).getOrderNumber() == order.getOrderNumber()) {
				orders.remove(i);
				orders.add(i, order);
			}
		}
		persistOrders(orderDate, orders);
	}

	@Override
	public void deleteOrder(Order order, String orderDate) {
		List<Order> orders = retrieveOrders(orderDate);

		orders.removeIf((p) -> p.getOrderNumber() == order.getOrderNumber());
		
		persistOrders(orderDate, orders);
	}

	@Override
	public Integer getMaxOrderNumber() {
		Integer maxNum = 0;
		
		File directory = new File(ordersDirectory);
		File[] listOfFiles = directory.listFiles();
		
		for( File file : listOfFiles) {
			file.getName();
			try {
				bReader = new BufferedReader(new FileReader(file.getPath()));
				titles = bReader.readLine();
				String line = bReader.readLine();
				
				while(line != null) {
					String[] orderArr = line.split(",");
					Integer orderNum = Integer.parseInt(orderArr[0]);
					if(orderNum > maxNum) {
						maxNum = orderNum;
					}
					
					line = bReader.readLine();
				}
				
				bReader.close();
				
			} catch(IOException e) {
				
			}
		}
		
		return maxNum;
	}

	@Override
	public boolean exportAllData(Map<String, List<Order>> orders) {
		
		Set<String> orderSet = orders.keySet();
		Iterator<String> iter = orderSet.iterator();
		
		while(iter.hasNext()) {
			String orderDate = iter.next();
			List<Order> orderList = orders.get(orderDate);
			orderFileName = orderFileStart.concat(orderDate.concat(orderFileEnd));

			
			// check if file exists already
			File tempFile = new File(orderFileName);
			if(tempFile.exists()) {
				List<String> orderDetails = null;
				List<Order> existingOrders = new ArrayList<>();
				// if exists, read current file and write new contents to the end
				try {
					bReader = new BufferedReader( new FileReader(orderFileName));
					titles = bReader.readLine();
					String line = bReader.readLine();
					
					while(line != null) {
						String[] orderArr = line.split(",");
						orderDetails = new ArrayList<String>(Arrays.asList(orderArr));
						
						//checking if customer name has a ',' in it
						while (orderDetails.size() >= 13) {
							String name = orderDetails.get(1).concat(",").concat(orderDetails.get(2));
							orderDetails.remove(1);
							orderDetails.remove(1);
							orderDetails.add(1, name);
						}
						
						orderArr = orderDetails.toArray(new String[0]);
						
						Integer orderNumber = Integer.parseInt(orderArr[0]);
						String customerName = orderArr[1];
						String state = orderArr[2];
						BigDecimal taxRate = new BigDecimal(orderArr[3]);
						String productType = orderArr[4];
						BigDecimal area = new BigDecimal(orderArr[5]);
						BigDecimal costPerSquareFoot = new BigDecimal(orderArr[6]);
						BigDecimal labourCostPerSquareFoot = new BigDecimal(orderArr[7]);
						BigDecimal materialCost = new BigDecimal(orderArr[8]);
						BigDecimal labourCost = new BigDecimal(orderArr[9]);
						BigDecimal tax = new BigDecimal(orderArr[10]);
						BigDecimal total = new BigDecimal(orderArr[11]);
						
						Order o = new Order(orderNumber, customerName, state, taxRate, productType, area, costPerSquareFoot, labourCostPerSquareFoot, materialCost, labourCost, tax, total);
						existingOrders.add(o);
						line = bReader.readLine();
					}
					
					bReader.close();
					
					//combine both lists (new and existing for specific date)
					for(Order o : orderList) {
						existingOrders.add(o);
					}
					
					bWriter = new BufferedWriter( new FileWriter(orderFileName));
					bWriter.write(titles + "\r\n");
					
					String toWrite;
					
					// order objects have 12 values that need to be written to file
					for(Order o : existingOrders) {
						toWrite = (o.getOrderNumber().toString()).concat(",");
						toWrite = toWrite.concat(o.getCustomerName()).concat(",");
						toWrite = toWrite.concat(o.getState()).concat(",");
						toWrite = toWrite.concat(o.getTaxRate().toString()).concat(",");
						toWrite = toWrite.concat(o.getProductType()).concat(",");
						toWrite = toWrite.concat(o.getArea().toString()).concat(",");
						toWrite = toWrite.concat(o.getCostPerSquareFoot().toString()).concat(",");
						toWrite = toWrite.concat(o.getLabourCostPerSquareFoot().toString()).concat(",");
						toWrite = toWrite.concat(o.getMaterialCost().toString()).concat(",");
						toWrite = toWrite.concat(o.getLabourCost().toString()).concat(",");
						toWrite = toWrite.concat(o.getTax().toString()).concat(",");
						toWrite = toWrite.concat(o.getTotal().toString());
						
						bWriter.write(toWrite + "\r\n");
					}
					
					bWriter.flush();
					bWriter.close();
					
					return true;
				} catch(IOException e) {
					System.out.println("error with file");
					return false;
				}
			} else {
				// if not existing, write to new file.
				try {
					bWriter = new BufferedWriter( new FileWriter(orderFileName));
					bWriter.write(titles + "\r\n");
					
					String toWrite;
					
					// order objects have 12 values that need to be written to file
					for(Order o : orderList) {
						toWrite = (o.getOrderNumber().toString()).concat(",");
						toWrite = toWrite.concat(o.getCustomerName()).concat(",");
						toWrite = toWrite.concat(o.getState()).concat(",");
						toWrite = toWrite.concat(o.getTaxRate().toString()).concat(",");
						toWrite = toWrite.concat(o.getProductType()).concat(",");
						toWrite = toWrite.concat(o.getArea().toString()).concat(",");
						toWrite = toWrite.concat(o.getCostPerSquareFoot().toString()).concat(",");
						toWrite = toWrite.concat(o.getLabourCostPerSquareFoot().toString()).concat(",");
						toWrite = toWrite.concat(o.getMaterialCost().toString()).concat(",");
						toWrite = toWrite.concat(o.getLabourCost().toString()).concat(",");
						toWrite = toWrite.concat(o.getTax().toString()).concat(",");
						toWrite = toWrite.concat(o.getTotal().toString());
						
						bWriter.write(toWrite + "\r\n");
					}
					
					bWriter.flush();
					bWriter.close();
					
					return true;
				} catch(IOException e) {
					System.out.println("Error with file");
					return false;
				}
			}
		}

		return false;
	}

	@Override
	public void backupData() {
		// make map, write all existing orders to map using file directory (like maxOrderNumber), persist to backupdata file
		Comparator<Integer> comNum = new OrderNumComparator();
		Map<String, List<Order>> backupOrders = new TreeMap<>();
		Map<Integer, String> backupOrdersSorted = new TreeMap<>(comNum);
		File directory = new File(ordersDirectory);
		File[] listOfFiles = directory.listFiles();
		
		for( File file : listOfFiles) {
			file.getName();
			String orderDate = file.getName().substring(7, 15);
			String day = orderDate.substring(2, 4);
			String month = orderDate.substring(0, 2);
			String year = orderDate.substring(4, 8);
			orderDate = month + "-" + day + "-" + year;
			
			List<Order> ordersFromDate = new ArrayList<>();
			List<String> orderString = new ArrayList<>();
			try {
				bReader = new BufferedReader( new FileReader(file.getPath()));
				titles = bReader.readLine();
				String line = bReader.readLine();
				
				while(line != null) {
					String[] orderArr = line.split(",");
					orderString = new ArrayList<String>(Arrays.asList(orderArr));
					
					//checking if customer name has a ',' in it
					while (orderString.size() >= 13) {
						String name = orderString.get(1).concat(",").concat(orderString.get(2));
						orderString.remove(1);
						orderString.remove(1);
						orderString.add(1, name);
					}
					
					orderArr = orderString.toArray(new String[0]);
					
					Integer orderNumber = Integer.parseInt(orderArr[0]);
					String customerName = orderArr[1];
					String state = orderArr[2];
					BigDecimal taxRate = new BigDecimal(orderArr[3]);
					String productType = orderArr[4];
					BigDecimal area = new BigDecimal(orderArr[5]);
					BigDecimal costPerSquareFoot = new BigDecimal(orderArr[6]);
					BigDecimal labourCostPerSquareFoot = new BigDecimal(orderArr[7]);
					BigDecimal materialCost = new BigDecimal(orderArr[8]);
					BigDecimal labourCost = new BigDecimal(orderArr[9]);
					BigDecimal tax = new BigDecimal(orderArr[10]);
					BigDecimal total = new BigDecimal(orderArr[11]);
					
					Order o = new Order(orderNumber, customerName, state, taxRate, productType, area, costPerSquareFoot, labourCostPerSquareFoot, materialCost, labourCost, tax, total);
					
					ordersFromDate.add(o);
					line = bReader.readLine();
				}
				backupOrders.put(orderDate, ordersFromDate);
				bReader.close();
			} catch(IOException e) {
				System.out.println("Error with file");
			}
		}
		
		// map is now full, persist to backup data file
		try {
			bReader = new BufferedReader( new FileReader(backupDataFileName));
			String backupTitles = bReader.readLine();
			bReader.close();
			
			bWriter = new BufferedWriter( new FileWriter(backupDataFileName));
			bWriter.write(backupTitles + "\r\n");
			
			String toWrite = "";
			
			// order objects have 12 values that need to be written to file
			Set<String> orderSet = backupOrders.keySet();
			Iterator<String> iter = orderSet.iterator();
			
			while(iter.hasNext()) {
				String key = iter.next();
				List<Order> values = backupOrders.get(key);
				
				for(Order o : values) {		
					toWrite = (o.getOrderNumber().toString()).concat(",");
					toWrite = toWrite.concat(o.getCustomerName()).concat(",");
					toWrite = toWrite.concat(o.getState()).concat(",");
					toWrite = toWrite.concat(o.getTaxRate().toString()).concat(",");
					toWrite = toWrite.concat(o.getProductType()).concat(",");
					toWrite = toWrite.concat(o.getArea().toString()).concat(",");
					toWrite = toWrite.concat(o.getCostPerSquareFoot().toString()).concat(",");
					toWrite = toWrite.concat(o.getLabourCostPerSquareFoot().toString()).concat(",");
					toWrite = toWrite.concat(o.getMaterialCost().toString()).concat(",");
					toWrite = toWrite.concat(o.getLabourCost().toString()).concat(",");
					toWrite = toWrite.concat(o.getTax().toString()).concat(",");
					toWrite = toWrite.concat(o.getTotal().toString());
					toWrite = toWrite.concat(",").concat(key);
					
					backupOrdersSorted.put(o.getOrderNumber(), toWrite);
				}
				
			}
			
			Set<Integer> stringSet = backupOrdersSorted.keySet();
			Iterator<Integer> iterStr = stringSet.iterator();
			
			while(iterStr.hasNext()) {
				Integer key = iterStr.next();
				String values = backupOrdersSorted.get(key);
				
				bWriter.write(values + "\r\n");
				
			}
			bWriter.flush();
			bWriter.close();
			
		} catch(IOException e) {
			System.out.println("Error with file");
		}
		//System.out.println("finished successfully");
	}
}
