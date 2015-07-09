package com.v3pm_prototype.view;

import java.util.List;
import java.util.concurrent.ExecutorCompletionService;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class TabStartController implements EventHandler<ActionEvent>{

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
			
			//:::::::::: START BUTTON BEHAVIOUR ::::::::::
			if(button == btnStartCalc){
				//Update Statusbar
				this.mainApp.getV3pmGUIController().setProgress(-1);
				this.mainApp.getV3pmGUIController().setStatus("Generating Roadmaps...");
				
				//Create a Service that executes the RMGenerator Task and start it
				Service<?> service = new Service(){
					@Override
					protected Task<List<RoadMap>> createTask() {
						return new RMGenerator(RunConfiguration.standardConfig);
					}
				};
				
				//When the task is succeeded update Statusbar
				service.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
					@Override
					public void handle(WorkerStateEvent event) {
						System.out.println("SUCCEEDED");
						MainApp.instance.getV3pmGUIController().setProgress(0);
						MainApp.instance.getV3pmGUIController().setStatus("Roadmaps generated.");
					}
				});
				service.start();
				
			}
		}
		
	}
	
}
