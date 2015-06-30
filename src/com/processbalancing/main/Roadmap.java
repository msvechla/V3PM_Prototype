package com.processbalancing.main;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for value objects which holds the project-sequence and the NPV of a roadmap
 */
public class Roadmap {

	private List<String> projectSequence = new ArrayList<String>();
	private double npv;


	public Roadmap(List<String> projectSequence, double npv) {
		super();
		this.projectSequence = projectSequence;
		this.npv = npv;

	}
		
	public double getNpv() {
		return npv;
	}

	public void setNpv(double npv) {
		this.npv = npv;
	}

	public List<String> getProjectSequence() {
		return projectSequence;
	}

	public void setProjectSequence(List<String> projectSequence) {
		this.projectSequence = projectSequence;
	}
}