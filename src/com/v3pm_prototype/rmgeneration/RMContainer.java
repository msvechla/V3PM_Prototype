package com.v3pm_prototype.rmgeneration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.calculation.RMRestrictionHandler;

/**
 * Stores roadmaps and groups them into containers for efficiency
 * @author Marius Svechla
 *
 */
public class RMContainer {
	
	private boolean isCombinedContainer;
	private List<RMContainer> lstRMContainerSingle = new ArrayList<RMContainer>();
	private List<RMContainer> lstRMContainerCombined = new ArrayList<RMContainer>();
	private HashSet<Integer> implementedProjects;
	private List<RoadMap> lstRM;
	
	public RMContainer(boolean isCombinedContainer,HashSet<Integer> implementedProjects,List<RMContainer> lstRMContainerSingle,List<RMContainer> lstRMContainerCombined, List<HashSet<Integer>> lstIDsOverall){
		this.implementedProjects = implementedProjects;
		this.lstRM = new ArrayList<RoadMap>();
		this.isCombinedContainer = isCombinedContainer;
		this.lstRMContainerSingle = lstRMContainerSingle;
		this.lstRMContainerCombined = lstRMContainerCombined;
		
		if(isCombinedContainer){
			lstRMContainerCombined.add(this);
		}else{
			lstRMContainerSingle.add(this);
		}
		lstIDsOverall.add(implementedProjects);
	}
	
	
	
	public void addRoadMap(RoadMap rm){
		this.lstRM.add(rm);
		//countRoadMapsGenerated++;
	}
	
	public List<RoadMap> getLstRM(){
		return this.lstRM;
	}
	
	public HashSet<Integer> getImplementedProjects(){
		return this.implementedProjects;
	}

}
