package com.v3pm_prototype.main;


import java.io.IOException;
import java.util.ArrayList;

import com.v3pm_prototype.view.controller.V3PMGUIController;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main entry class of the application
 * @author Marius Svechla
 *
 */
public class V3PM_Prototype extends Application {

	private Stage primaryStage;
	private VBox rootLayout;
	private V3PMGUIController v3pmGUIController;
	public static V3PM_Prototype instance;
	
	public static final String RELEASE_VERSION = "1.3";
	
	public static ArrayList<Task> lstTasks = new ArrayList<Task>();
	
	@Override
	public void start(Stage primaryStage) {
		instance = this;
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("V3PM Prototype V"+RELEASE_VERSION);
		
		try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(V3PM_Prototype.class.getResource("/com/v3pm_prototype/view/V3PMGUI.fxml"));
            rootLayout = (VBox) loader.load();
            
            System.out.println("javafx.runtime.version: " + System.getProperties().get("javafx.runtime.version"));
            
            v3pmGUIController = loader.getController();
            v3pmGUIController.setMainApp(this);
            v3pmGUIController.getTabStartController().setMainApp(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	

	public V3PMGUIController getV3pmGUIController() {
		return v3pmGUIController;
	}
	
}
