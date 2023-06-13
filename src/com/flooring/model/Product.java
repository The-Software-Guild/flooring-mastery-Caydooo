package com.flooring.model;

import java.math.BigDecimal;

public class Product {

	private String productType;
	private BigDecimal costPerSquareFoot;
	private BigDecimal labourCostPerSquareFoot;
	
	public Product() {}

	public Product(String productType, BigDecimal costPerSquareFoot, BigDecimal labourCostPerSquareFoot) {
		super();
		this.productType = productType;
		this.costPerSquareFoot = costPerSquareFoot;
		this.labourCostPerSquareFoot = labourCostPerSquareFoot;
	}

	public String getProductType() {
		return productType;
	}

	public BigDecimal getCostPerSquareFoot() {
		return costPerSquareFoot;
	}

	public BigDecimal getLabourCostPerSquareFoot() {
		return labourCostPerSquareFoot;
	}

	@Override
	public String toString() {
		return "[productType: " + productType + ", costPerSquareFoot: $" + costPerSquareFoot + ", labourCostPerSquareFoot: $" + labourCostPerSquareFoot + "]";
	}
	
	
}
