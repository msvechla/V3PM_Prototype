package com.v3pm_prototype.database;

public class DBProject {
	private int id;
	private String name;
	private String type;
	private int periods;
	private DBProcess process;
	
	//TODO ADD process to DBProject
	
	public DBProject(int id, String name, String type, int periods, DBProcess process) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.periods = periods;
		this.setProcess(process);
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

	public DBProcess getProcess() {
		return process;
	}

	public void setProcess(DBProcess process) {
		this.process = process;
	}
	
	
	
}
