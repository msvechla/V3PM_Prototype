package com.v3pm_prototype.database;

import java.io.Serializable;

import com.v3pm_prototype.calculation.Project;

public class DBProject implements Serializable {
	private int id;
	private String name;
	private String type;
	private int periods;
	private double fixedCosts;
	private double oInv;
	private DBProcess process;
	private double a;
	private double b;
	private double e;
	private double u;
	private double m;
	private String absRelQ;
	private String absRelT;
	private String absRelOop;
	
	

	public DBProject(int id, String name, String type, int periods,
			double fixedCosts, double oInv, DBProcess process, double a,
			double b, double e, double u, double m, String absRelQ,
			String absRelT, String absRelOop) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.periods = periods;
		this.fixedCosts = fixedCosts;
		this.oInv = oInv;
		this.process = process;
		this.a = a;
		this.b = b;
		this.e = e;
		this.u = u;
		this.m = m;
		this.absRelQ = absRelQ;
		this.absRelT = absRelT;
		this.absRelOop = absRelOop;
	}

	//Converts this object to the Project class (used for calculations)
	public Project toProject(){
		Project p = new Project(id, name, periods, type,fixedCosts, oInv,process.getId(),
				a, b, e, u, m, absRelQ, absRelT, absRelOop);
		
		//Only called once when converting to Project. Not called at constructor because copys don't have to be adjusted anymore
		p.adjustForMultiPeriodScenario();
		return p;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Project){
			Project p = (Project)obj;
			return (p.getId() == this.id);
		}
		if(obj instanceof DBProject){
			DBProject p = (DBProject) obj;
			return p.getId() == this.id;
		}
		return false;
	}
	
	public String toString(){
		return this.name + " "+this.type+ " "+this.periods+"p";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

	public double getFixedCosts() {
		return fixedCosts;
	}

	public void setFixedCosts(double fixCosts) {
		this.fixedCosts = fixCosts;
	}

	public double getOInv() {
		return oInv;
	}

	public void setOInv(double oInv) {
		this.oInv = oInv;
	}

	public DBProcess getProcess() {
		return process;
	}

	public void setProcess(DBProcess process) {
		this.process = process;
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

	
	
	
}
