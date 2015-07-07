package com.v3pm_prototype.view;

import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;
import com.v3pm_prototype.utility.ThreadCompleteListener;

public class TabStartController implements EventHandler<ActionEvent>, ThreadCompleteListener{

	private MainApp mainApp;
	
	@FXML
	private Label lblSolution;
	
	@FXML
	private Label lblNPV;
	
	@FXML
	private Button btnStartCalc;
	
	public TabStartController() {
		
	}
	
	@FXML
	private void initialize(){
		btnStartCalc.setOnAction(this);
	}
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
	
	public void displaySolution (int NPV, int countGenerated){
		this.lblSolution.setText(countGenerated + " Roadmaps were generated using this configuration. The optimal solution yields a NPV of "+NPV+"€.");
		this.lblNPV.setText(String.valueOf(NPV));
	}

	@Override
	public void handle(ActionEvent event) {
		
		if(event.getSource() instanceof Button){
			Button button = (Button) event.getSource();
			
			if(button == btnStartCalc){
				RMGenerator rmGenerator = new RMGenerator(RunConfiguration.standardConfig, this.mainApp);
				rmGenerator.addListener(this);
				
				this.mainApp.getV3pmGUIController().setProgress(-1);
				this.mainApp.getV3pmGUIController().setStatus("Generating Roadmaps...");
				rmGenerator.start();
			}
		}
		
	}

	@Override
	public void onThreadFinish(Thread thread) {
		
		Platform.runLater(new Runnable(){
			
			@Override
			public void run() {
				MainApp.instance.getV3pmGUIController().setProgress(0);
				MainApp.instance.getV3pmGUIController().setStatus("Roadmaps generated.");
			}
			
		});
		
		
	}
	
}
