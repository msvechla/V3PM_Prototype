package com.v3pm_prototype.view.controller;

import java.lang.reflect.Field;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import org.controlsfx.control.Notifications;

import com.v3pm_prototype.analysis.SAProjectSuccess;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.tools.TableViewSnapshot;

public class SAProjectSuccessController extends AnalysisController{
	
	public static final String CLM_PARAMETER = "Parameter";
	public static final String CLM_INITIALVALUE = "InitialValue";
	public static final String CLM_SD = "StandardDeviation";
	
	@FXML
	private Label lblRoadmap;
	
	@FXML
	private ChoiceBox<Project> cbProject;
	private ObservableList<Project> olProject = FXCollections.observableArrayList();
	
	@FXML
	private LineChart<Number, Number> lineChart;
	
	@FXML
	private TableView tvResults;
	private ObservableList<Map> olResults = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<Map,String> clmParameter;
	@FXML
	private TableColumn<Map,String> clmInitialValue;
	@FXML
	private TableColumn<Map,String> clmSD;
	
	private Node snapshotNode;
	
	private RoadMap selectedRoadmap;
	
	public SAProjectSuccessController(){
		super();
	}
	
	@Override
	public void initialize() {
		super.initialize();
		setupSnapshots();
	}
	
	public void startSAProjectSuccess(){
		Task<SAProjectSuccess> sapsTask = new Task<SAProjectSuccess>() {

			@Override
			protected SAProjectSuccess call() throws Exception {
				SAProjectSuccess saps = new SAProjectSuccess(tsc.getConfig(), cbModus.getSelectionModel().getSelectedItem(), cbAbsRel.getSelectionModel().getSelectedItem(), Double.valueOf(tfRadius.getText()), Double.valueOf(tfStep.getText()), piSolution, cbProject.getSelectionModel().getSelectedItem(), selectedRoadmap);
				saps.start();
				return saps;
			}

			@Override
			protected void succeeded() {
				initLineChart(this.getValue());
				initTVResults(this.getValue());
				super.succeeded();
			}
			
		};
		
		Thread t = new Thread(sapsTask);
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
		
		MenuItem item = new MenuItem("Copy to Clipboard");
		item.setOnAction(new TableViewSnapshot(tvResults));
		ContextMenu menu = new ContextMenu(item);
		tvResults.setContextMenu(menu);
		
	}
	
	private void initTVResults(SAProjectSuccess saps){
		olResults = saps.getMapStandardDeviation();
		clmParameter.setCellValueFactory(new MapValueFactory(CLM_PARAMETER));
		clmInitialValue.setCellValueFactory(new MapValueFactory(CLM_INITIALVALUE));
		clmSD.setCellValueFactory(new MapValueFactory(CLM_SD));
		
		
		
		tvResults.setItems(olResults);	
		
	}
	
	private void initLineChart(SAProjectSuccess saps){
		lineChart.getData().clear();
		int index = 0;
		for(Field field : saps.getLstFields()){
			//Create a series for each field
			Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(field.getName());
			

			for(RoadMap rm : saps.getHmResults().get(field.getName())){
				series.getData().add((new XYChart.Data<Number, Number>(saps.getLstSteps().get(index),rm.getNpv())));
				index++;
			}
			
			lineChart.getData().add(series);
		}
	}

	private void initCBProject() {
		cbProject.setItems(olProject);
		for(Project p : this.tsc.getConfig().getLstProjects()){
			for(Project p2 : selectedRoadmap.getLstProjectCalculated()){
				if(p.getId() == p2.getId()){
					olProject.add(p);
				}
			}
		}
		cbProject.getSelectionModel().select(olProject.get(0));
	}

	@Override
	public MainApp getMainApp() {
		return super.getMainApp();
	}



	@Override
	public void setMainApp(MainApp mainApp) {
		// TODO Auto-generated method stub
		super.setMainApp(mainApp);
	}



	@Override
	public TabScenarioCalculationController getTsc() {
		// TODO Auto-generated method stub
		return super.getTsc();
	}



	@Override
	public void setTsc(TabScenarioCalculationController tsc) {
		// TODO Auto-generated method stub
		super.setTsc(tsc);
	}
	
	public void setRoadmap(RoadMap roadmap){
		this.selectedRoadmap = roadmap;
		this.lblRoadmap.setText(selectedRoadmap.getDisplayText());
		initCBProject();
	}
	
}
