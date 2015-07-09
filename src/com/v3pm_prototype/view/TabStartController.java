package com.v3pm_prototype.view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class TabStartController implements EventHandler<ActionEvent>{

	private MainApp mainApp;
	
	@FXML
	private Button btnNewScenario;
	@FXML
	private Button btnNewProject;
	@FXML
	private Button btnNewProcess;
	
	@FXML
	private TableView tvScenarios;
	
	@FXML
	private TableView tvProjects;
	@FXML
	private TableColumn clmProjectProject;
	@FXML
	private TableColumn clmProjectType;
	@FXML
	private TableColumn clmProjectPeriods;
	
	@FXML
	private TableView tvProcesses;
	@FXML
	private TableColumn clmProcessProcess;
	
	private static ObservableList<DBProject> olProject = FXCollections.observableArrayList();
	private static ObservableList<DBProcess> olProcess = FXCollections.observableArrayList();
	
	public TabStartController() {
		
	}
	
	@FXML
	private void initialize(){
		
		//Setup the Project TableView
		clmProjectProject.setCellValueFactory(new PropertyValueFactory<DBProject, String>("name"));
		clmProjectType.setCellValueFactory(new PropertyValueFactory<DBProject, String>("type"));
		clmProjectPeriods.setCellValueFactory(new PropertyValueFactory<DBProject, Integer>("periods"));
		tvProjects.setItems(olProject);
		
		//Setup the Process TableView
		clmProcessProcess.setCellValueFactory(new PropertyValueFactory<DBProcess, String>("name"));
		tvProcesses.setItems(olProcess);
		
		loadProjects();
		loadProcesses();
		
	}
	
	/**
	 * Starts a task that loads all projects from the database
	 */
	private void loadProjects() {
		Task loadProjectTask = new Task(){
			@Override
			protected Object call() throws Exception {
				Connection conn = DBConnection.getInstance().getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM Project");
				
				while(rs.next()){
					olProject.add(new DBProject(rs.getInt("id"),rs.getString("name"),rs.getString("type"), rs.getInt("periods")));
				}
				return null;
			}	
		};
		
		Thread t = new Thread(loadProjectTask);
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * Starts a task that loads all projects from the database
	 */
	private void loadProcesses() {
		Task loadProcessesTask = new Task(){
			@Override
			protected Object call() throws Exception {
				Connection conn = DBConnection.getInstance().getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM Process");
				
				while(rs.next()){
					olProcess.add(new DBProcess(rs.getInt("id"),rs.getString("name")));
				}
				return null;
			}	
		};
		
		Thread t = new Thread(loadProcessesTask);
		t.setDaemon(true);
		t.start();
	}

	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
	

	public void myAction(){
		System.out.println("myAction");
	}
	
	@Override
	public void handle(ActionEvent event) {
		
		if(event.getSource() instanceof Button){
			Button button = (Button) event.getSource();
			
			//:::::::::: START BUTTON BEHAVIOUR ::::::::::
//			if(button == btnStartCalc){
//				//Update Statusbar
//				this.mainApp.getV3pmGUIController().setProgress(-1);
//				this.mainApp.getV3pmGUIController().setStatus("Generating Roadmaps...");
//				
//				//Create a Service that executes the RMGenerator Task and start it
//				Service<?> service = new Service(){
//					@Override
//					protected Task<List<RoadMap>> createTask() {
//						return new RMGenerator(RunConfiguration.standardConfig);
//					}
//				};
//				
//				//When the task is succeeded update Statusbar
//				service.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
//					@Override
//					public void handle(WorkerStateEvent event) {
//						System.out.println("SUCCEEDED");
//						MainApp.instance.getV3pmGUIController().setProgress(0);
//						MainApp.instance.getV3pmGUIController().setStatus("Roadmaps generated.");
//					}
//				});
//				service.start();
//				
//			}
		}
		
	}
	
}
