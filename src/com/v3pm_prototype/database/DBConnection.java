package com.v3pm_prototype.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles connections to the database, Singleton Pattern
 * @author Marius Svechla
 *
 */
public class DBConnection {
	private static DBConnection instance;
	
	private Connection connection;
	
	private DBConnection(){
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			// create a database connection
		    connection = DriverManager.getConnection("jdbc:sqlite::resource:com/v3pm_prototype/database/V3PMDB.db");
		    
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	};
	
	public static DBConnection getInstance(){
		if(DBConnection.instance == null){
			DBConnection.instance = new DBConnection();
		}
		return DBConnection.instance;
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
}
