package com.v3pm_prototype.database;

public class DBProcess {
	public static final int ID_EMPTYPROCESS = 0;
	public static final String NAME_EMPTYPROCESS = "No Process";
	
	private int id;
	private String name;
	
	public DBProcess(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public String toString(){
		return this.name;
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
	
}
