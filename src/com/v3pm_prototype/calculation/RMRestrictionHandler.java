package com.v3pm_prototype.calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.v3pm_prototype.database.DBConstraint;
import com.v3pm_prototype.exceptions.ProjectIsNotInRoadmapException;
import com.v3pm_prototype.rmgeneration.RMContainer;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * The RMRestrictionHandler contains methods to check if the given roadmap
 * violates any restriction or not.
 */
public class RMRestrictionHandler {

	/**
	 * Checks all restrictions that are periodic specific during NPV Calculation
	 * 
	 * @param currentPeriod
	 *            The period that is currently calculated
	 * @param lstProjectsInPeriod
	 *            A List of all projects implemented in the currently calculated
	 *            period
	 * @param config
	 *            the RunConfiguration used for NPV calculation
	 * @return
	 */
	public static boolean meetsPeriodicSpecificConstraints(int currentPeriod,
			List<Project> lstProjectsInPeriod, RunConfiguration config) {
		if (rBudget(currentPeriod, lstProjectsInPeriod, config) == false)
			return false;
		if (rBudBPM(currentPeriod, lstProjectsInPeriod, config) == false)
			return false;
		if (rBudPro(currentPeriod, lstProjectsInPeriod, config) == false)
			return false;

		return true;
	}

	/**
	 * Checks all restrictions that can be checked when generating a
	 * SingleContainer
	 * 
	 * @param p
	 *            Project that is implemented in the container
	 * @param startPeriod
	 *            The implementation start period
	 * @return False if one of the restrictions is broken, true otherwise
	 */
	public static boolean meetsPostRoadmapGenerationCheck(Project[][] roadmap,
			HashSet<Integer> implementedProjects, RunConfiguration config) {

		if (rGloMutEx(implementedProjects, config) == false)
			return false;
		if (rGloMutDep(implementedProjects, config) == false)
			return false;
		if (rMandatoryProject(implementedProjects, config) == false)
			return false;

		List<Project> alreadyImplemented = new ArrayList<Project>();

		for (int period = 0; period < roadmap.length; period++) {
			List<Project> tmpNew = new ArrayList<Project>();
			tmpNew.addAll(Arrays.asList(roadmap[period]));
			tmpNew.removeAll(Collections.singleton(null));

			if (tmpNew.size() > 0) {
				if (rEarliest(period, alreadyImplemented, tmpNew, config) == false)
					return false;
				if (rLatest(period, alreadyImplemented, tmpNew, config) == false)
					return false;
				if (rPreSuc(period, alreadyImplemented, tmpNew, config) == false)
					return false;

				for (Project p : tmpNew) {
					if (!alreadyImplemented.contains(p)) {
						alreadyImplemented.add(p);
					}
				}
			}

		}
		return true;
	}

	/**
	 * Checks all restrictions that can be checked before two containers are
	 * combined
	 * 
	 * @param implementedProjectIDs
	 *            A list of project IDs that the container combines
	 * @return False if one of the restrictions is broken, true otherwise
	 */
	public static boolean meetsPreCombinedContainerGenerationCheck(
			HashSet<Integer> implementedProjectIDs, RunConfiguration config) {
		if (rMandatoryProject(implementedProjectIDs, config) == false)
			return false;

		return true;
	}

	public static boolean meetsOnCombinedContainerGenerationCheck(
			List<Project> lstProjectsInPeriod, RunConfiguration config) {
		if (rLocMutDep(lstProjectsInPeriod, config) == false)
			return false;
		if (rLocMutEx(lstProjectsInPeriod, config) == false)
			return false;

		return true;
	}

	// -------------------- CONSTRAINT SPECIFIC METHODS --------------------
	// All restriction methods return FALSE if restriction is broken, TRUE
	// otherwise

