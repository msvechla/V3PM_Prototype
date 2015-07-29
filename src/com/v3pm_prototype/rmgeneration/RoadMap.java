package com.v3pm_prototype.rmgeneration;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.main.Main;

public class RoadMap implements Comparable<RoadMap>, Cloneable{
	private Project[][] rmArray;
	public HashSet<Integer> implementedProjectIDs;
	private double npv;
	private boolean restrictionBroken = false;
	private List<Process> lstProcessCalculated;
	private List<Project> lstProjectCalculated;
	
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
				if(rmArray[period][slot] != null){
					sb.append(rmArray[period][slot]+" ");
				}else{
					sb.append(" ");
				}
			}
			sb.append("]");
		}
		
		return sb.toString();
	}
	
	public boolean equals(Object o){
		if(o instanceof RoadMap){
			return ((RoadMap) o).createIDRoadmap().equals(this.createIDRoadmap());
		}else{
			return false;
		}
	}
	
	public Project[][] getRMArray(){
		return this.rmArray;
	}
	
	public Collection<Project> createCollection(RunConfiguration config){
		
		Collection<Project> collProjects = new LinkedHashSet<Project>();
		HashSet<Project> implementedProjects = new HashSet<Project>();
		
		for(int period = 0; period < config.getPeriods(); period++){
			for(int slot = 0; slot < config.getSlotsPerPeriod();slot++){
				Project p = this.rmArray[period][slot];
				if(p != null){
					//Lookup Project from config to take into account changed values from Robustness Analysis
					Project cp = config.getProject(p.getId());
					Project copy = new Project(cp.getId(), cp.getName(), cp.getNumberOfPeriods(), cp.getType(), cp.getFixedCosts(), cp.getOinv(),cp.getI(), cp.getA(), cp.getB(), cp.getE(), cp.getU(),
							cp.getM(), cp.getAbsRelQ(), cp.getAbsRelT(), cp.getAbsRelOop());
					copy.setPeriod(period);
					
					//Set the starting period
					if(implementedProjects.contains(copy)){
						for(Project i : implementedProjects){
							if(i.getId() == copy.getId()){
								copy.setStartPeriod(i.getStartPeriod());
								break;
							}
						}
					}else{
						copy.setStartPeriod(period);
						implementedProjects.add(copy);
					}
					
					collProjects.add(copy);
				}else{
					Project emptyProject = new Project(-1, "Empty", 0, "", 0, 0, 0, 0, 0, 0, 0, 0, null, null, null);
					emptyProject.setPeriod(period);
					collProjects.add(emptyProject);
				}
			}
		}
		return collProjects;
	}
	
public List<Integer> createIDRoadmap(){
		
		List<Integer> idRoadmap = new ArrayList<Integer>();
		
		for(int period = 0; period < rmArray.length; period++){
			for(int slot = 0; slot < rmArray[period].length;slot++){
				Project p = this.rmArray[period][slot];
				if(p != null){
					idRoadmap.add(p.getId());
				}else{
					idRoadmap.add(-1);
				}
			}
		}
		return idRoadmap;
	}
	
	public String getNPVString(){
		DecimalFormat df = new DecimalFormat("#,###.00 €");
		return df.format(npv);
	}

	public String getDisplayText(){
		return this.toString();
	}

	public void setNpv(double npv) {
		this.npv = npv;
	}


	public List<Process> getLstProcessCalculated() {
		return lstProcessCalculated;
	}


	public void setLstProcessCalculated(List<Process> lstProcessesCaculated) {
		this.lstProcessCalculated = lstProcessesCaculated;
	}

	

	public List<Project> getLstProjectCalculated() {
		return lstProjectCalculated;
	}


	public void setLstProjectCalculated(List<Project> lstProjectCalculated) {
		this.lstProjectCalculated = lstProjectCalculated;
	}


	public double getNpv() {
		return this.npv;
	}


	public boolean isRestrictionBroken() {
		return restrictionBroken;
	}


	public void setRestrictionBroken(boolean restrictionBroken) {
		this.restrictionBroken = restrictionBroken;
	}


	@Override
	public int compareTo(RoadMap rm) {
		return Double.compare(rm.getNpv(), this.npv);
	}
	
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}

	
}
