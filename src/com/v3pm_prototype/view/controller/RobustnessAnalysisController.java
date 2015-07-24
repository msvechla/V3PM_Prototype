package com.v3pm_prototype.view.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.calculation.RobustnessAnalysis;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.MainApp;

public class RobustnessAnalysisController {
	@FXML
	private ChoiceBox<String> cbType;
	private ObservableList<String> olType = FXCollections.observableArrayList();
	
	
	@FXML
	private ChoiceBox<Object> cbObject;
	private ObservableList<Object> olObject = FXCollections.observableArrayList();
	
	@FXML
	private ChoiceBox<String> cbParameter;
	private ObservableList<String> olParameter = FXCollections.observableArrayList();
	
	@FXML
	private TextField tfRadius;
	
	@FXML
	private TextField tfStep;
	
	@FXML
	private ChoiceBox<String> cbModus;
	private ObservableList<String> olModus = FXCollections.observableArrayList();
	
	@FXML
	private Button btnStartAnalysis;
	
	@FXML
	private LineChart lineChart;
	
	
	private MainApp mainApp;
	private TabScenarioCalculationController tsc;
	private DBScenario scenario;
	
	
	
	public RobustnessAnalysisController(){
		
	}
	
	@FXML
	public void initialize(){
		initCBType();
		initCBModus();
	}
	
	public void startRobustnessAnalysis(){
		Task raTask = new Task<RobustnessAnalysis>() {

			@Override
			protected RobustnessAnalysis call() throws Exception {
				RobustnessAnalysis ra = new RobustnessAnalysis(tsc.getRmList(),
						tsc.getConfig(), cbModus.getValue(),
						cbObject.getValue(), cbParameter.getValue(),
						Double.valueOf(tfRadius.getText()),
						Double.valueOf(tfStep.getText()));
				ra.start();
				return ra;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				
				initLineChart(this.getValue());
			}

		};
		
		Thread t = new Thread(raTask);
		t.setDaemon(false);
		t.start();
	}
	
	private void initLineChart(RobustnessAnalysis robustnessAnalysis){
		lineChart.getData().clear();


			// Create a series for the best Roadmap and for the old best Roadmap
			Series<String, Number> seriesOld = new XYChart.Series<String, Number>();
			seriesOld.setName(tsc.getRmList().get(0).toString());

			Series<String, Number> seriesNew = new XYChart.Series<String, Number>();
			seriesNew.setName("Best Roadmap");

			// Add data to each series for each step of the parameter modification
			for (int i = 0; i < robustnessAnalysis.getLstResults().size(); i++) {
				if(i<robustnessAnalysis.getLstDoubleOldRoadmap().size()){
					seriesOld.getData().add(
							new XYChart.Data<String, Number>(robustnessAnalysis.getLstDoubleOldRoadmap().get(i).toString(),robustnessAnalysis.getLstResultsOldRoadmap().get(i).getNpv()));
				}
				
				seriesNew.getData().add(
						new XYChart.Data<String, Number>(robustnessAnalysis.getLstDouble().get(i).toString(),robustnessAnalysis.getLstResults().get(i).getNpv()));

				
			}

			lineChart.getData().add(seriesOld);
			lineChart.getData().add(seriesNew);

	}
	
	private void initCBModus() {
		cbModus.setItems(olModus);
		olModus.add(RobustnessAnalysis.MODE_MINUS);
		olModus.add(RobustnessAnalysis.MODE_PLUS);
		olModus.add(RobustnessAnalysis.MODE_PLUSMINUS);
		cbModus.getSelectionModel().select(olModus.get(0));
	}

	private void initCBParameter() {
		cbParameter.setItems(olParameter);
		olParameter.clear();
		
		
		if(cbType.getSelectionModel().getSelectedItem().equals("Project")){
			olParameter.add("a");
			olParameter.add("b");
			olParameter.add("e");
			olParameter.add("u");
			olParameter.add("m");
		}
		
		if(cbType.getSelectionModel().getSelectedItem().equals("Process")){
			olParameter.add("q");
			olParameter.add("t");
			olParameter.add("p");
			olParameter.add("oop");
			olParameter.add("fixedCosts");
		}
		
		cbParameter.getSelectionModel().select(olParameter.get(0));
		
	}
	
	private void initCBObject(){
		cbObject.setItems(olObject);
		olObject.clear();
		
		if(cbType.getSelectionModel().getSelectedItem().equals("Project")){
			olObject.addAll(tsc.getConfig().getLstProjects());
		}
		
		if(cbType.getSelectionModel().getSelectedItem().equals("Process")){
			olObject.addAll(tsc.getConfig().getLstProcesses());
		}
		cbObject.getSelectionModel().select(olObject.get(0));
	}
	
	private void initCBType(){
		cbType.setItems(olType);
		olType.add("Project");
		olType.add("Process");
		cbType.getSelectionModel().select(olType.get(0));
		
		//Listen for changes
		cbType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				
				//Update ChoiceBoxes accordingly
				initCBObject();
				initCBParameter();
			}
		});
	}

	public MainApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void setScenario(DBScenario scenario) {
		this.scenario = scenario;
		initCBObject();
		initCBParameter();
	}

	public TabScenarioCalculationController getTsc() {
		return tsc;
	}

	public void setTsc(TabScenarioCalculationController tsc) {
		this.tsc = tsc;
	}
	
	
	
	
	
}
