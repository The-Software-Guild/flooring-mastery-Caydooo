package com.flooring.unittests;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flooring.dao.FlooringDao;
import com.flooring.dao.IDao;
import com.flooring.model.Order;
import com.flooring.model.Product;
import com.flooring.model.TaxData;

@DisplayName("FlooringDaoTest")
class FlooringDaoTest {

	private IDao dao = null;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Starting dao unit tests...");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println("dao unit tests completed.");
	}

	@Test
	void testRetrieveOrders() {
		String orderDate = "06012013";
		dao = new FlooringDao();
		
		List<Order> actualResult = dao.retrieveOrders(orderDate);
		List<Order> expectedResult = new ArrayList<>();
		Order o = new Order(1,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));
		expectedResult.add(o);
		
		assertEquals(expectedResult.get(0).toString(), actualResult.get(0).toString());
	}

	@Test
	void testRetrieveTaxes() {
		dao = new FlooringDao();
		
		List<TaxData> taxes = dao.retrieveTaxes();
		TaxData actualTaxData = taxes.get(0);
		
		TaxData expectedTaxData = new TaxData("TX", "Texas", new BigDecimal("4.45"));
		
		assertEquals(expectedTaxData.toString(), actualTaxData.toString());
	}

	@Test
	void testRetrieveProducts() {
		dao = new FlooringDao();
		
		List<Product> products = dao.retrieveProducts();
		Product actualProduct = products.get(0);
		
		Product expectedProduct = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
		
		assertEquals(expectedProduct.toString(), actualProduct.toString());
	}

	@Test
	void testPersistOrders() {
		String orderDate = "06012013";
		dao = new FlooringDao();
		List<Order> orders = new ArrayList<>();
		Order o = new Order(1,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));
		orders.add(o);
		assertDoesNotThrow(() -> dao.persistOrders(orderDate, orders));
	}

	@Test
	void testModifyOrder() {
		String orderDate = "06012013";
		dao = new FlooringDao();
		Order order = new Order(1,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));

		assertDoesNotThrow(() -> dao.modifyOrder(order, orderDate));
	}

	@Test
	void testDeleteOrder() {
		String orderDate = "06012013";
		dao = new FlooringDao();
		Order order = new Order(1,"Ada Lovelace","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06"));

		assertDoesNotThrow(() -> dao.deleteOrder(order, orderDate));
	}

	@Test
	void testGetMaxOrderNumber() {
		dao = new FlooringDao();
		Integer actualNum = dao.getMaxOrderNumber();
		Integer expectedNum = 5;
		
		assertEquals(expectedNum, actualNum);
	}

	@Test
	void testExportAllData() {
		
		dao = new FlooringDao();
		Map<String, List<Order>> ordersMap = new TreeMap<>();
		List<Order> orders1 = new ArrayList<>();
		List<Order> orders2 = new ArrayList<>();
		
		orders1.add(new Order(6,"John Smith","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06")));
		orders1.add(new Order(7,"Bob Davidson","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06")));
		orders2.add(new Order(8,"Paul Brown","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06")));
		orders2.add(new Order(9,"Kyle Jones","CA",new BigDecimal("25.00"),"Tile",new BigDecimal("249.00"),new BigDecimal("3.50"),new BigDecimal("4.15"),new BigDecimal("871.50"),new BigDecimal("1033.35"),new BigDecimal("476.21"),new BigDecimal("2381.06")));
		
		ordersMap.put("06102023", orders1);
		ordersMap.put("08202023", orders2);
		
		assertTrue(dao.exportAllData(ordersMap));
	}

	@Test
	void testBackupData() {
		dao = new FlooringDao();
		assertDoesNotThrow(() -> dao.backupData());
	}

}
