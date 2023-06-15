package com.flooring.view;

import java.util.List;
import java.util.Scanner;

import com.flooring.model.Order;

public class UserIO implements IView {

	public static Scanner scanner = new Scanner(System.in);
	
	@Override
	public int printMainMenu() {
		int value = 0;
		
		System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
		System.out.println("  * <<Flooring Program>>");
		System.out.println("  * 1. Display Orders");
		System.out.println("  * 2. Add an Order");
		System.out.println("  * 3. Edit an Order");
		System.out.println("  * 4. Remove an Order");
		System.out.println("  * 5. Export All Data");
		System.out.println("  * 6. Quit");
		System.out.println("  *\n  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
		System.out.print("  * Input: ");
		value = Integer.parseInt(scanner.nextLine());
		
		return value;
	}

	@Override
	public void displayOrders(String orderDate, List<Order> orders) {
		System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
		System.out.println("  * Printing orders for order date " + orderDate + ":\n  *");
		for(Order o : orders) {
			System.out.println("  * " + o.toString());
		}
	}

	@Override
	public void displayData(List<?> list, String type) {
		System.out.println("  *\n  * Printing " + type + " list:");
		
		for(Object data : list) {
			System.out.println("  * " + data.toString());
		}
	}
	
	@Override
	public String userQueries(String msg, int i) {
		if(i==0) {
			System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
			System.out.println("  * Order details:");
		}
		System.out.print("  * " + msg + ": ");
		String value = scanner.nextLine();
		return value;
	}
	
	@Override
	public String[] findOrder() {
		String[] responses = new String[2];
		
		System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
		System.out.println("  * Enter the following to find an order:");
		System.out.print("  * Order date (dd-MM-yyyy): ");
		responses[0] = scanner.nextLine();
		System.out.print("  * Order number: ");
		responses[1] = scanner.nextLine();
		
		return responses;
	}

	@Override
	public boolean addOrderSummary(Order order) {
		System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
		System.out.println("  * Current order summary:");
		System.out.println("  * " + order.toString());
		
		System.out.print("  * Would you like to confirm this order? (Y/N): ");
		String choice = scanner.nextLine();
		
		if(choice.equalsIgnoreCase("Y")) {
			System.out.println("  *\n  * Saving order...");
			return true;
		}
		System.out.println("  *\n  * Cancelling order and returning to menu...");
		return false;
	}

	
	@Override
	public boolean editOrderSummary(Order oldOrder, Order newOrder) {
		System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
		System.out.println("  * Current order changes:");
		System.out.println("  * Original order:");
		System.out.println("  * " + oldOrder.toString());
		
		System.out.println("  * Changed order:");
		System.out.println("  * " + newOrder.toString());
		
		System.out.print("  * Would you like to confirm this order? (Y/N): ");
		String choice = scanner.nextLine();
		
		if(choice.equalsIgnoreCase("Y")) {
			System.out.println("  *\n  * Saving order...");
			return true;
		}
		System.out.println("  *\n  * Cancelling order and returning to menu...");
		return false;
	}


	@Override
	public boolean removeOrderSummary(Order order) {
		System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
		System.out.println("  * Order removal summary:");
		System.out.println("  * " + order.toString());
		
		System.out.print("  * Would you like to confirm the removal of this order? (Y/N): ");
		String choice = scanner.nextLine();
		
		if(choice.equalsIgnoreCase("Y")) {
			System.out.println("  *\n  * Removing order...");
			return true;
		}
		System.out.println("  *\n  * Cancelling removal and returning to menu...");
		return false;
	}

	@Override
	public void exportData() {
		System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
		System.out.println("  * Writing all new orders to their respective files...\n  *");
	}
	
	@Override
	public void exportDataSummary() {
		System.out.println("  * All data exported successfully!");
	}


	@Override
	public void exitMenu() {
		System.out.println("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n  *");
		System.out.println("  * Exiting the flooring program. Have a great day!");
		System.out.println("  *\n  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");	
	}
}
