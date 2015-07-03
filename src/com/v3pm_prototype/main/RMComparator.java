package com.v3pm_prototype.main;

import java.util.Comparator;

import com.v3pm_prototype.rmgeneration.RoadMap;

/**
 * This class is needed to compare two NPVs. This is necessary to sort the roadmaps by NPV.
 * 
 */
public class RMComparator implements Comparator<RoadMap> {

	@Override
	public int compare(RoadMap r1, RoadMap r2) {
		return Double.compare(r1.getNpv(), r2.getNpv());
	}

}
