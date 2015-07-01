package com.v3pm_prototype.rmgeneration;

public class RunConfiguration {
	private int countPeriods;
	private int countProjectsMaxPerPeriod;
	
	public static RunConfiguration standardConfig = null;
	
	public RunConfiguration(int countPeriods, int countProjectsMaxPerPeriod) {
		super();
		this.countPeriods = countPeriods;
		this.countProjectsMaxPerPeriod = countProjectsMaxPerPeriod;
	}

	public int getCountPeriods() {
		return countPeriods;
	}

	public int getCountProjectsMaxPerPeriod() {
		return countProjectsMaxPerPeriod;
	}
	
	
	
}
