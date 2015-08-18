package com.v3pm_prototype.analysis;

import javafx.scene.control.ProgressIndicator;

import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.exceptions.NoValidThetaIDException;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * Parent Class for various analysis techniques
 * @author Marius Svechla
 *
 */
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
	
	public abstract void start() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, NoValidThetaIDException;
	
	public static String mapToCodeParameter(String parameterReadable){
		switch(parameterReadable){
		case Project.FX_READABLE_A:
			return "a";
		case Project.FX_READABLE_B:
			return "b";
		case Project.FX_READABLE_E:
			return "e";
		case Project.FX_READABLE_U:
			return "u";
		case Project.FX_READABLE_M:
			return "m";
		case RunConfiguration.FX_READABLE_DISCOUNTRATE:
			return "discountRate";
		case RunConfiguration.FX_READABLE_OOAFIXED:
			return "oOAFixed";
		case Process.FX_READABLE_P:
			return "p";
		case Process.FX_READABLE_OOP:
			return "oop";
		case Process.FX_READABLE_FIXEDCOSTS:
			return "fixedCosts";
		case Process.FX_READABLE_Q:
			return "q";
		case Process.FX_READABLE_T:
			return "t";
		
		}
		
		return null;
	}
	
	public static String mapToReadableParameter(String parameterCode){
		switch(parameterCode){
		case "a":
			return  Project.FX_READABLE_A;
		case "b":
			return Project.FX_READABLE_B;
		case "e":
			return Project.FX_READABLE_E;
		case "u":
			return Project.FX_READABLE_U;
		case "m":
			return Project.FX_READABLE_M;
		case "discountRate":
			return RunConfiguration.FX_READABLE_DISCOUNTRATE;
		case "oOAFixed":
			return RunConfiguration.FX_READABLE_OOAFIXED;
		case "p":
			return Process.FX_READABLE_P;
		case "oop":
			return Process.FX_READABLE_OOP;
		case "fixedCosts":
			return Process.FX_READABLE_FIXEDCOSTS;
		case "q":
			return Process.FX_READABLE_Q;
		case "t":
			return Process.FX_READABLE_T;
		
		}
		return null;
	}
	
}
