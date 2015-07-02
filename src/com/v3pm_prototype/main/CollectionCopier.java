package com.v3pm_prototype.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class CollectionCopier {

	/**
	 * loops through the roadmap and copies every project which is indicated in the roadmap to a new collection (tempCollProj)
	 */
	public static Collection<Project> createTemporaryOrderedProjectCollectionAccordingToRoadmap(Collection<Project> collProj, List<String> projectSequence) {
		Collection<Project> tempCollPoj = new LinkedHashSet<Project>(); // LinkedHashSet because we want to keep the order in which we add the elements.
		char[] projectsInPeriod;
		int periodCounter = 0;
		for (Iterator<String> itPS = projectSequence.iterator(); itPS.hasNext();) {
			projectsInPeriod = itPS.next().toCharArray();
			for (int i = 0; i < projectsInPeriod.length; i++) {
				for (Iterator<Project> itProj = collProj.iterator(); itProj.hasNext();) {
					Project p = itProj.next();
					if (p.getId() == projectsInPeriod[i]) {
						Project copy = new Project(p.getName(), p.getId(), p.getType(), p.getI(), p.getOinv(), p.getA(), p.getB(), p.getE(), p.getU(),
								p.getM(), p.getEarliestImplementationPeriod(), p.getLatestImplementationPeriod(), p.getPredecessorProject(),
								p.getSuccessorProject(), p.getTogetherInPeriodWith(), p.getNotTogetherInPeriodWith(), p.getFixedCostEffect(), p.getAbsRelq(), p.getAbsRelt(), p.getAbsRelOop());
						copy.setPeriod(periodCounter);
						tempCollPoj.add(copy);
						break;
					}
				}
			}
			periodCounter++;
		}
		return tempCollPoj;
	}

	/**
	 * creates an exact copy of the process-collection
	 */
	public static Collection<Process> createTemporaryProcessCollection(Collection<Process> collProcess) {
		Collection<Process> tempCollPocess = new HashSet<Process>();
		for (Iterator<Process> it = collProcess.iterator(); it.hasNext();) {
			Process p = it.next();
			Process copy = new Process(p.getName(), p.getId(), p.getQ(), p.getQmax(), p.getT(), p.getP(), p.getOop(), p.getD(), p.getV(), p.getFixedCosts(),p.getQmin(),p.getTmax(), p.getN(), p.getRoh(),
					p.getLamda(), p.getAlpha(), p.getThetaID_q(), p.getBeta(), p.getThetaID_t());
			tempCollPocess.add(copy);
		}
		return tempCollPocess;
	}

	/**
	 * creates an exact copy of the roadmap-collection
	 */
	public static List<Roadmap> createTemporaryRoadMapCollection(List<Roadmap> collRM) {
		List<Roadmap> tempCollRM = new ArrayList<Roadmap>();
		for (Iterator<Roadmap> itRM = collRM.iterator(); itRM.hasNext();) {
			Roadmap tempRoadmap = itRM.next();
			List<String> projectSequence = tempRoadmap.getProjectSequence();
			List<String> projectSequenceCopy = new ArrayList<String>();
			for (Iterator<String> itPS = projectSequence.iterator(); itPS.hasNext();) {
				projectSequenceCopy.add(itPS.next());
			}
			tempCollRM.add(new Roadmap(projectSequenceCopy, tempRoadmap.getNpv()));
		}
		return tempCollRM;
	}

}
