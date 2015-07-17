package com.v3pm_prototype.calculation;

import java.util.ArrayList;
import java.util.List;

import com.v3pm_prototype.database.DBProject;

/**
 * class for value objects which holds all the information about a single project
 * 
 */
public class Project {

	//public static List<Project> projectList = new ArrayList<Project>();
	
	private int id; // id of the project, starts with 1
	private String name; // name of the project
	public int numberOfPeriods;
	private int period; // periode in which the project will be implemented (default: null; only required for the NPV-calculation)
	private int startPeriod; //Used for multi-period projects. Indicates when the implementation originally started (can differ from current period)
	private String type; // bpmLevel or processLevel
	private double fixedCosts;
	private double oinv; // investment-outflows
	private int i; // influenced processes
	private double a; // relative effect on the operating outflows of a process (only for BPM-level projects)
	private double b; // relative effect on the investment-Outflows of future projects (only for BPM-level projects)
	private double e; // relative effect on time of a process
	private double u; // relative effect on quality of a process
	private double m; // relative effect on operating outflows of a process
	private String absRelQ;
	private String absRelT;
	private String absRelOop;
	

	public Project(int id, String name, int numberOfPeriods, String type,
			double fixedCosts, double oinv, int i, double a, double b,
			double e, double u, double m, String absRelQ, String absRelT,
			String absRelOop) {
		super();
		this.id = id;
		this.name = name;
		this.numberOfPeriods = numberOfPeriods;
		this.type = type;
		this.fixedCosts = fixedCosts;
		this.oinv = oinv;
		this.i = i;
		this.a = a;
		this.b = b;
		this.e = e;
		this.u = u;
		this.m = m;
		this.absRelQ = absRelQ;
		this.absRelT = absRelT;
		this.absRelOop = absRelOop;
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
		if(obj instanceof DBProject){
			DBProject p = (DBProject) obj;
			return p.getId() == this.id;
		}
		return false;
	}
	
	/**
	 * Adjusts the oInv to the number of periods Only call this method once per object!
	 * Not called at constructor because Project copys would be adjusted multiple times!
	 */
	public void adjustForMultiPeriodScenario(){
		this.oinv = this.oinv / this.numberOfPeriods;
	}
	
	public boolean isFinished(int currentPeriod){
		if(currentPeriod >= (this.startPeriod + this.numberOfPeriods)){
			return true;
		}else{
			return false;
		}
	}
	
	public String getAbsRelQ() {
		return absRelQ;
	}

	public void setAbsRelQ(String absRelQ) {
		this.absRelQ = absRelQ;
	}

	public String getAbsRelT() {
		return absRelT;
	}

	public void setAbsRelT(String absRelT) {
		this.absRelT = absRelT;
	}

	public String getAbsRelOop() {
		return absRelOop;
	}

	public void setAbsRelOop(String absRelOop) {
		this.absRelOop = absRelOop;
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

	public double getFixedCosts() {
		return fixedCosts;
	}

	public void setFixedCosts(double fixedCosts) {
		this.fixedCosts = fixedCosts;
	}
	
	
	
}
