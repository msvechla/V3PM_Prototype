package com.v3pm_prototype.view.controller;

import java.io.File;

import com.v3pm_prototype.main.MainApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class V3PMGUIController {
	
	public static final String STATUS_LOADING_START = "Loading configuration file...";
	public static final String STATUS_LOADING_FINISH = "Configuration file loaded.";
	
	@FXML
	private MenuItem menuQuit;
	
	@FXML
	private Label lblStatusText;
	
	@FXML
	private ProgressBar progressBar;
	
	@FXML
	private TabPane tpMain;
	
	@FXML
	private TabStartController tabStartController;
	
	private MainApp mainApp;
	
	public V3PMGUIController(){
		
	}
	
	@FXML
	private void initialize(){
	}
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		tabStartController.setMainApp(mainApp);
	}
	
	public void setStatus(String message){
		this.lblStatusText.setText(message);
	}
	
	public void setProgress(int progress){
		this.progressBar.setProgress(progress);
	}

	public TabStartController getTabStartController() {
		return tabStartController;
	}

	public TabPane getTpMain() {
		return tpMain;
	}

	public void setTpMain(TabPane tpMain) {
		this.tpMain = tpMain;
	}
	
	
	
	
}
