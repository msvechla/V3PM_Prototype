package com.v3pm_prototype.view.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.graphstream.util.set.Array;

import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import com.v3pm_prototype.analysis.CompleteRobustnessAnalysis;
import com.v3pm_prototype.calculation.Calculator;
import com.v3pm_prototype.calculation.ConstraintSet;
import com.v3pm_prototype.database.DBConstraint;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;
import com.v3pm_prototype.calculation.Process;

public class ScenarioComparisonController {
	@FXML
	private Label lblScenarioName1;
	@FXML
	private Label lblScenarioName2;
	
	@FXML
	private VBox roadmapBox1;
	
	@FXML
	private VBox roadmapBox2;
	
	@FXML
	private Label lblNPV1;
	@FXML
	private Label lblNPV2;
	
	@FXML
	private Label lblNPVRA1;
	@FXML
	private Label lblNPVRA2;
	
	@FXML
	private Label lblRobustness1;
	@FXML
	private Label lblRobustness2;
	
	@FXML
	private ProgressIndicator piRobustness1;
	@FXML
	private ProgressIndicator piRobustness2;
	
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
	
	@FXML
	private AreaChart<String, Double> sacCashflows;
	
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
	
	private void startCompleteRobustnessAnalysis(){
		CompleteRobustnessAnalysis cra1 = new CompleteRobustnessAnalysis(config1, rmList1){

			@Override
			protected void succeeded() {
				super.succeeded();
				initRoadmapContainer(roadmapBox1, rmList1,config1,this);
				DecimalFormat df = new DecimalFormat("#.#%");
				lblRobustness1.setText(df.format(this.getPercentage()));
				lblRobustness1.setVisible(true);
				lblRobustness1.setManaged(true);
				piRobustness1.setVisible(false);
				piRobustness1.setManaged(false);
				
				df = new DecimalFormat("#,###.00 €");
				lblNPVRA1.setText(df.format(rmList1.get(0).getNpv()*this.getPercentage()));
			}
			
		};
		
		CompleteRobustnessAnalysis cra2 = new CompleteRobustnessAnalysis(config2, rmList2){

			@Override
			protected void succeeded() {
				super.succeeded();
				initRoadmapContainer(roadmapBox2, rmList2,config2,this);
				DecimalFormat df = new DecimalFormat("#.#%");
				lblRobustness2.setText(df.format(this.getPercentage()));
				lblRobustness2.setVisible(true);
				lblRobustness2.setManaged(true);
				piRobustness2.setVisible(false);
				piRobustness2.setManaged(false);
				
				df = new DecimalFormat("#,###.00 €");
				lblNPVRA2.setText(df.format(rmList2.get(0).getNpv()*this.getPercentage()));
			}
			
		};

		Thread t1 = new Thread(cra1);
		t1.setDaemon(false);
		t1.start();
		
		Thread t2 = new Thread(cra2);
		t2.setDaemon(false);
		t2.start();
	}
	
