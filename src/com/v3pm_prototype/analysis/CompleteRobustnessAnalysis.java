package com.v3pm_prototype.analysis;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class CompleteRobustnessAnalysis extends Task<List<RobustnessAnalysis>>{
	
	private RoadMap roadmap;
	private RunConfiguration config;
	private List<RoadMap> lstRoadmap;
	private List<RobustnessAnalysis> lstResults = new ArrayList<RobustnessAnalysis>();
	private double percentage;
	
	
	public CompleteRobustnessAnalysis(RunConfiguration config,
			List<RoadMap> lstRoadmap) {
		super();
		this.config = config;
		this.lstRoadmap = lstRoadmap;
	}


	@Override
	protected List<RobustnessAnalysis> call() throws Exception {
		
		this.roadmap = lstRoadmap.get(0);
		
		for(Integer pID : roadmap.implementedProjectIDs){
			for(Project p : config.getLstProjects()){
				if(p.getId() == pID){
					
					//Start a robustness analysis for each Project parameter
					if (p.getA() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, "a",
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						lstResults.add(ra);

					}
					if (p.getB() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, "b",
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						lstResults.add(ra);
					}
					if (p.getE() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, "e",
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						lstResults.add(ra);
					}
					if (p.getU() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, "u",
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						lstResults.add(ra);
					}
					if (p.getM() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, "m",
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						lstResults.add(ra);
					}

					break;
				}
			}
		}
		
		for(RobustnessAnalysis ra : lstResults){
			ra.start();
		}
		
		calculatePercentage();
		
		
		return lstResults;
	}
	
	
	private void calculatePercentage(){
		
		double percentage = 0;
		
		for(RobustnessAnalysis ra : lstResults){
			percentage += ra.getPercentage();
		}
		
		percentage = percentage / lstResults.size();
		this.percentage = percentage;
	}


	public double getPercentage() {
		return percentage;
	}


	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}


	public List<RobustnessAnalysis> getLstResults() {
		return lstResults;
	}
	
	
	
}
