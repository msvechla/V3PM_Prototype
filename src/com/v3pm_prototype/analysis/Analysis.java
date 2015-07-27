package com.v3pm_prototype.analysis;

import javafx.scene.control.ProgressIndicator;

import com.v3pm_prototype.exceptions.NoValidThetaIDException;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public abstract class Analysis {

	public static final String MODE_PLUS = "MODE_PLUS";
	public static final String MODE_MINUS = "MODE_MINUS";
	public static final String MODE_PLUSMINUS = "MODE_PLUSMINUS";
	
	public static final String ABSOLUT = "ABSOLUT";
	public static final String RELATIVE = "RELATIVE";
	
	protected RunConfiguration config;
	protected String mode;
	protected String absRel;
	protected double radius = 0.01;
	protected double step = 0.001;
	
	protected ProgressIndicator piSolution;

	public Analysis(RunConfiguration config, String mode, String absRel,
			double radius, double step, ProgressIndicator piSolution) {
		super();
		this.config = (RunConfiguration) config.clone();
		this.mode = mode;
		this.absRel = absRel;
		this.radius = radius;
		this.step = step;
		this.piSolution = piSolution;
	}
	
	public abstract void start() throws IllegalArgumentException, IllegalAccessException, NoValidThetaIDException, NoSuchFieldException, SecurityException;
	
}
