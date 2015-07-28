package com.v3pm_prototype.calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.concurrent.Task;

import com.v3pm_prototype.exceptions.NoValidThetaIDException;
import com.v3pm_prototype.rmgeneration.*;

/**
 * This Class is used to calculate the NPV of each roadmap.
 */
public class Calculator{
	
	private List<RoadMap> collRM;
	private RunConfiguration config;
	
	public Calculator(List<RoadMap> collRM, RunConfiguration config) {
		super();
		this.collRM = collRM;
		this.config = config;
	}
	
	public List<RoadMap> start() throws NoValidThetaIDException{
		calculateNPVs();
		Collections.sort(collRM);
		return collRM;
	}


	/**
	 * calculates the NPV and set the NPV-value for each roadmap
	 * @throws NoValidThetaIDException 
	 */
	public void calculateNPVs() throws NoValidThetaIDException {

		List<RoadMap> brokenRoadmaps = new ArrayList<RoadMap>();
		
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each roadmap
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<RoadMap> itRM = collRM.iterator(); itRM.hasNext();) {
			RoadMap RM = itRM.next();
			boolean restrictionClear = false; //QualMin, TimeMax
			double inflows = 0;
			double outflows = 0;
			double fixedCostsOA = config.getOOAFixed();  //MLe
			double fixedCostsOAGes = 0;  //MLe
			int prePeriod = -1;
			int projectNumberWithinPeriod = 1; // to indicate the last project of a period
			// Create a temporary sorted (according to the roadmap) deep copy of the PROJECT-collection and a deep copy of the PROCESS-collection to be able to
			// modify them without touching the original collections. Create another deep copy of the tempProcess Collection to be able to compare t and q from
			// the previous and the actual period (for degeneration handling in multiproject scenarios)
			Collection<Project> tempCollPoj_sorted = RM.createCollection(config);
			Collection<Process> tempCollProcess = CollectionCopier.createTemporaryProcessCollection(config.getLstProcesses());
			Collection<Process> bufferedTempCollProcess = CollectionCopier.createTemporaryProcessCollection(config.getLstProcesses());
			
			List<Project> projectsInPeriod = new ArrayList<Project>();
			
			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// for each project
			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			for (Iterator<Project> itProj = tempCollPoj_sorted.iterator(); itProj.hasNext();) {
				Project tempProject = itProj.next();

				projectsInPeriod.add(tempProject);
				
//				if(RM.toString().contains("[ Six Sigma null ][ null null ]")){
//					System.out.println("FOUND");
//				}
				if(RM.toString().contains("[ Einführung SEPA Standardisierung ][ Automatisierung null ]")){
					System.out.println("FOUND");
				}
				
				// calculate inflows
				if (tempProject.getPeriod() > prePeriod) {
					// In case this is the first project of a new period. For further projects within the same period, no inflows will be added.
					inflows = inflows + calculateInflowsPerPeriode(tempCollProcess, tempProject, config);
					
					//MLe  korrekt und getestet!
					
					fixedCostsOAGes = fixedCostsOAGes + (fixedCostsOA / (Math.pow((1 + config.getDiscountRate()), tempProject.getPeriod())));
														
					// Create a deep copy of the tempProcess collection to be able to compare t and q for degeneration handling in multiproject scenarios.
					bufferedTempCollProcess = CollectionCopier.createTemporaryProcessCollection(tempCollProcess);
				}
				// calculate outflows
		
				outflows = outflows + (tempProject.getOinv() / (Math.pow((1 + config.getDiscountRate()), tempProject.getPeriod())));

				// MLe Übergreifende Fixkosteneffekte durch Projekte müssen noch verbucht werden 
				// Hier nun je Projekt. Wirkung in der nächsten Periode vorschüssig.
				
				if (tempProject.getType().equals(Project.TYPE_BPMLEVEL))
				{
					
					fixedCostsOA = fixedCostsOA + tempProject.getM();
					
				}
						
				
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				// for each process
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				// modify processes by project for next iteration
				restrictionClear = ProjectAndProcessModifier.modifyProcessesAndProjectsByProject(tempCollProcess, bufferedTempCollProcess, tempProject, tempCollPoj_sorted,
						projectNumberWithinPeriod, config);
				if(!restrictionClear){
					RM.setRestrictionBroken(true);
					break;
				}
				
				prePeriod = tempProject.getPeriod();
				projectNumberWithinPeriod++;
				
				//Last project in period
				if (projectNumberWithinPeriod > config.getSlotsPerPeriod()) {
					projectNumberWithinPeriod = 1;
					
					if(RMRestrictionHandler.meetsPeriodicSpecificConstraints(tempProject.getPeriod(), projectsInPeriod, config) == false){
						RM.setRestrictionBroken(true);
					}
					
					projectsInPeriod.clear();
				}
			}
			
			
			// NPV of a roadmap = sum of all inflows - sum of all outflows
			if(RM.isRestrictionBroken()){
				RM.setNpv(0);
				brokenRoadmaps.add(RM);
			}else{
				double npv = Math.round((inflows - outflows - fixedCostsOAGes) * 100);
				RM.setNpv(npv / 100);
			}
			
			
			
			//Save the calculated quality and time per period
			saveCalculatedValuesToRM(RM, tempCollProcess,tempCollPoj_sorted);

		}
		
		//Remove roadmaps with broken restrictions
		//TODO SLOW
		collRM.removeAll(brokenRoadmaps);
	}
	
	/**
	 * Stores the temporarily calculated q and t values per period
	 * @param rm
	 * @param tempCollProcesss
	 */
	public void saveCalculatedValuesToRM(RoadMap rm, Collection<Process> tempCollProcess, Collection<Project> tempCollProj){
		List<Process> lstProcessesCaculated = new ArrayList<Process>();
		lstProcessesCaculated.addAll(tempCollProcess);
		
		//Sort processes by id so they have matching colors
		Collections.sort(lstProcessesCaculated);
		
		List<Project> lstProjectsCalculated = new ArrayList<Project>();
		lstProjectsCalculated.addAll(tempCollProj);
		
		rm.setLstProcessCalculated(lstProcessesCaculated);
		rm.setLstProjectCalculated(lstProjectsCalculated);
	}
	

	/**
	 * calculate inflows for each process and adds them up to the total inflows
	 * @throws NoValidThetaIDException 
	 */
	private static synchronized double calculateInflowsPerPeriode(Collection<Process> tempCollPocess, Project tempProject, RunConfiguration config) throws NoValidThetaIDException {
		double inflowsPerPeriode = 0;
		for (Iterator<Process> itProcess = tempCollPocess.iterator(); itProcess.hasNext();) {
			Process p = itProcess.next();
			//MLe: Die Formel muss um Fixkosten ergänzt werden, aber mit anderer Diskontierung!
			
			double inflowsPerPeriodePerProcess = (((DemandCalculator.calculateN(p) * (p.getP() - p.getOop())) / (Math.pow(1 + config.getDiscountRate(),
					tempProject.getPeriod() + 1))) - ((p.getFixedCosts())/ (Math.pow(1 + config.getDiscountRate(),
							tempProject.getPeriod()))));
			inflowsPerPeriode = inflowsPerPeriode + inflowsPerPeriodePerProcess;
			

		}
		return inflowsPerPeriode;
	}

}
