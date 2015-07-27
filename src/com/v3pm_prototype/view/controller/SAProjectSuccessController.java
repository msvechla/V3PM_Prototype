package com.v3pm_prototype.view.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RoadMap;

public class SAProjectSuccessController extends AnalysisController{
	
	@FXML
	private ChoiceBox<Object> cbProject;
	private ObservableList<Object> olProject = FXCollections.observableArrayList();
	
	private RoadMap selectedRoadmap;
	
	public SAProjectSuccessController(){
		
	}
	
	@Override
	public void initialize() {
		super.initialize();
	}

	private void initCBProject() {
		cbProject.setItems(olProject);
		for(Project p : this.tsc.getConfig().getLstProjects()){
			for(Project p2 : selectedRoadmap.getLstProjectCalculated()){
				if(p.getId() == p2.getId()){
					olProject.add(p);
				}
			}
		}
		cbProject.getSelectionModel().select(olProject.get(0));
	}

	@Override
	public MainApp getMainApp() {
		return super.getMainApp();
	}



	@Override
	public void setMainApp(MainApp mainApp) {
		// TODO Auto-generated method stub
		super.setMainApp(mainApp);
	}



	@Override
	public TabScenarioCalculationController getTsc() {
		// TODO Auto-generated method stub
		return super.getTsc();
	}



	@Override
	public void setTsc(TabScenarioCalculationController tsc) {
		// TODO Auto-generated method stub
		super.setTsc(tsc);
	}
	
	public void setRoadmap(RoadMap roadmap){
		this.selectedRoadmap = roadmap;
		initCBProject();
	}
	
}
