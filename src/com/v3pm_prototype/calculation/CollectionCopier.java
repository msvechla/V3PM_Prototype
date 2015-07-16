package com.v3pm_prototype.calculation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CollectionCopier {

	/**
	 * creates an exact copy of the process-collection
	 */
	public static Collection<Process> createTemporaryProcessCollection(Collection<Process> collProcess) {
		Collection<Process> tempCollPocess = new HashSet<Process>();
		for (Iterator<Process> it = collProcess.iterator(); it.hasNext();) {
			Process p = it.next();
			Process copy = new Process(p.getName(), p.getId(), p.getQ(),
					p.getQmax(), p.getT(), p.getP(), p.getOop(), p.getD(),
					p.getV(), p.getFixedCosts(), p.getN(), p.getRoh(),
					p.getLamda(), p.getAlpha(), p.getThetaID_q(), p.getBeta(),
					p.getThetaID_t());
			tempCollPocess.add(copy);
		}
		return tempCollPocess;
	}

}
