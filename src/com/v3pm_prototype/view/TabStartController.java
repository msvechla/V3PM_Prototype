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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
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
	private TableColumn<DBProject, String> clmProjectsProject;
	@FXML
	private TableColumn<DBProject, Integer> clmProjectsPeriods;
	@FXML
	private TableColumn<DBProject, String> clmProjectsType;
	@FXML
	private TableColumn<DBProject, DBProcess> clmProjectsProcess;
	@FXML
	private TableColumn<DBProject, Float> clmProjectsFixCosts;
	@FXML
	private TableColumn<DBProject, Float> clmProjectsOInv;
	@FXML
	private TableColumn<DBProject, Float> clmProjectsA;
	@FXML
	private TableColumn<DBProject, Float> clmProjectsB;
	@FXML
	private TableColumn<DBProject, Float> clmProjectsE;
	@FXML
	private TableColumn<DBProject, Float> clmProjectsU;
	@FXML
	private TableColumn<DBProject, Float> clmProjectsM;
	
	@FXML
	private TableView<DBProcess> tvProcesses;
	@FXML
	private TableColumn<DBProcess, String> clmProcessesProcess;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesFixCosts;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesQ;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesT;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesP;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesOop;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesDGQ;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesDGT;
	@FXML
	private TableColumn<DBProcess, String> clmProcessesDFKT;
	
	public static ObservableList<DBProject> olProjects = FXCollections.observableArrayList();
	public static ObservableList<DBProcess> olProcesses = FXCollections.observableArrayList();
	
	public TabStartController() {
		
	}
	
	@FXML
	private void initialize(){
		
		initTVProjects();
		initTVProcesses();
		
		loadProjectsAndProcesses();
		
		
	}
	
	public void openAddProjectWindow(){
		// Load root layout from fxml file. 
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/com/v3pm_prototype/view/AddProject.fxml"));
        VBox root;
		try {
			root = (VBox) loader.load();
			
			AddProjectController controller = loader.getController();
	        controller.setTSC(this);
	        
			// Show the scene containing the root layout.
	        Stage stage = new Stage();
	        stage.setTitle("New Project");
	        stage.setScene(new Scene(root));
	        stage.show();
	        controller.updateType();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	public void openAddProcessWindow(){
		// Load root layout from fxml file. 
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/com/v3pm_prototype/view/AddProcess.fxml"));
        VBox root;
		try {
			root = (VBox) loader.load();
			
			AddProcessController controller = loader.getController();
	        controller.setTSC(this);
			
			// Show the scene containing the root layout.
	        Stage stage = new Stage();
	        stage.setTitle("New Process");
	        stage.setScene(new Scene(root));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	public void initTVProjects(){
		clmProjectsProject.setCellValueFactory(
	            new PropertyValueFactory<DBProject, String>("name"));
		clmProjectsPeriods.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Integer>("periods"));
		clmProjectsType.setCellValueFactory(
	            new PropertyValueFactory<DBProject, String>("type"));
		clmProjectsProcess.setCellValueFactory(
	            new PropertyValueFactory<DBProject, DBProcess>("process"));
		clmProjectsOInv.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("oInv"));
		clmProjectsFixCosts.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("fixedCosts"));
		clmProjectsA.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("a"));
		clmProjectsB.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("b"));
		clmProjectsE.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("e"));
		clmProjectsU.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("u"));
		clmProjectsM.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("m"));
		
		tvProjects.setItems(this.olProjects);
		
		final ContextMenu projectsContextMenu = new ContextMenu();
		MenuItem delete = new MenuItem("Delete");
		projectsContextMenu.getItems().add(delete);
		
		tvProjects.setContextMenu(projectsContextMenu);
		
		delete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBProject project = tvProjects.getSelectionModel().getSelectedItem();
				olProjects.remove(project);
				
				//Delete from database
				try {
					Connection conn = DBConnection.getInstance().getConnection();
					Statement st = conn.createStatement();
					st.executeUpdate("DELETE FROM Project WHERE id = "+project.getId()+";");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		});
		
		
	}
	
	public void initTVProcesses(){
		clmProcessesProcess.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, String>("name"));
		clmProcessesP.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("p"));
		clmProcessesOop.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("oop"));
		clmProcessesFixCosts.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("fixedCosts"));
		clmProcessesQ.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("q"));
		clmProcessesDGQ.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("degQ"));
		clmProcessesT.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("t"));
		clmProcessesDGT.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("degT"));
		clmProcessesDFKT.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, String>("demandFunction"));
		tvProcesses.setItems(this.olProcesses);
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
					if(processID == DBProcess.ID_ALLPROCESSES){
						olProjects.add(new DBProject(rs.getInt("id"),rs.getString("name"),rs.getString("type"), rs.getInt("periods"),new DBProcess(DBProcess.ID_ALLPROCESSES, DBProcess.NAME_ALLPROCESSES, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", ""),rs.getFloat("fixedCosts"),rs.getFloat("oInv"),rs.getFloat("a"),rs.getFloat("b"),rs.getFloat("e"),rs.getFloat("u"),rs.getFloat("m")));
					}else{
						for(DBProcess p : olProcesses){
							if(p.getId() == processID){
								olProjects.add(new DBProject(rs.getInt("id"),rs.getString("name"),rs.getString("type"), rs.getInt("periods"),p,rs.getFloat("fixedCosts"),rs.getFloat("oInv"),rs.getFloat("a"),rs.getFloat("b"),rs.getFloat("e"),rs.getFloat("u"),rs.getFloat("m")));
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
					String dmFktQ = null;
					switch (rs.getInt("dmFktQ")){
						case 0:
							dmFktQ = "0q";
							break;
						case 1:
							dmFktQ = "q";
							break;
						case 2:
							dmFktQ = "ln q";
							break;
						case 3:
							dmFktQ = "e^(1/q)";
							break;
					}
					
					String dmFktT = null;
					switch (rs.getInt("dmFktT")){
						case 0:
							dmFktT = "0t";
							break;
						case 1:
							dmFktT = "t";
							break;
						case 2:
							dmFktT = "ln t";
							break;
						case 3:
							dmFktT = "e^(1/t)";
							break;
					}	

					olProcesses.add(new DBProcess(rs.getInt("id"), rs
							.getString("name"), rs.getFloat("p"), rs
							.getFloat("oop"), rs.getFloat("fixedCosts"), rs
							.getFloat("q"), rs.getFloat("degQ"), rs
							.getFloat("t"), rs.getFloat("degT"), rs
							.getFloat("dmP"), rs.getFloat("dmLambda"), rs
							.getFloat("dmAlpha"), rs.getFloat("dmBeta"),
							dmFktQ, dmFktT));
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
