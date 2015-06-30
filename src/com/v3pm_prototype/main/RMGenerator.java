package com.v3pm_prototype.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

/**
 * Creates roadmaps for each possible sequence without duplicates like [1,x,x] and [1,x,x]. Roadmaps like [12,3] and [21,3] are not seen as duplicates. The
 * implementation of no projects at all is possible (e.g. [x,x,x]). Combinatoricslib-library is used for creating subsets and permutations. For more information
 * see the documentation of the combinatoricslib-library: https://code.google.com/p/combinatoricslib/#1._Simple_permutations
 */
public class RMGenerator {

	private List<Roadmap> collRM;

	public List<Roadmap> generateRoadmapCollection(Collection<Project> collProj) {

		collRM = new LinkedList<Roadmap>(); // LinkedList has better performance in adding and deleing

		// -----------------------------------------------------------------------------
		// create a list of all project-IDs (from project-collection)
		// -----------------------------------------------------------------------------
		List<String> collRMIDList = createListOfAllProjectIDs(collProj);

		// -----------------------------------------------------------------------------
		// create subsets out of projects
		// -----------------------------------------------------------------------------
		// subsets for [1,2] would be: [], [1], [2], [1,2]
		// create vector with a list of all project-IDs and generate the subsets
		ICombinatoricsVector<String> projectSetVector = Factory.createVector(collRMIDList);
		Generator<String> collSubsets = Factory.createSubSetGenerator(projectSetVector);

		// -----------------------------------------------------------------------------
		// create all permutation of each subset and add them to the roadmap-collection
		// -----------------------------------------------------------------------------
		createPermutations(collSubsets, collProj);

		return collRM;
	}

	/**
	 * create permutations (without duplicates) of this subset and add them to the roadmap-collection
	 */
	private void createPermutations(Generator<String> collSubsets, Collection<Project> collProj) {
		for (Iterator<ICombinatoricsVector<String>> it_subsets = collSubsets.iterator(); it_subsets.hasNext();) {
			ICombinatoricsVector<String> subset = it_subsets.next();
			// create a list of all Project-IDs and empty-project-character out of a subset in String format
			List<String> subsetProjectIDList = new ArrayList<String>();
			for (Iterator<String> iString = subset.iterator(); iString.hasNext();) {
				subsetProjectIDList.add(iString.next());
			}
			fillUpWithEmptyProjects(subsetProjectIDList);
			if (subsetProjectIDList.size() <= Main.periodsUnderInvestigation) {
				// create a vector with a list of the subset and generate all permutations of this subset
				ICombinatoricsVector<String> initialVector = Factory.createVector(subsetProjectIDList);
				Generator<String> collPermutations = Factory.createPermutationGenerator(initialVector);
				for (Iterator<ICombinatoricsVector<String>> it_permutations = collPermutations.iterator(); it_permutations.hasNext();) {
					addToCollRM(it_permutations.next(), collProj);
				}
			}
		}
	}

	/**
	 * creates a list of all project-IDs (including 1 empty project indicating that no project is implemented in this project-slot) formatted as a String
	 */
	private List<String> createListOfAllProjectIDs(Collection<Project> collProj) {
		List<String> collRMIDList = new ArrayList<String>(); //
		for (Iterator<Project> i = collProj.iterator(); i.hasNext();) {
			collRMIDList.add(String.valueOf(i.next().getId()));
		}
		return collRMIDList;
	}

	/**
	 * fill subset with empty projects until project-slots under investigation match the amount of projects
	 */
	private void fillUpWithEmptyProjects(List<String> subsetProjectIDList) {
		while (subsetProjectIDList.size() < Main.periodsUnderInvestigation) {
			subsetProjectIDList.add(String.valueOf(Main.CHAR_FOR_EMPTY_PROJECTS));
		}
	}

	/**
	 * check for restrictions and add the project-sequence to the roadmap-collection
	 */
	private void addToCollRM(ICombinatoricsVector<String> permutation, Collection<Project> collProj) {
		int tempCount = 0;
		StringBuilder SB = new StringBuilder();
		List<String> tempProjectSequence = new ArrayList<String>();
		// create the projectSequence out of the permutation
		for (Iterator<String> it_perm = permutation.iterator(); it_perm.hasNext();) {
			SB.append(it_perm.next());
			if ((tempCount >= (Main.maxProjectsPerPeriod - 1)) || (it_perm.hasNext() == false)) {
				tempCount = 0;
				tempProjectSequence.add(SB.toString());
				SB.setLength(0);
				continue;
			}
			tempCount++;
		}
		// check for restrictions
		if (RMRestrictionHandler.breakRestrictionBeforeAddingToRoadMapCollection(tempProjectSequence, collProj) == false) {
			collRM.add(new Roadmap(tempProjectSequence, 0));
		}
	}

}