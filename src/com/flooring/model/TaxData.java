package com.flooring.model;

import java.math.BigDecimal;

public class TaxData {

	private String stateAbbreviation;
	private String stateName;
	private BigDecimal taxRate;
	
	public TaxData() {}

	public TaxData(String stateAbbreviation, String stateName, BigDecimal taxRate) {
		super();
		this.stateAbbreviation = stateAbbreviation;
		this.stateName = stateName;
		this.taxRate = taxRate;
	}

	public String getStateAbbreviation() {
		return stateAbbreviation;
	}

	public String getStateName() {
		return stateName;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	@Override
	public String toString() {
		return "[stateAbbreviation: " + stateAbbreviation + ", stateName: " + stateName + ", taxRate: $" + taxRate + "]";
	}
	
	
}
