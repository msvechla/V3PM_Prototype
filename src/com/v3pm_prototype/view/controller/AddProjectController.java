package com.v3pm_prototype.view.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBProject;

/**
 * 
 * @author Marius Svechla
 *
 */
public class AddProjectController {
	
	@FXML
	private VBox mainBox;
	
	//General Infos
	@FXML
	private Label lblHeading;
	@FXML
	private TextField tfName;
	
	@FXML
	private TextField tfPeriods;
	@FXML
	private ComboBox<String> cbType;
	@FXML
	private ComboBox<DBProcess> cbAffectedProcess;
	
	//General Settings
	@FXML
	private TextField tfOInv;
	@FXML
	private TextField tfFixedCosts;
	
	
	//Effect Settings
	@FXML
	private TextField tfA;
	@FXML
	private TextField tfB;
	@FXML
	private TextField tfE;
	@FXML
	private TextField tfU;
	@FXML
	private TextField tfM;
	
	@FXML
	private ToggleButton tbA;
	@FXML
	private ToggleButton tbE;
	@FXML
	private ToggleButton tbU;
	@FXML
	private ToggleButton tbM;
	
	@FXML
	private AnchorPane apA;
	@FXML
	private AnchorPane apB;
	@FXML
	private AnchorPane apE;
	@FXML
	private AnchorPane apU;
	@FXML
	private AnchorPane apM;
	
	@FXML
	private Button btnAddProject;
	
	private TabStartController tsc;
	
	private ObservableList<DBProcess> availableProcesses = FXCollections.observableArrayList();
	private ObservableList<String> projectTypes = FXCollections.observableArrayList("processLevel","bpmLevel");
	
	private ValidationSupport validationSupport;
	
	private boolean isEdit = false;
	private DBProject blueprint = null;
	
	public AddProjectController(){
		validationSupport = new ValidationSupport();
	}
	
