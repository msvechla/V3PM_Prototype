package com.v3pm_prototype.view;

import com.v3pm_prototype.rmgeneration.RoadMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class TabScenarioCalculationController {
	@FXML
	private TextField tfNPV;
	@FXML
	private ListView<RoadMap> lvRoadmaps;
	private ObservableList<RoadMap> olRoadmap = FXCollections.observableArrayList();
	
	public TabScenarioCalculationController() {
		
	}
	
	@FXML
	public void initialize(){
		
	}
}
