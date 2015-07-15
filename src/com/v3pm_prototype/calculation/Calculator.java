package com.v3pm_prototype.calculation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.v3pm_prototype.exceptions.NoValidThetaIDException;
import com.v3pm_prototype.rmgeneration.*;

/**
 * This Class is used to calculate the NPV of each roadmap.
 */
public class Calculator {
	
	
	/**
	 * calculates the NPV and set the NPV-value for each roadmap
	 * @throws NoValidThetaIDException 
	 */
	public static void calculateNPVs(List<RoadMap> collRM, Collection<Process> collProcess, Collection<Project> collProj, RunConfiguration config) throws NoValidThetaIDException {

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each roadmap
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<RoadMap> itRM = collRM.iterator(); itRM.hasNext();) {
			RoadMap RM = itRM.next();
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
			Collection<Process> tempCollPocess = CollectionCopier.createTemporaryProcessCollection(collProcess);
			Collection<Process> bufferedTempCollProcess = CollectionCopier.createTemporaryProcessCollection(collProcess);

			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// for each project
			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			for (Iterator<Project> itProj = tempCollPoj_sorted.iterator(); itProj.hasNext();) {
				Project tempProject = itProj.next();

				// calculate inflows
				if (tempProject.getPeriod() > prePeriod) {
					// In case this is the first project of a new period. For further projects within the same period, no inflows will be added.
					inflows = inflows + calculateInflowsPerPeriode(tempCollPocess, tempProject, config);
					
					//MLe  korrekt und getestet!
					
					fixedCostsOAGes = fixedCostsOAGes + (fixedCostsOA / (Math.pow((1 + config.getDiscountRate()), tempProject.getPeriod())));
														
					// Create a deep copy of the tempProcess collection to be able to compare t and q for degeneration handling in multiproject scenarios.
					bufferedTempCollProcess = CollectionCopier.createTemporaryProcessCollection(tempCollPocess);
				}
				// calculate outflows
		
				outflows = outflows + (tempProject.getOinv() / (Math.pow((1 + config.getDiscountRate()), tempProject.getPeriod())));

				// MLe Übergreifende Fixkosteneffekte durch Projekte müssen noch verbucht werden 
				// Hier nun je Projekt. Wirkung in der nächsten Periode vorschüssig.
				
				if (tempProject.getType().equals("bpmLevel"))
				{
					
					fixedCostsOA = fixedCostsOA + tempProject.getFixedCostEffect();
					
				}
						
				
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				// for each process
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				// modify processes by project for next iteration
				ProjectAndProcessModifier.modifyProcessesAndProjectsByProject(tempCollPocess, bufferedTempCollProcess, tempProject, tempCollPoj_sorted,
						projectNumberWithinPeriod, config);
				prePeriod = tempProject.getPeriod();
				projectNumberWithinPeriod++;
				if (projectNumberWithinPeriod > config.getSlotsPerPeriod()) {
					projectNumberWithinPeriod = 1;
				}
			}
			// NPV of a roadmap = sum of all inflows - sum of all outflows
			double npv = Math.round((inflows - outflows - fixedCostsOAGes) * 100);
			RM.setNpv(npv / 100);
			

		}
	}

	/**
	 * calculate inflows for each process and adds them up to the total inflows
	 * @throws NoValidThetaIDException 
	 */
	private static double calculateInflowsPerPeriode(Collection<Process> tempCollPocess, Project tempProject, RunConfiguration config) throws NoValidThetaIDException {
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
