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
import com.v3pm_prototype.calculation.Calculator;
import com.v3pm_prototype.calculation.ConstraintSet;
import com.v3pm_prototype.database.DBConstraint;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;
import com.v3pm_prototype.tools.Colorpalette;
import com.v3pm_prototype.tools.TableViewSnapshot;
import com.v3pm_prototype.calculation.Process;

public class RoadmapComparisonController {
	@FXML
	private Label lblRoadmapName1;
	@FXML
	private Label lblRoadmapName2;
	
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
	private AreaChart<String, Double> sacCashflows;
	
	private MainApp mainApp;
	private Tab tab;
	private RoadMap roadmap1;
	private RoadMap roadmap2;
	private RunConfiguration config;
	
	private javafx.scene.Node snapshotNode;
	
	public RoadmapComparisonController(){
		
	}
	
	@FXML
	public void initialize(){
		setupSnapshots();
	}
	
	
	private void generateSolutionText(){
		RoadMap winRM = null;
		RoadMap loseRM = null;
		String solution = "";
		
		if(roadmap1.getNpv() > roadmap2.getNpv()){
			winRM = roadmap1;
			roadmapBox1.setStyle(Colorpalette.COMPARE_SCENARIO_WINNER);
			loseRM = roadmap2;
		}else if (roadmap1.getNpv() < roadmap2.getNpv()){
			winRM = roadmap2;
			roadmapBox2.setStyle(Colorpalette.COMPARE_SCENARIO_WINNER);
			loseRM =roadmap1;
		}
		
		double NPVDiff = 0;
		DecimalFormat df = new DecimalFormat("#,###.00 €");
		if(winRM != null){
			NPVDiff = winRM.getNpv()-loseRM.getNpv();
		}
		
		if (winRM != null) {
			solution = winRM.getDisplayText()
					+ " dominates "
					+ loseRM.getDisplayText()
					+ " by "
					+ df.format(NPVDiff)
					+ ". \n"+winRM.getDisplayText()+" should be chosen.";
		}else{
			solution = "Both Scenarios are equally good. ";
		}
		lblSolution.setText(solution);
		lblSolution.autosize();
		
	}
	
	private void initBarChartsProcess(){
		bcQuality.getData().clear();
		bcTime.getData().clear();
		bcOOP.getData().clear();
		bcFixedCosts.getData().clear();
		
		Series<String, Number> series1Q = new XYChart.Series<String, Number>();
		series1Q.setName(roadmap1.getDisplayText());
		Series<String, Number> series2Q = new XYChart.Series<String, Number>();
		series2Q.setName(roadmap2.getDisplayText());
		Series<String, Number> series1T = new XYChart.Series<String, Number>();
		series1T.setName(roadmap1.getDisplayText());
		Series<String, Number> series2T = new XYChart.Series<String, Number>();
		series2T.setName(roadmap2.getDisplayText());
		Series<String, Number> series1OOP = new XYChart.Series<String, Number>();
		series1OOP.setName(roadmap1.getDisplayText());
		Series<String, Number> series2OOP = new XYChart.Series<String, Number>();
		series2OOP.setName(roadmap2.getDisplayText());
		Series<String, Number> series1FC = new XYChart.Series<String, Number>();
		series1FC.setName(roadmap1.getDisplayText());
		Series<String, Number> series2FC = new XYChart.Series<String, Number>();
		series2FC.setName(roadmap2.getDisplayText());
		
		bcQuality.getData().addAll(series1Q,series2Q);
		bcTime.getData().addAll(series1T,series2T);
		bcOOP.getData().addAll(series1OOP,series2OOP);
		bcFixedCosts.getData().addAll(series1FC,series2FC);
		
		for(Process p : roadmap1.getLstProcessCalculated()){
			series1Q.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getQDelta()));
			series1T.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getTDelta()));
			series1OOP.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getOopDelta()));
			series1FC.getData().add(new XYChart.Data<String, Number>(p.getShortName(),p.getFixedCostsDelta()));
		}
		
		for(Process p : roadmap2.getLstProcessCalculated()){
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

		bcFixedCosts.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		bcOOP.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		bcQuality.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		bcTime.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		sacCashflows.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		roadmapBox1.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		roadmapBox2.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		 
	}
	
	private void initSACCashflows(){
		XYChart.Series<String,Double> series1= new Series<String, Double>();
        series1.setName(roadmap1.getDisplayText());
        
        XYChart.Series<String,Double> series2= new Series<String, Double>();
        series2.setName(roadmap2.getDisplayText());
        
        
        for(int period = 0; period < config.getPeriods(); period++){
        	series1.getData().add(new Data<String, Double>("Period "+(period), roadmap1.getCashflowsPerPeriod(config)[period]));
        	series2.getData().add(new Data<String, Double>("Period "+(period), roadmap2.getCashflowsPerPeriod(config)[period]));
        }
        
        sacCashflows.getData().addAll(series1,series2);   
	}
	
	private void initRoadmapContainer(VBox rmc, RoadMap roadmap, RunConfiguration config, CompleteRobustnessAnalysis cra) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class
				.getResource("/com/v3pm_prototype/view/RoadmapBox.fxml"));
		VBox root;
		try {
			root = (VBox) loader.load();
			RoadmapBoxController rmbController = loader.getController();
			rmbController.generate(roadmap, config, cra);
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

	public void setRoadmaps(RunConfiguration config, RoadMap roadmap1, RoadMap roadmap2) {
		this.roadmap1 = roadmap1;
		this.roadmap2 = roadmap2;
		this.config = config;
		lblRoadmapName1.setText(roadmap1.getDisplayText());
		lblRoadmapName2.setText(roadmap2.getDisplayText());
		
		initBarChartsProcess();
		initSACCashflows();
		initRoadmapContainer(roadmapBox1, roadmap1, config, null);
		initRoadmapContainer(roadmapBox2, roadmap2, config, null);
		generateSolutionText();
	}
	
	
}
