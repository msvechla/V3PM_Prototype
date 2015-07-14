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
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
	private TabStartController tsc;
	private static final DataFormat DF_PROJECT = new DataFormat("com.v3pm_prototype.database.DBProject");
	private static final DataFormat DF_PROCESS = new DataFormat("com.v3pm_prototype.database.DBProcess");
	
	@FXML
	private Button btnAddProject;
	@FXML
	private Button btnAddProcess;
	@FXML
	private TextField tfName;
	
	//ListViews (Pools)
	@FXML
	private ListView<DBProject> lvProjectPool;
	@FXML
	private ListView<DBProcess> lvProcessPool;
	
	private ObservableList<DBProject> availableProjects = FXCollections.observableArrayList();
	private ObservableList<DBProcess> availableProcesses = FXCollections.observableArrayList();
	
	
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
	
	private ObservableList<DBProject> olProjects = FXCollections.observableArrayList();
	public static ObservableList<DBProcess> olProcesses = FXCollections.observableArrayList();
	
	public NewScenarioController(){

	}
	
	@FXML
	public void initialize(){
		//Setup the Projects & Process TableView
		initTVProjects();
		initTVProcesses();
		
		//Setup Pools
		initPools();
	}
	
//	/**
//	 * Updates the list of available projects for the scenario
//	 */
//	public void updateAvailableProjects(){
//		boolean added = false;
//		for(DBProcess process : this.olProcesses){
//			for(DBProject project : TabStartController.olProjects){
//				if(project.getProcess().getId() == process.getId() || project.getProcess().getId() == DBProcess.ID_ALLPROCESSES){
//					availableProjects.add(project);
//					added = true;
//				}
//			}
//		}
//		
//		if(added){
//			cbProject.setValue(availableProjects.get(0));
//		}
//		
//	}
	
	private void initPools(){
		availableProjects.addAll(tsc.olProjects);
		lvProjectPool.setItems(availableProjects);
		availableProcesses.addAll(tsc.olProcesses);
		lvProcessPool.setItems(availableProcesses);
		
		
		//-------------------- DRAG AND DROP --------------------
		
		lvProjectPool.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Save Project in Clipboard
				Dragboard db = lvProjectPool.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				DBProject project = lvProjectPool.getSelectionModel().getSelectedItem();
				content.put(DF_PROJECT, project);
				db.setContent(content);
                
                event.consume();
			}
		});
		
		lvProcessPool.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Save Process in Clipboard
				Dragboard db = lvProcessPool.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				DBProcess process = lvProcessPool.getSelectionModel().getSelectedItem();
				content.put(DF_PROCESS, process);
				db.setContent(content);
                
                event.consume();
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
					availableProcesses.remove(lvProcessPool.getSelectionModel().getSelectedItem());
					success = true;
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
	}
	
	public void initTVProjects(){
		tvProjects.setEditable(true);
		
		//Setup Table Columns
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
		clmProjectsOInv.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("oInv"));
		clmProjectsA.setCellValueFactory(
	            new PropertyValueFactory<DBProject, Float>("a"));
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
		
		tvProjects.setItems(olProjects);

		
		//-------------------- DRAG AND DROP --------------------
		
		tvProjects.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                //accept it only if it is  not dragged from the same node and if it has Project Data
                if (event.getGestureSource() != tvProjects &&
                        event.getDragboard().hasContent(DF_PROJECT)) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
		
		tvProjects.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasContent(DF_PROJECT)) {
                	//Drop project on scenario and remove from available list
                    olProjects.add((DBProject) db.getContent(DF_PROJECT));
                    availableProjects.remove(lvProjectPool.getSelectionModel().getSelectedItem());
                    success = true;
                }
                event.setDropCompleted(success); 
                event.consume();
            }
        });
		
	}

	public TabStartController getTsc() {
		return tsc;
	}

	public void setTsc(TabStartController tsc) {
		this.tsc = tsc;
	}
	
}