	@FXML
	public void initialize(){
		cbAffectedProcess.setItems(availableProcesses);

		cbType.setItems(projectTypes);
		cbType.setValue(projectTypes.get(0));
		
		//Update the available input field for process / bpm level
		cbType.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateType();
			}
		});
		
		initValidation();
	}


	public void updateType() {
		String type = cbType.getValue();
		double standardHeight = apM.getHeight();
		
		if(type.equals("processLevel")){
			//Hide b
			apB.setVisible(false);
			apB.setPrefHeight(0);
			tfB.setText("0");
			
			//enable a
			tbA.setDisable(false);
			tbA.setSelected(false);
			
			//Show e & u
			apE.setVisible(true);
			apE.setPrefHeight(standardHeight);
			tfE.setText("0");
			apU.setVisible(true);
			apU.setPrefHeight(standardHeight);
			tfU.setText("0");
			
			//Update available processes
			availableProcesses.clear();
			availableProcesses.addAll(tsc.olProcesses);
			if(availableProcesses.size()>0){
				cbAffectedProcess.setValue(availableProcesses.get(0));
			}
			
		}else{
			//Show b
			apB.setVisible(true);
			apB.setPrefHeight(standardHeight);
			tfB.setText("0");
			
			//disable a
			tbA.setSelected(true);
			tbA.setDisable(true);
			
			//Hide e & u
			apE.setVisible(false);
			apE.setPrefHeight(0);
			tfE.setText("0");
			apU.setVisible(false);
			apU.setPrefHeight(0);
			tfU.setText("0");
			
			//Update available processes
			availableProcesses.clear();
			DBProcess allProcess = new DBProcess(DBProcess.ID_ALLPROCESSES, DBProcess.NAME_ALLPROCESSES, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "");
			availableProcesses.add(allProcess);
			cbAffectedProcess.setValue(availableProcesses.get(0));
			
		}
		mainBox.autosize();
	}

	public void setTSC(TabStartController tsc){
		this.tsc = tsc;
		availableProcesses.addAll(tsc.olProcesses);
		if(availableProcesses.size()>0){
			cbAffectedProcess.setValue(availableProcesses.get(0));
		}
	}
	
	/**
	 * Writes the new 
	 */
	public void createProject(){
		
		if(!validationSupport.isInvalid()){
			Connection conn = DBConnection.getInstance().getConnection();
			
			try {
				
				float fixedCosts = 0;
				if(tfFixedCosts.getText().length() != 0){
					fixedCosts = Float.valueOf(tfFixedCosts.getText());
				}
				float a = 0;
				if(tfA.getText().length() != 0){
					a = Float.valueOf(tfA.getText());
				}
				float b = 0;
				if(tfB.getText().length() != 0){
					b = Float.valueOf(tfB.getText());
				}
				float e = 0;
				if(tfE.getText().length() != 0){
					e = Float.valueOf(tfE.getText());
				}
				float u = 0;
				if(tfU.getText().length() != 0){
					u = Float.valueOf(tfU.getText());
				}
				float m = 0;
				if(tfM.getText().length() != 0){
					m = Float.valueOf(tfM.getText());
				}
				
				Statement st = conn.createStatement();
				
				if (isEdit) {
					
					String query = "UPDATE PROJECT SET name='"
							+ tfName.getText()
							+ "', type='"
							+ cbType.getValue()
							+ "', periods="
							+ tfPeriods.getText()
							+ ", processID="
							+ cbAffectedProcess.getValue().getId()
							+ ", oInv="
							+ Float.valueOf(tfOInv.getText())
							+ ", fixedCosts=" + fixedCosts
							+ ", a=" + a + ", b=" + b + ", e=" + e
							+ ", u=" + u + ", m=" + m
							+ ", absrelQ='" + getAbsRel(tbU) + "', absrelT='" + getAbsRel(tbE)
							+ "', absrelOop='" + getAbsRel(tbA) + "' WHERE id="
							+ blueprint.getId();
					
					System.out.println("[SQLITE] "+query);
					
					st.executeUpdate(query);
					
					//Update the process in the list
					blueprint.setName(tfName.getText());
					blueprint.setType(cbType.getValue());
					blueprint.setPeriods(Integer.valueOf(tfPeriods.getText()));
					blueprint.setProcess(cbAffectedProcess.getValue());
					blueprint.setOInv(Float.valueOf(tfOInv.getText()));
					blueprint.setFixedCosts(fixedCosts);
					blueprint.setA(a);
					blueprint.setB(b);
					blueprint.setE(e);
					blueprint.setU(u);
					blueprint.setM(m);
					blueprint.setAbsRelQ(getAbsRel(tbU));
					blueprint.setAbsRelT(getAbsRel(tbE));
					blueprint.setAbsRelOop(getAbsRel(tbA));
					
				} else {

					st.executeUpdate("INSERT INTO Project(name, type, periods, processID, oInv, fixedCosts, a, b, e, u, m, absrelQ, absrelT, absrelOop) VALUES ('"
							+ tfName.getText()
							+ "', '"
							+ cbType.getValue()
							+ "',"
							+ tfPeriods.getText()
							+ ","
							+ cbAffectedProcess.getValue().getId()
							+ ","
							+ Float.valueOf(tfOInv.getText())
							+ ","
							+ fixedCosts
							+ ","
							+ a
							+ ","
							+ b
							+ ","
							+ e
							+ ","
							+ u
							+ ","
							+ m
							+ ",'"
							+ getAbsRel(tbU)
							+ "','"
							+ getAbsRel(tbE) + "','" + getAbsRel(tbA) + "');");
					int insertedID = st.getGeneratedKeys().getInt(1);

					TabStartController.olProjects.add(new DBProject(insertedID,
							tfName.getText(), cbType.getValue().toString(),
							Integer.parseInt(tfPeriods.getText()), fixedCosts,
							Float.valueOf(tfOInv.getText()), cbAffectedProcess
									.getValue(), a, b, e, u, m, getAbsRel(tbU),
							getAbsRel(tbE), getAbsRel(tbA)));
				}
				
				//Close the window
				Stage stage = (Stage) btnAddProject.getScene().getWindow();
				stage.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}else{
			Notifications.create()
            .title("Mandatory exception!")
            .text("Not all mandatory fields have been filled out.")
            .showInformation();
		}
		
		
	}
	
	/**
	 * Fills everything with information when editing a process
	 * @param blueprint
	 */
	public void setBlueprint(DBProject blueprint){
		this.blueprint = blueprint;
		this.isEdit = true;
		
		tfName.setText(blueprint.getName());
		cbType.getSelectionModel().select(blueprint.getType());
		tfPeriods.setText(String.valueOf(blueprint.getPeriods()));
		cbAffectedProcess.getSelectionModel().select(blueprint.getProcess());
		tfOInv.setText(String.valueOf(blueprint.getOInv()));
		tfFixedCosts.setText(String.valueOf(blueprint.getFixedCosts()));
		tfA.setText(String.valueOf(blueprint.getA()));
		tfB.setText(String.valueOf(blueprint.getB()));
		tfE.setText(String.valueOf(blueprint.getE()));
		tfU.setText(String.valueOf(blueprint.getU()));
		tfM.setText(String.valueOf(blueprint.getM()));
		tbU.setSelected(parseAbsRel(blueprint.getAbsRelQ()));
		tbE.setSelected(parseAbsRel(blueprint.getAbsRelT()));
		tbA.setSelected(parseAbsRel(blueprint.getAbsRelOop()));
		
		lblHeading.setText("Editing Project");
		btnAddProject.setText("Save Changes");
	}
	
	private void initValidation(){
		validationSupport.registerValidator(tfName, Validator.createEmptyValidator("Name is required"));
		validationSupport.registerValidator(tfPeriods, Validator.createEmptyValidator("Number of Periods are required"));
		validationSupport.registerValidator(tfOInv, Validator.createEmptyValidator("Investment Outflows are required"));
		validationSupport.initInitialDecoration();
	}

	private boolean parseAbsRel(String value){
		if(value.equals("relativ")){
			return true;
		}else{
			return false;
		}
	}
	
	private String getAbsRel(ToggleButton tb){
		if(tb.isSelected()){
			return "relativ";
		}else{
			return "absolut";
		}
	}
	
}
