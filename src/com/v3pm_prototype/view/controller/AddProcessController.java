package com.v3pm_prototype.view.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.database.DBProcess;

public class AddProcessController {

	// General Settings
	@FXML
	private TextField tfName;
	@FXML
	private TextField tfP;
	@FXML
	private TextField tfOop;
	@FXML
	private TextField tfFixedCosts;
	// Quality Settings
	@FXML
	private Slider slQ;
	@FXML
	private Label lblQ;
	@FXML
	private TextField tfDegQ;
	// Time Settings
	@FXML
	private TextField tfT;
	@FXML
	private TextField tfTMax;
	@FXML
	private TextField tfDegT;
	// Demand Settings
	@FXML
	private TextField tfDMP;
	@FXML
	private TextField tfDMLambda;
	@FXML
	private TextField tfDMAlpha;
	@FXML
	private TextField tfDMBeta;
	@FXML
	private ComboBox<String> cbDMFktQ;
	@FXML
	private ComboBox<String> cbDMFktT;
	@FXML
	private Button btnAddProcess;

	// Contains demand functions
	private ObservableList<String> dmFktQList = FXCollections
			.observableArrayList("0q", "q", "ln q", "e^(1/q)");
	private ObservableList<String> dmFktTList = FXCollections
			.observableArrayList("0t", "t", "ln t", "e^(1/t)");

	private TabStartController tsc;
	
	private ValidationSupport validationSupport;

	public AddProcessController() {
		validationSupport = new ValidationSupport();
	}

	@FXML
	public void initialize() {
		// Set values for the demand function comboboxes
		cbDMFktQ.setItems(dmFktQList);
		cbDMFktQ.setValue(dmFktQList.get(0));

		cbDMFktT.setItems(dmFktTList);
		cbDMFktT.setValue(dmFktTList.get(0));
		
		initValidation();
		
		// Setup quality slider hint
		slQ.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				updateQualityHint();
			}
		});
	}

	public void setTSC(TabStartController tsc) {
		this.tsc = tsc;
	}

	/**
	 * Writes the new
	 */
	public void createProcess() {

		Connection conn = DBConnection.getInstance().getConnection();
		if (!validationSupport.isInvalid()) {

			float fixedCosts = 0;
			if (tfFixedCosts.getText().length() != 0) {
				fixedCosts = Float.valueOf(tfFixedCosts.getText());
			}
			float t = 0;
			if (tfT.getText().length() != 0) {
				t = Float.valueOf(tfT.getText());
			}
			float degQ = 0;
			if (tfDegQ.getText().length() != 0) {
				degQ = Float.valueOf(tfDegQ.getText());
			}
			float degT = 0;
			if (tfDegT.getText().length() != 0) {
				degT = Float.valueOf(tfDegT.getText());
			}
			float dmP = 0;
			if (tfDMP.getText().length() != 0) {
				dmP = Float.valueOf(tfDMP.getText());
			}
			float dmLambda = 0;
			if (tfDMLambda.getText().length() != 0) {
				dmLambda = Float.valueOf(tfDMLambda.getText());
			}
			float dmAlpha = 0;
			if (tfDMAlpha.getText().length() != 0) {
				dmAlpha = Float.valueOf(tfDMAlpha.getText());
			}
			float dmBeta = 0;
			if (tfDMBeta.getText().length() != 0) {
				dmBeta = Float.valueOf(tfDMBeta.getText());
			}

			try {
				Statement st = conn.createStatement();
				st.executeUpdate("INSERT INTO Process(name, p, oop, fixedCosts, q, t, degQ, degT, dmP, dmLambda, dmAlpha, dmBeta, dmFktQ, dmFktT) VALUES ('"
						+ tfName.getText()
						+ "',"
						+ Float.valueOf(tfP.getText())
						+ ","
						+ Float.valueOf(tfOop.getText())
						+ ","
						+ fixedCosts
						+ ","
						+ (float) slQ.getValue()
						+ ","
						+ t
						+ ","
						+ degQ
						+ ","
						+ degT
						+ ","
						+ dmP
						+ ","
						+ dmLambda
						+ ","
						+ dmAlpha
						+ ","
						+ dmBeta
						+ ","
						+ getDMFkt(cbDMFktQ)
						+ "," + getDMFkt(cbDMFktT) + ");");

				int insertedID = st.getGeneratedKeys().getInt(1);

				DBProcess process = new DBProcess(insertedID, tfName.getText(),
						Float.valueOf(tfP.getText()), Float.valueOf(tfOop
								.getText()), fixedCosts,
						(float) slQ.getValue(), degQ, t, degT, dmP, dmLambda,
						dmAlpha, dmBeta, cbDMFktQ.getValue(),
						cbDMFktT.getValue());

				this.tsc.olProcesses.add(process);

				// Close the window
				Stage stage = (Stage) btnAddProcess.getScene().getWindow();
				stage.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Notifications.create().title("Mandatory exception!")
					.text("Not all mandatory fields have been filled out.")
					.showInformation();
		}

	}
	
	private void initValidation(){
		validationSupport.registerValidator(tfName, Validator.createEmptyValidator("Name is required"));
		validationSupport.registerValidator(tfP, Validator.createEmptyValidator("Price is required"));
		validationSupport.registerValidator(tfOop, Validator.createEmptyValidator("Operating Outflows are required"));
		validationSupport.initInitialDecoration();
	}

	public void updateQualityHint() {
		this.lblQ.setText(String.valueOf(this.slQ.getValue() + "%"));
	}

	private int getDMFkt(ComboBox<String> cb) {
		if (cb.getValue().equals("0q") || cb.getValue().equals("0t")) {
			return 0;
		} else if (cb.getValue().equals("q") || cb.getValue().equals("t")) {
			return 1;
		} else if (cb.getValue().equals("ln q") || cb.getValue().equals("ln t")) {
			return 2;
		} else {
			return 3;
		}
	}

}
