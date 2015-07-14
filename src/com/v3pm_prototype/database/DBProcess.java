package com.v3pm_prototype.database;

import java.io.Serializable;

public class DBProcess implements Serializable{
	public static final int ID_ALLPROCESSES = 0;
	public static final String NAME_ALLPROCESSES = "All Processes";
	
	private int id;
	private String name;
	private float p;
	private float oop;
	private float fixedCosts;
	private float q;
	private float degQ;
	private float t;
	private float degT;
	
	private float dmP;
	private float dmLambda;
	private float dmAlpha;
	private float dmBeta;
	private String dmFktQ;
	private String dmFktT;
	
	private String demandFunction;
	
	
	public DBProcess(int id, String name, float p,
			float oop, float fixedCosts, float q,
			float degQ, float t, float degT, float dmP,
			float dmLambda, float dmAlpha, float dmBeta, String dmFktQ,
			String dmFktT) {
		super();
		this.id = id;
		this.name = name;
		this.p = p;
		this.oop = oop;
		this.fixedCosts = fixedCosts;
		this.q = q;
		this.degQ = degQ;
		this.t = t;
		this.degT = degT;
		this.dmP = dmP;
		this.dmLambda = dmLambda;
		this.dmAlpha = dmAlpha;
		this.dmBeta = dmBeta;
		this.dmFktQ = dmFktQ;
		this.dmFktT = dmFktT;
		generateDemandfunction();
	}
	
	public String toString(){
		return this.name;
	}
	
	private void generateDemandfunction(){
		this.demandFunction = String.valueOf(this.dmP)+" + "+String.valueOf(this.dmLambda)+" *( "+String.valueOf(this.dmAlpha)+"*"+this.dmFktQ+" + "+String.valueOf(this.dmBeta)+"*"+this.dmFktT;
		//TODO DEMANDFKT
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

	public String getDemandFunction() {
		return demandFunction;
	}

	public void setDemandFunction(String demandFunction) {
		this.demandFunction = demandFunction;
	}

	public float getP() {
		return p;
	}

	public void setP(float p) {
		this.p = p;
	}

	public float getOop() {
		return oop;
	}

	public void setOop(float oop) {
		this.oop = oop;
	}

	public float getFixedCosts() {
		return fixedCosts;
	}

	public void setFixedCosts(float fixedCosts) {
		this.fixedCosts = fixedCosts;
	}

	public float getQ() {
		return q;
	}

	public void setQ(float q) {
		this.q = q;
	}

	public float getDegQ() {
		return degQ;
	}

	public void setDegQ(float degQ) {
		this.degQ = degQ;
	}

	public float getT() {
		return t;
	}

	public void setT(float t) {
		this.t = t;
	}

	public float getDegT() {
		return degT;
	}

	public void setDegT(float degT) {
		this.degT = degT;
	}

	public float getDmP() {
		return dmP;
	}

	public void setDmP(float dmP) {
		this.dmP = dmP;
	}

	public float getDmLambda() {
		return dmLambda;
	}

	public void setDmLambda(float dmLambda) {
		this.dmLambda = dmLambda;
	}

	public float getDmAlpha() {
		return dmAlpha;
	}

	public void setDmAlpha(float dmAlpha) {
		this.dmAlpha = dmAlpha;
	}

	public float getDmBeta() {
		return dmBeta;
	}

	public void setDmBeta(float dmBeta) {
		this.dmBeta = dmBeta;
	}

	public String getDmFktQ() {
		return dmFktQ;
	}

	public void setDmFktQ(String dmFktQ) {
		this.dmFktQ = dmFktQ;
	}

	public String getDmFktT() {
		return dmFktT;
	}

	public void setDmFktT(String dmFktT) {
		this.dmFktT = dmFktT;
	}
	
	
	
}
