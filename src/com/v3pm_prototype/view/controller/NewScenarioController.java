package com.v3pm_prototype.view.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBConstraint;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.V3PM_Prototype;

/**
 * 
 * @author Marius Svechla
 *
 */
public class NewScenarioController {
	private TabStartController tsc;
	private static final DataFormat DF_PROJECT = new DataFormat(
			"com.v3pm_prototype.database.DBProject");
	private static final DataFormat DF_PROCESS = new DataFormat(
			"com.v3pm_prototype.database.DBProcess");
	
	@FXML
	private Button btnAddProject;
	@FXML
	private Button btnAddProcess;
	@FXML
	private TextField tfName;
	@FXML
	private Button btnCreateScenario;
	
	//General Settings
	@FXML
	private ComboBox<Integer> cbPeriods;
	private ObservableList<Integer> availablePeriods = FXCollections
			.observableArrayList();
	@FXML
	private ComboBox<Integer> cbSlotsPerPeriod;
	private ObservableList<Integer> availableSlots = FXCollections
			.observableArrayList();
	@FXML
	private TextField tfDiscountRate;
	@FXML
	private TextField tfOAFixedOutflows;
	
	

	// ListViews (Pools)
	@FXML
	private Accordion accordionP;
	@FXML
	private TitledPane tpProcessPool;
	
	@FXML
	private ListView<DBProject> lvProjectPool;
	@FXML
	private ListView<DBProcess> lvProcessPool;

	private ObservableList<DBProject> availableProjects = FXCollections
			.observableArrayList();
	private ObservableList<DBProcess> availableProcesses = FXCollections
			.observableArrayList();

	//ListViews (Constraint Pools)
	@FXML
	private Accordion accordionC;
	@FXML
	private TitledPane tpConstraintPool1;
	
	@FXML
	private ListView<String> lvCAmongProjects;
	private ObservableList<String> olCAmongProjects = FXCollections
			.observableArrayList();
	@FXML
	private ListView<String> lvCProjectSpecific;
	private ObservableList<String> olCProjectSpecific = FXCollections
			.observableArrayList();
	@FXML
	private ListView<String> lvCProcessSpecific;
	private ObservableList<String> olCProcessSpecific = FXCollections
			.observableArrayList();
	@FXML
	private ListView<String> lvCPeriodSpecific;
	private ObservableList<String> olCPeriodSpecific = FXCollections
			.observableArrayList();

	//Main Constraint ListView
	@FXML
	private ListView<DBConstraint> lvConstraints;
	public ObservableList<DBConstraint> olConstraints = FXCollections
			.observableArrayList();
	
	//Project Table
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
	private TableColumn<DBProcess, Float> clmProcessesQMin;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesQMax;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesT;
	@FXML
	private TableColumn<DBProcess, Float> clmProcessesTMax;
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

	public ObservableList<DBProject> olProjects = FXCollections
			.observableArrayList();
	public ObservableList<DBProcess> olProcesses = FXCollections
			.observableArrayList();
	
	private Stage stage = null;
	private DBScenario blueprint = null; //contains a Scenario that is used to populate views with basic data

	private ValidationSupport validationSupport;
	@FXML
	private VBox mainContainer;
	
	public NewScenarioController() {
		validationSupport = new ValidationSupport();
	}

	@FXML
	public void initialize() {
//		initValidation();
		
		// Setup the Projects & Process TableView
		initTVProjects();
		initTVProcesses();

		// Setup Pools
		initPools();
		initLVConstraints();
		
		initGeneralSettings();
		
	}

	// /**
	// * Updates the list of available projects for the scenario
	// */
	// public void updateAvailableProjects(){
	// boolean added = false;
	// for(DBProcess process : this.olProcesses){
	// for(DBProject project : TabStartController.olProjects){
	// if(project.getProcess().getId() == process.getId() ||
	// project.getProcess().getId() == DBProcess.ID_ALLPROCESSES){
	// availableProjects.add(project);
	// added = true;
	// }
	// }
	// }
	//
	// if(added){
	// cbProject.setValue(availableProjects.get(0));
	// }
	//
	// }
	private void initValidation(){		
		validationSupport.registerValidator(tfName, Validator.createEmptyValidator("Scenario Name is required"));
		validationSupport.registerValidator(tfDiscountRate, Validator.createEmptyValidator("Discount Rate is required"));
		validationSupport.registerValidator(tfOAFixedOutflows, Validator.createEmptyValidator("Overarching Fixed Outflows are required"));
		validationSupport.registerValidator(cbPeriods, Validator.createEmptyValidator("Number of Periods must be set"));
		validationSupport.registerValidator(cbSlotsPerPeriod, Validator.createEmptyValidator("Number of Slots per Period must be set"));
		validationSupport.initInitialDecoration();
	}
	
