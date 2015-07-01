package com.v3pm_prototype.rmgeneration;
import java.util.HashSet;

import com.v3pm_prototype.main.Project;

public class RoadMap {
	private Project[][] rmArray;
	public HashSet<Integer> implementedProjectIDs;
	private double npv;
	public static int equalsCalls = 0;

	public RoadMap(Project[][] rmArray, HashSet<Integer> implementedProjectIDs) {
		this.rmArray = rmArray;
		this.implementedProjectIDs = implementedProjectIDs;
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		for(int period=0; period < rmArray.length;period++){
			sb.append("[ ");
			for(int slot=0; slot< rmArray[period].length;slot++){
				sb.append(rmArray[period][slot]+" ");
			}
			sb.append("] ");
		}
		
		return sb.toString();
	}
	
	public Project[][] getRMArray(){
		return this.rmArray;
	}
}
