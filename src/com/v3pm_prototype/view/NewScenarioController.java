package com.v3pm_prototype.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBScenarioProcess;
import com.v3pm_prototype.database.DBScenarioProject;
import com.v3pm_prototype.main.MainApp;

public class NewScenarioController {
	@FXML
	private Button btnAddProject;
	@FXML
	private Button btnAddProcess;
	@FXML
	private TextField tfName;
	@FXML
	private ComboBox<DBProject> cbProject;
	@FXML
	private ComboBox<DBProcess> cbProcess;
	
	@FXML
	private TableView<DBScenarioProject> tvProjects;
	@FXML
	private TableColumn<DBScenarioProject, String> clmProjectsProject;
	@FXML
	private TableColumn<DBScenarioProject, Integer> clmProjectsPeriods;
	@FXML
	private TableColumn<DBScenarioProject, String> clmProjectsType;
	@FXML
	private TableColumn<DBScenarioProject, DBProcess> clmProjectsProcess;
	@FXML
	private TableColumn<DBScenarioProject, Integer> clmProjectsFixCosts;
	@FXML
	private TableColumn<DBScenarioProject, Integer> clmProjectsOInv;
	@FXML
	private TableColumn<DBScenarioProject, Float> clmProjectsA;
	@FXML
	private TableColumn<DBScenarioProject, Float> clmProjectsB;
	@FXML
	private TableColumn<DBScenarioProject, Float> clmProjectsE;
	@FXML
	private TableColumn<DBScenarioProject, Float> clmProjectsU;
	@FXML
	private TableColumn<DBScenarioProject, Float> clmProjectsM;
	
	@FXML
	private TableView<DBScenarioProcess> tvProcesses;
	@FXML
	private TableColumn<DBScenarioProcess, String> clmProcessesProcess;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesFixCosts;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesQ;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesQMin;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesQMax;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesT;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesTMax;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesP;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesOop;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesDGQ;
	@FXML
	private TableColumn<DBScenarioProcess, Float> clmProcessesDGT;
	@FXML
	private TableColumn<DBScenarioProcess, String> clmProcessesDFKT;
	
	
	private ObservableList<DBProject> availableProjects = FXCollections.observableArrayList();
	public static ObservableList<DBProcess> availableProcesses = FXCollections.observableArrayList();
	
	private ObservableList<DBScenarioProject> olProjects = FXCollections.observableArrayList();
	public static ObservableList<DBScenarioProcess> olProcesses = FXCollections.observableArrayList();
	
	public NewScenarioController(){

	}
	
	@FXML
	public void initialize(){
		//Setup project combobox
		cbProject.setItems(availableProjects);
		
		
		//Setup process combobox
		availableProcesses.addAll(TabStartController.olProcess);
		cbProcess.setItems(availableProcesses);
		cbProcess.setValue(availableProcesses.get(0));
		
		//Setup the Projects & Process TableView
		initTVProjects();
		initTVProcesses();
	}
	