	private void generateSolutionText(){
		String winScenarioName = "";
		String loseScenarioName = "";
		String solution = "";
		
		RoadMap winRM = null;
		RoadMap loseRM = null;
		
		if(rmList1.get(0).getNpv() > rmList2.get(0).getNpv()){
			winScenarioName = scenario1.getName();
			winRM = rmList1.get(0);
			loseScenarioName = scenario2.getName();
			loseRM = rmList2.get(0);
		}else if (rmList1.get(0).getNpv() < rmList2.get(0).getNpv()){
			winScenarioName = scenario2.getName();
			winRM = rmList2.get(0);
			loseScenarioName = scenario1.getName();
			loseRM = rmList1.get(0);
		}
		
		double NPVDiff = 0;
		DecimalFormat df = new DecimalFormat("#,###.00 €");
		if(winRM != null){
			NPVDiff = winRM.getNpv()-loseRM.getNpv();
		}
		
		if(winRM != null){
			solution = "Scenario: \"" +winScenarioName+"\" dominates Scenario: \""+loseScenarioName+"\" by "+df.format(NPVDiff);
		}else{
			solution = "Both Scenarios are equally good. ";
		}
		lblSolution.setText(solution);
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
				lblNPV1.setText(rmList1.get(0).getNPVString());
				lblNPV2.setText(rmList2.get(0).getNPVString());
				initBarChartsProcess();
				initBarChartRBroken();
				initRoadmapContainer(roadmapBox1, rmList1, config1, null);
				initRoadmapContainer(roadmapBox2, rmList2, config2, null);
				generateSolutionText();
				initSACCashflows();
				startCompleteRobustnessAnalysis();
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
	
	private void initBarChartsProcess(){
		bcQuality.getData().clear();
		bcTime.getData().clear();
		bcOOP.getData().clear();
		bcFixedCosts.getData().clear();
		
		Series<String, Number> series1Q = new XYChart.Series<String, Number>();
		series1Q.setName(scenario1.getName());
		Series<String, Number> series2Q = new XYChart.Series<String, Number>();
		series2Q.setName(scenario2.getName());
		Series<String, Number> series1T = new XYChart.Series<String, Number>();
		series1T.setName(scenario1.getName());
		Series<String, Number> series2T = new XYChart.Series<String, Number>();
		series2T.setName(scenario2.getName());
		Series<String, Number> series1OOP = new XYChart.Series<String, Number>();
		series1OOP.setName(scenario1.getName());
		Series<String, Number> series2OOP = new XYChart.Series<String, Number>();
		series2OOP.setName(scenario2.getName());
		Series<String, Number> series1FC = new XYChart.Series<String, Number>();
		series1FC.setName(scenario1.getName());
		Series<String, Number> series2FC = new XYChart.Series<String, Number>();
		series2FC.setName(scenario2.getName());
		
		bcQuality.getData().addAll(series1Q,series2Q);
		bcTime.getData().addAll(series1T,series2T);
		bcOOP.getData().addAll(series1OOP,series2OOP);
		bcFixedCosts.getData().addAll(series1FC,series2FC);
		
		for(Process p : rmList1.get(0).getLstProcessCalculated()){
			series1Q.getData().add(new XYChart.Data<String, Number>(p.getName().substring(0, 5),p.getQDelta()));
			series1T.getData().add(new XYChart.Data<String, Number>(p.getName().substring(0, 5),p.getTDelta()));
			series1OOP.getData().add(new XYChart.Data<String, Number>(p.getName().substring(0, 5),p.getOopDelta()));
			series1FC.getData().add(new XYChart.Data<String, Number>(p.getName().substring(0, 5),p.getFixedCostsDelta()));
		}
		
		for(Process p : rmList2.get(0).getLstProcessCalculated()){
			series2Q.getData().add(new XYChart.Data<String, Number>(p.getName().substring(0, 5),p.getQDelta()));
			series2T.getData().add(new XYChart.Data<String, Number>(p.getName().substring(0, 5),p.getTDelta()));
			series2OOP.getData().add(new XYChart.Data<String, Number>(p.getName().substring(0, 5),p.getOopDelta()));
			series2FC.getData().add(new XYChart.Data<String, Number>(p.getName().substring(0, 5),p.getFixedCostsDelta()));
		}
		
	}
	
	private void initBarChartRBroken(){
		bcBrokenRestrictions.getData().clear();
		
		//Add data to the chart for each restriction type
		List<String> lstTypeAlreadyCounted = new ArrayList<String>();
		
		Series<String, Number> series1 = new XYChart.Series<String, Number>();
		Series<String, Number> series2 = new XYChart.Series<String, Number>();
		
		for(DBConstraint dbConstraint : config1.getConstraintSet().getLstConstraints()){
			int countBrokenAll = 0;
			
			// Get all constraints of the same type
			if(!lstTypeAlreadyCounted.contains(dbConstraint.getType())){
				for(DBConstraint dbc : config1.getConstraintSet().getLstConstraints()){
					if(dbConstraint.getType().equals(dbc.getType())){
						countBrokenAll = countBrokenAll + dbc.getCountBroken();
					}
				}
			
				// Dont count constraints of this type again
				lstTypeAlreadyCounted.add(dbConstraint.getType());
				// Add a Bar of the type to the chart
				series1.getData().add(new XYChart.Data<String, Number>(dbConstraint.getType(),countBrokenAll));
			}	
		}
		
		lstTypeAlreadyCounted.clear();
		
		for(DBConstraint dbConstraint : config2.getConstraintSet().getLstConstraints()){
			int countBrokenAll = 0;
			
			// Get all constraints of the same type
			if(!lstTypeAlreadyCounted.contains(dbConstraint.getType())){
				for(DBConstraint dbc : config2.getConstraintSet().getLstConstraints()){
					if(dbConstraint.getType().equals(dbc.getType())){
						countBrokenAll = countBrokenAll + dbc.getCountBroken();
					}
				}
			
				// Dont count constraints of this type again
				lstTypeAlreadyCounted.add(dbConstraint.getType());
				// Add a Bar of the type to the chart
				series2.getData().add(new XYChart.Data<String, Number>(dbConstraint.getType(),countBrokenAll));
			}	
		}
		
		bcBrokenRestrictions.getData().addAll(series1,series2);
		
	}
	
	private void initSACCashflows(){
		XYChart.Series<String,Double> series1= new Series<String, Double>();
        series1.setName(scenario1.getName());
        
        XYChart.Series<String,Double> series2= new Series<String, Double>();
        series2.setName(scenario2.getName());
        
        
        for(int period = 0; period < config1.getPeriods(); period++){
        	series1.getData().add(new Data<String, Double>("Period "+(period+1), rmList1.get(0).getCashflowsPerPeriod(config1)[period]));
        	System.out.println("Series1: "+rmList1.get(0).getCashflowsPerPeriod(config1)[period]);
        }
        
        
        for(int period = 0; period < config2.getPeriods(); period++){
        	series2.getData().add(new Data<String, Double>("Period "+(period+1), rmList2.get(0).getCashflowsPerPeriod(config2)[period]));
        	System.out.println("Series2: "+rmList2.get(0).getCashflowsPerPeriod(config2)[period]);
        }
        
        sacCashflows.getData().addAll(series1,series2);   
	}
	
	private void initRoadmapContainer(VBox rmc, List<RoadMap>rmList, RunConfiguration config, CompleteRobustnessAnalysis cra) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class
				.getResource("/com/v3pm_prototype/view/RoadmapBox.fxml"));
		VBox root;
		try {
			root = (VBox) loader.load();
			RoadmapBoxController rmbController = loader.getController();
			rmbController.generate(rmList.get(0), config, cra);
			rmc.getChildren().clear();
			rmc.getChildren().add(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
		lblScenarioName1.setText(scenario1.getName());
		lblScenarioName2.setText(scenario2.getName());
		initialRMGeneration();
	}
	
	
	
	
}
