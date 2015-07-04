package com.v3pm_prototype.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.v3pm_prototype.rmgeneration.RoadMap;

public class CollectionCopier {

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
//	public static List<Roadmap> createTemporaryRoadMapCollection(List<RoadMap> collRM) {
//		List<RoadMap> tempCollRM = new ArrayList<RoadMap>();
//		for (Iterator<RoadMap> itRM = collRM.iterator(); itRM.hasNext();) {
//			RoadMap tempRoadmap = itRM.next();
//			List<String> projectSequence = tempRoadmap.getProjectSequence();
//			List<String> projectSequenceCopy = new ArrayList<String>();
//			for (Iterator<String> itPS = projectSequence.iterator(); itPS.hasNext();) {
//				projectSequenceCopy.add(itPS.next());
//			}
//			tempCollRM.add(new RoadMap(projectSequenceCopy, tempRoadmap.getNpv()));
//		}
//		return tempCollRM;
//	}

}
