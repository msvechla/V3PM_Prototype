package com.v3pm_prototype.main;

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
	private char i; // influenced processes
	private double oinv; // investment-outflows
	private double a; // relative effect on the operating outflows of a process (only for BPM-level projects)
	private double b; // relative effect on the investment-Outflows of future projects (only for BPM-level projects)
	private double e; // relative effect on time of a process
	private double u; // relative effect on quality of a process
	private double m; // relative effect on operating outflows of a process
	// variables for restrictions
	private int earliestImplementationPeriod;
	private int latestImplementationPeriod;
	private Project predecessorProject;
	private Project successorProject;
	private Project togetherInPeriodWith;
	private Project notTogetherInPeriodWith;
	private Project gloMutEx;
	private Project gloMutDep;
	private boolean mandatory = false;//TODO Mandatory
	private double fixedCostEffect; // MLe: FixedCostEffect
	private String absRelq; // MLe absolute or relative effect for q
	private String absRelt; // MLe absolute or relative effect for t
	private String absRelOop; // MLe absolute or relative effect for Oop
	
	public Project(int id, String name, int numberOfPeriods, String type, char i, double oinv, double a, double b, double e, double u, double m, int earliestImplementationPeriod,
			int latestImplementationPeriod, Project pre, Project suc, Project tipw,
			Project ntipw, Project gloMutEx, Project gloMutDep, double fixedCostEffect, String absRelq, String absRelt, String absRelOop) {
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
		this.earliestImplementationPeriod = earliestImplementationPeriod;
		this.latestImplementationPeriod = latestImplementationPeriod;
		this.predecessorProject = pre;
		this.successorProject = suc;
		this.togetherInPeriodWith = tipw;
		this.notTogetherInPeriodWith = ntipw;
		this.gloMutEx = gloMutEx;
		this.gloMutDep = gloMutDep;
		this.fixedCostEffect = fixedCostEffect; //MLE
		this.absRelq = absRelq;					//MLE
		this.absRelt = absRelt;					//MLE
		this.absRelOop = absRelOop;				//MLE
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

	public char getI() {
		return i;
	}

	public void setI(char i) {
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

	public int getEarliestImplementationPeriod() {
		return earliestImplementationPeriod;
	}

	public void setEarliestImplementationPeriod(int earliestImplementationPeriod) {
		this.earliestImplementationPeriod = earliestImplementationPeriod;
	}

	public int getLatestImplementationPeriod() {
		return latestImplementationPeriod;
	}

	public void setLatestImplementationPeriod(int latestImplementationPeriod) {
		this.latestImplementationPeriod = latestImplementationPeriod;
	}

	public Project getPredecessorProject() {
		return predecessorProject;
	}

	public void setPredecessorProject(Project predecessorProject) {
		this.predecessorProject = predecessorProject;
	}

	public Project getSuccessorProject() {
		return successorProject;
	}

	public void setSuccessorProject(Project successorProject) {
		this.successorProject = successorProject;
	}

	public Project getTogetherInPeriodWith() {
		return togetherInPeriodWith;
	}

	public void setTogetherInPeriodWith(Project togetherInPeriodWith) {
		this.togetherInPeriodWith = togetherInPeriodWith;
	}

	public Project getNotTogetherInPeriodWith() {
		return notTogetherInPeriodWith;
	}

	public void setNotTogetherInPeriodWith(Project notTogetherInPeriodWith) {
		this.notTogetherInPeriodWith = notTogetherInPeriodWith;
	}
	
	public Project getGloMutEx() {
		return gloMutEx;
	}

	public void setGloMutEx(Project gloMutEx) {
		this.gloMutEx = gloMutEx;
	}

	public Project getGloMutDep() {
		return gloMutDep;
	}

	public void setGloMutDep(Project gloMutDep) {
		this.gloMutDep = gloMutDep;
	}

	//MLe 
	public double getFixedCostEffect() {
		return fixedCostEffect;
	}

	public void setFixedCostEffect(double fixedCostEffect) {
		this.fixedCostEffect = fixedCostEffect;
	}
	
	//MLe
	public String getAbsRelq() {
		return absRelq;
	}

	public void setAbsRelq(String absRelq) {
		this.absRelq = absRelq;
	}
	
	//MLe
	public String getAbsRelt() {
		return absRelt;
	}

	public void setAbsRelt(String absRelt) {
		this.absRelt = absRelt;
	}
	
	//MLe
	public String getAbsRelOop() {
		return absRelOop;
	}

	public void setAbsRelOop(String absRelOop) {
		this.absRelOop = absRelOop;
	}

	public boolean isMandatory() {
		return this.mandatory;
	}

	public void SetMandaytory(String stringCellValue) {
		if(stringCellValue.equals("true")){
			this.mandatory = true;
		}else{
			this.mandatory = false;
		}
	}
	

}
