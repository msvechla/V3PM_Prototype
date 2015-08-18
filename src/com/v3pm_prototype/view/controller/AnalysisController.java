package com.v3pm_prototype.view.controller;

import com.v3pm_prototype.analysis.RobustnessAnalysis;
import com.v3pm_prototype.main.V3PM_Prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

/**
 * 
 * @author Marius Svechla
 *
 */
public abstract class AnalysisController {
	
	@FXML
	protected TextField tfRadius;
	
	@FXML
	protected TextField tfStep;
	
	@FXML
	protected ChoiceBox<String> cbAbsRel;
	protected ObservableList<String> olAbsRel = FXCollections.observableArrayList();
	
	@FXML
	protected ChoiceBox<String> cbModus;
	protected ObservableList<String> olModus = FXCollections.observableArrayList();
	
	@FXML
	protected Button btnStartAnalysis;
	
	@FXML
	protected ProgressIndicator piSolution;
	
	protected V3PM_Prototype mainApp;
	protected TabScenarioCalculationController tsc;
	
	public AnalysisController() {
	}
	
	@FXML
	public void initialize(){
		initCBModus();
		initCBAbsRel();
	}
	
	private void initCBModus() {
		cbModus.setItems(olModus);
		olModus.add(RobustnessAnalysis.MODE_PLUSMINUS);
		olModus.add(RobustnessAnalysis.MODE_PLUS);
		olModus.add(RobustnessAnalysis.MODE_MINUS);	
		cbModus.getSelectionModel().select(olModus.get(0));
	}
	
	private void initCBAbsRel(){
		cbAbsRel.setItems(olAbsRel);
		olAbsRel.add(RobustnessAnalysis.RELATIVE);
		olAbsRel.add(RobustnessAnalysis.ABSOLUT);
		cbAbsRel.getSelectionModel().select(olAbsRel.get(0));
	}
	
	public V3PM_Prototype getMainApp() {
		return mainApp;
	}

	public void setMainApp(V3PM_Prototype mainApp) {
		this.mainApp = mainApp;
	}

	public TabScenarioCalculationController getTsc() {
		return tsc;
	}

	public void setTsc(TabScenarioCalculationController tsc) {
		this.tsc = tsc;
	}
	
}
