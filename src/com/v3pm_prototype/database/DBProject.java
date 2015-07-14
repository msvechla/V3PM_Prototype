package com.v3pm_prototype.database;

import java.io.Serializable;

public class DBProject implements Serializable {
	private int id;
	private String name;
	private String type;
	private int periods;
	private float fixedCosts;
	private float oInv;
	private DBProcess process;
	private float a;
	private float b;
	private float e;
	private float u;
	private float m;
	
	public DBProject(int id, String name, String type, int periods,DBProcess process,
			float fixedCosts, float oInv, float a, float b,
			float e, float u, float m) {
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

	public float getFixedCosts() {
		return fixedCosts;
	}

	public void setFixedCosts(float fixCosts) {
		this.fixedCosts = fixCosts;
	}

	public float getOInv() {
		return oInv;
	}

	public void setOInv(float oInv) {
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
