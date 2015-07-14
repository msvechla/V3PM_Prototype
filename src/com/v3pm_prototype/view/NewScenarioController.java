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
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
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
	
	
	private ObservableList<DBProject> availableProjects = FXCollections.observableArrayList();
	public static ObservableList<DBProcess> availableProcesses = FXCollections.observableArrayList();
	
	private ObservableList<DBProject> olProjects = FXCollections.observableArrayList();
	public static ObservableList<DBProcess> olProcesses = FXCollections.observableArrayList();
	
	public NewScenarioController(){

	}
	
	@FXML
	public void initialize(){
		//Setup project combobox
		cbProject.setItems(availableProjects);
		
		
		//Setup process combobox
		availableProcesses.addAll(TabStartController.olProcesses);
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
		for(DBProcess process : this.olProcesses){
			for(DBProject project : TabStartController.olProjects){
				if(project.getProcess().getId() == process.getId() || project.getProcess().getId() == DBProcess.ID_ALLPROCESSES){
					availableProjects.add(project);
					added = true;
				}
			}
		}
		
		if(added){
			cbProject.setValue(availableProjects.get(0));
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
	        //controller.setTSC(this);
			
			// Show the scene containing the root layout.
	        Stage stage = new Stage();
	        stage.setTitle("Add Project");
	        stage.setScene(new Scene(root));
	        stage.show();
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
	        //controller.setSelectedProcess(cbProcess.getValue());
	       // controller.setNSC(this);
			
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
	            new PropertyValueFactory<DBProcess, String>("name"));
		clmProcessesP.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("p"));
		clmProcessesOop.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("oop"));
		clmProcessesFixCosts.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("fixedCosts"));
		clmProcessesQ.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("q"));
		clmProcessesQMin.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("qMin"));
		clmProcessesQMax.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("p"));
		clmProcessesDGQ.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("degQ"));
		clmProcessesT.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("t"));
		clmProcessesTMax.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("tMax"));
		clmProcessesDGT.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, Float>("degT"));
		clmProcessesDFKT.setCellValueFactory(
	            new PropertyValueFactory<DBProcess, String>("demandFunction"));
		tvProcesses.setItems(this.olProcesses);
	}
	
	public void initTVProjects(){
		tvProjects.setEditable(true);
		
		clmProjectsProject.setCellValueFactory(
	            new PropertyValueFactory<DBProject, String>("name"));
		clmProjectsType.setCellValueFactory(
	            new PropertyValueFactory<DBProject, String>("type"));
		clmProjectsPeriods.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Integer>("periods"));		
		clmProjectsProcess.setCellValueFactory(
	            new PropertyValueFactory<DBProject, DBProcess>("process"));
		
		clmProjectsFixCosts.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("fixCosts"));
		clmProjectsFixCosts.setCellFactory(TextFieldTableCell.<DBProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsFixCosts.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBProject, Float> t) {
	                    ((DBProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setFixedCosts(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsOInv.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("oInv"));
		clmProjectsOInv.setCellFactory(TextFieldTableCell.<DBProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsOInv.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBProject, Float> t) {
	                    ((DBProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setOInv(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsA.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("a"));
		clmProjectsA.setCellFactory(TextFieldTableCell.<DBProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsA.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBProject, Float> t) {
	                    ((DBProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setA(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsA.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("a"));
		clmProjectsA.setCellFactory(TextFieldTableCell.<DBProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsA.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBProject, Float> t) {
	                    ((DBProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setA(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsB.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("b"));
		clmProjectsB.setCellFactory(TextFieldTableCell.<DBProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsB.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBProject, Float> t) {
	                    ((DBProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setB(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsE.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("e"));
		clmProjectsE.setCellFactory(TextFieldTableCell.<DBProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsE.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBProject, Float> t) {
	                    ((DBProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setE(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsU.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("u"));
		clmProjectsU.setCellFactory(TextFieldTableCell.<DBProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsU.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBProject, Float> t) {
	                    ((DBProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setU(t.getNewValue());
	                }
	            }
	        );
		
		clmProjectsM.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("m"));
		clmProjectsM.setCellFactory(TextFieldTableCell.<DBProject, Float>forTableColumn(new FloatStringConverter()));
		clmProjectsM.setOnEditCommit(
	            new EventHandler<CellEditEvent<DBProject, Float>>() {
	                @Override
	                public void handle(CellEditEvent<DBProject, Float> t) {
	                    ((DBProject) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setM(t.getNewValue());
	                }
	            }
	        );
		
		//TODO Only show projects related to processes
		tvProjects.setItems(olProjects);
		
	}
	
//	public void addProject(){
//		DBProject selectedProject = (DBProject) cbProject.getValue();
//		availableProjects.remove(selectedProject);
//		
//		//Create a new DBScenarioProject from selected project
//		DBScenarioProject dbSP = new DBScenarioProject(-1,selectedProject.getId(), selectedProject.getName(), selectedProject.getType(), selectedProject.getPeriods(), selectedProject.getProcess(), 0, 0, 0, 0, 0, 0, 0);
//		olProjects.add(dbSP);
//		cbProject.setValue(availableProjects.get(0));
//		tvProjects.setEditable(true);
//	}
	
	public ComboBox getCBProcess(){
		return this.cbProcess;
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
