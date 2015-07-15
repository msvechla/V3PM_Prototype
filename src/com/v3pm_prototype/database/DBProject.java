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
	
	public DBProject(int id, String name, String type, int periods,DBProcess process,
			double fixedCosts, double oInv, double a, double b,
			double e, double u, double m) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.periods = periods;
		this.process = process;
		this.fixedCosts = fixedCosts;
		this.oInv = oInv;
		this.a = a;
		this.b = b;
		this.e = e;
		this.u = u;
		this.m = m;
	}

	//Converts this object to the Project class (used for calculations)
	public Project toProject(){
		Project p = new Project(id, name, periods, type, process.getId(), oInv, a, b, e, u, m);
		return p;
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

	
	
	
}
