package com.v3pm_prototype.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import sun.util.locale.provider.AvailableLanguageTags;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBScenarioProcess;

public class AddProjectController {
	@FXML
	private Label lblProjectName;
	
	private DBProject selectedProject;
	
	//General Infos
	@FXML
	private Label lblPeriods;
	@FXML
	private Label lblType;
	@FXML
	private Label lblAffectedProcess;
	
	//General Settings
	@FXML
	private TextField tfOInv;
	@FXML
	private TextField tfFixedCosts;
	
	
	//Effect Settings
	@FXML
	private TextField tfA;
	@FXML
	private TextField tfB;
	@FXML
	private TextField tfE;
	@FXML
	private TextField tfU;
	@FXML
	private TextField tfM;
	
	@FXML
	private ToggleButton tbA;
	@FXML
	private ToggleButton tbE;
	@FXML
	private ToggleButton tbU;
	@FXML
	private ToggleButton tbM;
	
	@FXML
	private AnchorPane apA;
	@FXML
	private AnchorPane apB;
	@FXML
	private AnchorPane apE;
	@FXML
	private AnchorPane apU;
	@FXML
	private AnchorPane apM;
	
	@FXML
	private Button btnAddProject;
	
	private NewScenarioController nsc;
	
	public AddProjectController(){
	}
	
	@FXML
	public void initialize(){
		
		
	}

	public DBProject getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(DBProject selectedProject) {
		this.selectedProject = selectedProject;
		lblProjectName.setText(selectedProject.getName());
		
		lblPeriods.setText(String.valueOf(selectedProject.getPeriods()));
		lblAffectedProcess.setText(selectedProject.getProcess().toString());
		
		if(this.selectedProject.getType().equals("processLevel")){
			//Hide b
			apB.setVisible(false);
			apB.setPrefHeight(0);
			tfB.setText("0");
		}else{
			tbA.setSelected(true);
			tbA.setDisable(true);
			
			//Hide e & u
			apE.setVisible(false);
			apE.setPrefHeight(0);
			tfE.setText("0");
			apU.setVisible(false);
			apU.setPrefHeight(0);
			tfU.setText("0");
			
		}
	}

	public void setNSC(NewScenarioController nsc){
		this.nsc = nsc;
	}
	
	/**
	 * Writes the new 
	 */
//	public void createSceneProcess(){
//		DBScenarioProcess process = new DBScenarioProcess(-1,
//				this.selectedProcess.getId(), this.selectedProcess.getName(),
//				Float.valueOf(this.tfP.getText()), Float.valueOf(this.tfOop
//						.getText()),
//				Float.valueOf(this.tfFixedCosts.getText()),
//				(float) this.slQ.getValue(), (float) this.slQMin.getValue(),
//				(float) this.slQMax.getValue(), Float.valueOf(this.tfDegQ
//						.getText()), Float.valueOf(this.tfT.getText()),
//				Float.valueOf(this.tfTMax.getText()), Float.valueOf(this.tfDegT
//						.getText()), Float.valueOf(this.tfDMP.getText()),
//				Float.valueOf(this.tfDMLambda.getText()),
//				Float.valueOf(this.tfDMAlpha.getText()),
//				Float.valueOf(this.tfDMBeta.getText()),
//				this.cbDMFktQ.getValue(),
//				this.cbDMFktT.getValue());
//		
//		//TODO un-static
//		NewScenarioController.olProcesses.add(process);
//		NewScenarioController.availableProcesses.remove(this.selectedProcess);
//		nsc.getCBProcess().setValue(NewScenarioController.availableProcesses.get(0));
//		nsc.updateAvailableProjects();
//		
//		//Close the window
//		Stage stage = (Stage) btnAddProcess.getScene().getWindow();
//		stage.close();
//		
//	}
	
	
}
