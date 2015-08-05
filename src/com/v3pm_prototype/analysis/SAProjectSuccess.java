package com.v3pm_prototype.analysis;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;

import com.v3pm_prototype.calculation.Calculator;
import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.exceptions.NoValidThetaIDException;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;
import com.v3pm_prototype.view.controller.SAProjectSuccessController;

public class SAProjectSuccess extends Analysis {

	private RoadMap roadmap;
	private Project project;
	private HashMap<String, List<RoadMap>> hmResults = new HashMap<String, List<RoadMap>>();
	ObservableList<Map> mapStandardDeviation = FXCollections.observableArrayList();
	private List<Field> lstFields = new ArrayList<Field>();
	private List<Double> lstSteps = new ArrayList<Double>();
	
	private double workProgress = 0;
	
	public SAProjectSuccess(RunConfiguration config, String mode,
			String absRel, double radius, double step,
			ProgressIndicator piSolution, Project project, RoadMap roadmap) {
		super(config, mode, absRel, radius, step, piSolution);
		this.roadmap = roadmap;
		this.project = project;
	}

	@Override
	public void start() throws IllegalArgumentException,
			IllegalAccessException, NoValidThetaIDException,
			NoSuchFieldException, SecurityException {

		if(project.getA() != 0){
			lstFields.add(project.getClass().getDeclaredField("a"));
		}
		if(project.getB() != 0){
			lstFields.add(project.getClass().getDeclaredField("b"));
		}
		if(project.getE() != 0){
			lstFields.add(project.getClass().getDeclaredField("e"));
		}
		if(project.getU() != 0){
			lstFields.add(project.getClass().getDeclaredField("u"));
		}
		if(project.getM() != 0){
			lstFields.add(project.getClass().getDeclaredField("m"));
		}

		for (Field currentField : lstFields) {
			// Change each project parameter
			currentField.setAccessible(true);
			double start = currentField.getDouble(project);
			List<RoadMap> lstResultsPerField = new ArrayList<RoadMap>();
			
			for (double i = step; i <= radius; i += step) {
				
				List<RoadMap> lstRoadmap = new ArrayList<RoadMap>();
				lstRoadmap.add((RoadMap) roadmap.clone());
				
				// positive radius
				if (mode.equals(MODE_PLUS) || mode.equals(MODE_PLUSMINUS)) {

					if (absRel == ABSOLUT) {
						currentField.setDouble(project, start + i);
					} else {
						currentField.setDouble(project, start * (1 + i));
					}

					Calculator c = new Calculator(lstRoadmap, config);
					lstResultsPerField.add((RoadMap) c.start().get(0).clone());
					System.out.println("parameter: "+currentField.getName()+" STEP: "+i+" NPV: "+lstResultsPerField.get(lstResultsPerField.size()-1).getNPVString());
					lstSteps.add(i);
				}

				// negative radius
				if (mode.equals(MODE_MINUS) || mode.equals(MODE_PLUSMINUS)) {

					if (absRel == ABSOLUT) {
						currentField.setDouble(project, start - i);
					} else {
						currentField.setDouble(project, start * (1 - i));
					}

					Calculator c = new Calculator(lstRoadmap, config);
					lstResultsPerField.add((RoadMap) c.start().get(0).clone());
					System.out.println("parameter: "+currentField.getName()+" STEP: "+(-i)+" NPV: "+lstResultsPerField.get(lstResultsPerField.size()-1).getNPVString());
					lstSteps.add(-i);
				}

				workProgress = ((lstFields.indexOf(currentField)+1)*i) / (i*lstFields.size());
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						piSolution.setProgress(workProgress);
					}
				});
				
			}
			currentField.setDouble(project, start);
			hmResults.put(currentField.getName(), lstResultsPerField);
		}
		
		calculateSD();
	}
	
	private void calculateSD() throws IllegalArgumentException, IllegalAccessException{
		for(Field field : lstFields){
			List<RoadMap> lstNPVs = hmResults.get(field.getName());
			
			//Calculate Mean
			double mean = 0;
			for(RoadMap rm : lstNPVs){
				mean = mean+rm.getNpv();
			}
			mean = mean / lstNPVs.size();
			
			//Calculate Variance
			double variance = 0;
			for(RoadMap rm : lstNPVs){
				variance = variance+Math.pow((rm.getNpv() - mean), 2);
			}
			variance = variance / lstNPVs.size();
			
			Map<String, String> dataRow = new HashMap<>();
			dataRow.put(SAProjectSuccessController.CLM_PARAMETER, field.getName());
			
			dataRow.put(SAProjectSuccessController.CLM_INITIALVALUE, String.valueOf(field.getDouble(project)));
			
			DecimalFormat df = new DecimalFormat("#,###.00 €");
			dataRow.put(SAProjectSuccessController.CLM_SD, df.format(Math.sqrt(variance)));
			
			mapStandardDeviation.add(dataRow);
			
		}
	}

	public HashMap<String, List<RoadMap>> getHmResults() {
		return hmResults;
	}

	public void setHmResults(HashMap<String, List<RoadMap>> hmResults) {
		this.hmResults = hmResults;
	}

	public List<Field> getLstFields() {
		return lstFields;
	}

	public void setLstFields(List<Field> lstFields) {
		this.lstFields = lstFields;
	}

	public List<Double> getLstSteps() {
		return lstSteps;
	}

	public void setLstSteps(List<Double> lstSteps) {
		this.lstSteps = lstSteps;
	}

	public ObservableList<Map> getMapStandardDeviation() {
		return mapStandardDeviation;
	}

	
	
}
