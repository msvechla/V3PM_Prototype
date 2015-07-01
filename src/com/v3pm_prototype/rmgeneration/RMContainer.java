package com.v3pm_prototype.rmgeneration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class RMContainer {
	
	public static List<RMContainer> lstRMContainerSingle = new ArrayList<RMContainer>();
	public static List<RMContainer> lstRMContainerCombined = new ArrayList<RMContainer>();
	public static List<HashSet<Integer>> lstCombinedProjectIDs = new ArrayList<HashSet<Integer>>();
	
	public static int countRoadMapsGenerated=0;
	
	
	private boolean isCombinedContainer;
	private HashSet<Integer> implementedProjects;
	private List<RoadMap> lstRM;
	
	public RMContainer(boolean isCominedContainer,HashSet<Integer> implementedProjects){
		this.implementedProjects = implementedProjects;
		this.lstRM = new ArrayList<RoadMap>();
		if(isCominedContainer){
			lstRMContainerCombined.add(this);
		}else{
			lstRMContainerSingle.add(this);
		}
		lstCombinedProjectIDs.add(implementedProjects);
	}
	
	//TODO CLEAR Method
	
	public void addRoadMap(RoadMap rm){
		this.lstRM.add(rm);
		countRoadMapsGenerated++;
	}
	
	public List<RoadMap> getLstRM(){
		return this.lstRM;
	}
	
	public HashSet<Integer> getImplementedProjects(){
		return this.implementedProjects;
	}

}
