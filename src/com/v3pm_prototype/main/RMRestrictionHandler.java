package com.v3pm_prototype.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.v3pm_prototype.exceptions.ProjectIsNotInRoadmapException;

/**
 * The RMRestrictionHandler contains methods to check if the given roadmap violates any restriction or not.
 */
public class RMRestrictionHandler {

	/**
	 * checks if a roadmap breaks any restrictions. This is the main method of this class and sums up the other methods by calling them one after another. This
	 * method should be called before adding a roadmap to the RM-collection.
	 * 
	 * @return false if no restriction is violated
	 */
	
	public static int OverallAmount = 0;   						 //MLe
	public static int AmountisNoProjectInPeriodRestriction = 0;   //MLe
	public static int AmountisTogetherWithRestriction = 0;  	 //MLe
	public static int AmountisNotTogetherWithRestriction = 0; 	 //MLe
	public static int AmountisPredecessorRestriction = 0;  		 //MLe
	public static int AmountisSuccessorRestriction = 0; 	 	  //MLe
	public static int AmountisEarliestAndLatest = 0;   			 //MLe
	public static int AmountisMaxBudgetRestriction = 0;  		 //MLe
	public static int AdmissibleAmount = 0;  					 //MLe
	
	
	public static boolean breakRestrictionBeforeAddingToRoadMapCollection(List<String> tempProjectSequence, Collection<Project> collProj) {
		
		OverallAmount = OverallAmount + 1; //MLe
						
		if (isNoProjectInPeriodRestrictionViolation(tempProjectSequence) == true) {
			AmountisNoProjectInPeriodRestriction = AmountisNoProjectInPeriodRestriction + 1; //MLe
			return true;
		}
		if (isTogetherWithRestrictionViolation(tempProjectSequence, collProj) == true) {
			AmountisTogetherWithRestriction = AmountisTogetherWithRestriction + 1; //MLe
			return true;
		}
		if (isNotTogetherWithRestrictionViolation(tempProjectSequence, collProj) == true) {
			AmountisNotTogetherWithRestriction = AmountisNotTogetherWithRestriction + 1; //MLe
			return true;
		}
		if (isPredecessorRestrictionViolation(tempProjectSequence, collProj) == true) {
			AmountisPredecessorRestriction  = AmountisPredecessorRestriction  + 1; //MLe
			return true;
		}
		if (isSuccessorRestrictionViolation(tempProjectSequence, collProj) == true) {
			AmountisSuccessorRestriction = AmountisSuccessorRestriction + 1; //MLe
			return true;
		}
		if (isEarliestAndLatestImplementationRestrictionViolation(tempProjectSequence, collProj) == true) {
			AmountisEarliestAndLatest = AmountisEarliestAndLatest + 1; //MLe
			return true;
		}
		if (isMaxBudgetRestrictionViolation(tempProjectSequence, collProj) == true) {
			AmountisMaxBudgetRestriction = AmountisMaxBudgetRestriction + 1; //MLe
			return true;
		}
		
		AdmissibleAmount = AdmissibleAmount + 1; //MLe		
		return false; // no restriction is violated
	}

