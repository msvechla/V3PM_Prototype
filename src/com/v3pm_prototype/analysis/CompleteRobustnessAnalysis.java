package com.v3pm_prototype.analysis;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * Performs the Complete Robustness Analysis for a scenario
 * @author Marius Svechla
 *
 */
public class CompleteRobustnessAnalysis extends Task<List<CRAResult>>{
	
	private RoadMap roadmap;
	private RunConfiguration config;
	private List<RoadMap> lstRoadmap;
	private List<CRAResult> lstResults = new ArrayList<CRAResult>();
	private double percentage;
	double pProjectsAll = 0;
	double pGeneral;
	
	
	public CompleteRobustnessAnalysis(RunConfiguration config,
			List<RoadMap> lstRoadmap) {
		super();
		this.config = (RunConfiguration) config.clone();
		
		this.lstRoadmap = lstRoadmap;
	}


	@Override
	protected List<CRAResult> call() throws Exception {
		
		this.roadmap = (RoadMap) lstRoadmap.get(0).clone();
		int countProjects = 0;
		
		
		for(Integer pID : roadmap.implementedProjectIDs){
			for(Project p : config.getLstProjects()){
				
				if(isCancelled())break;
				
				if(p.getId() == pID){
					
					double pPerProject = 0;
					
					int countParameters = 0;
					//Start a robustness analysis for each Project parameter
					if (p.getA() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, Project.FX_READABLE_A,
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						ra.setTask(this);
						ra.start();
						pPerProject = pPerProject + ra.getPercentage();
						countParameters++;
						lstResults.add(new CRAResult(ra.getObject(), ra.getSelectedParameter(), ra.getPercentage()));
					}
					if (p.getB() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, Project.FX_READABLE_B,
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						ra.setTask(this);
						ra.start();
						pPerProject = pPerProject + ra.getPercentage();
						countParameters++;
						lstResults.add(new CRAResult(ra.getObject(), ra.getSelectedParameter(), ra.getPercentage()));
					}
					if (p.getE() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, Project.FX_READABLE_E,
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						ra.setTask(this);
						ra.start();
						pPerProject = pPerProject + ra.getPercentage();
						countParameters++;
						lstResults.add(new CRAResult(ra.getObject(), ra.getSelectedParameter(), ra.getPercentage()));
					}
					if (p.getU() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, Project.FX_READABLE_U,
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						ra.setTask(this);
						ra.start();
						pPerProject = pPerProject + ra.getPercentage();
						countParameters++;
						lstResults.add(new CRAResult(ra.getObject(), ra.getSelectedParameter(), ra.getPercentage()));
					}
					if (p.getM() != 0) {
						RobustnessAnalysis ra = new RobustnessAnalysis(
								lstRoadmap, config,
								RobustnessAnalysis.MODE_PLUSMINUS, p, Project.FX_READABLE_M,
								0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
						ra.setTask(this);
						ra.start();
						pPerProject = pPerProject + ra.getPercentage();
						countParameters++;
						lstResults.add(new CRAResult(ra.getObject(), ra.getSelectedParameter(), ra.getPercentage()));
					}

					if(countParameters == 0){
						pPerProject = pPerProject +1;
						countParameters++;
					}
					
					pPerProject = pPerProject / countParameters;
					
					pProjectsAll = pProjectsAll + pPerProject;
					countProjects++;
					break;
				}
			}
		}
		
		pProjectsAll =  pProjectsAll / countProjects;
		
		//General Parameters
		//DiscountRate
		RobustnessAnalysis ra = new RobustnessAnalysis(
				lstRoadmap, config,
				RobustnessAnalysis.MODE_PLUSMINUS, null, RunConfiguration.FX_READABLE_DISCOUNTRATE,
				0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
		ra.start();
		pGeneral = pGeneral + ra.getPercentage();
		
//		//OOAFixed
//		ra = new RobustnessAnalysis(
//				lstRoadmap, config,
//				RobustnessAnalysis.MODE_PLUSMINUS, config, "oOAFixed",
//				0.02, 0.005, RobustnessAnalysis.RELATIVE, null);
//		ra.start();
//		pGeneral = pGeneral + ra.getPercentage();
//		pGeneral = pGeneral / 2;
		
		percentage = 0.85*pProjectsAll + 0.15*pGeneral;
		
		
		return lstResults;
	}
	
	
	public double getpProjectsAll() {
		return pProjectsAll;
	}


	public void setpProjectsAll(double pProjectsAll) {
		this.pProjectsAll = pProjectsAll;
	}


	public double getpGeneral() {
		return pGeneral;
	}


	public void setpGeneral(double pGeneral) {
		this.pGeneral = pGeneral;
	}


	public double getPercentage() {
		return percentage;
	}


	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}


	public List<CRAResult> getLstResults() {
		return lstResults;
	}
	
	
	
}
