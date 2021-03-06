package com.v3pm_prototype.calculation;

import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * Used for Calculation
 * @author Marius Svechla
 *
 */
public class Process implements Comparable<Process>, Cloneable{
	
	public static final String FX_READABLE_P = "Price";
	public static final String FX_READABLE_OOP = "Operating Outflows";
	public static final String FX_READABLE_Q = "Quality";
	public static final String FX_READABLE_T = "Time";
	public static final String FX_READABLE_FIXEDCOSTS = "Fixed Costs";
	
	private String name;
	private int id;
	private double q; // quality
	private double qmax; // upper quality boundary
	private double t; // time
	private double p; // price
	private double oop; // operating outflows
	private double d; // quality specific degeneration effect
	private double v; // time specific degeneration effect
	private double fixedCosts;
	private double n; 
	
	private double[] qPerPeriod = null;
	private double[] tPerPeriod = null;
	private double[] fixedCostsPerPeriod = null;
	private double[] OopPerPeriod = null;
	
	// demand
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

	public String toString(){
		return this.name;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Process){
			Process p = (Process)obj;
			return (p.id == this.id);
		}
		if(obj instanceof DBProcess){
			DBProcess p = (DBProcess)obj;
			return (p.getId() == this.id);
		}
		return false;
	}
	
	public double[] gettPerPeriod(RunConfiguration config) {
		if(tPerPeriod == null){
			tPerPeriod = new double[config.getPeriods()];
		}
		return tPerPeriod;
	}

	public void settPerPeriod(double[] tPerPeriod) {
		this.tPerPeriod = tPerPeriod;
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
	
	public double[] getFixedCostsPerPeriod(RunConfiguration config) {
		if(fixedCostsPerPeriod == null){
			fixedCostsPerPeriod = new double[config.getPeriods()];
		}
		return fixedCostsPerPeriod;
	}
	
	public double[] getOopPerPeriod(RunConfiguration config) {
		if(OopPerPeriod == null){
			OopPerPeriod = new double[config.getPeriods()];
		}
		return OopPerPeriod;
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
	
	public String getNodeID(){
		return "PROCESS:"+String.valueOf(this.getId()) + String.valueOf(this.getName());
	}

	@Override
	public int compareTo(Process p) {
		return Integer.compare(this.id, p.getId());
	}
	
	public double getQDelta(){
		return q - this.qPerPeriod[0];
	}
	
	public double getTDelta(){
		return t - this.tPerPeriod[0];
	}
	
	public double getOopDelta(){
		if(this.OopPerPeriod != null){
			return oop - this.OopPerPeriod[0];
		}
		return 0;
	}
	
	public String getShortName(){
		if(this.name.length()>10){
			return this.name.substring(0, 10)+"..";
		}else{
			return this.name;
		}
	}
	
	public double getFixedCostsDelta(){
		if(this.fixedCostsPerPeriod != null){
			return fixedCosts - this.fixedCostsPerPeriod[0];
		}
		return 0;
		
	}
	
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}

}
