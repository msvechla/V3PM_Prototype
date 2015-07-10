package com.v3pm_prototype.view;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
	private TableView<?> tvScenarios;
	
	@FXML
	private TableView<DBProject> tvProjects;
	@FXML
	private TableColumn<DBProject, String> clmProjectProject;
	@FXML
	private TableColumn<DBProject, String> clmProjectType;
	@FXML
	private TableColumn<DBProject, Integer> clmProjectPeriods;
	@FXML
	private TableColumn<DBProject, DBProcess> clmProjectProcess;
	
	@FXML
	private TableView<DBProcess> tvProcesses;
	@FXML
	private TableColumn<DBProcess, String> clmProcessProcess;
	
	public static ObservableList<DBProject> olProject = FXCollections.observableArrayList();
	public static ObservableList<DBProcess> olProcess = FXCollections.observableArrayList();
	
	public TabStartController() {
		
	}
	
	@FXML
	private void initialize(){
		
		//Setup the Project TableView
		clmProjectProject.setCellValueFactory(new PropertyValueFactory<DBProject, String>("name"));
		clmProjectType.setCellValueFactory(new PropertyValueFactory<DBProject, String>("type"));
		clmProjectPeriods.setCellValueFactory(new PropertyValueFactory<DBProject, Integer>("periods"));
		clmProjectProcess.setCellValueFactory(new PropertyValueFactory<DBProject, DBProcess>("process"));
		tvProjects.setItems(olProject);
		
		//Setup the Process TableView
		clmProcessProcess.setCellValueFactory(new PropertyValueFactory<DBProcess, String>("name"));
		tvProcesses.setItems(olProcess);
		
		loadProjectsAndProcesses();
		
		
	}
	
	/**
	 * Starts a task that loads all projects from the database
	 */
	private void loadProjects() {
		Task<?> loadProjectTask = new Task<Object>(){
			@Override
			protected Object call() throws Exception {
				Connection conn = DBConnection.getInstance().getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM Project");
				
				while(rs.next()){
					//Lookup the affected process
					int processID = rs.getInt("processID");
					if(processID == DBProcess.ID_EMPTYPROCESS){
						olProject.add(new DBProject(rs.getInt("id"),rs.getString("name"),rs.getString("type"), rs.getInt("periods"),new DBProcess(DBProcess.ID_EMPTYPROCESS, DBProcess.NAME_EMPTYPROCESS)));
					}else{
						for(DBProcess p : olProcess){
							if(p.getId() == processID){
								olProject.add(new DBProject(rs.getInt("id"),rs.getString("name"),rs.getString("type"), rs.getInt("periods"),p));
								break;
							}
						}
					}
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
	private void loadProjectsAndProcesses() {
		//First load all Processes
		Task<?> loadProcessesTask = new Task<Object>(){
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

			@Override
			protected void succeeded() {
				super.succeeded();
				//When loading processes finished, load projects
				loadProjects();
			}	
			
			
			
		};
		
		Thread t = new Thread(loadProcessesTask);
		t.setDaemon(true);
		t.start();
	}
	
	public void openNewProjectWindow(){
		// Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/com/v3pm_prototype/view/NewProject.fxml"));
        AnchorPane root;
		try {
			root = (AnchorPane) loader.load();
			
			// Show the scene containing the root layout.
	        Stage stage = new Stage();
	        stage.setTitle("New Project");
	        stage.setScene(new Scene(root));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	public void openNewProcessWindow(){
		// Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/com/v3pm_prototype/view/NewProcess.fxml"));
        AnchorPane root;
		try {
			root = (AnchorPane) loader.load();
			
			// Show the scene containing the root layout.
	        Stage stage = new Stage();
	        stage.setTitle("New Process");
	        stage.setScene(new Scene(root));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	public void openNewScenarioWindow(){
		// Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/com/v3pm_prototype/view/NewScenario.fxml"));
        AnchorPane root;
		try {
			root = (AnchorPane) loader.load();
			
			// Show the scene containing the root layout.
	        Stage stage = new Stage();
	        stage.setTitle("New Scenario");
	        stage.setScene(new Scene(root));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}    
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
