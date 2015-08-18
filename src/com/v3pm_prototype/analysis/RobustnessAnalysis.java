package com.v3pm_prototype.analysis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

import com.v3pm_prototype.calculation.NPVCalculator;
import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.exceptions.NoValidThetaIDException;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * Performs a Robustness Analysis
 * @author Marius Svechla
 *
 */
public class RobustnessAnalysis extends Analysis{

	private List<RoadMap> lstRoadmap;
	
	private List<RoadMap> lstResults = new ArrayList<RoadMap>();
	private List<Double> lstDouble = new ArrayList<Double>();

	private List<RoadMap> lstResultsOldRoadmap = new ArrayList<RoadMap>();
	private List<Double> lstDoubleOldRoadmap = new ArrayList<Double>();
	
	private RoadMap rmOld;
	private String parameterReadable;
	private String parameterCode;
	private Field selectedParameter;
	
	private Object object;
	
	private double percentage;
	private String solutionText;
	private double workProgress;
	private Task task = null;
	

	public RobustnessAnalysis(List<RoadMap> lstRoadmap, RunConfiguration config, String mode, Object object, String parameterReadable, double radius, double step, String absRel, ProgressIndicator piSolution) {
		super(config, mode, absRel, radius, step, piSolution);
		this.lstRoadmap = new ArrayList<RoadMap>();
		
		for(RoadMap rm : lstRoadmap){
			this.lstRoadmap.add((RoadMap) rm.clone());
		}
		
		this.rmOld = this.lstRoadmap.get(0);

		// Get a copy of the object from the newly generated config, so the
		// initial object stays the same
		if(object != null){
			if(object instanceof Project){
				for(Project p : this.config.getLstProjects()){
					if(((Project)object).getId() == p.getId()){
						this.object = p;
						break;
					}
				}
			}else if(object instanceof Process){
				for(Process p : this.config.getLstProcesses()){
					if(((Process)object).getId() == p.getId()){
						this.object = p;
						break;
					}
				}
			}
		}else{
			this.object = this.config;
		}
		
		this.parameterReadable = parameterReadable;
		this.parameterCode = Analysis.mapToCodeParameter(parameterReadable);
		
		try {
			this.selectedParameter = this.object.getClass().getDeclaredField(parameterCode);
			this.selectedParameter.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void start() throws IllegalArgumentException, IllegalAccessException, NoValidThetaIDException{
		
		double start = selectedParameter.getDouble(object);

		for (double i = step; i <= radius; i += step) {

			if(task != null){
				if(task.isCancelled()) break;
			}
			
			// positive radius
			if(mode.equals(MODE_PLUS) || mode.equals(MODE_PLUSMINUS)){
				
				if(absRel == ABSOLUT){
					selectedParameter.setDouble(object, start + i);
				}else{
					selectedParameter.setDouble(object, start * (1+i));
				}
				
				NPVCalculator c = new NPVCalculator(lstRoadmap, config);
				
				List<RoadMap> calculatedRoadmaps =  c.start();
				c = null; 
				RoadMap bestRM = (RoadMap) calculatedRoadmaps.get(0).clone();
				lstResults.add(bestRM);
				lstDouble.add(selectedParameter.getDouble(object));
				
				if(!bestRM.equals(this.rmOld)){
					//Find the values of the old Roadmap
					for(RoadMap rm : calculatedRoadmaps){
						if(rm.implementedProjectIDs.equals(rmOld.implementedProjectIDs)){
							if(rm.equals(rmOld)){
								lstResultsOldRoadmap.add((RoadMap) rm.clone());
								lstDoubleOldRoadmap.add(selectedParameter.getDouble(object));
								break;
							}
						}
					}
				}else{
					lstResultsOldRoadmap.add(bestRM);
					lstDoubleOldRoadmap.add(selectedParameter.getDouble(object));
				}
				
				System.out.println("Value: " + selectedParameter.getDouble(object) + "\n" 
						+ lstResults.get(lstResults.size() - 1) + " NPV: "
						+ lstResults.get(lstResults.size() - 1).getNpv());
			}
			

			// negative radius
			if(mode.equals(MODE_MINUS) || mode.equals(MODE_PLUSMINUS)){
				
				if(absRel == ABSOLUT){
					selectedParameter.setDouble(object, start - i);
				}else{
					selectedParameter.setDouble(object, start * (1-i));
				}
				
				NPVCalculator c = new NPVCalculator(lstRoadmap, config);
				
				List<RoadMap> calculatedRoadmaps =  c.start();
				
				RoadMap bestRM = (RoadMap) calculatedRoadmaps.get(0).clone();
				lstResults.add(bestRM);
				lstDouble.add(selectedParameter.getDouble(object));
				
				if(!bestRM.equals(this.rmOld)){
					//Find the values of the old Roadmap
					for(RoadMap rm : calculatedRoadmaps){
						if(rm.implementedProjectIDs.equals(rmOld.implementedProjectIDs)){
							if(rm.equals(rmOld)){
								lstResultsOldRoadmap.add((RoadMap) rm.clone());
								lstDoubleOldRoadmap.add(selectedParameter.getDouble(object));
								break;
							}
						}
					}
				}else{
					lstResultsOldRoadmap.add(bestRM);
					lstDoubleOldRoadmap.add(selectedParameter.getDouble(object));
				}
				
				System.out.println("Value: " + selectedParameter.getDouble(object) + "\n"
						+ lstResults.get(lstResults.size() - 1) + " NPV: "
						+ lstResults.get(lstResults.size() - 1).getNpv());
			}
			
			workProgress = i / radius;
			
			if (piSolution != null) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						piSolution.setProgress(workProgress);
					}
				});
			}
			
		}
		
		workProgress = 1;

		selectedParameter.setDouble(object, start);
		
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
		
		this.percentage = (Double.valueOf(countRobust) / lstResults.size());

		if(rmNextBest == null){
			solutionText = "Parameter \""+parameterReadable+"\" is robust.\nNo new best solution has been found using the specified criteria.";
		}else{
			solutionText = "Parameter \""+parameterReadable+"\" robustness level: "+this.percentage*100+"%\nNew best Roadmap found using the specified criteria: "+rmNextBest;
		}
		
		System.out.println(solutionText);
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
		return parameterReadable;
	}

	public List<RoadMap> getLstResultsOldRoadmap() {
		return lstResultsOldRoadmap;
	}

	public void setLstResultsOldRoadmap(List<RoadMap> lstResultsOldRoadmap) {
		this.lstResultsOldRoadmap = lstResultsOldRoadmap;
	}

	public List<Double> getLstDouble() {
		return lstDouble;
	}

	public void setLstDouble(List<Double> lstDouble) {
		this.lstDouble = lstDouble;
	}

	public List<Double> getLstDoubleOldRoadmap() {
		return lstDoubleOldRoadmap;
	}

	public void setLstDoubleOldRoadmap(List<Double> lstDoubleOldRoadmap) {
		this.lstDoubleOldRoadmap = lstDoubleOldRoadmap;
	}

	public String getSolutionText() {
		return solutionText;
	}

	public void setTask(Task task){
		this.task = task;
	}
}