	private void initGeneralSettings() {
		cbPeriods.setItems(availablePeriods);
		cbSlotsPerPeriod.setItems(availableSlots);
		
		for(int i=1; i<=12; i++){
			availablePeriods.add(i);
		}
		
		cbPeriods.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				for(int i=1; i<=4; i++){
					availableSlots.add(i);
				}
				cbSlotsPerPeriod.setValue(availableSlots.get(0));
			}
		});
		
	}

	/**
	 * Writes the scenario and all its components to the database
	 */
	public void createScenario(){
		
		//Close the window
		Stage stage = (Stage) btnCreateScenario.getScene().getWindow();
		stage.close();
		
		tsc.getMainApp().getV3pmGUIController().setStatus("Writing Scenario to Database...");
		tsc.getMainApp().getV3pmGUIController().setProgress(-1);
		
		Task<?> writeScenarioTask = new Task<Object>(){
			@Override
			protected Object call() throws Exception {
				Connection conn = DBConnection.getInstance().getConnection();
				Statement st = conn.createStatement();
				
				//-------------------- WRITE Scenario TO DB --------------------
				st.executeUpdate("INSERT INTO Scenario(name, periods, slotsPerPeriod, discountRate, oOAFixed) VALUES('"
						+ tfName.getText()
						+ "',"
						+ cbPeriods.getValue()
						+ ","
						+ cbSlotsPerPeriod.getValue()
						+ ","
						+ Float.valueOf(tfDiscountRate.getText())
						+ ","
						+ Float.valueOf(tfOAFixedOutflows.getText()) + ");");
				int scenarioID = st.getGeneratedKeys().getInt(1);
				
				
				//Create DBScenario and add it to the Scenario List
				DBScenario scenario = new DBScenario(scenarioID,
						tfName.getText(), 0, cbPeriods.getValue(),
						cbSlotsPerPeriod.getValue(),
						Float.valueOf(tfDiscountRate.getText()),
						Float.valueOf(tfOAFixedOutflows.getText()));
				
				scenario.getLstConstraints().addAll(olConstraints);
				scenario.getLstProcesses().addAll(olProcesses);
				scenario.getLstProjects().addAll(olProjects);
				tsc.olScenarios.add(scenario);
				
				//TODO Loop through classes would be nicer code
				//-------------------- WRITE ScenarioConstraint TO DB --------------------
				StringBuilder query = new StringBuilder();
				query.append("INSERT INTO ScenarioConstraint(scenarioID, type, sID, sIID, iID, x, y) VALUES");
				
				for(DBConstraint constraint : olConstraints){
					int sID = 0;
					if(constraint.getS() != null){
						sID = constraint.getS().getId();
					}
					int sIID = 0;
					if(constraint.getSi() != null){
						sIID = constraint.getSi().getId();
					}
					int iID = 0;
					if(constraint.getI() != null){
						iID = constraint.getI().getId();
					}
					int y = -1;
					if(constraint.getY().equals("ALL")){
						y =-2;
					}else{
						y = Integer.parseInt(constraint.getY());
					}
					
					query.append("("+scenarioID+", '"+constraint.getType()+"', "+sID+", "+sIID+", "+iID+", "+constraint.getX()+", "+y+")");
					
					//If this is the last constraint  close the query ";", else add "," for next values
					if(olConstraints.indexOf(constraint) == olConstraints.size()-1){
						query.append(";");
					}else{
						query.append(",");
					}
				}
				
				//Execute the Constraint Query
				System.out.println("[SQLITE] " + query.toString());
				st.executeUpdate(query.toString());
				
				//-------------------- WRITE ScenarioProcess TO DB --------------------
				query = new StringBuilder();
				query.append("INSERT INTO ScenarioProcess(scenarioID, processID) VALUES");
				
				for(DBProcess process : olProcesses){
					
					query.append("("+scenarioID+", "+process.getId()+")");
					
					//If this is the last process  close the query ";", else add "," for next values
					if(olProcesses.indexOf(process) == olProcesses.size()-1){
						query.append(";");
					}else{
						query.append(",");
					}
				}
				
				//Execute the Process Query
				System.out.println("[SQLITE] " + query.toString());
				st.executeUpdate(query.toString());
				
				//-------------------- WRITE ScenarioProject TO DB --------------------
				query = new StringBuilder();
				query.append("INSERT INTO ScenarioProject(scenarioID, projectID) VALUES");
				
				for(DBProject project : olProjects){
					
					query.append("("+scenarioID+", "+project.getId()+")");
					
					//If this is the last project  close the query ";", else add "," for next values
					if(olProjects.indexOf(project) == olProjects.size()-1){
						query.append(";");
					}else{
						query.append(",");
					}
				}
				
				//Execute the Project Query
				System.out.println("[SQLITE] " + query.toString());
				st.executeUpdate(query.toString());
				
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				tsc.getMainApp().getV3pmGUIController().setStatus("Scenario saved to Database.");
				tsc.getMainApp().getV3pmGUIController().setProgress(0);
				
//				//Close the window
//				Stage stage = (Stage) btnCreateScenario.getScene().getWindow();
//				stage.close();
			}		
		};
		
		Thread t = new Thread(writeScenarioTask);
		t.setDaemon(true);
		t.start();
	}

	private void initLVConstraints() {
		// Set listview content
		lvCAmongProjects.setItems(olCAmongProjects);
		lvCProjectSpecific.setItems(olCProjectSpecific);
		lvCProcessSpecific.setItems(olCProcessSpecific);
		lvCPeriodSpecific.setItems(olCPeriodSpecific);
		lvConstraints.setItems(olConstraints);

		// Create Constraint Strings for the Constraint Pool
		olCAmongProjects.addAll(DBConstraint.TYPE_LOCMUTEX,
				DBConstraint.TYPE_LOCMUTDEP, DBConstraint.TYPE_GLOMUTEX,
				DBConstraint.TYPE_GLOMUTDEP, DBConstraint.TYPE_PRESUC);
		
		olCProjectSpecific.addAll(DBConstraint.TYPE_EARLIEST,
				DBConstraint.TYPE_LATEST, DBConstraint.TYPE_MANDATORY);
		
		olCProcessSpecific.addAll(DBConstraint.TYPE_QUALMIN,
				DBConstraint.Type_TIMEMAX);
		
		olCPeriodSpecific.addAll(DBConstraint.TYPE_BUDPRO,
				DBConstraint.TYPE_BUDBPM, DBConstraint.TYPE_BUDGET,
				DBConstraint.TYPE_NUMPROJ);
		
		// -------------------- DRAG AND DROP --------------------
		
		//Target: Constraint List
		lvConstraints.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				// accept it only if it is not dragged from the same node and if
				// it has String Data
				if (event.getGestureSource() != tvProcesses
						&& event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}
		});

		lvConstraints.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasString()) {
					// Open AddConstraint Window
					openAddConstraintWindow(db.getString());
					success = true;
				}
				event.setDropCompleted(success);
				event.consume();
				System.out.println(success);
			}
		});
		
		//Source: Constraint Pools
		EventHandler<MouseEvent> ConstraintDnDHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Save Constraint String to Clipboard
				ListView<String> source = ((ListView<String>)event.getSource());
				Dragboard db = source.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				String constraintType = source.getSelectionModel().getSelectedItem();
				content.putString(constraintType);
				db.setContent(content);
				event.consume();
			}
		};
		
		//Set the Drag and Drop Handler for all pools
		lvCAmongProjects.setOnDragDetected(ConstraintDnDHandler);
		lvCPeriodSpecific.setOnDragDetected(ConstraintDnDHandler);
		lvCProcessSpecific.setOnDragDetected(ConstraintDnDHandler);
		lvCProjectSpecific.setOnDragDetected(ConstraintDnDHandler);
		
		// -------------------- REMOVE CONTEXT MENU --------------------

		final ContextMenu constraintsContextMenu = new ContextMenu();
		MenuItem miRemove = new MenuItem("Remove");
		constraintsContextMenu.getItems().add(miRemove);

		lvConstraints.setContextMenu(constraintsContextMenu);
		miRemove.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBConstraint selectedConstraint = lvConstraints.getSelectionModel()
						.getSelectedItem();
				olConstraints.remove(selectedConstraint);
			}
		});

	}

	private void initPools() {
		availableProjects.addAll(tsc.olProjects);
		lvProjectPool.setItems(availableProjects);
		availableProcesses.addAll(tsc.olProcesses);
		lvProcessPool.setItems(availableProcesses);

		// -------------------- DRAG AND DROP --------------------

		lvProjectPool.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Save Project in Clipboard
				Dragboard db = lvProjectPool.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				DBProject project = lvProjectPool.getSelectionModel()
						.getSelectedItem();
				content.put(DF_PROJECT, project);
				db.setContent(content);

				event.consume();
			}
		});

		lvProcessPool.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Save Process in Clipboard
				Dragboard db = lvProcessPool.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				DBProcess process = lvProcessPool.getSelectionModel()
						.getSelectedItem();
				content.put(DF_PROCESS, process);
				db.setContent(content);

				event.consume();
			}
		});
	}

	public void initTVProcesses() {
		clmProcessesProcess
				.setCellValueFactory(new PropertyValueFactory<DBProcess, String>(
						"name"));
		clmProcessesP
				.setCellValueFactory(new PropertyValueFactory<DBProcess, Float>(
						"p"));
		clmProcessesOop
				.setCellValueFactory(new PropertyValueFactory<DBProcess, Float>(
						"oop"));
		clmProcessesFixCosts
				.setCellValueFactory(new PropertyValueFactory<DBProcess, Float>(
						"fixedCosts"));
		clmProcessesQ
				.setCellValueFactory(new PropertyValueFactory<DBProcess, Float>(
						"q"));
		clmProcessesDGQ
				.setCellValueFactory(new PropertyValueFactory<DBProcess, Float>(
						"degQ"));
		clmProcessesT
				.setCellValueFactory(new PropertyValueFactory<DBProcess, Float>(
						"t"));
		clmProcessesDGT
				.setCellValueFactory(new PropertyValueFactory<DBProcess, Float>(
						"degT"));
		clmProcessesDFKT
				.setCellValueFactory(new PropertyValueFactory<DBProcess, String>(
						"demandFunction"));
		tvProcesses.setItems(this.olProcesses);
		

		// -------------------- DRAG AND DROP --------------------

		tvProcesses.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				// accept it only if it is not dragged from the same node and if
				// it has Process Data
				if (event.getGestureSource() != tvProcesses
						&& event.getDragboard().hasContent(DF_PROCESS)) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}
		});

		tvProcesses.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasContent(DF_PROCESS)) {
					// Drop process on scenario and remove from available list
					olProcesses.add((DBProcess) db.getContent(DF_PROCESS));
					availableProcesses.remove(lvProcessPool.getSelectionModel()
							.getSelectedItem());
					success = true;
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
		
		// -------------------- REMOVE CONTEXT MENU --------------------

		final ContextMenu processesContextMenu = new ContextMenu();
		MenuItem miRemove = new MenuItem("Remove");
		processesContextMenu.getItems().add(miRemove);

		tvProcesses.setContextMenu(processesContextMenu);
		miRemove.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBProcess selectedProcess = tvProcesses.getSelectionModel()
						.getSelectedItem();
				olProcesses.remove(selectedProcess);
				availableProcesses.add(selectedProcess);
			}
		});
			
	}

	public void initTVProjects() {
		tvProjects.setEditable(true);

		// Setup Table Columns
		clmProjectsProject
				.setCellValueFactory(new PropertyValueFactory<DBProject, String>(
						"name"));
		clmProjectsType
				.setCellValueFactory(new PropertyValueFactory<DBProject, String>(
						"type"));
		clmProjectsPeriods
				.setCellValueFactory(new PropertyValueFactory<DBProject, Integer>(
						"periods"));
		clmProjectsProcess
				.setCellValueFactory(new PropertyValueFactory<DBProject, DBProcess>(
						"process"));
		clmProjectsFixCosts
				.setCellValueFactory(new PropertyValueFactory<DBProject, Float>(
						"fixCosts"));
		clmProjectsOInv
				.setCellValueFactory(new PropertyValueFactory<DBProject, Float>(
						"oInv"));
		clmProjectsA
				.setCellValueFactory(new PropertyValueFactory<DBProject, Float>(
						"a"));
		clmProjectsA
				.setCellValueFactory(new PropertyValueFactory<DBProject, Float>(
						"a"));
		clmProjectsB
				.setCellValueFactory(new PropertyValueFactory<DBProject, Float>(
						"b"));
		clmProjectsE
				.setCellValueFactory(new PropertyValueFactory<DBProject, Float>(
						"e"));
		clmProjectsU
				.setCellValueFactory(new PropertyValueFactory<DBProject, Float>(
						"u"));
		clmProjectsM
				.setCellValueFactory(new PropertyValueFactory<DBProject, Float>(
						"m"));

		tvProjects.setItems(olProjects);

		// -------------------- DRAG AND DROP --------------------

		tvProjects.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				// accept it only if it is not dragged from the same node and if
				// it has Project Data
				if (event.getGestureSource() != tvProjects
						&& event.getDragboard().hasContent(DF_PROJECT)) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}
		});

		tvProjects.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasContent(DF_PROJECT)) {
					// Drop project on scenario and remove from available list
					olProjects.add((DBProject) db.getContent(DF_PROJECT));
					availableProjects.remove(lvProjectPool.getSelectionModel()
							.getSelectedItem());
					success = true;
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
		
		// -------------------- REMOVE CONTEXT MENU --------------------

		final ContextMenu projectsContextMenu = new ContextMenu();
		MenuItem miRemove = new MenuItem("Remove");
		projectsContextMenu.getItems().add(miRemove);

		tvProjects.setContextMenu(projectsContextMenu);
		miRemove.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBProject selectedProject = tvProjects.getSelectionModel()
						.getSelectedItem();
				olProcesses.remove(selectedProject);
				availableProjects.add(selectedProject);
			}
		});

	}
	
	public void openAddConstraintWindow(String type){
		// Load root layout from fxml file. 
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(V3PM_Prototype.class.getResource("/com/v3pm_prototype/view/AddConstraint.fxml"));
        VBox root;
		try {
			root = (VBox) loader.load();
			
			AddConstraintController acController = loader.getController();
			acController.setType(type);
			acController.setNSC(this);
			acController.setTSC(tsc);
			acController.updateType();
			
			// Show the scene containing the root layout.
	        Stage stage = new Stage();
	        stage.setTitle("Define Constraint");
	        stage.setScene(new Scene(root));
	        stage.hide();
	        stage.show();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	public void setBlueprint(DBScenario blueprint){
		this.blueprint = blueprint;
		
		//Populate field based on scenario blueprint
		cbPeriods.setValue(blueprint.getPeriods());
		cbSlotsPerPeriod.setValue(blueprint.getSlotsPerPeriod());
		tfDiscountRate.setText(String.valueOf(blueprint.getDiscountRate()));
		tfOAFixedOutflows.setText(String.valueOf(blueprint.getOOAFixed()));
		
		olProcesses.addAll(blueprint.getLstProcesses());
		availableProcesses.removeAll(blueprint.getLstProcesses());
		
		olProjects.addAll(blueprint.getLstProjects());
		availableProjects.removeAll(blueprint.getLstProjects());
		
		olConstraints.addAll(blueprint.getLstConstraints());
		tfName.setText(blueprint.getName()+" Copy");
		
		
	}

	public TabStartController getTsc() {
		return tsc;
	}

	public void setTsc(TabStartController tsc) {
		this.tsc = tsc;
	}
	
	public int getPeriods(){
		return cbPeriods.getValue();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		
		//Open the accordion when shown
		stage.setOnShown(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				accordionP.expandedPaneProperty().set(tpProcessPool);
				accordionC.expandedPaneProperty().set(tpConstraintPool1);
				
			}
		});
		
	}

}
