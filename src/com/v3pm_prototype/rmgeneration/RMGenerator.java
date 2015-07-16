package com.v3pm_prototype.rmgeneration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javafx.concurrent.Task;

import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.calculation.RMRestrictionHandler;

public class RMGenerator extends Task<List<RoadMap>> {
	
	private RunConfiguration config;
	private List<RoadMap> generatedRoadmaps;
	
	public RMGenerator(RunConfiguration config){
		this.config = config;
	}

	
	@Override
	protected List<RoadMap> call() throws Exception {
		this.generateRoadmaps();
		return this.generatedRoadmaps;
	}

	/**
	 * Algorithm for generating all possible Roadmaps from pre-defined projects
	 * @param mainApp 
	 */
	
	private List<RoadMap> generateRoadmaps() {
		System.out.println("--- START: generateRoadmaps()");
		
		//STEP1 Generate SingleContainers
		for(Project p : config.getLstProjects()){
			
			//create a container for every project
			HashSet<Integer> implementedProjectIDs = new HashSet<Integer>();
			implementedProjectIDs.add(p.getId());
			RMContainer rmc = new RMContainer(false, implementedProjectIDs);
			
			//add a roadmaps to the container for implementation start in each period
			for(int period = 0; period < config.getPeriods() - p.numberOfPeriods+1; period++){
					Project[][] roadmap = new Project[config.getPeriods()][config.getSlotsPerPeriod()];
					
					for(int i = 0; i < config.getPeriods();i++){
						if((i<period) || (i> (period + p.numberOfPeriods-1))){
							//no project implemented
						}else{
							//project is implemented
							roadmap[i][0] = p;
						}
					}
					//add roadmap to the SingleContainer
					rmc.addRoadMap(new RoadMap(roadmap,implementedProjectIDs));
			}
			
		}
		
		//STEP2: Generate initial CombinedContainers from combining SingleContainers with SingleContainers
		//STEP3: Generate CombinedContainers from looping through CombinedContainers and combining with SingleContainers
		
		List<List<RMContainer>> lstSingleCombined = new ArrayList<List<RMContainer>>();
		lstSingleCombined.add(RMContainer.lstRMContainerSingle); //list for STEP2
		lstSingleCombined.add(RMContainer.lstRMContainerCombined); //list for STEP3
		
		//Work through STEP2 and STEP3
		for(List<RMContainer>lstRMContainer : lstSingleCombined){
			
			for(int i=0;i<lstRMContainer.size();i++){
				RMContainer rmcSingle2 = lstRMContainer.get(i);
				
				for(RMContainer rmcSingle : RMContainer.lstRMContainerSingle){
					
					//Generate list of combined IDs
					HashSet<Integer> implementedProjectIDs = new HashSet<Integer>();
					implementedProjectIDs.addAll(rmcSingle.getImplementedProjects());
					implementedProjectIDs.addAll(rmcSingle2.getImplementedProjects());
					
					//Perform Pre-CombinedContainerGeneration Restriction Check
					if(RMRestrictionHandler.meetsPreCombinedContainerGenerationCheck(implementedProjectIDs, config)){
						
						//If combination has not been generated yet -> Generate
						if(!RMContainer.lstCombinedProjectIDs.contains(implementedProjectIDs)){
							//create container
							RMContainer rmcCombined = null;
							
							for(RoadMap rmSingle: rmcSingle.getLstRM()){
								for(RoadMap rmSingle2: rmcSingle2.getLstRM()){
									
									//combine both roadmaps
									Project[][] roadmap = combineRoadMaps(rmSingle,rmSingle2,config);
									if(roadmap != null){				
										if(rmcCombined == null) rmcCombined = new RMContainer(true, implementedProjectIDs);
										rmcCombined.addRoadMap(new RoadMap(roadmap,implementedProjectIDs));
									}

								}
							}
							
						}
						
					}	
					
				}
				
			}
			
		}
		
		List<RoadMap> rmList = RMContainer.createRMList(config);
		List<RoadMap> rmListPostRMGenCheck = new ArrayList<RoadMap>();
		
		//TODO ERROR IN HERE
		for(RoadMap rm : rmList){
			if(RMRestrictionHandler.meetsPostRoadmapGenerationCheck(rm.getRMArray(), rm.implementedProjectIDs, config)){
				rmListPostRMGenCheck.add(rm);
			}
		}
		
		
		System.out.println("--- FINISH: generateRoadmaps()");
		System.out.println(rmListPostRMGenCheck.size() + " Roadmaps generated.");
		this.generatedRoadmaps = rmListPostRMGenCheck;
		return rmListPostRMGenCheck;
		
	}

	/**
	 * Combines two Roadmaps to a single Roadmap
	 * @param rmSingle the first Roadmap
	 * @param rmSingle2 the second Roadmap
	 * @return Combined roadmap is returned if combination fits setup. Otherwise null is returned.
	 */
	private static Project[][] combineRoadMaps(RoadMap rmSingle,
			RoadMap rmSingle2, RunConfiguration config) {
		
		Project[][] rmCombined = new Project[config.getPeriods()][config.getSlotsPerPeriod()];
		
		for(int period = 0; period<rmSingle.getRMArray().length;period++){
			
			//combine all projects from both roadmaps per period
			HashSet<Project> projectsInPeriod = new HashSet<Project>();
			projectsInPeriod.addAll(Arrays.asList(rmSingle.getRMArray()[period]));
			projectsInPeriod.addAll(Arrays.asList(rmSingle2.getRMArray()[period]));
			projectsInPeriod.remove(null);
			
			RMRestrictionHandler.meetsOnCombinedContainerGenerationCheck(projectsInPeriod, config);
			
			//save period if COUNT_PROJECTS_MAX_PER_PERIOD is not exceeded, otherwise stop combination
			if(projectsInPeriod.size() <= config.getSlotsPerPeriod()){
				int i=0;
				for(Project p : projectsInPeriod){
					rmCombined[period][i] =  p;
					i++;
				}
			}else{
				return null;
			}
		}

		return rmCombined;
	}
	
	
	public List<RoadMap> getGeneratedRoadmaps(){
		return this.generatedRoadmaps;
	}
	
}
