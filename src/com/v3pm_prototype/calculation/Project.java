package com.v3pm_prototype.calculation;

import java.util.ArrayList;
import java.util.List;

/**
 * class for value objects which holds all the information about a single project
 * 
 */
public class Project {

	//public static List<Project> projectList = new ArrayList<Project>();
	
	private String name; // name of the project
	public int numberOfPeriods;
	private int id; // id of the project, starts with 1
	private int period; // periode in which the project will be implemented (default: null; only required for the NPV-calculation)
	private int startPeriod; //Used for multi-period projects. Indicates when the implementation originally started (can differ from current period)
	private String type; // bpmLevel or processLevel
	private int i; // influenced processes
	private double oinv; // investment-outflows
	private double a; // relative effect on the operating outflows of a process (only for BPM-level projects)
	private double b; // relative effect on the investment-Outflows of future projects (only for BPM-level projects)
	private double e; // relative effect on time of a process
	private double u; // relative effect on quality of a process
	private double m; // relative effect on operating outflows of a process
	// variables for restrictions
	
	public Project(int id, String name, int numberOfPeriods, String type, int i, double oinv, double a, double b, double e, double u, double m) {
		super();
		this.name = name;
		this.id = id;
		this.numberOfPeriods = numberOfPeriods;
		this.type = type;
		this.i = i;
		this.oinv = oinv;
		this.a = a;
		this.b = b;
		this.e = e;
		this.u = u;
		this.m = m;
	}
	
	//TODO get via index?
//	public static Project getProject(int id){
//		for(Project p: projectList){
//			if(p.getId() == id) return p;
//		}
//		return null;
//	}
	
	public String toString(){
		return String.valueOf(this.id);
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Project){
			Project p = (Project)obj;
			return (p.id == this.id);
		}
		return false;
	}
	
	public void adjustForMultiPeriodScenario(){
		this.oinv = this.oinv / this.numberOfPeriods;
	}
	
	public int getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(int startPeriod) {
		this.startPeriod = startPeriod;
	}

	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfPeriods() {
		return numberOfPeriods;
	}

	public void setNumberOfPeriods(int numberOfPeriods) {
		this.numberOfPeriods = numberOfPeriods;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public double getOinv() {
		return oinv;
	}

	public void setOinv(double oinv) {
		this.oinv = oinv;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getE() {
		return e;
	}

	public void setE(double e) {
		this.e = e;
	}

	public double getU() {
		return u;
	}

	public void setU(double u) {
		this.u = u;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}
	
}
