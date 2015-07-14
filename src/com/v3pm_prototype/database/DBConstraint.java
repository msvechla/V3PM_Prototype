package com.v3pm_prototype.database;

public class DBConstraint {
	public static final String TYPE_LOCMUTEX="LocMutEx";
	public static final String TYPE_GLOMUTEX="GloMutEx";
	public static final String TYPE_LOCMUTDEP="LocMutDep";
	public static final String TYPE_GLOMUTDEP="GloMutDep";
	public static final String TYPE_PRESUC="PreSuc";
	
	public static final String TYPE_EARLIEST="Earliest";
	public static final String TYPE_LATEST="Latest";
	public static final String TYPE_MANDATORY="Mandatory";
	
	public static final String TYPE_QUALMIN="QualMin";
	public static final String Type_TIMEMAX="TimeMax";
	
	public static final String TYPE_BUDPRO="BudPro";
	public static final String TYPE_BUDBPM="BudBPM";
	public static final String TYPE_BUDGET="Budget";
	public static final String TYPE_NUMPROJ="NumProj";
	
	private DBProject s;
	private DBProject sI;
	private DBProcess i;
	private int y;
	private float x;
	private String type;
	
	public DBConstraint(String type, DBProject s, DBProject sI, DBProcess i, int y, float x) {
		super();
		this.type = type;
		this.s = s;
		this.sI = sI;
		this.i = i;
		this.y = y;
		this.x = x;
	}
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DBProject getS() {
		return s;
	}

	public void setS(DBProject s) {
		this.s = s;
	}

	public DBProject getSi() {
		return sI;
	}

	public void setSi(DBProject sI) {
		this.sI = sI;
	}

	public DBProcess getI() {
		return i;
	}

	public void setI(DBProcess i) {
		this.i = i;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	
	
	
}