	/**
	 * Updates the list of available projects for the scenario
	 */
	public void updateAvailableProjects(){
		boolean added = false;
		for(DBScenarioProcess process : this.olProcesses){
			for(DBProject project : TabStartController.olProject){
				if(project.getProcess().getId() == process.getId() || project.getProcess().getId() == DBProcess.ID_EMPTYPROCESS){
					availableProjects.add(project);
					added = true;
				}
			}
		}
		
		if(added){
			cbProject.setValue(availableProjects.get(0));
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
	        controller.setSelectedProcess(cbProcess.getValue());
			
			// Show the scene containing the root layout.
	        Stage stage = new Stage();
	        stage.setTitle("Add Process");
	        stage.setScene(new Scene(root));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	public void initTVProcesses(){
		clmProcessesProcess.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, String>("name"));
		clmProcessesP.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("p"));
		clmProcessesOop.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("oop"));
		clmProcessesFixCosts.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("fixedCosts"));
		clmProcessesQ.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("q"));
		clmProcessesQMin.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("qMin"));
		clmProcessesQMax.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("p"));
		clmProcessesDGQ.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("degQ"));
		clmProcessesT.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("t"));
		clmProcessesTMax.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("tMax"));
		clmProcessesDGT.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, Float>("degT"));
		clmProcessesDFKT.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProcess, String>("demandFunction"));
		tvProcesses.setItems(this.olProcesses);
	}
	
	public void initTVProjects(){
		tvProjects.setEditable(true);
		
		clmProjectsProject.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, String>("name"));
		clmProjectsType.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, String>("type"));
		clmProjectsPeriods.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Integer>("periods"));		
		clmProjectsProcess.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, DBProcess>("process"));
		
		clmProjectsFixCosts.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Integer>("fixCosts"));
		clmProjectsFixCosts.setCellFactory(TextFieldTableCell.<DBScenarioProject, Integer>forTableColumn(new IntegerStringConverter()));
		clmProjectsFixCosts.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBScenarioProject, Integer>>() {
	                @Override
	                public void handle(CellEditEvent<DBScenarioProject, Integer> t) {
	                    ((DBScenarioProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setFixCosts(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsOInv.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Integer>("oInv"));
		clmProjectsOInv.setCellFactory(TextFieldTableCell.<DBScenarioProject, Integer>forTableColumn(new IntegerStringConverter()));
		clmProjectsOInv.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBScenarioProject, Integer>>() {
	                @Override
	                public void handle(CellEditEvent<DBScenarioProject, Integer> t) {
	                    ((DBScenarioProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setOInv(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsA.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Float>("a"));
		clmProjectsA.setCellFactory(TextFieldTableCell.<DBScenarioProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsA.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBScenarioProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBScenarioProject, Float> t) {
	                    ((DBScenarioProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setA(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsA.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Float>("a"));
		clmProjectsA.setCellFactory(TextFieldTableCell.<DBScenarioProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsA.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBScenarioProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBScenarioProject, Float> t) {
	                    ((DBScenarioProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setA(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsB.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Float>("b"));
		clmProjectsB.setCellFactory(TextFieldTableCell.<DBScenarioProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsB.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBScenarioProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBScenarioProject, Float> t) {
	                    ((DBScenarioProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setB(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsE.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Float>("e"));
		clmProjectsE.setCellFactory(TextFieldTableCell.<DBScenarioProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsE.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBScenarioProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBScenarioProject, Float> t) {
	                    ((DBScenarioProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setE(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsU.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Float>("u"));
		clmProjectsU.setCellFactory(TextFieldTableCell.<DBScenarioProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsU.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBScenarioProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBScenarioProject, Float> t) {
	                    ((DBScenarioProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setU(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsM.setCellValueFactory(
	            new PropertyValueFactory<DBScenarioProject, Float>("m"));
		clmProjectsM.setCellFactory(TextFieldTableCell.<DBScenarioProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsM.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBScenarioProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBScenarioProject, Float> t) {
	                    ((DBScenarioProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setM(t.getNewValue());
	                }
	            }
	        );
		
		//TODO Only show projects related to processes
		tvProjects.setItems(olProjects);
		
	}
	
	public void addProject(){
		DBProject selectedProject = (DBProject) cbProject.getValue();
		availableProjects.remove(selectedProject);
		
		//Create a new DBScenarioProject from selected project
		DBScenarioProject dbSP = new DBScenarioProject(-1,selectedProject.getId(), selectedProject.getName(), selectedProject.getType(), selectedProject.getPeriods(), selectedProject.getProcess(), 0, 0, 0, 0, 0, 0, 0);
		olProjects.add(dbSP);
		cbProject.setValue(availableProjects.get(0));
		tvProjects.setEditable(true);
	}
	
	/**
	 * Writes the new Scenario into the Database and adds it to the Scenario List
	 */
	public void createScenario(){
//		Connection conn = DBConnection.getInstance().getConnection();
//		
//		try {
//			Statement st = conn.createStatement();
//			st.executeUpdate("INSERT INTO Project(name, type, periods) VALUES ('"+tfName.getText()+"', '"+cbType.getValue()+"',"+tfPeriods.getText()+");");
//			int insertedID = st.getGeneratedKeys().getInt(1);
//			
//			TabStartController.olProject.add(new DBProject(insertedID, tfName.getText(), cbType.getValue().toString(), Integer.parseInt(tfPeriods.getText())));
//			
//			//Close the window
//			Stage stage = (Stage) btnCreate.getScene().getWindow();
//			stage.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
	}
}
