package com.v3pm_prototype.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class TabScenarioCalculationController {
	@FXML
	private Label lblNPV;
	@FXML
	private ListView<RoadMap> lvRoadmaps;
	private ObservableList<RoadMap> olRoadmap = FXCollections.observableArrayList();
	
	private MainApp mainApp;
	private DBScenario scenario;
	
	public TabScenarioCalculationController() {
		
	}
	
	@FXML
	public void initialize(){
		lvRoadmaps.setItems(olRoadmap);
	}

	public void setScenario(DBScenario newScenario) {
		this.scenario = newScenario;
		
		//Update Statusbar
		this.mainApp.getV3pmGUIController().setProgress(-1);
		this.mainApp.getV3pmGUIController().setStatus("Generating Roadmaps...");
		
		//Create a Service that executes the RMGenerator Task and start it
		Service<List<RoadMap>> service = new Service<List<RoadMap>>(){
			@Override
			protected Task<List<RoadMap>> createTask() {
				return new RMGenerator(scenario.generateRunConfiguration());
			}
		};
		
		//When the task is succeeded update Statusbar
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("SUCCEEDED");
				mainApp.getV3pmGUIController().setProgress(0);
				mainApp.getV3pmGUIController().setStatus("Roadmaps generated.");
				olRoadmap.addAll((List<RoadMap>) event.getSource().getValue());
			}
		});
		service.start();
	}
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
	
}
