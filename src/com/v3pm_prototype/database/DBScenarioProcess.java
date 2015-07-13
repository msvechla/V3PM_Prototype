package com.v3pm_prototype.database;

public class DBScenarioProcess {
	private int id;
	private int processID;
	private String name;
	private float p;
	private float oop;
	private float fixedCosts;
	private float q;
	private float qMin;
	private float qMax;
	private float degQ;
	private float t;
	private float tMax;
	private float degT;
	
	private float dmP;
	private float dmLambda;
	private float dmAlpha;
	private float dmBeta;
	private String dmFktQ;
	private String dmFktT;
	
	private String demandFuction;
	
	
	public DBScenarioProcess(int id, int processID, String name, float p,
			float oop, float fixedCosts, float q, float qMin, float qMax,
			float degQ, float t, float tMax, float degT, float dmP,
			float dmLambda, float dmAlpha, float dmBeta, String dmFktQ,
			String dmFktT) {
		super();
		this.id = id;
		this.processID = processID;
		this.name = name;
		this.p = p;
		this.oop = oop;
		this.fixedCosts = fixedCosts;
		this.q = q;
		this.qMin = qMin;
		this.qMax = qMax;
		this.degQ = degQ;
		this.t = t;
		this.tMax = tMax;
		this.degT = degT;
		this.dmP = dmP;
		this.dmLambda = dmLambda;
		this.dmAlpha = dmAlpha;
		this.dmBeta = dmBeta;
		this.dmFktQ = dmFktQ;
		this.dmFktT = dmFktT;
	}
	
	private void generateDemandfunction(){
		this.demandFuction = String.valueOf(this.dmP);
		//TODO DEMANDFKT
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProcessID() {
		return processID;
	}

	public void setProcessID(int processID) {
		this.processID = processID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDemandFuction() {
		return demandFuction;
	}

	public void setDemandFuction(String demandFuction) {
		this.demandFuction = demandFuction;
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

	public float getQMin() {
		return qMin;
	}

	public void setQMin(float qMin) {
		this.qMin = qMin;
	}

	public float getQMax() {
		return qMax;
	}

	public void setQMax(float qMax) {
		this.qMax = qMax;
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

	public float getTMax() {
		return tMax;
	}

	public void setTMax(float tMax) {
		this.tMax = tMax;
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
