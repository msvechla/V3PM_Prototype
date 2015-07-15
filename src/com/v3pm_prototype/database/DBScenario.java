package com.v3pm_prototype.database;

public class DBScenario {
	private int id; 
	private String name;
	private double npv;
	private int periods;
	private int slotsPerPeriod;
	private double discountRate;
	private double oOAFixed;
	
	public DBScenario(int id, String name, double npv, int periods,
			int slotsPerPeriod, double discountRate, double oOAFixed) {
		super();
		this.id = id;
		this.name = name;
		this.npv = npv;
		this.periods = periods;
		this.slotsPerPeriod = slotsPerPeriod;
		this.discountRate = discountRate;
		this.oOAFixed = oOAFixed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNpv() {
		return npv;
	}

	public void setNpv(double npv) {
		this.npv = npv;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

	public int getSlotsPerPeriod() {
		return slotsPerPeriod;
	}

	public void setSlotsPerPeriod(int slotsPerPeriod) {
		this.slotsPerPeriod = slotsPerPeriod;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public double getOoAFixed() {
		return oOAFixed;
	}

	public void setOoAFixed(double oOAFixed) {
		this.oOAFixed = oOAFixed;
	}
	
	
	
}
