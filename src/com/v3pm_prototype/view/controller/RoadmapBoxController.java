package com.v3pm_prototype.view.controller;

import java.text.DecimalFormat;

import com.sun.javafx.css.converters.PaintConverter;
import com.v3pm_prototype.analysis.Analysis;
import com.v3pm_prototype.analysis.CRAResult;
import com.v3pm_prototype.analysis.CompleteRobustnessAnalysis;
import com.v3pm_prototype.analysis.RobustnessAnalysis;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;
import com.v3pm_prototype.tools.Colorpalette;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Creates the box at the top of the scenario analysis dynamically according to the roadmap
 * @author Marius Svechla
 *
 */
public class RoadmapBoxController {
	@FXML
	private HBox periodLabels;

	@FXML
	private HBox container;

	private RoadMap roadmap;

	public RoadmapBoxController() {
	}

	@FXML
	public void initialize() {

	}

	/**
	 * Generates the layout according to the roadmap
	 * 
	 * @param roadmap
	 * @param config
	 */
	public void generate(RoadMap roadmap, RunConfiguration config, CompleteRobustnessAnalysis cra) {
		this.roadmap = roadmap;

		System.out.println("GENERATE");
		
		for (int period = 0; period < config.getPeriods(); period++) {
			HBox periodBox = new HBox(4);
			periodBox.setStyle(Colorpalette.PERIOD_BOX);
			container.getChildren().add(periodBox);

			for (int slot = 0; slot < config.getSlotsPerPeriod(); slot++) {
				Project project = roadmap.getRMArray()[period][slot];

				if (project != null) {
					StackPane projectBox = new StackPane();
					projectBox.setPrefSize(35, 35);
					Circle projectCircle = new Circle(22);
					projectBox.getChildren().add(projectCircle);

					if (config.getColorID(project) < Colorpalette.PROJECT.length) {
						projectCircle.setFill(Color
								.valueOf(Colorpalette.PROJECT[config
										.getColorID(project)]));
						projectCircle.setStroke(Color.LIGHTGRAY);
						projectCircle.setStrokeWidth(1);
					} else {
						projectCircle.setFill(Color.valueOf("#336699"));
						projectCircle.setStroke(Color.LIGHTGRAY);
						projectCircle.setStrokeWidth(1);
					}

					Label lbl = new Label(project.getName());
					lbl.setPadding(new Insets(6));
					lbl.setWrapText(true);
					lbl.setTextAlignment(TextAlignment.CENTER);
					lbl.setFont(Font.font("System", FontWeight.BOLD, 10));
					lbl.setTextFill(Color.WHITE);
					projectBox.getChildren().add(lbl);
					lbl.setTooltip(generateToolTip(project,cra,config));
					periodBox.getChildren().add(projectBox);
				} else {
					StackPane projectBox = new StackPane();
					projectBox.setPrefSize(35, 35);
					Circle projectCircle = new Circle(22);
					projectCircle.setStroke(Color.LIGHTGRAY);
					projectCircle.setFill(Color.TRANSPARENT);
					projectCircle.setStrokeWidth(1);
					projectBox.getChildren().add(projectCircle);

					periodBox.getChildren().add(projectBox);
				}
			}

			// Add a label for each period
			HBox periodLabel = new HBox(8);
			periodLabels.getChildren().add(periodLabel);
			Label lbl = new Label("Period "+String.valueOf(period));
			lbl.setFont(Font.font("System", 10));
			lbl.setPrefWidth(94);
			lbl.setAlignment(Pos.CENTER);
			periodLabel.getChildren().add(lbl);

		}
	}
	
	private Tooltip generateToolTip(Project project, CompleteRobustnessAnalysis cra, RunConfiguration config) {
		Tooltip toolTip = new Tooltip();
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#,###.00 €");

		Project projectEndVal = null;

		for (Project p : roadmap.getLstProjectCalculated()) {
			if (p.getId() == project.getId()) {
				projectEndVal = p;
				break;
			}
		}

		double deltaOInv = projectEndVal.getOinv() - project.getOinv();
		String modifier = null;

		if (deltaOInv < 0) {
			modifier = " ";
		} else {
			modifier = " +";
		}

		sb.append("Periods:\t" + project.getNumberOfPeriods() + "\n");
		sb.append("Type:\t" + project.getType() + "\n");
		if(project.getType().equals(Project.TYPE_PROCESSLEVEL)){
			sb.append("Process:\t" + config.getProject(project.getI()) + "\n");
		}
		sb.append("OInv:\t" + df.format(projectEndVal.getOinv()) + "  ("
				+ modifier + df.format(deltaOInv) + ")\n\n");
		sb.append(Project.FX_READABLE_A+": " + project.getA() + "\n");
		sb.append(Project.FX_READABLE_B+": " + project.getB() + "\n");
		sb.append(Project.FX_READABLE_E+": " + project.getE() + "\n");
		sb.append(Project.FX_READABLE_U+": " + project.getU() + "\n");
		sb.append(Project.FX_READABLE_M+": " + project.getM() + "\n\n");
		
		
		//Add info from the robustness Analysis
		if(cra != null){
			sb.append("Robustness Info: \n");
			for(CRAResult craResult: cra.getLstResults()){
				if(craResult.getObject() instanceof Project){
					if(((Project)craResult.getObject()).getId()== project.getId()){
						//add info for every calculated parameter
						sb.append(Analysis.mapToReadableParameter(craResult.getSelectedParameter().getName()) + ": "+(craResult.getPercentage()*100)+" %\n");
					}
				}
			}
		}
		
		toolTip.setText(sb.toString());
		
		return toolTip;
	}

}
