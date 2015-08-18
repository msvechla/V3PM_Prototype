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
	
	public static void clear(){
		lstRMContainerSingle.clear();
		lstRMContainerCombined.clear();
		lstCombinedProjectIDs.clear();
		countRoadMapsGenerated = 0;
	}
	
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
	
	public static List<RoadMap> createRMList(RunConfiguration config){
		List<RoadMap> rmList = new ArrayList<RoadMap>();
		int countMandatory = config.getConstraintSet().getLstMandatory().size();
		int countGloMutDep = config.getConstraintSet().getLstGloMutDep().size();
		
			for(RMContainer rmc : lstRMContainerSingle){
				//Only add Single Containers if it contains mandatory projects
				if(countMandatory == 1 && countGloMutDep == 0){
					if(rmc.implementedProjects.contains(config.getConstraintSet().getLstMandatory().get(0).getS().getId())){
						rmList.addAll(rmc.getLstRM());
						break;
					}
				}
				
				if(countMandatory == 0 && countGloMutDep == 0){
					rmList.addAll(rmc.getLstRM());
				}		
			}	
		
		for(RMContainer rmc : lstRMContainerCombined){
			rmList.addAll(rmc.getLstRM());
		}
		return rmList;
	}

}
