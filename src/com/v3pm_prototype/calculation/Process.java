package com.v3pm_prototype.calculation;

import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * class for value objects which holds all the information about a single process
 *
 */
public class Process {
	private String name;
	private int id;
	private double q; // quality
	private double qmax; // upper quality boundary
	private double t; // time
	private double p; // price
	private double oop; // operating outflows
	private double d; // quality specific degeneration effect
	private double v; // time specific degeneration effect
	private double fixedCosts; // MLe
	private double n; // demand
	
	private double[] qPerPeriod = null;
	
	// variables for calculating the demand
	private double roh;
	private double lamda;
	private double alpha;
	private int thetaID_q;
	private double beta;
	private int thetaID_t;

	public Process(String name, int id, double q, double qmax, double t, double p, double oop, double d, double v, double fixedCosts, double n, double roh, double lamda,
			double alpha, int thetaID_q, double beta, int thetaID_t) {
		super();
		this.name = name;
		this.id = id;
		this.q = q;
		this.qmax = qmax;
		this.t = t;
		this.p = p;
		this.oop = oop;
		this.d = d;
		this.v = v;
		this.fixedCosts = fixedCosts;	//MLe
		this.n = n;
		this.roh = roh;
		this.lamda = lamda;
		this.alpha = alpha;
		this.thetaID_q = thetaID_q;
		this.beta = beta;
		this.thetaID_t = thetaID_t;
	}

	public boolean equals(Object obj){
		if(obj instanceof Process){
			Process p = (Process)obj;
			return (p.id == this.id);
		}
		return false;
	}
	
	public double[] getqPerPeriod(RunConfiguration config) {
		if(qPerPeriod == null){
			qPerPeriod = new double[config.getPeriods()];
		}
		return qPerPeriod;
	}

	public void setqPerPeriod(double[] qPerPeriod) {
		this.qPerPeriod = qPerPeriod;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getQ() {
		return q;
	}

	public void setQ(double q) {
		this.q = q;
	}

	public double getQmax() {
		return qmax;
	}

	public void setQmax(double qmax) {
		this.qmax = qmax;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getOop() {
		return oop;
	}

	public void setOop(double oop) {
		this.oop = oop;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public double getV() {
		return v;
	}

	public void setV(double v) {
		this.v = v;
	}
	
	//MLe
	public double getFixedCosts() {
		return fixedCosts;
	}

	public void setFixedCosts(double fixedCosts) {
		this.fixedCosts = fixedCosts;
	}
	
		
	public double getN() {
		return n;
	}

	public void setN(double n) {
		this.n = n;
	}

	public double getRoh() {
		return roh;
	}

	public void setRoh(double roh) {
		this.roh = roh;
	}

	public double getLamda() {
		return lamda;
	}

	public void setLamda(double lamda) {
		this.lamda = lamda;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int getThetaID_q() {
		return thetaID_q;
	}

	public void setThetaID_q(int thetaID_q) {
		this.thetaID_q = thetaID_q;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public int getThetaID_t() {
		return thetaID_t;
	}

	public void setThetaID_t(int thetaID_t) {
		this.thetaID_t = thetaID_t;
	}

}
