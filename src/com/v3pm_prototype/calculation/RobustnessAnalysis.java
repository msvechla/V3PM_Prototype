package com.v3pm_prototype.calculation;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class RobustnessAnalysis extends Task {

	public static final String MODE_PLUS = "MODE_PLUS";
	public static final String MODE_MINUS = "MODE_MINUS";
	public static final String MODE_PLUSMINUS = "MODE_PLUSMINUS";
	
	private List<RoadMap> lstRoadmap;
	private RunConfiguration config;
	private ObservableList<List<RoadMap>> lstResults = FXCollections
			.observableArrayList();
	private String mode;
	private RoadMap rmOld;

	public RobustnessAnalysis(List<RoadMap> lstRoadmap, RunConfiguration config, String mode) {
		super();
		this.lstRoadmap = new ArrayList<RoadMap>();
		this.lstRoadmap.addAll(lstRoadmap);
		this.rmOld = lstRoadmap.get(0);
		this.config = config;
		this.mode = mode;
	}

	@Override
	protected Object call() throws Exception {
		double radius = 0.07;
		double step = 0.005;

		double start = config.getDiscountRate();

		for (double i = step; i <= radius; i += step) {

			// positive radius
			if(mode.equals(MODE_PLUS) || mode.equals(MODE_PLUSMINUS)){
				config.setDiscountRate(start + i);
				Calculator c = new Calculator(lstRoadmap, config);
				lstResults.add(c.start());
				System.out.println("Rate: " + config.getDiscountRate() + "\n" 
						+ lstResults.get(lstResults.size() - 1).get(0) + " NPV: "
						+ lstResults.get(lstResults.size() - 1).get(0).getNpv());
			}
			

			// negative radius
			if(mode.equals(MODE_MINUS) || mode.equals(MODE_PLUSMINUS)){
				config.setDiscountRate(start - i);
				Calculator c = new Calculator(lstRoadmap, config);
				lstResults.add(c.start());
				System.out.println("Rate: " + config.getDiscountRate() + "\n"
						+ lstResults.get(lstResults.size() - 1).get(0) + " NPV: "
						+ lstResults.get(lstResults.size() - 1).get(0).getNpv());
			}
			
		}

		RoadMap rmNewBest = getNewBest();
		 if(rmOld.equals(rmNewBest)){
			 System.out.println("ES WURDE KEINE BESSERE ROADMAP GEFUNDEN. ROADMAP ROBUST");
		 }else{
			 System.out.println("NEUE BESTE LÖSUNG GEFUNDEN MIT: "+rmNewBest);
		 }

		return null;
	}

	private RoadMap getNewBest() {

		for (List<RoadMap> rmList : lstResults) {
			RoadMap rmCurrent = rmList.get(0);

			if (!rmCurrent.equals(rmOld)) {
				System.out.println("NOT EQUAL");
				return rmCurrent;
			}
		}
		return rmOld;
	}

}
