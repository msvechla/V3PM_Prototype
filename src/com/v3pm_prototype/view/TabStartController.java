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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import com.v3pm_prototype.calculation.ConstraintSet;
import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBConstraint;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBScenario;
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
	private Button btnCalculateScenario;
	
	@FXML
	private TableView<DBScenario> tvScenarios;
	@FXML
	private TableColumn<DBScenario, String> clmScenariosName;
	@FXML
	private TableColumn<DBScenario, Integer> clmScenariosPeriods;
	@FXML
	private TableColumn<DBScenario, Integer> clmScenariosSlots;
	@FXML
	private TableColumn<DBScenario, Float> clmScenarioDR;
	@FXML
	private TableColumn<DBScenario, Float> clmScenarioOAF;
	
	public ObservableList<DBScenario> olScenarios = FXCollections.observableArrayList();
	
	
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
		initTVScenarios();
		
		loadFromDatabase();	
	}
	
	public void calculateScenario(){
		if(tvScenarios.getSelectionModel().getSelectedItem() != null){
			DBScenario selectedScenario = tvScenarios.getSelectionModel().getSelectedItem();
			
			// Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("/com/v3pm_prototype/view/TabScenarioCalculation.fxml"));
	        VBox root;
			try {
				root = (VBox) loader.load();
				TabScenarioCalculationController scController = loader.getController();
				scController.setMainApp(this.mainApp);
				
				Tab tabSC = new Tab(selectedScenario.getName());
				tabSC.setContent(root);
				tabSC.setClosable(true);
				mainApp.getV3pmGUIController().getTpMain().getSelectionModel().select(tabSC);
				
				mainApp.getV3pmGUIController().getTpMain().getTabs().add(tabSC);
				scController.setScenario(selectedScenario);
				
			} catch (IOException e) {
				e.printStackTrace();
			}    
			
			
		}
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
	
	private void initTVScenarios(){
		clmScenariosName.setCellValueFactory(
	            new PropertyValueFactory<DBScenario, String>("name"));
		clmScenariosPeriods.setCellValueFactory(
	            new PropertyValueFactory<DBScenario, Integer>("periods"));
		clmScenariosSlots.setCellValueFactory(
	            new PropertyValueFactory<DBScenario, Integer>("slotsPerPeriod"));
		clmScenarioDR.setCellValueFactory(
	            new PropertyValueFactory<DBScenario, Float>("discountRate"));
		clmScenarioOAF.setCellValueFactory(
	            new PropertyValueFactory<DBScenario, Float>("oOAFixed"));
		
		tvScenarios.setItems(olScenarios);
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
	 * Calls LoadScenarios()
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
						olProjects.add(new DBProject(rs.getInt("id"),rs.getString("name"),rs.getString("type"), rs.getInt("periods"),rs.getFloat("fixedCosts"),rs.getFloat("oInv"),new DBProcess(DBProcess.ID_ALLPROCESSES, DBProcess.NAME_ALLPROCESSES, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", ""),rs.getFloat("a"),rs.getFloat("b"),rs.getFloat("e"),rs.getFloat("u"),rs.getFloat("m"),rs.getString("absrelQ"),rs.getString("absrelT"),rs.getString("absrelOop")));
					}else{
						for(DBProcess p : olProcesses){
							if(p.getId() == processID){
								olProjects.add(new DBProject(rs.getInt("id"),rs.getString("name"),rs.getString("type"), rs.getInt("periods"),rs.getFloat("fixedCosts"),rs.getFloat("oInv"),p,rs.getFloat("a"),rs.getFloat("b"),rs.getFloat("e"),rs.getFloat("u"),rs.getFloat("m"),rs.getString("absrelQ"),rs.getString("absrelT"),rs.getString("absrelOop")));
								break;
							}
						}
					}
				}
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				//Start loading Scenarios
				loadScenarios();
			}	
		};
		
		Thread t = new Thread(loadProjectTask);
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * Starts a task that loads all Scenarios from the Database
	 */
	private void loadScenarios() {
		Task<?> loadScenarioTask = new Task<Object>(){
			@Override
			protected Object call() throws Exception {
				Connection conn = DBConnection.getInstance().getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM Scenario");
				
				DBScenario scenario = null;
				
				while(rs.next()){
					scenario = new DBScenario(rs.getInt("id"), rs.getString("name"),
							rs.getFloat("npv"), rs.getInt("periods"),
							rs.getInt("slotsPerPeriod"),
							rs.getFloat("discountRate"),
							rs.getFloat("oOAFixed"), null, null, null);
					olScenarios.add(scenario);
				}
				
				//Load ScenarioProcess
				st = conn.createStatement();
				rs = st.executeQuery("SELECT * FROM ScenarioProcess WHERE scenarioID = "+scenario.getId());
				
				List<DBProcess> lstScenarioProcess = new ArrayList<DBProcess>();
				while(rs.next()){
					int processID = rs.getInt("processID");
					
					for(DBProcess process : olProcesses){
						if(process.getId() == processID){
							lstScenarioProcess.add(process);
						}
					}
				}
				
				scenario.setLstProcesses(lstScenarioProcess);
				
				//Load ScenarioProject
				st = conn.createStatement();
				rs = st.executeQuery("SELECT * FROM ScenarioProject WHERE scenarioID = "+scenario.getId());
				
				List<DBProject> lstScenarioProject = new ArrayList<DBProject>();
				while(rs.next()){
					int projectID = rs.getInt("projectID");
					
					for(DBProject project : olProjects){
						if(project.getId() == projectID){
							lstScenarioProject.add(project);
						}
					}
				}
				
				scenario.setLstProjects(lstScenarioProject);
				
				//Load ScenarioConstraint
				st = conn.createStatement();
				rs = st.executeQuery("SELECT * FROM ScenarioConstraint WHERE scenarioID = "+scenario.getId());
				
				List<DBConstraint> lstScenarioConstraint = new ArrayList<DBConstraint>();
				while(rs.next()){
					int y = rs.getInt("y");
					String yString = null;
					if(y == -2){
						yString = DBConstraint.PERIOD_ALL;
					}else{
						yString = Integer.toString(y);
					}
					
					int sID = rs.getInt("sID");
					DBProject s = null;
					int SIID = rs.getInt("sIID");
					DBProject sI = null;
					int iID = rs.getInt("iID");
					DBProcess i = null;
					
					//Find s
					if(sID != 0){
						for(DBProject p : olProjects){
							if(p.getId() == sID){
								s = p;
								break;
							}
						}
					}
					
					//Find sI
					if(SIID != 0){
						for(DBProject p : olProjects){
							if(p.getId() == SIID){
								sI = p;
								break;
							}
						}
					}
					
					//Find i
					if(iID != 0){
						for(DBProcess p : olProcesses){
							if(p.getId() == iID){
								i = p;
								break;
							}
						}
					}
					
					lstScenarioConstraint.add(new DBConstraint(rs
							.getString("type"), s, sI, i, rs.getFloat("x"),
							yString));

				}
				scenario.setLstConstraints(lstScenarioConstraint);
				
				return null;
			}	
		};
		
		Thread t = new Thread(loadScenarioTask);
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * Starts a task that loads all data from the DB.
	 * Calls loadProjects() and loadScenarios()
	 */
	private void loadFromDatabase() {
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
			NewScenarioController nsController = loader.getController();
			nsController.setTsc(this);
			
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
	
	public MainApp getMainApp(){
		return this.mainApp;
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
