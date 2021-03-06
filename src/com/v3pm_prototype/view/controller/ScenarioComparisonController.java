package com.v3pm_prototype.view.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.Notifications;
import org.graphstream.util.set.Array;

import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import com.v3pm_prototype.analysis.CompleteRobustnessAnalysis;
import com.v3pm_prototype.calculation.NPVCalculator;
import com.v3pm_prototype.calculation.ConstraintSet;
import com.v3pm_prototype.database.DBConstraint;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.V3PM_Prototype;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;
import com.v3pm_prototype.tools.Colorpalette;
import com.v3pm_prototype.tools.TableViewSnapshot;
import com.v3pm_prototype.calculation.Process;

/**
 * 
 * @author Marius Svechla
 *
 */
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
	
	private V3PM_Prototype mainApp;
	private Tab tab;
	private DBScenario scenario1;
	private DBScenario scenario2;
	private RunConfiguration config1;
	private RunConfiguration config2;
	private List<RoadMap> rmList1 = new ArrayList<RoadMap>();
	private List<RoadMap> rmList2 = new ArrayList<RoadMap>();
	
	private javafx.scene.Node snapshotNode;
	
	public ScenarioComparisonController(){
		
	}
	
	@FXML
	public void initialize(){
		setupSnapshots();
	}
	
	private void startCompleteRobustnessAnalysis(){
		lblRobustness1.setManaged(false);
		lblRobustness2.setManaged(false);
		
		CompleteRobustnessAnalysis cra1 = new CompleteRobustnessAnalysis(config1, rmList1){

			@Override
			protected void succeeded() {
				super.succeeded();
				initRoadmapContainer(roadmapBox1, rmList1,config1,this);
				DecimalFormat df = new DecimalFormat("#.#%");
				piRobustness1.setVisible(false);
				piRobustness1.setManaged(false);
				lblRobustness1.setText(df.format(this.getPercentage())+"\nRobustness");
				lblRobustness1.setVisible(true);
				lblRobustness1.setManaged(true);
			}
			
		};
		
		CompleteRobustnessAnalysis cra2 = new CompleteRobustnessAnalysis(config2, rmList2){

			@Override
			protected void succeeded() {
				super.succeeded();
				initRoadmapContainer(roadmapBox2, rmList2,config2,this);
				DecimalFormat df = new DecimalFormat("#.#%");
				piRobustness2.setVisible(false);
				piRobustness2.setManaged(false);
				lblRobustness2.setText(df.format(this.getPercentage())+"\nRobustness");
				lblRobustness2.setVisible(true);
				lblRobustness2.setManaged(true);
			}
			
		};

		Thread t1 = new Thread(cra1);
		t1.setDaemon(true);
		t1.start();
		
		Thread t2 = new Thread(cra2);
		t2.setDaemon(true);
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
			roadmapBox1.setStyle(Colorpalette.COMPARE_SCENARIO_WINNER);
			loseScenarioName = scenario2.getName();
			loseRM = rmList2.get(0);
		}else if (rmList1.get(0).getNpv() < rmList2.get(0).getNpv()){
			winScenarioName = scenario2.getName();
			roadmapBox2.setStyle(Colorpalette.COMPARE_SCENARIO_WINNER);
			winRM = rmList2.get(0);
			loseScenarioName = scenario1.getName();
			loseRM = rmList1.get(0);
		}
		
		double NPVDiff = 0;
		DecimalFormat df = new DecimalFormat("#,###.00 �");
		if(winRM != null){
			NPVDiff = winRM.getNpv()-loseRM.getNpv();
		}
		
		if (winRM != null) {
			solution = winScenarioName
					+ " dominates "
					+ loseScenarioName
					+ " by "
					+ df.format(NPVDiff)
					+ ". \n"+winScenarioName+" should be chosen.";
		}else{
			solution = "Both Scenarios are equally good. ";
		}
		lblSolution.setText(solution);
		lblSolution.autosize();
		
	}
	
	private void initialRMCalculation(){
		Task calcTask = new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				NPVCalculator calc1 = new NPVCalculator(rmList1, config1);
				NPVCalculator calc2 = new NPVCalculator(rmList2, config2);
				
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
				initSACCashflows();
				
				if ((SettingsController.FORCE_CRA)
						|| (rmList1.size()<50000 && rmList2.size()<50000)) {
					startCompleteRobustnessAnalysis();
				}else{
					lblRobustness1.setText("Go into Settings to force this feature on.");
					lblRobustness2.setText("Go into Settings to force this feature on.");
					piRobustness1.setProgress(0);
					piRobustness2.setProgress(0);
				}
				
				generateSolutionText();
				super.succeeded();
			}
			
			
		};
		
		Thread t = new Thread(calcTask);
		t.setDaemon(true);
		t.start();
	}
	
	private void initialRMGeneration(){
		final RMGenerator rmGen1 = new RMGenerator(config1);
		final RMGenerator rmGen2 = new RMGenerator(config2);
		
		Thread t1 = new Thread(rmGen1);
		t1.setDaemon(true);
		
		final Thread t2 = new Thread(rmGen2);
		t2.setDaemon(true);
		
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
			series1Q.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getQDelta()));
			series1T.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getTDelta()));
			series1OOP.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getOopDelta()));
			series1FC.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getFixedCostsDelta()));
		}
		
		for(Process p : rmList2.get(0).getLstProcessCalculated()){
			series2Q.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getQDelta()));
			series2T.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getTDelta()));
			series2OOP.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getOopDelta()));
			series2FC.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getFixedCostsDelta()));
		}
		
	}
	
	/**
	 * Adds a snapshot feature to the charts
	 */
	private void setupSnapshots() {
		final ContextMenu snapshotCM = new ContextMenu();
		MenuItem miSnapshot = new MenuItem("Copy to Clipboard");
		snapshotCM.getItems().add(miSnapshot);

		miSnapshot.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				WritableImage snapshot = new WritableImage((int)snapshotNode.getBoundsInLocal().getWidth(), (int)snapshotNode.getBoundsInLocal().getHeight());
				snapshotNode.snapshot(new SnapshotParameters(), snapshot);
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putImage(snapshot); 
				clipboard.clear();
				clipboard.setContent(content);
				
				Notifications.create()
	              .title("Snapshot Taken")
	              .text("A snapshot of the component has been taken and is available in your clipboard.")
	              .showInformation();
				
			}
		});
		
		EventHandler<MouseEvent> eventHandlerSnapshot = (new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				 if (MouseButton.SECONDARY.equals(event.getButton())) {
					 snapshotNode = (javafx.scene.Node) event.getSource();
				      snapshotCM.show(mainApp.getPrimaryStage(), event.getScreenX(), event.getScreenY());
				    }  
			}
		});
		
		bcBrokenRestrictions.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		bcFixedCosts.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		bcOOP.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		bcQuality.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		bcTime.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		sacCashflows.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		roadmapBox1.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		roadmapBox2.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		
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
        	series1.getData().add(new Data<String, Double>("Period "+(period), rmList1.get(0).getCashflowsPerPeriod(config1)[period]));
        	System.out.println("Series1: "+rmList1.get(0).getCashflowsPerPeriod(config1)[period]);
        }
        
        
        for(int period = 0; period < config2.getPeriods(); period++){
        	series2.getData().add(new Data<String, Double>("Period "+(period), rmList2.get(0).getCashflowsPerPeriod(config2)[period]));
        	System.out.println("Series2: "+rmList2.get(0).getCashflowsPerPeriod(config2)[period]);
        }
        
        sacCashflows.getData().addAll(series1,series2);   
	}
	
	private void initRoadmapContainer(VBox rmc, List<RoadMap>rmList, RunConfiguration config, CompleteRobustnessAnalysis cra) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(V3PM_Prototype.class
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

	public void setMainApp(V3PM_Prototype mainApp) {
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
