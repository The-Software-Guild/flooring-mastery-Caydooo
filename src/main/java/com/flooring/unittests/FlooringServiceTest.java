package com.flooring.unittests;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.flooring.dao.FlooringDao;
import com.flooring.dao.IDao;
import com.flooring.model.Order;
import com.flooring.service.FlooringService;
import com.flooring.service.IService;

@DisplayName("FlooringService Test")
class FlooringServiceTest {
	
	private IService service = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Starting dao unit tests...");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println("dao unit tests completed.");
	}

	@Test
	void testSaveOrder() {
		service = new FlooringService();
		Order o = new Order(1,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));
		
		assertDoesNotThrow(() -> service.saveOrder(o, "01-06-2013"));
	}

	@Test
	void testValidateOrderDate() {
		service = new FlooringService();
		
		assertTrue(service.validateOrderDate("11-05-2001", false));
		assertTrue(service.validateOrderDate("25-12-2040", true));
		
		assertFalse(service.validateOrderDate("11-05-3001", false));
		assertFalse(service.validateOrderDate("11-05-2001", true));
		assertFalse(service.validateOrderDate("30-02-2015", false));
	}

	@Test
	void testValidateOrderNumber() {
		service = new FlooringService();
		List<Order> orders = new ArrayList<>();
		Order o1 = new Order(1,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));
		Order o2 = new Order(2,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));
		Order o3 = new Order(3,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));
		Order o4 = new Order(4,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));
		orders.add(o1);
		orders.add(o2);
		orders.add(o3);
		orders.add(o4);
		
		Order actual1 = service.validateOrderNumber(orders, 2);
		Order actual2 = service.validateOrderNumber(orders, 15);
		
		assertEquals(o2.toString(), actual1.toString());
		assertNull(actual2);
	}

	@Test
	void testValidateCustomerName() {
		service = new FlooringService();
		assertTrue(service.validateCustomerName("Cayden"));
		assertTrue(service.validateCustomerName("Acme, Inc."));
		
		assertFalse(service.validateCustomerName("C3-PO"));
		assertFalse(service.validateCustomerName(""));
		assertFalse(service.validateCustomerName("1+2=3"));
	}

	@Test
	void testValidateArea() {
		service = new FlooringService();
		assertTrue(service.validateArea(new BigDecimal("1000")));
		assertTrue(service.validateArea(new BigDecimal("150")));
		
		assertFalse(service.validateArea(new BigDecimal("1")));
		assertFalse(service.validateArea(new BigDecimal("99")));
	}

}
