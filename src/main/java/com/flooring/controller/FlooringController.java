package com.flooring.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.flooring.model.Order;
import com.flooring.model.Product;
import com.flooring.model.TaxData;
import com.flooring.service.FlooringService;
import com.flooring.service.IService;
import com.flooring.view.UserIO;
import com.flooring.view.IView;

public class FlooringController {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("flooring-bean.xml");
		IView view = (IView) context.getBean("view", UserIO.class);
		IService service = (IService) context.getBean("service", FlooringService.class);
		
		while(true) {
			int response = 0;
			response = view.printMainMenu();
			List<String> outputList = null;
			
			switch (response) {
				case 1: {
					boolean retry = true;
					outputList = new ArrayList<String>();
					outputList.add("Please enter an order date (dd-MM-yyyy)");
						
					for(int i = 0; i < outputList.size(); i++) {
						retry = true;
						while(retry) {
							retry = false;
							String orderDate = view.userQueries(outputList.get(i), i);
							if(!service.validateOrderDate(orderDate, false)) {
								retry = true;
								continue;
							}
								
							List<Order> orders = service.retrieveOrders(orderDate);
							if(orders != null) {
								view.displayOrders(orderDate, orders);
							}			
							break;
						}
					}
					break;
				}
				case 2: {
					boolean retry = true;
					outputList = new ArrayList<String>();
					outputList.add("Please enter an order date (dd-MM-yyyy)");
					outputList.add("\n  * Please enter a customer name");
					outputList.add("\n  * Please enter the state where it will be delivered to");
					outputList.add("\n  * Please enter your chosen product type");
					outputList.add("\n  * Please enter the area amount [min. 100 sq ft]");
					
					String orderDate = "";
					String customerName = "";
					String state = "";
					String prodType = "";
					BigDecimal area = new BigDecimal("0");
					
					for(int i = 0; i < outputList.size(); i++) {
						retry = true;
						while(retry) {
							retry = false;
							String responseMsg = view.userQueries(outputList.get(i), i);

							switch(i) {
								case 0: {
									if(!service.validateOrderDate(responseMsg, true)) {
										retry = true;
										continue;
									}
									orderDate = responseMsg;
									break;
								}
								case 1: {
									if(!service.validateCustomerName(responseMsg)) {
										retry = true;
										continue;
									}
									customerName = responseMsg;
									//if customer name succeeds, print state list
									List<TaxData> taxes = service.retrieveTaxes();
									view.displayData(taxes, "taxes");
									break;
								}
								case 2: {
									if(!service.validateState(responseMsg)) {
										retry = true;
										continue;
									}
									state = responseMsg;
									//if state succeeds, print product list
									List<Product> products = service.retrieveProducts();
									view.displayData(products, "product");
									break;
								}
								case 3: {
									if(!service.validateProdType(responseMsg)) {
										retry = true;
										continue;
									}
									prodType = responseMsg;
									break;
								}
								case 4: {
									if(!service.validateArea(new BigDecimal(responseMsg))) {
										retry = true;
										continue;
									}
									area = new BigDecimal(responseMsg);
									break;
								}
							}
						}
					}
					
					Integer newOrderNumber = service.getMaxOrderNumber()+1;
					Order newOrder = service.createNewOrder(newOrderNumber, customerName, state, prodType, area);
					
					if(view.addOrderSummary(newOrder)) {
						service.saveOrder(newOrder, orderDate);
					}
					
					break;
				}
				case 3: {
					String[] responses = view.findOrder();
					Order oldOrder = service.findOrder(responses[0], Integer.parseInt(responses[1]));
					
					if(oldOrder == null) {
						continue;
					}
					
					boolean retry = true;
					outputList = new ArrayList<String>();
					outputList.add("\n  * Please enter a customer name (" + oldOrder.getCustomerName() + ")");
					outputList.add("\n  * Please enter the state where it will be delivered to (" + oldOrder.getState() + ")");
					outputList.add("\n  * Please enter your chosen product type (" + oldOrder.getProductType() + ")");
					outputList.add("\n  * Please enter the area amount [min. 100 sq ft] (" + oldOrder.getArea().toString() + ")");
					
					String customerName = "";
					String state = "";
					String prodType = "";
					BigDecimal area = new BigDecimal("0");
					
					for(int i = 0; i < outputList.size(); i++) {
						retry = true;
						while(retry) {
							retry = false;
							String responseMsg = view.userQueries(outputList.get(i), i);

							switch(i) {
								case 0: {
									if(responseMsg.equals("")) {
										customerName = oldOrder.getCustomerName();
									} else {
										if(!service.validateCustomerName(responseMsg)) {
											retry = true;
											continue;
										} else {
											customerName = responseMsg;
										}
									}
									//if customer name succeeds, print state list
									List<TaxData> taxes = service.retrieveTaxes();
									view.displayData(taxes, "taxes");
									break;
								}
								case 1: {
									if(responseMsg.equals("")) {
										state = oldOrder.getState();
									} else {
										if(!service.validateState(responseMsg)) {
											retry = true;
											continue;
										} else {
											state = responseMsg;
										}
									}
									//if state succeeds, print product list
									List<Product> products = service.retrieveProducts();
									view.displayData(products, "product");
									break;
								}
								case 2: {
									if(responseMsg.equals("")) {
										prodType = oldOrder.getProductType();
									} else {
										if(!service.validateProdType(responseMsg)) {
											retry = true;
											continue;
										} else {
											prodType = responseMsg;
										}
									}
									break;
								}
								case 3: {
									if(responseMsg.equals("")) {
										area = oldOrder.getArea();
									} else {
										if(!service.validateArea(new BigDecimal(responseMsg))) {
											retry = true;
											continue;
										} else {
											area = new BigDecimal(responseMsg);
										}
									}
									break;
								}
							}
						}
					}
					
					
					Order newOrder = service.createNewOrder(oldOrder.getOrderNumber(), customerName, state, prodType, area);
					
					if(view.editOrderSummary(oldOrder, newOrder)) {
						service.editOrder(newOrder, responses[0]);
					}
					
					break;
				}
				case 4: {
					String[] responses = view.findOrder();
					Order order = service.findOrder(responses[0], Integer.parseInt(responses[1]));
					
					if(order != null) {
						if(view.removeOrderSummary(order)) {
							service.removeOrder(order, responses[0]);
						}
					}
					
					break;
				}
				case 5: {
					view.exportData();
					if(service.exportAllData()) {
						view.exportDataSummary();
					}
					
					break;
				}
				case 6: {
					view.exitMenu();
					service.backupData();
					System.exit(0);
					break;
				}
				default: {
					System.out.println("  *\n  * That isn't an option, try again.");
				}
			}
		}
	}
}
