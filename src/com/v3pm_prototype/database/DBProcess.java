package com.v3pm_prototype.database;

import java.io.Serializable;
import com.v3pm_prototype.calculation.Process;

public class DBProcess implements Serializable{
	public static final int ID_ALLPROCESSES = 0;
	public static final String NAME_ALLPROCESSES = "All Processes";
	
	private int id;
	private String name;
	private double p;
	private double oop;
	private double fixedCosts;
	private double q;
	private double degQ;
	private double t;
	private double degT;
	
	private double dmP;
	private double dmLambda;
	private double dmAlpha;
	private double dmBeta;
	private String dmFktQ;
	private String dmFktT;
	
	private String demandFunction;
	
	
	public DBProcess(int id, String name, double p,
			double oop, double fixedCosts, double q,
			double degQ, double t, double degT, double dmP,
			double dmLambda, double dmAlpha, double dmBeta, String dmFktQ,
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
	
	public Process toProcess(){
		//TODO Convert to Process
		Process process = new Process(name, id, q, 100, t, p, oop, degQ, degT, fixedCosts, 0, dmP, dmLambda, dmAlpha, dmFktQ, dmBeta, dmFktT);
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

	public double getFixedCosts() {
		return fixedCosts;
	}

	public void setFixedCosts(double fixedCosts) {
		this.fixedCosts = fixedCosts;
	}

	public double getQ() {
		return q;
	}

	public void setQ(double q) {
		this.q = q;
	}

	public double getDegQ() {
		return degQ;
	}

	public void setDegQ(double degQ) {
		this.degQ = degQ;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public double getDegT() {
		return degT;
	}

	public void setDegT(double degT) {
		this.degT = degT;
	}

	public double getDmP() {
		return dmP;
	}

	public void setDmP(double dmP) {
		this.dmP = dmP;
	}

	public double getDmLambda() {
		return dmLambda;
	}

	public void setDmLambda(double dmLambda) {
		this.dmLambda = dmLambda;
	}

	public double getDmAlpha() {
		return dmAlpha;
	}

	public void setDmAlpha(double dmAlpha) {
		this.dmAlpha = dmAlpha;
	}

	public double getDmBeta() {
		return dmBeta;
	}

	public void setDmBeta(double dmBeta) {
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
