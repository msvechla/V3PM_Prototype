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
import com.v3pm_prototype.database.DBProject;

public class NewProjectController {
	@FXML
	private Button btnCreate;
	@FXML
	private TextField tfName;
	@FXML
	private ChoiceBox cbType;
	@FXML
	private TextField tfPeriods;
	
	public NewProjectController(){
		
	}
	
	@FXML
	public void initialize(){
		//Set values for the type combobox
		ObservableList<String> typeData = FXCollections.observableArrayList("processLevel","bpmLevel");
		cbType.setItems(typeData);
		cbType.setValue("processLevel");
	}
	
	/**
	 * Writes the new Project into the Database and adds it to the Project List
	 */
	public void createProject(){
		Connection conn = DBConnection.getInstance().getConnection();
		
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Project(name, type, periods) VALUES ('"+tfName.getText()+"', '"+cbType.getValue()+"',"+tfPeriods.getText()+");");
			int insertedID = st.getGeneratedKeys().getInt(1);
			
			TabStartController.olProject.add(new DBProject(insertedID, tfName.getText(), cbType.getValue().toString(), Integer.parseInt(tfPeriods.getText())));
			
			//Close the window
			Stage stage = (Stage) btnCreate.getScene().getWindow();
			stage.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
