package com.v3pm_prototype.main;

import java.util.List;
import java.util.ListIterator;

/**
 * Checks if there are duplicates in the roadmap. The check is only necessary if there are more than one projects per period allowed, because duplicate roadmaps
 * like [1,x,x,2] and [1,x,x,2] are already filtered during roadmap generation. This class checks for duplicates where the projects change their sequence within
 * one period like [12,34] and [21,34].
 * 
 */
public class DuplicateCheck {
	public static int DuplicateAmount = 0;  
	public static int DuplicateAmountQMIN = 0;   

	/**
	 * deletes roadmaps with the same NPV. This is by far the fastest approach to delete duplicates. Attention: The algorithm only works if the list
	 * of roadmaps is already sorted by NPV!
	 */
//	public static void deleteDuplicatesWithSameNPV(List<Roadmap> sortedCollRM) {
//		Roadmap tempRoadmap_previous = null;
//		for (ListIterator<Roadmap> itRM = sortedCollRM.listIterator(); itRM.hasNext();) {
//			Roadmap tempRoadmap = itRM.next();
//			if (tempRoadmap_previous != null) {
//				
//				if ((tempRoadmap_previous.getNpv() != tempRoadmap.getNpv()) && (tempRoadmap_previous.getNpv() <-5000000 )) {
//					DuplicateAmountQMIN++;	
//				}
//				
//				if (tempRoadmap_previous.getNpv() == tempRoadmap.getNpv()) {
//					DuplicateAmount++;
//										
//					itRM.remove();
//					continue;
//				}
//							
//			
//			}
//			tempRoadmap_previous = new Roadmap(tempRoadmap.getProjectSequence(), tempRoadmap.getNpv());
//		}
//	}

}