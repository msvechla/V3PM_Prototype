package com.v3pm_prototype.analysis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.ProgressIndicator;

import com.v3pm_prototype.calculation.Calculator;
import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.exceptions.NoValidThetaIDException;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class SAProjectSuccess extends Analysis {

	private RoadMap roadmap;
	private Project project;
	private HashMap<String, List<RoadMap>> hmResults = new HashMap<String, List<RoadMap>>();

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

		List<Field> lstFields = new ArrayList<Field>();
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

				}

			}
			currentField.setDouble(project, start);
			hmResults.put(currentField.getName(), lstResultsPerField);
		}

	}

}
