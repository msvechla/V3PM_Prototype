package com.v3pm_prototype.view.controller;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.util.set.Array;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import com.v3pm_prototype.calculation.Calculator;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class ScenarioComparisonController {
	@FXML
	private VBox roadmapBox1;
	
	@FXML
	private VBox roadmapBox2;
	
	@FXML
	private Label lblNPV1;
	
	@FXML
	private Label lblNPV2;
	
	@FXML
	private Label lblSolution;
	
	//Charts
	@FXML
	private BarChart bcQuality;
	@FXML
	private BarChart bcTime;
	@FXML
	private BarChart bcOOP;
	@FXML
	private BarChart bcFixedCosts;
	
	@FXML
	private BarChart bcBrokenRestrictions;
	
	private MainApp mainApp;
	private Tab tab;
	private DBScenario scenario1;
	private DBScenario scenario2;
	private RunConfiguration config1;
	private RunConfiguration config2;
	private List<RoadMap> rmList1 = new ArrayList<RoadMap>();
	private List<RoadMap> rmList2 = new ArrayList<RoadMap>();
	
	public ScenarioComparisonController(){
		
	}
	
	@FXML
	public void initialize(){
		
	}
	
	private void initialRMCalculation(){
		Task calcTask = new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				Calculator calc1 = new Calculator(rmList1, config1);
				Calculator calc2 = new Calculator(rmList2, config2);
				
				rmList1 = calc1.start();
				rmList2 = calc2.start();
				return null;
			}

			@Override
			protected void succeeded() {
				System.out.println("SUCCEEDED");
				lblNPV1.setText("NPV: "+rmList1.get(0).getNPVString());
				lblNPV2.setText("NPV: "+rmList2.get(0).getNPVString());
				
				super.succeeded();
			}
			
			
		};
		
		Thread t = new Thread(calcTask);
		t.setDaemon(false);
		t.start();
	}
	
	private void initialRMGeneration(){
		RMGenerator rmGen1 = new RMGenerator(config1);
		RMGenerator rmGen2 = new RMGenerator(config2);
		
		Thread t1 = new Thread(rmGen1);
		t1.setDaemon(false);
		
		Thread t2 = new Thread(rmGen2);
		t2.setDaemon(false);
		
		rmGen1.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				rmList1 = rmGen1.getValue();
				t2.start();
			}
		});
		
		rmGen2.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				rmList2 = rmGen2.getValue();
				initialRMCalculation();
			}
		});
		
		t1.start();
		
		
	}
	
	/**
	 * Generates the initial Roadmaps and updates the UI
	 * 
	 * @return
	 */
//	private Service<List<RoadMap>> initialRMGenService() {
//		// Update Statusbar
//		this.mainApp.getV3pmGUIController().setProgress(-1);
//		this.mainApp.getV3pmGUIController().setStatus("Generating Roadmaps...");
//
//		// Create a Service that executes the RMGenerator Task and start it
//		Service<List<RoadMap>> service = new Service<List<RoadMap>>() {
//			@Override
//			protected Task<List<RoadMap>> createTask() {
//				return new RMGenerator(config) {
//
//					@Override
//					protected void succeeded() {
//						mainApp.getV3pmGUIController().setProgress(0);
//						mainApp.getV3pmGUIController().setStatus(
//								"Roadmaps generated.");
//						rmList = (List<RoadMap>) getValue();
//						olRoadmap.addAll(rmList);
//						System.out.println(config);
//						super.succeeded();
//					}
//
//				};
//			}
//		};
//		return service;
//	}
//
//	/**
//	 * Calculates the NPV of the initial Roadmaps and updates the UI
//	 * 
//	 * @param r
//	 * @return
//	 */
//	private Service<List<RoadMap>> initialNPVCalcService(
//			final List<RoadMap> generatedRoadmaps) { // Update Statusbar
//		this.mainApp.getV3pmGUIController().setProgress(-1);
//		this.mainApp.getV3pmGUIController().setStatus("Calculating NPVs...");
//
//		// Create a Service that executes the RMGenerator Task and start it
//		Service<List<RoadMap>> service = new Service<List<RoadMap>>() {
//			@Override
//			protected Task<List<RoadMap>> createTask() {
//
//				return new Task<List<RoadMap>>() {
//
//					@Override
//					protected List<RoadMap> call() throws Exception {
//						Calculator c = new Calculator(generatedRoadmaps, config);
//						return c.start();
//					}
//
//					@Override
//					protected void succeeded() {
//						super.succeeded();
//						mainApp.getV3pmGUIController().setProgress(0);
//						mainApp.getV3pmGUIController().setStatus(
//								"NPVs calculated.");
//						// Display the NPV
//						scenario.setNpv(this.getValue().get(0).getNpv());
//						lblNPV.setText(scenario.getNPVString());
//
//						try {
//							writeDBNPV();
//						} catch (SQLException e) {
//							System.err
//									.println("[SQL ERROR] writing NPV to Database");
//						}
//
//						// Add items to the Roadmap table, select first item and
//						// update charts
//						olRoadmap.clear();
//						rmList = getValue();
//						olRoadmap.addAll(rmList);
//						tvRoadmap.getSelectionModel().select(rmList.get(0));
//						tvRoadmap.getSelectionModel().selectedItemProperty()
//								.addListener(rmListChangeListener);
//						initLineCharts();
//						initGraphStream();
//						initRoadmapContainer(null);
//						updateTVProcesses();
//						startCompleteRobustnessAnalysis();
//						initBarChartRBroken();
//					}
//
//				};
//
//			}
//		};
//		return service;
//	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public void setScenarios(DBScenario scenario1, DBScenario scenario2) {
		this.scenario1 = scenario1;
		this.scenario2 = scenario2;
		this.config1 = scenario1.generateRunConfiguration();
		this.config2 = scenario2.generateRunConfiguration();
		initialRMGeneration();
	}
	
	
	
	
}
