package com.v3pm_prototype.rmgeneration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.v3pm_prototype.main.Process;
import com.v3pm_prototype.main.Project;

public class RunConfiguration {
	private int countPeriods;
	private int countProjectsMaxPerPeriod;
	private double discountRate;
	private int periodWithNoScheduledProjects;
	private int budgetMaxPerPeriod;
	private List<Double> budgetMaxforEachPeriod;
	private int overarchingFixedOutflows;
	
	private List<Project> lstProjects;
	private List<Process> lstProcesses;
	
	public static RunConfiguration standardConfig = null;
	
	public RunConfiguration() {
		super();
		this.lstProjects = new ArrayList<Project>();
		this.lstProcesses = new ArrayList<Process>();
	}

	
	public Project getProject(int id){
		for(Project p : lstProjects){
			if(p.getId() == id){
				return p;
			}
		}
		return null;
	}
	
	public List<Project> getMandatoryProjects(){
		List<Project> lstMandatory = new ArrayList<Project>();
		
		for(Project p : lstProjects){
			if(p.isMandatory()){
				lstMandatory.add(p);
			}
		}
		return lstMandatory;
	}
	
	public List<Integer> getMandatoryProjectIDs(){
		List<Integer> lstMandatory = new ArrayList<Integer>();
		
		for(Project p : lstProjects){
			if(p.isMandatory()){
				lstMandatory.add(p.getId());
			}
		}
		return lstMandatory;
	}
	
	public List<HashSet<Integer>> getGloMutDeps(){
		List<HashSet<Integer>> lstGloMutDep = new ArrayList<HashSet<Integer>>();
		
		for(Project p : lstProjects){
			if(p.getGloMutDep() != null){
				HashSet<Integer> hs = new HashSet<Integer>();
				hs.add(p.getId());
				hs.add(p.getGloMutDep().getId());
				lstGloMutDep.add(hs);
			}
		}
		
		return lstGloMutDep;
	}
	
	public List<HashSet<Integer>> getGloMutExs(){
		List<HashSet<Integer>> lstGloMutEx = new ArrayList<HashSet<Integer>>();
		
		for(Project p : lstProjects){
			if(p.getGloMutEx() != null){
				HashSet<Integer> hs = new HashSet<Integer>();
				hs.add(p.getId());
				hs.add(p.getGloMutEx().getId());
				lstGloMutEx.add(hs);
			}
		}
		
		return lstGloMutEx;
	}
	
	public List<Project> getGloMutDepProjects(){
		List<Project> lstGloMutDep = new ArrayList<Project>();
		
		for(Project p : lstProjects){
			if(p.getGloMutDep() != null){
				lstGloMutDep.add(p);
			}
		}
		return lstGloMutDep;
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

	public List<Project> getLstProjects() {
		return lstProjects;
	}

	public void setLstProjects(List<Project> lstProjects) {
		this.lstProjects = lstProjects;
	}

	public List<Process> getLstProcesses() {
		return lstProcesses;
	}

	public void setLstProcesses(List<Process> lstProcesses) {
		this.lstProcesses = lstProcesses;
	}

}
