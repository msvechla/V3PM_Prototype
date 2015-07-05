package com.v3pm_prototype.rmgeneration;

import java.util.List;

public class RunConfiguration {
	private int countPeriods;
	private int countProjectsMaxPerPeriod;
	private double discountRate;
	private int periodWithNoScheduledProjects;
	private int budgetMaxPerPeriod;
	private List<Double> budgetMaxforEachPeriod;
	private int overarchingFixedOutflows;
	
	public static RunConfiguration standardConfig = null;
	
	public RunConfiguration() {
		super();
	}

	public int getCountPeriods() {
		return countPeriods;
	}

	public void setCountPeriods(int countPeriods) {
		this.countPeriods = countPeriods;
	}

	public int getCountProjectsMaxPerPeriod() {
		return countProjectsMaxPerPeriod;
	}

	public void setCountProjectsMaxPerPeriod(int countProjectsMaxPerPeriod) {
		this.countProjectsMaxPerPeriod = countProjectsMaxPerPeriod;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public int getPeriodWithNoScheduledProjects() {
		return periodWithNoScheduledProjects;
	}

	public void setPeriodWithNoScheduledProjects(int periodWithNoScheduledProjects) {
		this.periodWithNoScheduledProjects = periodWithNoScheduledProjects;
	}

	public int getBudgetMaxPerPeriod() {
		return budgetMaxPerPeriod;
	}

	public void setBudgetMaxPerPeriod(int budgetMaxPerPeriod) {
		this.budgetMaxPerPeriod = budgetMaxPerPeriod;
	}

	public int getOverarchingFixedOutflows() {
		return overarchingFixedOutflows;
	}

	public void setOverarchingFixedOutflows(int overarchingFixedOutflows) {
		this.overarchingFixedOutflows = overarchingFixedOutflows;
	}

	public List<Double> getBudgetMaxforEachPeriod() {
		return budgetMaxforEachPeriod;
	}

	public void setBudgetMaxforEachPeriod(List<Double> budgetMaxforEachPeriod) {
		this.budgetMaxforEachPeriod = budgetMaxforEachPeriod;
	}

	
	
	
	
}