	/**
	 * compares the position of a project and the position of the project which should not be in the same period.
	 * 
	 * @return true if the two projects are in the same period (restriction violated)
	 */
	private static boolean isNotTogetherWithRestrictionViolation(List<String> tempProjectSequence, Collection<Project> collProj) {
		int projectPositionInRoadmap;
		int projectPositionInRoadmapWhichShouldNotBeInSamePeriod;
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each project
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<Project> it_pr = collProj.iterator(); it_pr.hasNext();) {
			Project project = it_pr.next();
			if (project.getNotTogetherInPeriodWithProject() != '-' && isProjectInRoadmap(tempProjectSequence, project.getId()) == true) {
				try {
					projectPositionInRoadmap = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence, project.getId());
					projectPositionInRoadmapWhichShouldNotBeInSamePeriod = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence,
							project.getNotTogetherInPeriodWithProject());
					if (projectPositionInRoadmap == projectPositionInRoadmapWhichShouldNotBeInSamePeriod) {
						return true; // restriction violated
					}
				} catch (ProjectIsNotInRoadmapException e) {
					continue; // roadmap does not contain one of the projects -> no restriction violated so far
				}
			}
		}
		return false;
	}

	/**
	 * compares the position of a project and the position of the project which must be in the same period.
	 * 
	 * @return true if the two projects are not in the same period (restriction violated)
	 */
	private static boolean isTogetherWithRestrictionViolation(List<String> tempProjectSequence, Collection<Project> collProj) {
		int projectPositionInRoadmap;
		int projectPositionInRoadmapWhichShouldBeInSamePeriod;
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each project
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<Project> it_pr = collProj.iterator(); it_pr.hasNext();) {
			Project project = it_pr.next();
			if (project.getTogetherInPeriodWithProject() != '-') {
				try {
					projectPositionInRoadmap = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence, project.getId());
				} catch (ProjectIsNotInRoadmapException e) {
					continue; // roadmap does not contain the project -> no restriction violated so far
				}
				try {
					projectPositionInRoadmapWhichShouldBeInSamePeriod = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence,
							project.getTogetherInPeriodWithProject());
					if (projectPositionInRoadmap != projectPositionInRoadmapWhichShouldBeInSamePeriod) {
						return true; // restriction violated
					}
				} catch (ProjectIsNotInRoadmapException e) {
					return true; // roadmap does not contain the projects which should be in the same period -> restriction violated
				}
			}
		}
		return false;
	}

	private static boolean isPredecessorRestrictionViolation(List<String> tempProjectSequence, Collection<Project> collProj) {
		int projectPositionInRoadmap;
		int predecessorProjectPositionInRoadmap;
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each project
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<Project> it_pr = collProj.iterator(); it_pr.hasNext();) {
			Project project = it_pr.next();
			// check if there is a predecessor project given ( '-' is none)
			if (project.getPredecessorProject() != '-' && isProjectInRoadmap(tempProjectSequence, project.getId())){
//					&& isProjectInRoadmap(tempProjectSequence, project.getPredecessorProject())) {
				// get the positions of the projects within the project-sequence
				try {
					projectPositionInRoadmap = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence, project.getId());
				} catch (ProjectIsNotInRoadmapException e) {
					continue; // roadmap does not contain the project -> no restriction violated so far
				}
				try {
					predecessorProjectPositionInRoadmap = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence, project.getPredecessorProject());
				} catch (ProjectIsNotInRoadmapException e) {
					return true; // roadmap does not contain the predecessor project -> restriction violated
				}
				// handle predecessor restriction
				if (predecessorProjectPositionInRoadmap >= projectPositionInRoadmap) {
					return true; // restriction violated
				}
			}
		}
		return false;
	}

	private static boolean isSuccessorRestrictionViolation(List<String> tempProjectSequence, Collection<Project> collProj) {
		int projectPositionInRoadmap;
		int successorProjectPositionInRoadmap;
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each project
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<Project> it_pr = collProj.iterator(); it_pr.hasNext();) {
			Project project = it_pr.next();
			if (project.getSuccessorProject() != '-' && isProjectInRoadmap(tempProjectSequence, project.getId())
					&& isProjectInRoadmap(tempProjectSequence, project.getSuccessorProject())) {
				// check if there is a predecessor Project given ( '-' is none)
				// get the positions of the projects within the projectSequence
				try {
					projectPositionInRoadmap = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence, project.getId());
				} catch (ProjectIsNotInRoadmapException e) {
					continue; // roadmap does not contain the project -> no restriction violated so far
				}
				try {
					successorProjectPositionInRoadmap = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence, project.getSuccessorProject());
				} catch (ProjectIsNotInRoadmapException e) {
					return true; // roadmap does contain the project AND does not contain the successor project -> restriction violated
				}
				// handle successor restriction
				if (successorProjectPositionInRoadmap <= projectPositionInRoadmap) {
					return true; // restriction violated
				}
			}
		}
		return false;
	}

	/**
	 * two checks: 1. checks for earliest implementation period restriction. 2. checks for latest implementation period restriction. There are two checks in one
	 * method for better performance.
	 */
	private static boolean isEarliestAndLatestImplementationRestrictionViolation(List<String> tempProjectSequence, Collection<Project> collProj) {
		int projectPositionInRoadmap;
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each project
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<Project> it_pr = collProj.iterator(); it_pr.hasNext();) {
			Project project = it_pr.next();
			// find out for which period this project is scheduled
			try {
				projectPositionInRoadmap = getThePeriodForWhichThisProjectIsScheduled(tempProjectSequence, project.getId());
			} catch (ProjectIsNotInRoadmapException e) {
				// this exception happens if this roadmap does not contain the project
				if (project.getLatestImplementationPeriod() > 0) {
					return true; // roadmap does contain the project -> violation against the Latest Implementation Period restriction
				} else {
					continue;
				}
			}
			// handle LIP (Latest Implementation Period) restrictions
			if (project.getLatestImplementationPeriod() > 0 && projectPositionInRoadmap > project.getLatestImplementationPeriod()) {
				// if no latestImpementationPeriod is set, latestImpementationPeriod will be 0
				return true;
			}
			// handle EIP (Earliest Implementation Period) restrictions
			if (project.getEarliestImplementationPeriod() > 0 && projectPositionInRoadmap < project.getEarliestImplementationPeriod()) {
				// if no earliestImplementationPeriod is set, earliestImplementationPeriod will be 0
				return true;
			}
		}
		return false; // no restriction violation
	}

	private static boolean isNoProjectInPeriodRestrictionViolation(List<String> tempProjectSequence) {
		if (Main.periodWithNoScheduledProjects == 0) { // if no period with no scheduled project is set, the value is 0
			return false; // no restriction set -> no restriction violation
		}
		int periodCounter = 1;
		for (Iterator<String> itPS = tempProjectSequence.iterator(); itPS.hasNext();) {
			String SequenceInPeriod = itPS.next();
			if (Main.periodWithNoScheduledProjects == periodCounter && isPeriodWithNoProjects(SequenceInPeriod) == false) {
				return true;
			}
			periodCounter++;
		}
		return false; // no restriction violation
	}

	/**
	 * two checks: 1. check if there is any period of the roadmap in which projects exceed the general budget-limit. 2. check for individual budged restriction
	 * violations (check every period for its own budget-limit). There are two checks in one method for better performance.
	 */
	private static boolean isMaxBudgetRestrictionViolation(List<String> tempProjectSequence, Collection<Project> collProj) {
		if (isGlobalMaxBudgetSet() == false && isMaxBudgetPerPeriodSet() == false) {
			return false; // no budget-restriction is set -> no restriction violation
		}
		double outflowsPerPeriod = 0;
		int periodCounter = 0;
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each period within the project-sequence
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<String> itPS = tempProjectSequence.iterator(); itPS.hasNext();) {
			char[] projectsInPeriod = itPS.next().toCharArray();
			outflowsPerPeriod = 0;
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// for each project-slot within period
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			for (int i = 0; i < projectsInPeriod.length; i++) {
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				// for each project
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				for (Iterator<Project> it_pr = collProj.iterator(); it_pr.hasNext();) {
					Project project = it_pr.next();
					if (project.getId() == projectsInPeriod[i]) {
						outflowsPerPeriod = outflowsPerPeriod + project.getOinv();
						break; // found project, go to the next project slot
					}
				}
			}
			if (isGlobalMaxBudgetSet() == true && outflowsPerPeriod > Main.budgetMaxPerPeriod) {
				return true;
			}
			if (Main.budgetMaxforEachPeriod.get(periodCounter) > 0 && outflowsPerPeriod > Main.budgetMaxforEachPeriod.get(periodCounter)) {
				return true;
			}
			periodCounter++;
		}
		return false; // no restriction violation
	}

	/**
	 * checks if a global maximum Budget is set
	 */
	private static boolean isGlobalMaxBudgetSet() {
		if (Main.budgetMaxPerPeriod > 0) { // if no maximum Budget is set, the value is 0
			return true;
		}
		return false;
	}

	/**
	 * checks if there is any value in the budgetMaxforEachPeriod-list
	 * 
	 * @return true if there is any value
	 */
	private static boolean isMaxBudgetPerPeriodSet() {
		for (Iterator<Double> itBudget = Main.budgetMaxforEachPeriod.iterator(); itBudget.hasNext();) {
			if (itBudget.next() != 0.0) {
				return true;
			}
		}
		return false;
	}

	private static int getThePeriodForWhichThisProjectIsScheduled(List<String> projectSequence, int i) throws ProjectIsNotInRoadmapException {
		int position = 1;
		for (Iterator<String> itPS = projectSequence.iterator(); itPS.hasNext();) {
			if (itPS.next().contains(Integer.toString(i)) == true) {
				return position;
			}
			position++;
		}
		// exception-case if the project is not in the roadmap
		throw new ProjectIsNotInRoadmapException();
	}

	private static boolean isPeriodWithNoProjects(String SequenceInPeriod) {
		char[] projectsInPeriod = SequenceInPeriod.toCharArray();
		for (int i = 0; i < projectsInPeriod.length; i++) {
			if (projectsInPeriod[i] != Main.CHAR_FOR_EMPTY_PROJECTS) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if the roadmap contains this project
	 */
	private static boolean isProjectInRoadmap(List<String> tempProjectSequence, int j) {
		for (Iterator<String> itPS = tempProjectSequence.iterator(); itPS.hasNext();) {
			char[] projectsInPeriod = itPS.next().toCharArray();
			for (int i = 0; i < projectsInPeriod.length; i++) {
				if (j == projectsInPeriod[i]) {
					return true; // roadmap contains this project
				}
			}
		}
		return false;
	}
}