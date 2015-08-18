package com.v3pm_prototype.analysis;

import java.lang.reflect.Field;

/**
 * Stores the Results of a Complete Robustness Analysis
 * Much more efficient than storing everything
 * @author Marius Svechla
 *
 */
public class CRAResult {
	private Object object;
	private Field selectedParameter;
	private double percentage;
	public CRAResult(Object object, Field selectedParameter, double percentage) {
		super();
		this.object = object;
		this.selectedParameter = selectedParameter;
		this.percentage = percentage;
	}
	public Object getObject() {
		return object;
	}
	public Field getSelectedParameter() {
		return selectedParameter;
	}
	public double getPercentage() {
		return percentage;
	}
	
	
}