	public static boolean rPreSuc(int period, List<Project> alreadyImplemented,
			List<Project> tmpNew, RunConfiguration config) {

		for (Project p : tmpNew) {
			// if restriction is set and project has not been checked yet
			if (!alreadyImplemented.contains(p)) {

				for (DBConstraint cPreSuc : config.getConstraintSet()
						.getLstPreSuc())

					if (p.equals(cPreSuc.getSi())) {
						if (alreadyImplemented.contains(cPreSuc.getS())) {
							if (!alreadyImplemented.get(
									alreadyImplemented.indexOf(cPreSuc.getS()))
									.isFinished(period)) {
								cPreSuc.addCountBroken();
								return false;
							}
						} else {
							cPreSuc.addCountBroken();
							return false;
						}

					}
			}
		}
		return true;
	}

	public static boolean rLocMutDep(List<Project> lstProjectsInPeriod,
			RunConfiguration config) {
		if (config.getConstraintSet().getLstLocMutDep().size() > 0) {
			for (DBConstraint cLocMutDep : config.getConstraintSet()
					.getLstLocMutDep()) {
				if (!(lstProjectsInPeriod.contains(cLocMutDep.getS()) && lstProjectsInPeriod
						.contains(cLocMutDep.getSi()))) {
					cLocMutDep.addCountBroken();
					return false;
				}
			}
		}
		return true;
	}

	public static boolean rLocMutEx(List<Project> lstProjectsInPeriod,
			RunConfiguration config) {
		if (config.getConstraintSet().getLstLocMutEx().size() > 0) {
			for (DBConstraint cLocMutEx : config.getConstraintSet()
					.getLstLocMutEx()) {
				if (lstProjectsInPeriod.contains(cLocMutEx.getS())
						&& lstProjectsInPeriod.contains(cLocMutEx.getSi())) {
					cLocMutEx.addCountBroken();
					System.out.println("LocMutEx");
					return false;
				}
			}
		}
		return true;
	}

	public static boolean rEarliest(int startPeriod,
			List<Project> alreadyImplemented, List<Project> tmpNew,
			RunConfiguration config) {
		// Check all projects that are newly implemented this period
		for (Project p : tmpNew) {
			// if restriction is set and project has not been checked yet
			for (DBConstraint cEarliest : config.getConstraintSet()
					.getLstEarliest()) {
				if (cEarliest.getS().equals(p)
						&& (startPeriod < Integer.valueOf(cEarliest.getY()))) {
					cEarliest.addCountBroken();
					return false;
				}
			}
		}
		return true;
	}

	public static boolean rLatest(int startPeriod,
			List<Project> alreadyImplemented, List<Project> tmpNew,
			RunConfiguration config) {
		// Check all projects that are newly implemented this period
		for (Project p : tmpNew) {
			// if restriction is set
			for (DBConstraint cLatest : config.getConstraintSet()
					.getLstLatest()) {
				if (cLatest.getS().equals(p)
						&& (startPeriod + p.getNumberOfPeriods() - 1 > Integer
								.valueOf(cLatest.getY()))) {
					cLatest.addCountBroken();
					return false;
				}
			}
		}
		return true;
	}

	public static boolean rGloMutEx(HashSet<Integer> implementedProjectIDs,
			RunConfiguration config) {
		for (DBConstraint cGloMutEx : config.getConstraintSet()
				.getLstGloMutEx()) {
			if (implementedProjectIDs.contains(cGloMutEx.getS().getId())
					&& implementedProjectIDs
							.contains(cGloMutEx.getSi().getId())) {
				cGloMutEx.addCountBroken();
				return false;
			}
		}
		return true;
	}

	public static boolean rGloMutDep(HashSet<Integer> implementedProjectIDs,
			RunConfiguration config) {
		for (DBConstraint cGloMutDep : config.getConstraintSet()
				.getLstGloMutDep()) {
			if (!(implementedProjectIDs.contains(cGloMutDep.getS().getId()) && implementedProjectIDs
					.contains(cGloMutDep.getSi().getId()))) {
				cGloMutDep.addCountBroken();
				return false;
			}
		}
		return true;
	}

