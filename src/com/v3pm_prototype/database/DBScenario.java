package com.v3pm_prototype.database;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.v3pm_prototype.calculation.ConstraintSet;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * Stores information about scenarios
 * @author Marius Svechla
 *
 */
public class DBScenario {
	private int id; 
	private String name;
	private double npv;
	private int periods;
	private int slotsPerPeriod;
	private double discountRate;
	private double oOAFixed;
	
	private List<DBProject> lstProjects;
	private List<DBProcess> lstProcesses;
	private List<DBConstraint> lstConstraints;
	
	public DBScenario(int id, String name, double npv, int periods,
			int slotsPerPeriod, double discountRate, double oOAFixed, List<DBProject> lstProjects, List<DBProcess> lstProcesses, List<DBConstraint> lstConstraints) {
		super();
		this.id = id;
		this.name = name;
		this.npv = npv;
		this.periods = periods;
		this.slotsPerPeriod = slotsPerPeriod;
		this.discountRate = discountRate;
		this.oOAFixed = oOAFixed;
		
		if(lstProjects != null){
			this.lstProjects = lstProjects;
		}else{
			this.lstProjects = new ArrayList<DBProject>();
		}
		
		if(lstProcesses != null){
			this.lstProcesses = lstProcesses;
		}else{
			this.lstProcesses = new ArrayList<DBProcess>();
		}
		
		if(lstConstraints != null){
			this.lstConstraints = lstConstraints;
		}else{
			this.lstConstraints = new ArrayList<DBConstraint>();
		}
		
	}
	
	public DBScenario(int id, String name, double npv, int periods,
			int slotsPerPeriod, double discountRate, double oOAFixed){
		this(id, name, npv, periods, slotsPerPeriod, discountRate, oOAFixed, null, null,null);
	}

	public RunConfiguration generateRunConfiguration(){
		List<Project> projects = new ArrayList<Project>();
		for(DBProject dbp:lstProjects){
			projects.add(dbp.toProject());
		}
		
		List<Process> processes = new ArrayList<Process>();
		for(DBProcess dbp:lstProcesses){
			processes.add(dbp.toProcess());
		}
		
		
		RunConfiguration config = new RunConfiguration(periods, slotsPerPeriod, discountRate, oOAFixed, projects, processes, new ConstraintSet(lstConstraints));
		return config;
	}
	
	public List<DBConstraint> getLstConstraints() {
		return lstConstraints;
	}

	public void setLstConstraints(List<DBConstraint> lstConstraints) {
		this.lstConstraints = lstConstraints;
	}

	public List<DBProject> getLstProjects() {
		return lstProjects;
	}

	public void setLstProjects(List<DBProject> lstProjects) {
		this.lstProjects = lstProjects;
	}

	public List<DBProcess> getLstProcesses() {
		return lstProcesses;
	}


	public void setLstProcesses(List<DBProcess> lstProcesses) {
		this.lstProcesses = lstProcesses;
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

	public double getOOAFixed() {
		return oOAFixed;
	}

	public void setOOAFixed(double oOAFixed) {
		this.oOAFixed = oOAFixed;
	}
	
	public String getNPVString(){
		DecimalFormat df = new DecimalFormat("#,###.00 �");
		return df.format(npv); 
	}
	
	
	
}
