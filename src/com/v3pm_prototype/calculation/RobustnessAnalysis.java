package com.v3pm_prototype.calculation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.v3pm_prototype.exceptions.NoValidThetaIDException;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class RobustnessAnalysis {

	public static final String MODE_PLUS = "MODE_PLUS";
	public static final String MODE_MINUS = "MODE_MINUS";
	public static final String MODE_PLUSMINUS = "MODE_PLUSMINUS";
	
	private List<RoadMap> lstRoadmap;
	private RunConfiguration config;
	private List<RoadMap> lstResults = new ArrayList<RoadMap>();
	private String mode;
	private RoadMap rmOld;
	private String parameter;
	private Field selectedParameter;
	private Object object;
	private double radius = 0.01;
	private double step = 0.001;
	private double percentage;

	public RobustnessAnalysis(List<RoadMap> lstRoadmap, RunConfiguration config, String mode, Object object, String parameter, double radius, double step) {
		super();
		this.lstRoadmap = new ArrayList<RoadMap>();
		this.lstRoadmap.addAll(lstRoadmap);
		this.rmOld = (RoadMap) lstRoadmap.get(0).clone();
		this.config = new RunConfiguration(config.getPeriods(), config.getSlotsPerPeriod(), config.getDiscountRate(), config.getOOAFixed(), config.getConstraintSet());
		this.radius = radius;
		this.step = step;
		this.parameter = parameter;
		
		
		List<Project> lstProjects = new ArrayList<Project>();
		lstProjects.addAll(config.getLstProjects());
		this.config.setLstProjects(lstProjects);
		
		List<Process> lstProcesses = new ArrayList<Process>();
		lstProcesses.addAll(config.getLstProcesses());
		this.config.setLstProcesses(lstProcesses);
		
		this.mode = mode;

		// Get a copy of the object from the newly generated config, so the
		// initial object stays the same
		if(object instanceof Project){
			for(Project p : config.getLstProjects()){
				if(((Project)object).getId() == p.getId()){
					this.object = p;
					break;
				}
			}
		}
		
		if(object instanceof Process){
			for(Process p : config.getLstProcesses()){
				if(((Process)object).getId() == p.getId()){
					this.object = p;
					break;
				}
			}
		}
		
		try {
			this.selectedParameter = object.getClass().getDeclaredField(parameter);
			this.selectedParameter.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public void start() throws IllegalArgumentException, IllegalAccessException, NoValidThetaIDException{

		double start = selectedParameter.getDouble(object);

		for (double i = step; i <= radius; i += step) {

			// positive radius
			if(mode.equals(MODE_PLUS) || mode.equals(MODE_PLUSMINUS)){
				selectedParameter.setDouble(object, start + i);
				Calculator c = new Calculator(lstRoadmap, config);
				lstResults.add((RoadMap) c.start().get(0).clone());
				System.out.println("Value: " + selectedParameter.getDouble(object) + "\n" 
						+ lstResults.get(lstResults.size() - 1) + " NPV: "
						+ lstResults.get(lstResults.size() - 1).getNpv());
			}
			

			// negative radius
			if(mode.equals(MODE_MINUS) || mode.equals(MODE_PLUSMINUS)){
				selectedParameter.setDouble(object, start - i);
				Calculator c = new Calculator(lstRoadmap, config);
				List<RoadMap> r = new ArrayList<RoadMap>();
				r.addAll(c.start());
				lstResults.add((RoadMap) c.start().get(0).clone());
				System.out.println("Value: " + selectedParameter.getDouble(object) + "\n"
						+ lstResults.get(lstResults.size() - 1) + " NPV: "
						+ lstResults.get(lstResults.size() - 1).getNpv());
			}
			
		}

		evaluation();
	}

	private void evaluation() {
		RoadMap rmNextBest = null;
		int countRobust = 0;
		
		for (RoadMap rmCurrent : lstResults) {
			
			if (rmCurrent.equals(rmOld)) {
				countRobust++;
			}else{
				if(rmNextBest == null){
					rmNextBest = rmCurrent;
				}
			}
		}
		
		if(rmNextBest == null){
			 System.out.println("ES WURDE KEINE BESSERE ROADMAP GEFUNDEN. ROADMAP ROBUST");
		}else{
			System.out.println("NEUE BESTE L�SUNG GEFUNDEN MIT: "+rmNextBest);
		}
		
		this.percentage = (Double.valueOf(countRobust) / lstResults.size());
		
		System.out.println("Roadmap robustness level: "+ this.percentage);
	}

	public List<RoadMap> getLstRoadmap() {
		return lstRoadmap;
	}

	public void setLstRoadmap(List<RoadMap> lstRoadmap) {
		this.lstRoadmap = lstRoadmap;
	}

	public RunConfiguration getConfig() {
		return config;
	}

	public void setConfig(RunConfiguration config) {
		this.config = config;
	}

	public List<RoadMap> getLstResults() {
		return lstResults;
	}

	public void setLstResults(List<RoadMap> lstResults) {
		this.lstResults = lstResults;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public RoadMap getRmOld() {
		return rmOld;
	}

	public void setRmOld(RoadMap rmOld) {
		this.rmOld = rmOld;
	}

	public Field getSelectedParameter() {
		return selectedParameter;
	}

	public void setSelectedParameter(Field selectedParameter) {
		this.selectedParameter = selectedParameter;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getStep() {
		return step;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public String getParameter() {
		return parameter;
	}
	
	

}