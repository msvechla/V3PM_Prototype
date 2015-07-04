package com.v3pm_prototype.rmgeneration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.v3pm_prototype.main.Project;
import com.v3pm_prototype.main.RMRestrictionHandler;


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
	
	public static List<RoadMap> createRMList(){
		List<RoadMap> rmList = new ArrayList<RoadMap>();
		int countMandatory = Project.getMandatoryProjects().size();
		
			for(RMContainer rmc : lstRMContainerSingle){
				//Only add Single Containers if it contains mandatory projects
				if(countMandatory == 1){
					if(rmc.implementedProjects.contains(Project.getMandatoryProjects().get(0).getId())){
						rmList.addAll(rmc.getLstRM());
					}
				}
				
				if(countMandatory == 0){
					rmList.addAll(rmc.getLstRM());
				}		
			}	
		
		for(RMContainer rmc : lstRMContainerCombined){
			//if container passes cleanup-check -> add all roadmaps in container to final list
			
			//TODO NTH: If restriction passed one time -> dont check it anymore
			if(RMRestrictionHandler.cMandatoryProject(rmc, countMandatory)){
				rmList.addAll(rmc.getLstRM());
			}
		}
		return rmList;
	}

}
