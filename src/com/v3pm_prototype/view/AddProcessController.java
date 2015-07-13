package com.v3pm_prototype.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBScenarioProcess;

public class AddProcessController {
	@FXML
	private Label lblProcessName;
	
	private DBProcess selectedProcess;
	
	//General Settings
	@FXML
	private TextField tfP;
	@FXML
	private TextField tfOop;
	@FXML
	private TextField tfFixedCosts;
	//Quality Settings
	@FXML
	private Slider slQ;
	@FXML
	private Slider slQMin;
	@FXML
	private Slider slQMax;
	@FXML
	private TextField tfDegQ;
	//Time Settings
	@FXML
	private TextField tfT;
	@FXML
	private TextField tfTMax;
	@FXML
	private TextField tfDegT;
	//Demand Settings
	@FXML
	private TextField tfDMP;
	@FXML
	private TextField tfDMLambda;
	@FXML
	private TextField tfDMAlpha;
	@FXML
	private TextField tfDMBeta;
	@FXML
	private ComboBox<String> cbDMFktQ;
	@FXML
	private ComboBox<String> cbDMFktT;
	@FXML
	private Button btnAddProcess;
	
	//Contains demand functions
	private ObservableList<String> dmFktQList = FXCollections.observableArrayList("0q","q","ln q","e^(1/q)");
	private ObservableList<String> dmFktTList = FXCollections.observableArrayList("0t","t","ln t","e^(1/t)");
	
	public AddProcessController(){
	}
	
	@FXML
	public void initialize(){
		//Set values for the demand function comboboxes
		cbDMFktQ.setItems(dmFktQList);
		cbDMFktQ.setValue(dmFktQList.get(0));
		
		cbDMFktT.setItems(dmFktTList);
		cbDMFktT.setValue(dmFktTList.get(0));
		
		
	}

	public DBProcess getSelectedProcess() {
		return selectedProcess;
	}

	public void setSelectedProcess(DBProcess selectedProcess) {
		this.selectedProcess = selectedProcess;
		lblProcessName.setText(selectedProcess.getName());
	}

	
	
	/**
	 * Writes the new 
	 */
	public void createSceneProcess(){
		DBScenarioProcess process = new DBScenarioProcess(-1,
				this.selectedProcess.getId(), this.selectedProcess.getName(),
				Float.valueOf(this.tfP.getText()), Float.valueOf(this.tfOop
						.getText()),
				Float.valueOf(this.tfFixedCosts.getText()),
				(float) this.slQ.getValue(), (float) this.slQMin.getValue(),
				(float) this.slQMax.getValue(), Float.valueOf(this.tfDegQ
						.getText()), Float.valueOf(this.tfT.getText()),
				Float.valueOf(this.tfTMax.getText()), Float.valueOf(this.tfDegT
						.getText()), Float.valueOf(this.tfDMP.getText()),
				Float.valueOf(this.tfDMLambda.getText()),
				Float.valueOf(this.tfDMAlpha.getText()),
				Float.valueOf(this.tfDMBeta.getText()),
				this.cbDMFktQ.getValue(),
				this.cbDMFktT.getValue());
		
		NewScenarioController.olProcesses.add(process);
		NewScenarioController.availableProcesses.remove(this.selectedProcess);
		
		//Close the window
		Stage stage = (Stage) btnAddProcess.getScene().getWindow();
		stage.close();
		
	}
	
	
}
