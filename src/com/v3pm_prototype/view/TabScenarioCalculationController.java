package com.v3pm_prototype.view;

import java.text.DecimalFormat;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
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
	private TableView<RoadMap> tvRoadmap;
	private ObservableList<RoadMap> olRoadmap = FXCollections.observableArrayList();
	@FXML
	private TableColumn<RoadMap, String> clmRoadmap;
	@FXML
	private TableColumn<RoadMap, Double> clmNPV;
	
	
	//Charts
	@FXML
	private LineChart<String, Number> lcProcessQuality;
	@FXML
	private LineChart<String, Number> lcProcessTime;
	
	private MainApp mainApp;
	private DBScenario scenario;
	private RunConfiguration config;
	private List<RoadMap> rmList;
	
	public TabScenarioCalculationController() {
		
	}
	
	@FXML
	public void initialize(){
		initTVRoadmaps();
	}
	
	/**
	 * Generates the initial Roadmaps and calculates their NPVs
	 */
	private void startInitialCalculations(){
		Service<List<RoadMap>> svRMGen = initialRMGenService();
		
		svRMGen.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				Service<List<RoadMap>> svNPVCalc = initialNPVCalcService((List<RoadMap>) event.getSource().getValue());
				svNPVCalc.start();
			}
		});
		
		svRMGen.start();
		
	}
	
	private void initTVRoadmaps(){
		this.tvRoadmap.setItems(olRoadmap);
		clmRoadmap.setCellValueFactory(new PropertyValueFactory<RoadMap, String>("displayText"));
		clmNPV.setCellValueFactory(new PropertyValueFactory<RoadMap, Double>("npv"));
		clmNPV.setSortType(SortType.DESCENDING);
		tvRoadmap.getSortOrder().add(clmNPV);
		tvRoadmap.sort();
	}
	
	private void initLineCharts(){

		for (com.v3pm_prototype.calculation.Process p : rmList.get(0)
				.getLstProcessCalculated()) {
			
			Series<String, Number> seriesQ = new XYChart.Series<String, Number>();
			seriesQ.setName(p.getName());
			
			Series<String, Number> seriesT = new XYChart.Series<String, Number>();
			seriesT.setName(p.getName());

			for (int period = 0; period < config.getPeriods(); period++) {
				seriesQ.getData().add(
						new XYChart.Data<String, Number>("Period "
								+ String.valueOf(period), p
								.getqPerPeriod(config)[period]));
				
				seriesT.getData().add(
						new XYChart.Data<String, Number>("Period "
								+ String.valueOf(period), p
								.gettPerPeriod(config)[period]));
			}
			
			lcProcessQuality.getData().add(seriesQ);
			lcProcessTime.getData().add(seriesT);
		}
		
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
						
						DecimalFormat df = new DecimalFormat("#,###.00 €");
						lblNPV.setText(df.format(this.getValue().get(0).getNpv()));
						
						olRoadmap.clear();
						rmList = getValue();
						olRoadmap.addAll(rmList);
						
						initLineCharts();
						
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
