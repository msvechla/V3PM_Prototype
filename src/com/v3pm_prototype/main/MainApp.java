package com.v3pm_prototype.main;


import java.io.File;
import java.io.IOException;

import com.v3pm_prototype.excel.ExcelImporter;
import com.v3pm_prototype.rmgeneration.RunConfiguration;
import com.v3pm_prototype.view.V3PMGUIController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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

	/**
	 * Creates the standard configuration from an Excel file
	 * @param excelFile
	 */
	public static void loadConfiguration(File excelFile) {
		try {
			
			RunConfiguration.standardConfig = new RunConfiguration();
			ExcelImporter.importAllExcelData(excelFile, RunConfiguration.standardConfig);
		
		} catch (Exception e) {
			System.err.println("Fehler beim Excel-Import");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
}
