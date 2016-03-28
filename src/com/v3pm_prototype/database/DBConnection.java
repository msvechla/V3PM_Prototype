package com.v3pm_prototype.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.v3pm_prototype.main.V3PM_Prototype;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Handles connections to the database, Singleton Pattern
 * 
 * @author Marius Svechla
 *
 */

public class DBConnection {

	public static String CONFIG_PATH = "V3PMConfig.conf";
	public static String DB_PATH = "V3PMDB.db";

	private static DBConnection instance;

	private Connection connection;

	private DBConnection() {

		try {
			Class.forName("org.sqlite.JDBC");

			File dbFile = new File(loadConfigs());
			if (!dbFile.exists()) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Database file not found");
				alert.setHeaderText("Database file not found at the default location");
				alert.setContentText("There was no database file found at the working directory. Please specify the location of the file.");
				alert.showAndWait();

				openDBFileDialog();
				dbFile = new File(loadConfigs());
			}
			
			if(dbFile.exists()){
				String connString = "jdbc:sqlite:"
						+ dbFile.getAbsolutePath().replace("\\", "\\\\");
				
				// create a database connection
				connection = DriverManager.getConnection(connString);
			}else{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Database file not found");
				alert.setHeaderText("Database file not found at the default location");
				alert.setContentText("There was no database file found at the working directory. Please specify the location of the file via the Menu.");
				alert.showAndWait();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	};

	public void openDBFileDialog() {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select the Database File");
			fileChooser.getExtensionFilters().add(
					new ExtensionFilter("Database Files", "*.db", "*.sqlite"));

			File newDBFileLocation = fileChooser
					.showOpenDialog(V3PM_Prototype.instance.getPrimaryStage());
			
			// Write the new location to the config file
			FileWriter fw = new FileWriter(CONFIG_PATH);
			DB_PATH = newDBFileLocation.getAbsolutePath();
			fw.write("CONFIG_PATH:" + newDBFileLocation.getAbsolutePath());
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static DBConnection getInstance() {
		if (DBConnection.instance == null) {
			DBConnection.instance = new DBConnection();
		}
		return DBConnection.instance;
	}

	public Connection getConnection() {
		return this.connection;
	}

	/**
	 * Loads configurations from file (currently only DBPath and returns the DBPath)
	 * @return Path to the database file
	 */
	private static String loadConfigs() {
		try {

			// Check if the config file is present, otherwise create it
			File test = new File(CONFIG_PATH);
			if (!test.exists()) {
				writeDBPath();
			}

			// Read the config file
			FileReader configFile = new FileReader(CONFIG_PATH);
			BufferedReader br = new BufferedReader(configFile);

			String configLine;
			while ((configLine = br.readLine()) != null) {

				// Get the DB Path
				if (configLine.startsWith("CONFIG_PATH:")) {
					String dbPath = configLine.substring(12,
							configLine.length());
					DB_PATH = dbPath;
					return dbPath;
				}

			}

			writeDBPath();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DB_PATH;
	}

	/**
	 * Writes the DBPath to the config file
	 * @throws IOException
	 */
	private static void writeDBPath() throws IOException {
		FileWriter fw = new FileWriter(CONFIG_PATH);

		// Standard DB Path is in the same folder as the jar file,
		// therefore write relative path
		fw.write("CONFIG_PATH:" + DB_PATH);
		fw.close();
	}

}
