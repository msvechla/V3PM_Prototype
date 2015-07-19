package com.v3pm_prototype.view;

import java.text.DecimalFormat;

import com.sun.javafx.css.converters.PaintConverter;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class RoadmapBoxController {
	@FXML
	private HBox periodLabels;
	
	@FXML
	private HBox container;
	
	private RoadMap roadmap;
	
	public RoadmapBoxController() {
	}
	
	@FXML
	public void initialize(){
		
	}
	
	/**
	 * Generates the layout according to the roadmap
	 * @param roadmap
	 * @param config
	 */
	public void generate(RoadMap roadmap, RunConfiguration config){
		this.roadmap = roadmap;
		
		for(int period = 0; period < config.getPeriods(); period++){
			HBox periodBox = new HBox(4);
			container.getChildren().add(periodBox);
			
			
			for(int slot = 0; slot < config.getSlotsPerPeriod(); slot++){
				Project project = roadmap.getRMArray()[period][slot];
				
				if(project != null){
					HBox projectBox = new HBox();
					projectBox.setPrefSize(48, 48);
					projectBox.setPadding(new Insets(2));
					
					if (project.getId() < Colorpalette.PROJECT.length) {
						projectBox.setStyle("-fx-background-color: "
								+ Colorpalette.PROJECT[project.getId()]
								+ ";-fx-border-color: lightgray;\r\n"
								+ "    -fx-border-width: 1px;");
					} else {
						projectBox
								.setStyle("-fx-background-color: #336699;-fx-border-color: lightgray;\r\n"
										+ "    -fx-border-width: 1px;");
					}
					
					Label lbl = new Label(project.getName());
					lbl.setWrapText(true);
					lbl.setFont(Font.font("System", FontWeight.BOLD, 14));
					lbl.setTextFill(Color.WHITE);
					projectBox.getChildren().add(lbl);
					lbl.setTooltip(generateToolTip(project));
					periodBox.getChildren().add(projectBox);
				}else{
					HBox projectBox = new HBox();
					projectBox.setPrefSize(48, 48);
						projectBox.setStyle("-fx-border-color: lightgray;\r\n" + 
								"    -fx-border-width: 1px;");
					
					periodBox.getChildren().add(projectBox);
				}
			}
			
			//Add a label for each period
			HBox periodLabel = new HBox(4);
			periodLabels.getChildren().add(periodLabel);
			periodLabel.setPrefSize(100, 16);
			Label lbl = new Label(String.valueOf(period));
			lbl.setPrefWidth(100);
		    lbl.setAlignment(Pos.TOP_CENTER);
			periodLabel.getChildren().add(lbl);
			
			
		}
	}
	
	private Tooltip generateToolTip(Project project){
		Tooltip toolTip = new Tooltip();
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#,###.00 €");
		
		Project projectEndVal = null;
		
		for(Project p : roadmap.getLstProjectCalculated()){
			if(p.getId() == project.getId()){
				projectEndVal = p;
				break;
			}
		}
		
		double deltaOInv = projectEndVal.getOinv() - project.getOinv();
		String modifier = null;
		
		if(deltaOInv <0){
			modifier = " ";
		}else{
			modifier = " +";
		}
		
		sb.append("Periods: "+project.getNumberOfPeriods() + "\n");
		sb.append("Type: "+project.getType() + "\n");
		sb.append("OInv new: "+df.format(projectEndVal.getOinv()) +"  ("+modifier+df.format(deltaOInv)+"€)\n");
		sb.append("Fixed Costs: "+df.format(project.getFixedCosts()) + "\n");
		
		toolTip.setText(sb.toString());
		
		return toolTip;
	}
	
}