	public static boolean rTimeMax(int period, Process process,
			RunConfiguration config) {
		for (DBConstraint cTimeMax : config.getConstraintSet().getLstTimeMax()) {
			if (cTimeMax.getI().equals(process)) {
				if (cTimeMax.getY().equals(DBConstraint.PERIOD_ALL)) {
					if (process.getT() > cTimeMax.getX()){
						cTimeMax.addCountBroken();
						return false;
					}
				} else {
					if (process.getT() > cTimeMax.getX()
							&& period == Integer.valueOf(cTimeMax.getY())) {
						cTimeMax.addCountBroken();
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean rQualMin(int period, Process process,
			RunConfiguration config) {
		for (DBConstraint cQualMin : config.getConstraintSet().getLstQualMin()) {
			if (cQualMin.getI().equals(process)) {
				if (cQualMin.getY().equals(DBConstraint.PERIOD_ALL)) {
					if (process.getQ() < cQualMin.getX()){
						cQualMin.addCountBroken();
						return false;
					}
				} else {
					if (process.getQ() < cQualMin.getX()
							&& period == Integer.valueOf(cQualMin.getY())) {
						cQualMin.addCountBroken();
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean rBudget(int currentPeriod,
			List<Project> lstProjectsInPeriod, RunConfiguration config) {
		for (DBConstraint cBudget : config.getConstraintSet().getLstBudget()) {
			if (cBudget.getY().equals(DBConstraint.PERIOD_ALL)
					|| Integer.valueOf(cBudget.getY()) == currentPeriod) {
				double oInvGes = 0;
				for (Project p : lstProjectsInPeriod) {
					oInvGes += p.getOinv();
				}

				if (oInvGes > cBudget.getX()) {
					cBudget.addCountBroken();
					return false;
				}
			}
		}
		return true;
	}

	public static boolean rBudBPM(int currentPeriod,
			List<Project> lstProjectsInPeriod, RunConfiguration config) {
		for (DBConstraint cBudBPM : config.getConstraintSet().getLstBudBPM()) {
			if (cBudBPM.getY().equals(DBConstraint.PERIOD_ALL)
					|| Integer.valueOf(cBudBPM.getY()) == currentPeriod) {
				double oInvBPMGes = 0;
				for (Project p : lstProjectsInPeriod) {
					if (p.getType().equals(Project.TYPE_BPMLEVEL)) {
						oInvBPMGes += p.getOinv();
					}
				}

				if (oInvBPMGes > cBudBPM.getX()) {
					cBudBPM.addCountBroken();
					return false;
				}
			}
		}
		return true;
	}

	public static boolean rBudPro(int currentPeriod,
			List<Project> lstProjectsInPeriod, RunConfiguration config) {
		for (DBConstraint cBudPro : config.getConstraintSet().getLstBudPro()) {
			if (cBudPro.getY().equals(DBConstraint.PERIOD_ALL)
					|| Integer.valueOf(cBudPro.getY()) == currentPeriod) {
				double oInvProGes = 0;
				for (Project p : lstProjectsInPeriod) {
					if (p.getType().equals(Project.TYPE_PROCESSLEVEL)) {
						if (p.getI() == cBudPro.getI().getId()) {
							oInvProGes += p.getOinv();
						}
					}
				}

				if (oInvProGes > cBudPro.getX()) {
					cBudPro.addCountBroken();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks that only mandatory projects are implemented.
	 * @param implementedProjectIDs
	 * @return
	 */
	public static boolean rMandatoryProject(
			HashSet<Integer> implementedProjectIDs, RunConfiguration config) {

		List<Integer> mandatoryIDs = config.getConstraintSet()
				.getLstMandatoryIDs();

		// When more projects are implemented than there are mandatory projects
		// -> all mandatory projects have to be implemented
		if (implementedProjectIDs.size() >= mandatoryIDs.size()) {
			boolean containsAll =  implementedProjectIDs.containsAll(mandatoryIDs);
			if(containsAll == false){
				config.getConstraintSet().getLstMandatory().get(0).addCountBroken();
				return false;
			}
		} else {
			// Else only mandatory projects have to be implemented
			boolean containsAll = mandatoryIDs.containsAll(implementedProjectIDs);
			if(containsAll == false){
				config.getConstraintSet().getLstMandatory().get(0).addCountBroken();
				return false;
			}
		}
		return true;
	}
}
