package com.processbalancing.main;

import java.util.Comparator;

/**
 * This class is needed to compare two NPVs. This is necessary to sort the roadmaps by NPV.
 * 
 */
public class RMComparator implements Comparator<Roadmap> {

	@Override
	public int compare(Roadmap r1, Roadmap r2) {
		return Double.compare(r1.getNpv(), r2.getNpv());
	}

}
