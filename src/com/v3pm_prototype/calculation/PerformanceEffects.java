package com.v3pm_prototype.calculation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * This class is used to support the Calculator class. It is responsible for
 * changing the values of the processes and projects according to the conducted
 * project.
 * 
 */
public class PerformanceEffects {

	/**
	 * loops through every process and modifies the values affected from the
	 * conducted project. Thereafter it loops through the project collection and
	 * modifies the values affected by the b-parameter. Returns: TRUE if no
	 * restriction is broken, FALSE otherwise
	 */
	public static boolean modifyProcessesAndProjectsByProject(
			Collection<Process> tempCollPocess,
			Collection<Process> bufferedTempCollProcess, Project tempProject,
			Collection<Project> tempCollPoj_sorted,
			int projectNumberWithinPeriod, RunConfiguration config) {

		// Indicates whether the current project is finishing this period
		boolean projectFinishes;
		if (tempProject.getPeriod() == (tempProject.getStartPeriod()
				+ tempProject.getNumberOfPeriods() - 1)) {
			projectFinishes = true;
		} else {
			projectFinishes = false;
		}
		

		// Iterate through processes
		// check each modification parameter of a project and modify the
		// regarding processes
		for (Iterator<Process> itProcessModify = tempCollPocess.iterator(); itProcessModify
				.hasNext();) {
			Process tempProcess = itProcessModify.next();
			boolean projectRegardsProcess = false;
			if (tempProject.getI() == DBProcess.ID_ALLPROCESSES
					|| tempProject.getI() == tempProcess.getId()) {
				projectRegardsProcess = true;
			}
			
			//Save initial values for history
			if(tempProject.getPeriod() == 0 && projectNumberWithinPeriod == 1){
				tempProcess.getqPerPeriod(config)[tempProject.getPeriod()] = tempProcess
						.getQ();
				tempProcess.gettPerPeriod(config)[tempProject.getPeriod()] = tempProcess
						.getT();
				tempProcess.getFixedCostsPerPeriod(config)[tempProject.getPeriod()] = tempProcess
						.getFixedCosts();
				tempProcess.getOopPerPeriod(config)[tempProject.getPeriod()] = tempProcess
						.getOop();
			}
			

			// ------------------------------------------------------
			// e mit Absolutem Effekt
			// ------------------------------------------------------
			if (projectFinishes && projectRegardsProcess
					&& tempProject.getE() != 0) {

				if (tempProject.getAbsRelT().equals("relativ")) {

					tempProcess.setT(tempProcess.getT() * tempProject.getE());
				}

				else if (tempProject.getAbsRelT().equals("absolut")) {

					tempProcess.setT(tempProcess.getT() + tempProject.getE());

					// check for time (time can't be lower than 0)
					if (tempProcess.getT() < 0) {
						tempProcess.setT(0);
					}

				} else if (!tempProject.getAbsRelT().equals("relativ")
						|| !tempProject.getAbsRelT().equals("absolut")) {

					System.out
							.println("Fehler, absoluter/relativer Effekt nicht korrekt angegeben bei "
									+ tempProject.getName());

				}

			}

			else {
				// degeneration-effect on time
				// differ between one or multiple projects per period
				if (config.getSlotsPerPeriod() == 1) {
					tempProcess.setT(tempProcess.getT()
							* (1 + tempProcess.getV())); // degeneration
				} else {
					// check if it is the last handled project of a period.
					if (config.getSlotsPerPeriod() == projectNumberWithinPeriod) {
						// check if there are any changes of t between handling
						// the first and the last project in a period
						if (isTheSameValue(tempProcess.getT(),
								bufferedTempCollProcess, tempProcess.getId(),
								't')) {
							tempProcess.setT(tempProcess.getT()
									* (1 + tempProcess.getV())); // degeneration
						}
					}
				}
			}

			// ------------------------------------------------------
			// Platz für T MAX Genau Zuordnung zu Roadmap ToDo
			// ------------------------------------------------------

			if (RMRestrictionHandler.rTimeMax(tempProject.getPeriod(),
					tempProcess, config) == false)
				return false;

			// ------------------------------------------------------
			// u
			// ------------------------------------------------------
			if (projectFinishes && projectRegardsProcess
					&& tempProject.getU() != 0) {

				if (tempProject.getAbsRelQ().equals("relativ")) {

					tempProcess.setQ(tempProcess.getQ() * tempProject.getU());

				}

				else if (tempProject.getAbsRelQ().equals("absolut")) {

					tempProcess.setQ(tempProcess.getQ() + tempProject.getU());

					// check for quality (quality can't be lower than 0)
					if (tempProcess.getQ() < 0) {
						tempProcess.setQ(0);
					}

				} else if (!tempProject.getAbsRelQ().equals("relativ")
						|| !tempProject.getAbsRelQ().equals("absolut")) {

					System.out
							.println("Fehler, absoluter/relativer Effekt nicht korrekt angegeben bei "
									+ tempProject.getName());
				}

			} else {
				// degeneration-effect on quality
				// differ between one or multiple projects per period
				if (config.getSlotsPerPeriod() == 1) {
					tempProcess.setQ(tempProcess.getQ()
							* (1 - tempProcess.getD())); // degeneration
				} else {
					// check if it is the last handled project of a period.
					if (config.getSlotsPerPeriod() == projectNumberWithinPeriod) {
						// check if there are any changes of q between handling
						// the first and the last project in a period
						if (isTheSameValue(tempProcess.getQ(),
								bufferedTempCollProcess, tempProcess.getId(),
								'q')) {
							tempProcess.setQ(tempProcess.getQ()
									* (1 - tempProcess.getD())); // degeneration
						}
					}
				}
			}

			// check for qMax (q can't be higher than qMax)
			if (tempProcess.getQ() >= tempProcess.getQmax()) {
				tempProcess.setQ(tempProcess.getQmax());
			}

			if (RMRestrictionHandler.rQualMin(tempProject.getPeriod(),
					tempProcess, config) == false)
				return false;

			
			if (projectFinishes && projectRegardsProcess
					&& tempProject.getA() != 0) {

				if (tempProject.getAbsRelOop().equals("relativ")) {

					tempProcess.setOop(tempProcess.getOop()
							* tempProject.getA());
				}

				else if (tempProject.getAbsRelOop().equals("absolut")) {

					tempProcess.setOop(tempProcess.getOop()
							+ tempProject.getA());

					// check for Oop (Oop can't be lower than 0)
					if (tempProcess.getOop() < 0) {
						tempProcess.setOop(0);
					}

				} else {
					System.out
							.println("Fehler, absoluter/relativer Effekt nicht korrekt angegeben bei "
									+ tempProject.getName());

				}

			}

			// ------------------------------------------------------
			// FixedCosts MLe
			// ------------------------------------------------------

			if (projectFinishes && tempProject.getType().equals(Project.TYPE_PROCESSLEVEL)
					&& tempProject.getM() != 0) {

				tempProcess.setFixedCosts(tempProcess.getFixedCosts()
						+ tempProject.getM());

				// check for FixedCosts (FixedCosts can't be lower than 0)
				if (tempProcess.getFixedCosts() < 0) {
					tempProcess.setFixedCosts(0);

				}

			}

			// Save values per period, store in period+1 because values are calculated before the next period starts
			if(tempProject.getPeriod()< config.getPeriods()-1){
				tempProcess.getqPerPeriod(config)[tempProject.getPeriod()+1] = tempProcess
						.getQ();
				tempProcess.gettPerPeriod(config)[tempProject.getPeriod()+1] = tempProcess
						.getT();
				tempProcess.getOopPerPeriod(config)[tempProject.getPeriod()+1] = tempProcess
						.getOop();
				tempProcess.getFixedCostsPerPeriod(config)[tempProject.getPeriod()+1] = tempProcess
						.getFixedCosts();
				
			}
			

		}

		// ------------------------------------------------------
		// b
		// ------------------------------------------------------
		if (projectFinishes && tempProject.getB() != 0) {
			// Iterate through projects
			// check all future projects for process-level-projects
			for (Iterator<Project> itProjB = tempCollPoj_sorted.iterator(); itProjB
					.hasNext();) {
				Project tempProjectFutureB = itProjB.next();
				if (tempProjectFutureB.getType().equals(Project.TYPE_PROCESSLEVEL)
						&& tempProject.getPeriod() < tempProjectFutureB
								.getPeriod()) {
					tempProjectFutureB.setOinv(tempProjectFutureB.getOinv()
							* tempProject.getB());
				}
			}
		}

		return true;

	}

	/**
	 * checks if q/t of two given processes have the same values
	 * 
	 * @param dimension_value
	 *            (value of q or t)
	 * @param dimension
	 *            (q or t)
	 */
	private static boolean isTheSameValue(double dimension_value,
			Collection<Process> bufferedTempCollProcess, int processId,
			char dimension) {
		for (Iterator<Process> itProcessBuffer = bufferedTempCollProcess
				.iterator(); itProcessBuffer.hasNext();) {
			Process tempProcessBuffer = itProcessBuffer.next();
			if (dimension == 't') {
				if (tempProcessBuffer.getId() == processId
						&& tempProcessBuffer.getT() == dimension_value) {
					return true;
				}
			} else if (dimension == 'q') {
				if (tempProcessBuffer.getId() == processId
						&& tempProcessBuffer.getQ() == dimension_value) {
					return true;
				}
			}
		}
		return false;
	}

}
