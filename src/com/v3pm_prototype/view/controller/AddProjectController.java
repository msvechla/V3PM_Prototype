package com.v3pm_prototype.view.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.Notifications;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBProcess;

public class AddProjectController {
	
	//General Infos
	@FXML
	private TextField tfName;
	
	@FXML
	private TextField tfPeriods;
	@FXML
	private ComboBox<String> cbType;
	@FXML
	private ComboBox<DBProcess> cbAffectedProcess;
	
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
	
	private TabStartController tsc;
	
	private ObservableList<DBProcess> availableProcesses = FXCollections.observableArrayList();
	private ObservableList<String> projectTypes = FXCollections.observableArrayList("processLevel","bpmLevel");
	
	public AddProjectController(){
	}
	
	@FXML
	public void initialize(){
		cbAffectedProcess.setItems(availableProcesses);

		cbType.setItems(projectTypes);
		cbType.setValue(projectTypes.get(0));
		
		//Update the available input field for process / bpm level
		cbType.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateType();
			}
		});
	}


	public void updateType() {
		String type = cbType.getValue();
		double standardHeight = apM.getHeight();
		
		if(type.equals("processLevel")){
			//Hide b
			apB.setVisible(false);
			apB.setPrefHeight(0);
			tfB.setText("0");
			
			//enable a
			tbA.setDisable(false);
			tbA.setSelected(false);
			
			//Show e & u
			apE.setVisible(true);
			apE.setPrefHeight(standardHeight);
			tfE.setText("0");
			apU.setVisible(true);
			apU.setPrefHeight(standardHeight);
			tfU.setText("0");
			
			//Update available processes
			availableProcesses.clear();
			availableProcesses.addAll(tsc.olProcesses);
			if(availableProcesses.size()>0){
				cbAffectedProcess.setValue(availableProcesses.get(0));
			}
			
		}else{
			//Show b
			apB.setVisible(true);
			apB.setPrefHeight(standardHeight);
			tfB.setText("0");
			
			//disable a
			tbA.setSelected(true);
			tbA.setDisable(true);
			
			//Hide e & u
			apE.setVisible(false);
			apE.setPrefHeight(0);
			tfE.setText("0");
			apU.setVisible(false);
			apU.setPrefHeight(0);
			tfU.setText("0");
			
			//Update available processes
			availableProcesses.clear();
			DBProcess allProcess = new DBProcess(DBProcess.ID_ALLPROCESSES, DBProcess.NAME_ALLPROCESSES, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "");
			availableProcesses.add(allProcess);
			cbAffectedProcess.setValue(availableProcesses.get(0));
			
			
		}
	}

	public void setTSC(TabStartController tsc){
		this.tsc = tsc;
		availableProcesses.addAll(tsc.olProcesses);
		if(availableProcesses.size()>0){
			cbAffectedProcess.setValue(availableProcesses.get(0));
		}
	}
	
	/**
	 * Writes the new 
	 */
	public void createProject(){
		
		if(mandatoryFilled()){
			Connection conn = DBConnection.getInstance().getConnection();
			
			try {
				
				float fixedCosts = 0;
				if(tfFixedCosts.getText().length() != 0){
					fixedCosts = Float.valueOf(tfFixedCosts.getText());
				}
				float a = 0;
				if(tfA.getText().length() != 0){
					a = Float.valueOf(tfA.getText());
				}
				float b = 0;
				if(tfB.getText().length() != 0){
					b = Float.valueOf(tfB.getText());
				}
				float e = 0;
				if(tfE.getText().length() != 0){
					e = Float.valueOf(tfE.getText());
				}
				float u = 0;
				if(tfU.getText().length() != 0){
					u = Float.valueOf(tfU.getText());
				}
				float m = 0;
				if(tfM.getText().length() != 0){
					m = Float.valueOf(tfM.getText());
				}
				
				Statement st = conn.createStatement();
				st.executeUpdate("INSERT INTO Project(name, type, periods, processID, oInv, fixedCosts, a, b, e, u, m, absrelQ, absrelT, absrelOop) VALUES ('"
						+ tfName.getText()
						+ "', '"
						+ cbType.getValue()
						+ "',"
						+ tfPeriods.getText()
						+ ","
						+ cbAffectedProcess.getValue().getId()
						+ ","
						+ Float.valueOf(tfOInv.getText())
						+ ","
						+ fixedCosts
						+ ","
						+ a
						+ ","
						+ b
						+ ","
						+ e
						+ ","
						+ u
						+ ","
						+ m
						+ ",'"
						+ getAbsRel(tbU)
						+ "','" + getAbsRel(tbE) + "','" + getAbsRel(tbA) + "');");
				int insertedID = st.getGeneratedKeys().getInt(1);

				TabStartController.olProjects.add(new DBProject(insertedID, tfName
						.getText(), cbType.getValue().toString(), Integer
						.parseInt(tfPeriods.getText()), fixedCosts, Float.valueOf(tfOInv.getText()),
						cbAffectedProcess.getValue(), a,
						b,e,
						u, m,
						getAbsRel(tbU), getAbsRel(tbE), getAbsRel(tbA)));

				//Close the window
				Stage stage = (Stage) btnAddProject.getScene().getWindow();
				stage.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}else{
			Notifications.create()
            .title("Mandatory exception!")
            .text("Not all mandatory fields have been filled out.")
            .showInformation();
		}
		
		
	}
	
	private boolean mandatoryFilled() {
		if(tfName.getText().length() == 0) return false;
		if(tfPeriods.getText().length() == 0) return false;
		if(tfOInv.getText().length() == 0) return false;
		
		return true;
	}

	private String getAbsRel(ToggleButton tb){
		if(tb.isSelected()){
			return "relativ";
		}else{
			return "absolut";
		}
	}
	
}
