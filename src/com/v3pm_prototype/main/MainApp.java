package com.v3pm_prototype.main;


import java.io.IOException;
import com.v3pm_prototype.view.controller.V3PMGUIController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private VBox rootLayout;
	private V3PMGUIController v3pmGUIController;
	public static MainApp instance;
	
	@Override
	public void start(Stage primaryStage) {
		instance = this;
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("V3PM Prototype");
		
		try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/v3pm_prototype/view/v3pmGUI.fxml"));
            rootLayout = (VBox) loader.load();
            
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
