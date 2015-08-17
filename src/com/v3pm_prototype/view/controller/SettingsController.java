package com.v3pm_prototype.view.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.v3pm_prototype.database.DBConnection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class SettingsController {
	
	@FXML 
	private CheckBox cbFCRA;
	public static boolean FORCE_CRA = false;
	
	public SettingsController(){
		
	}
	
	@FXML
	public void initialize() {
		try {
			initCBFCRA();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		cbFCRA.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				
				Connection conn = DBConnection.getInstance().getConnection();
				Statement st;
				try {
					st = conn.createStatement();
					if(newValue){
						st.executeUpdate("UPDATE Settings SET boolean = 1 WHERE type = 'FCRA';");
						FORCE_CRA = true;
					}else{
						st.executeUpdate("UPDATE Settings SET boolean = 0 WHERE type = 'FCRA';");
						FORCE_CRA = false;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
			}
		});
		
	}
	
	
	private void initCBFCRA() throws SQLException{
		Connection conn = DBConnection.getInstance().getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM Settings WHERE type = 'FCRA'");

		rs.next();
		int bool = rs.getInt("boolean");
		
		if(bool == 0){
			cbFCRA.setSelected(false);
			FORCE_CRA = false;
		}else{
			cbFCRA.setSelected(true);
			FORCE_CRA = true;
		}
			
	}

}
