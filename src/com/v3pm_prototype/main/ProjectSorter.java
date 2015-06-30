package com.v3pm_prototype.main;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ProjectSorter {

	/**
	 * sorts projects within a period. This is nice to have because due to roadmap generation and duplicate deletion, the order of projects within a period is
	 * different every time the program is executed.
	 */
	public static void sortProjects(List<Roadmap> collRM) {
		//
		List<String> projectSequence;
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each roadmap
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (Iterator<Roadmap> it_rm = collRM.iterator(); it_rm.hasNext();) {
			projectSequence = it_rm.next().getProjectSequence();
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// for each period within the project-sequence
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			for (int i = 0; i < projectSequence.size(); i++) {
				char[] projectsInPeriod = projectSequence.get(i).toCharArray();
				Arrays.sort(projectsInPeriod);
				projectSequence.set(i, String.valueOf(projectsInPeriod));
			}
		}
	}

}
