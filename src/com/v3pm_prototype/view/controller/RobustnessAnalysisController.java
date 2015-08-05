package com.v3pm_prototype.view.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import org.controlsfx.control.Notifications;

import com.v3pm_prototype.analysis.RobustnessAnalysis;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.tools.TableViewSnapshot;

public class RobustnessAnalysisController extends AnalysisController{
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
	private Label lblSolution;
	
	@FXML
	private Label lblScenarioName;
	
	@FXML
	private LineChart<Number, Number> lineChart;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	
	private DBScenario scenario;
	private Node snapshotNode;
	
	public RobustnessAnalysisController(){
		
	}
	
	@Override
	public void initialize() {
		initCBType();
		setupSnapshots();
		super.initialize();
	}



	public void startRobustnessAnalysis(){
		lblSolution.setText("");
		Task<RobustnessAnalysis> raTask = new Task<RobustnessAnalysis>() {

			@Override
			protected RobustnessAnalysis call() throws Exception {
				
				Object obj;
				
				if(cbType.getSelectionModel().getSelectedItem().equals("General")){
					obj = null;
				}else{
					obj = cbObject.getValue();
				}
				
				RobustnessAnalysis ra = new RobustnessAnalysis(tsc.getRmList(),
						tsc.getConfig(), cbModus.getValue(), obj
						, cbParameter.getValue(),
						Double.valueOf(tfRadius.getText()),
						Double.valueOf(tfStep.getText()),cbAbsRel.getValue(), piSolution);
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
		
		lineChart.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		
	}
	
	private void initLineChart(RobustnessAnalysis robustnessAnalysis){
		lineChart.getData().clear();

			xAxis.setLabel(robustnessAnalysis.getParameter());
		
			// Create a series for the best Roadmap and for the old best Roadmap
			Series<Number, Number> seriesOld = new XYChart.Series<Number, Number>();
			seriesOld.setName(tsc.getRmList().get(0).toString());

			Series<Number, Number> seriesNew = new XYChart.Series<Number, Number>();
			seriesNew.setName("Best Roadmap");

			// Add data to each series for each step of the parameter modification
			for (int i = 0; i < robustnessAnalysis.getLstResults().size(); i++) {
				if(i<robustnessAnalysis.getLstDoubleOldRoadmap().size()){
					seriesOld.getData().add(
							new XYChart.Data<Number, Number>(robustnessAnalysis.getLstDoubleOldRoadmap().get(i),robustnessAnalysis.getLstResultsOldRoadmap().get(i).getNpv()));
				}
				
				seriesNew.getData().add(
						new XYChart.Data<Number, Number>(robustnessAnalysis.getLstDouble().get(i),robustnessAnalysis.getLstResults().get(i).getNpv()));

				
			}
			
			lineChart.getData().add(seriesNew);
			lineChart.getData().add(seriesOld);
			lblSolution.setText(robustnessAnalysis.getSolutionText());

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
		
		if(cbType.getSelectionModel().getSelectedItem().equals("General")){
			olParameter.add("discountRate");
			olParameter.add("oOAFixed");
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
		if(olObject.size() > 0){
			cbObject.getSelectionModel().select(olObject.get(0));	
		}
	
	}
	
	private void initCBType(){
		cbType.setItems(olType);
		olType.add("Project");
		olType.add("Process");
		olType.add("General");
		cbType.getSelectionModel().select(olType.get(0));
		
		//Listen for changes
		cbType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				//Update ChoiceBoxes accordingly
				initCBObject();
				initCBParameter();				
			}
		});
	}

	public void setScenario(DBScenario scenario) {
		this.scenario = scenario;
		lblScenarioName.setText(this.scenario.getName());
		initCBObject();
		initCBParameter();
	}
	
}
