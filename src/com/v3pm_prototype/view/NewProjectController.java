package com.v3pm_prototype.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;

public class NewProjectController {
	@FXML
	private Button btnCreate;
	@FXML
	private TextField tfName;
	@FXML
	private ChoiceBox<String> cbType;
	@FXML
	private ChoiceBox<DBProcess> cbProcess;
	@FXML
	private TextField tfPeriods;
	
	private ObservableList<DBProcess> availableProcesses = FXCollections.observableArrayList();
	
	public NewProjectController(){
		
	}
	
	@FXML
	public void initialize(){
		//Set values for the type combobox
		ObservableList<String> typeData = FXCollections.observableArrayList("processLevel","bpmLevel");
		cbType.setItems(typeData);
		cbType.setValue("processLevel");
		
		availableProcesses.add(new DBProcess(DBProcess.ID_EMPTYPROCESS, DBProcess.NAME_EMPTYPROCESS));
		availableProcesses.addAll(TabStartController.olProcess);
		cbProcess.setItems(availableProcesses);
		cbProcess.setValue(availableProcesses.get(0));
	}
	
	/**
	 * Writes the new Project into the Database and adds it to the Project List
	 */
	public void createProject(){
		Connection conn = DBConnection.getInstance().getConnection();
		
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Project(name, type, periods,processID) VALUES ('"+tfName.getText()+"', '"+cbType.getValue()+"',"+tfPeriods.getText()+","+cbProcess.getValue().getId()+");");
			int insertedID = st.getGeneratedKeys().getInt(1);
			
			TabStartController.olProject.add(new DBProject(insertedID, tfName.getText(), cbType.getValue().toString(), Integer.parseInt(tfPeriods.getText()),cbProcess.getValue()));
			
			//Close the window
			Stage stage = (Stage) btnCreate.getScene().getWindow();
			stage.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
