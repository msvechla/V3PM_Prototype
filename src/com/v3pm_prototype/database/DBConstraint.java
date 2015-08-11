package com.v3pm_prototype.database;

public class DBConstraint implements Cloneable{
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
	
	public static final String PERIOD_ALL="ALL";
	
	private DBProject s;
	private DBProject sI;
	private DBProcess i;
	private String y;
	private double x;
	private String type;
	private int countBroken = 0;
	
	public DBConstraint(String type, DBProject s, DBProject sI, DBProcess i, double d,String y) {
		super();
		this.type = type;
		this.s = s;
		this.sI = sI;
		this.i = i;
		this.y = y;
		this.x = d;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.type+"( ");
		
		if(s != null){
			sb.append(s.getName());
		}
		if( x != -1){
			sb.append(x);
		}
		if(!(sI == null && x == -1 && y == "-1" && i == null)){
			sb.append(", ");
		}
		if(sI != null){
			sb.append(sI.getName());
		}
		if(i != null){
			sb.append(i.getName());
			sb.append(", ");
		}
		if(!y.equals("-1")){
			sb.append(y);
		}
		
		sb.append(" )");
		
		return sb.toString();
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

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public int getCountBroken() {
		return countBroken;
	}

	public void addCountBroken(){
		this.countBroken++;
	}
	
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
	
	
	
}
