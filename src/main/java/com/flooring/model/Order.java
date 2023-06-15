package com.flooring.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Order {

	private Integer orderNumber;
	private String customerName;
	private String state;
	private BigDecimal taxRate;
	private String productType;
	private BigDecimal area;
	private BigDecimal costPerSquareFoot;
	private BigDecimal labourCostPerSquareFoot;
	private BigDecimal materialCost;
	private BigDecimal labourCost;
	private BigDecimal tax;
	private BigDecimal total;
	
	public Order() {}

	public Order(Integer orderNumber, String customerName, String state, String productType, BigDecimal area) {
		super();
		this.orderNumber = orderNumber;
		this.customerName = customerName;
		this.state = state;
		this.productType = productType;
		this.area = area;
	}

	public Order(Integer orderNumber, String customerName, String state, BigDecimal taxRate, String productType,
			BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal labourCostPerSquareFoot, BigDecimal materialCost,
			BigDecimal labourCost, BigDecimal tax, BigDecimal total) {
		super();
		this.orderNumber = orderNumber;
		this.customerName = customerName;
		this.state = state;
		this.taxRate = taxRate;
		this.productType = productType;
		this.area = area;
		this.costPerSquareFoot = costPerSquareFoot;
		this.labourCostPerSquareFoot = labourCostPerSquareFoot;
		this.materialCost = materialCost;
		this.labourCost = labourCost;
		this.tax = tax;
		this.total = total;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public String getState() {
		return state;
	}
	
	public String getProductType() {
		return productType;
	}

	public BigDecimal getArea() {
		return area;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public BigDecimal getCostPerSquareFoot() {
		return costPerSquareFoot;
	}

	public BigDecimal getLabourCostPerSquareFoot() {
		return labourCostPerSquareFoot;
	}

	public BigDecimal getMaterialCost() {
		return materialCost;
	}

	public BigDecimal getLabourCost() {
		return labourCost;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public BigDecimal getTotal() {
		return total;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}
	
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
		this.costPerSquareFoot = costPerSquareFoot;
	}

	public void setLabourCostPerSquareFoot(BigDecimal labourCostPerSquareFoot) {
		this.labourCostPerSquareFoot = labourCostPerSquareFoot;
	}

	public void calcMaterialCost() {
		this.materialCost = this.area.multiply(this.costPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
	}
	
	public void calcLabourCost() {
		this.labourCost = this.area.multiply(this.labourCostPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
	}
	
	public void calcTax() {
		BigDecimal roughTax = (this.taxRate.divide(new BigDecimal("100"))).multiply(this.materialCost.add(this.labourCost));
		this.tax = roughTax.setScale(0, RoundingMode.HALF_UP);
	}
	
	public void calcTotal() {
		this.total = this.materialCost.add(this.labourCost.add(this.tax)).setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public String toString() {
		return "<<Order #" + orderNumber + ">>\n"
				+ "  * [customerName: " + customerName + ", state: " + state + ", taxRate: $" + taxRate + ", productType: " + productType + ",\n"
				+ "  * area: " + area + "ft^2, costPerSquareFoot: $" + costPerSquareFoot + ", labourCostPerSquareFoot: $" + labourCostPerSquareFoot + ",\n"
				+ "  * materialCost: $" + materialCost + ", labourCost: $" + labourCost + ", tax: $" + tax + ", total: $" + total + "]\n  *";
	}
}
