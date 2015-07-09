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

public class NewProcessController {
	@FXML
	private Button btnCreate;
	@FXML
	private TextField tfName;
	
	public NewProcessController(){
		
	}
	
	@FXML
	public void initialize(){
	
	}
	
	/**
	 * Writes the new Project into the Database and adds it to the Project List
	 */
	public void createProcess(){
		Connection conn = DBConnection.getInstance().getConnection();
		
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Process(name) VALUES ('"+tfName.getText()+"');");
			int insertedID = st.getGeneratedKeys().getInt(1);
			
			TabStartController.olProcess.add(new DBProcess(insertedID, tfName.getText()));
			
			//Close the window
			Stage stage = (Stage) btnCreate.getScene().getWindow();
			stage.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
