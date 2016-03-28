package com.v3pm_prototype.view.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.main.V3PM_Prototype;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 * @author Marius Svechla
 *
 */
public class V3PMGUIController {
	
	public static final String STATUS_LOADING_START = "Loading configuration file...";
	public static final String STATUS_LOADING_FINISH = "Configuration file loaded.";
	
	@FXML
	private MenuItem menuQuit;
	
	@FXML
	private MenuItem menuSettings;
	
	@FXML
	private MenuItem menuSetDB;
	
	@FXML
	private Label lblStatusText;
	
	@FXML
	private ProgressBar progressBar;
	
	@FXML
	private TabPane tpMain;
	
	@FXML
	private TabStartController tabStartController;
	
	private V3PM_Prototype mainApp;
	
	public V3PMGUIController(){
		
	}
	
	@FXML
	private void initialize(){
		initSettings();
		initSetDB();
	}
	
	private void initSettings(){
		menuSettings.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Load root layout from fxml file.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(V3PM_Prototype.class
						.getResource("/com/v3pm_prototype/view/Settings.fxml"));
				VBox root;
				try {
					root = (VBox) loader.load();

					SettingsController controller = loader.getController();

					// Show the scene containing the root layout.
					Stage stage = new Stage();
					stage.setTitle("Settings");
					stage.setScene(new Scene(root));
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	private void initSetDB(){	
		// Update menu item text
		setSetDBName();
		
		menuSetDB.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// Open the file dialog and write new path to config
				DBConnection.getInstance().openDBFileDialog();
				
				// Update menu item text
				setSetDBName();
				
				//Open dialog to prompt the user to restart the application
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Notification");
				alert.setHeaderText("Changes require application restart");
				alert.setContentText("Your settings have been saved. Please restart the application for the changes to take effect.");
				alert.showAndWait();
				
			}
		});
	}
	
	/**
	 * Updates the text of the menu item and adds the current path
	 */
	private void setSetDBName(){
		Path path = new File(DBConnection.DB_PATH).toPath();
		final int len = path.getNameCount();	
		
		if (len >= 2){
			menuSetDB.setText("Set Database... | "+path.subpath(len - 2, len));
		}else{
			menuSetDB.setText("Set Database... | "+DBConnection.DB_PATH);
		}
		
	}
	
	public void setMainApp(V3PM_Prototype mainApp){
		this.mainApp = mainApp;
		tabStartController.setMainApp(mainApp);
	}
	
	public void setStatus(String message){
		this.lblStatusText.setText(message);
	}
	
	public void setProgress(int progress){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				progressBar.setProgress(progress);
			}
		});
		
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
