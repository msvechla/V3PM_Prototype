package com.v3pm_prototype.view.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.MainApp;

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
	
	public ScenarioComparisonController(){
		
	}
	
	@FXML
	public void initialize(){
		
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
		
		
	}
	
	
	
	
}
