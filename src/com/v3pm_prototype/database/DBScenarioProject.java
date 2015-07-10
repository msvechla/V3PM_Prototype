package com.v3pm_prototype.database;

public class DBScenarioProject {
	private int id;
	private int projectID;
	private String name;
	private String type;
	private int periods;
	private int fixCosts;
	private int oInv;
	private DBProcess process;
	private float a;
	private float b;
	private float e;
	private float u;
	private float m;
	
	public DBScenarioProject(int id, int projectID, String name, String type, int periods,DBProcess process,
			int fixedCosts, int oInv, float a, float b,
			float e, float u, float m) {
		super();
		this.id = id;
		this.projectID = projectID;
		this.name = name;
		this.type = type;
		this.periods = periods;
		this.process = process;
		this.fixCosts = fixedCosts;
		this.oInv = oInv;
		this.a = a;
		this.b = b;
		this.e = e;
		this.u = u;
		this.m = m;
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

	public int getFixCosts() {
		return fixCosts;
	}

	public void setFixCosts(int fixCosts) {
		this.fixCosts = fixCosts;
	}

	public int getOInv() {
		return oInv;
	}

	public void setOInv(int oInv) {
		this.oInv = oInv;
	}

	public DBProcess getProcess() {
		return process;
	}

	public void setProcess(DBProcess process) {
		this.process = process;
	}

	public float getA() {
		return a;
	}

	public void setA(float a) {
		this.a = a;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public float getE() {
		return e;
	}

	public void setE(float e) {
		this.e = e;
	}

	public float getU() {
		return u;
	}

	public void setU(float u) {
		this.u = u;
	}

	public float getM() {
		return m;
	}

	public void setM(float m) {
		this.m = m;
	}

	
	
	
}
