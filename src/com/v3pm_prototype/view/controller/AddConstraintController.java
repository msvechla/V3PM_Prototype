package com.v3pm_prototype.view.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
import com.v3pm_prototype.database.DBConstraint;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBProcess;

/**
 * 
 * @author Marius Svechla
 *
 */
public class AddConstraintController {
	
	private String type;
	private NewScenarioController nsc;
	private TabStartController tsc;
	
	@FXML
	private Label lblType;
	@FXML
	private ComboBox<DBProject> cbS;
	@FXML
	private ComboBox<DBProject> cbSI;
	@FXML
	private ComboBox<DBProcess> cbI;
	@FXML
	private TextField tfX;
	@FXML
	private ComboBox<String> cbY;
	@FXML
	private Label lblComma1;
	@FXML
	private Label lblComma2;
	
	private ObservableList<DBProject> availableS = FXCollections.observableArrayList();
	private ObservableList<DBProject> availableSI = FXCollections.observableArrayList();
	private ObservableList<DBProcess> availableI = FXCollections.observableArrayList();
	private ObservableList<String> availableY = FXCollections.observableArrayList();
	
	public AddConstraintController(){
	}
	
	@FXML
	public void initialize(){
		cbS.setItems(availableS);
		cbSI.setItems(availableSI);
		cbI.setItems(availableI);
		cbY.setItems(availableY);
		
		//Some initialization in setNSC
	}
	
	public void addConstraint(){
		DBConstraint constraint = new DBConstraint(type, cbS.getValue(),
				cbSI.getValue(), cbI.getValue(), Float.valueOf(tfX.getText()),
				cbY.getValue());

		nsc.olConstraints.add(constraint);
		
		//Close the window
		Stage stage = (Stage) tfX.getScene().getWindow();
		stage.close();
	}
	

	//Updates the available SI Projects, according to already chosen Project S
	private void updateAvailableSI(){
		availableSI.addAll(availableS);
		availableSI.remove(cbS.getSelectionModel().getSelectedItem());
	}
	
	//Updates the layout according to the constraint type
	public void updateType() {
		double standardWidth = cbS.getWidth();
		
		//Interactions among projects
		if (type.equals(DBConstraint.TYPE_LOCMUTDEP)
				|| type.equals(DBConstraint.TYPE_LOCMUTEX)
				|| type.equals(DBConstraint.TYPE_GLOMUTDEP)
				|| type.equals(DBConstraint.TYPE_GLOMUTEX)
				|| type.equals(DBConstraint.TYPE_PRESUC)) {

			hideX();
			hideY();
			hideI();
			hideComma2();
		}
		
		//Project-specific constraints I
		if (type.equals(DBConstraint.TYPE_EARLIEST)
				|| type.equals(DBConstraint.TYPE_LATEST)) {
			
			hideSI();
			hideX();
			hideI();
			hideComma2();
		}

		// Project-specific constraints II
		if (type.equals(DBConstraint.TYPE_MANDATORY)) {

			hideSI();
			hideX();
			hideI();
			hideY();
			hideComma1();
			hideComma2();
		}

		// Process-specific constraints 
		if (type.equals(DBConstraint.TYPE_QUALMIN)
				|| type.equals(DBConstraint.Type_TIMEMAX)) {

			hideSI();
			hideS();
		}

		// Period-specific constraints I
		if (type.equals(DBConstraint.TYPE_BUDPRO)) {

			hideSI();
			hideS();
		}
		
		// Period-specific constraints II
		if (type.equals(DBConstraint.TYPE_BUDBPM)
				|| type.equals(DBConstraint.TYPE_BUDGET)
				|| type.equals(DBConstraint.TYPE_NUMPROJ)) {

			hideSI();
			hideS();
			hideI();
			hideComma1();
		}
		
	}
	
	private void hideComma1(){
		lblComma1.setVisible(false);
		lblComma1.setManaged(false);
	}

	private void hideComma2(){
		lblComma2.setVisible(false);
		lblComma2.setManaged(false);
	}
	
	private void hideS(){
		cbS.setVisible(false);
		cbS.setValue(null);
		cbS.setManaged(false);
	}
	
	private void hideSI(){
		cbSI.setVisible(false);
		cbSI.setValue(null);
		cbSI.setManaged(false);
	}
	
	private void hideI(){
		cbI.setVisible(false);
		cbI.setValue(null);
		cbI.setManaged(false);
	}
	
	private void hideX(){
		tfX.setVisible(false);
		tfX.setText("-1");
		tfX.setManaged(false);
	}
	
	private void hideY(){
		cbY.setVisible(false);
		cbY.setValue("-1");
		cbY.setManaged(false);
	}
	
	public void setNSC(NewScenarioController nsc){
		this.nsc = nsc;
		//Setup Project and Process lists
		availableI.addAll(nsc.olProcesses);
		availableS.addAll(nsc.olProjects);
		cbS.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateAvailableSI();
			}
		});
		
		//Setup Period list
		for(int i=0;i<nsc.getPeriods();i++){
			availableY.add(Integer.toString(i));
		}
		
		//Add all Periods
		if (type.equals(DBConstraint.TYPE_QUALMIN)
				|| type.equals(DBConstraint.Type_TIMEMAX)
				|| type.equals(DBConstraint.TYPE_BUDPRO)
				|| type.equals(DBConstraint.TYPE_BUDBPM)
				|| type.equals(DBConstraint.TYPE_BUDGET)
				|| type.equals(DBConstraint.TYPE_NUMPROJ)) {
			availableY.add("ALL");
		}
		
	}
	
	public void setTSC(TabStartController tsc){
		this.tsc = tsc;
	}
	
	public void setType(String type){
		this.type = type;
		this.lblType.setText(type+"( ");
	}
	
	
}
