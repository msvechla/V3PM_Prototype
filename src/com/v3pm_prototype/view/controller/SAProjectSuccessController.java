package com.v3pm_prototype.view.controller;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import com.v3pm_prototype.analysis.SAProjectSuccess;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RoadMap;

public class SAProjectSuccessController extends AnalysisController{
	
	public static final String CLM_PARAMETER = "Parameter";
	public static final String CLM_SD = "StandardDeviation";
	
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
	private TableColumn<Map,String> clmSD;
	
	
	private RoadMap selectedRoadmap;
	
	public SAProjectSuccessController(){
		super();
	}
	
	@Override
	public void initialize() {
		super.initialize();
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
	
	private void initTVResults(SAProjectSuccess saps){
		olResults = saps.getMapStandardDeviation();
		clmParameter.setCellValueFactory(new MapValueFactory(CLM_PARAMETER));
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
		initCBProject();
	}
	
}
