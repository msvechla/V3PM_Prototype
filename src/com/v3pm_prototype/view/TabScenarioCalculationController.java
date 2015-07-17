package com.v3pm_prototype.view;

import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import com.v3pm_prototype.calculation.Calculator;
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
	private RunConfiguration config;
	private List<RoadMap> rmList;
	
	public TabScenarioCalculationController() {
		
	}
	
	@FXML
	public void initialize(){
		lvRoadmaps.setItems(olRoadmap);
	}
	
	/**
	 * Generates the initial Roadmaps and calculates their NPVs
	 */
	private void startInitialCalculations(){
		Service<List<RoadMap>> svRMGen = initialRMGenService();
		
		svRMGen.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("TEST");
				Service<List<RoadMap>> svNPVCalc = initialNPVCalcService((List<RoadMap>) event.getSource().getValue());
				svNPVCalc.start();
			}
		});
		
		svRMGen.start();
		
	}
	
	/**
	 * Generates the initial Roadmaps and updates the UI
	 * @return
	 */
	private Service<List<RoadMap>> initialRMGenService() {
		// Update Statusbar
		this.mainApp.getV3pmGUIController().setProgress(-1);
		this.mainApp.getV3pmGUIController().setStatus("Generating Roadmaps...");

		// Create a Service that executes the RMGenerator Task and start it
		Service<List<RoadMap>> service = new Service<List<RoadMap>>() {
			@Override
			protected Task<List<RoadMap>> createTask() {
				return new RMGenerator(config) {

					@Override
					protected void succeeded() {
						mainApp.getV3pmGUIController().setProgress(0);
						mainApp.getV3pmGUIController().setStatus(
								"Roadmaps generated.");
						rmList = (List<RoadMap>) getValue();
						olRoadmap.addAll(rmList);
						super.succeeded();
					}

				};
			}
		};
		return service;
	}
	
	/**
	 * Calculates the NPV of the initial Roadmaps and updates the UI
	 * @param r 
	 * @return
	 */
	private Service<List<RoadMap>> initialNPVCalcService(final List<RoadMap> generatedRoadmaps) {		// Update Statusbar
		this.mainApp.getV3pmGUIController().setProgress(-1);
		this.mainApp.getV3pmGUIController().setStatus("Calculating NPVs...");

		// Create a Service that executes the RMGenerator Task and start it
		Service<List<RoadMap>> service = new Service<List<RoadMap>>() {
			@Override
			protected Task<List<RoadMap>> createTask() {
				return new Calculator(generatedRoadmaps, config) {

					@Override
					protected void succeeded() {
						super.succeeded();
						mainApp.getV3pmGUIController().setProgress(0);
						mainApp.getV3pmGUIController().setStatus(
								"NPVs calculated.");
						lblNPV.setText(String.valueOf(this.getValue().get(0).getNpv()));
						olRoadmap.clear();
						rmList = getValue();
						
						olRoadmap.addAll(rmList);
						
					}

				};
			}
		};
		return service;
	}

	public void setScenario(DBScenario newScenario) {
		this.scenario = newScenario;
		this.config = newScenario.generateRunConfiguration();
		startInitialCalculations();
		
	}
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
	
}
