package com.processbalancing.main;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.processbalancing.UI.SwingUI;
import com.processbalancing.excel.ExcelImporter;
import com.processbalancing.rmgeneration.RMContainer;
import com.processbalancing.rmgeneration.RunConfiguration;

/**
 * The whole procedure of the Process Balancing Calculation Tool is controlled from this Main-Class
 *   
 */

//  ToDo MLe
// qmin + t max -> Hinweis auf Roadmap
// 
// 
//
//
// Merke Wenn Projekt durchgeführt wird, aber kein Effekt bei abs/rel stattfindet, dann "relativ" wählen und "1" eintragen!



public class Main {
	public static final char CHAR_FOR_EMPTY_PROJECTS = 'x';
	public static final String RETURN_STRING_SUCCESSFUL = "Kalkulation erfolgreich abgeschlossen";
	public static final String processLevel = "processLevel";
	public static double discountRatePerPeriod;
	public static int periodsUnderInvestigation;
	public static int maxProjectsPerPeriod;
	public static int periodWithNoScheduledProjects;
	public static double budgetMaxPerPeriod; // global budget restrictions for every period
	public static List<Double> budgetMaxforEachPeriod; // use of an ArrayList because it keeps the order and is faster in accessing it via index
	public static double overarchingFixedOutflows;  //Neu MLe

	
	public static void main(String[] args) {
		// -----------------------------------------------------------------------------
		// create the user interface
		// -----------------------------------------------------------------------------
		// callback method will trigger Main.processMainCalculation(File Excelfile)
		// exception handling happens in the SwingUI class
		SwingUI sUI = new SwingUI();
		sUI.createSwingUI();
	}

	public static String processMainCalculation(File excelFile) throws Exception {
		// -----------------------------------------------------------------------------
		// create project- and process-collection
		// -----------------------------------------------------------------------------
		Collection<Process> collProcess = new HashSet<Process>();
		Collection<Project> collProj = new HashSet<Project>();

		// -----------------------------------------------------------------------------
		// read excel input-data and write them into the collections and the static class variables
		// -----------------------------------------------------------------------------
		ExcelImporter.importAllExcelData(collProj, collProcess, excelFile);

		// -----------------------------------------------------------------------------
		// generate all possible roadmaps and filter them by restrictions
		// -----------------------------------------------------------------------------
		//RMGenerator rmGenerator = new RMGenerator();
		//List<Roadmap> collRM = rmGenerator.generateRoadmapCollection(collProj);

		double millisStart = System.currentTimeMillis();
		com.processbalancing.rmgeneration.RMGenerator rmGenerator = new com.processbalancing.rmgeneration.RMGenerator();
		rmGenerator.generateRoadmaps(Project.projectList, RunConfiguration.standardConfig);
		
		double millisFinish = System.currentTimeMillis();
		
//		for(RMContainer rmc : RMContainer.lstRMContainerSingle){
//		System.out.println("");
//		for(RoadMap rm: rmc.getLstRM()){
//			System.out.println(rm);
//		}
//	}
//	
//	for(RMContainer rmc : RMContainer.lstRMContainerCombined){
//		System.out.println("");
//		for(RoadMap rm: rmc.getLstRM()){
//			System.out.println(rm);
//		}
//	}
	
	System.out.println(RMContainer.countRoadMapsGenerated+" Roadmaps generated in "+(millisFinish-millisStart)+"ms");
		
		// -----------------------------------------------------------------------------
		// add empty project to collection (for NPV calculation)
		// -----------------------------------------------------------------------------
		//collProj.add(new Project("No Project", CHAR_FOR_EMPTY_PROJECTS, "empty", CHAR_FOR_EMPTY_PROJECTS, 0, 0, 0, 0, 0, 0, 0, 0, '-', '-', '-', '-', 0, "empty", "empty", "empty"));

		// -----------------------------------------------------------------------------
		// calculate NPV of each roadmap
		// -----------------------------------------------------------------------------
		//Calculator.calculateNPVs(collRM, collProcess, collProj);

		// -----------------------------------------------------------------------------
		// sort roadmaps by NPV
		// -----------------------------------------------------------------------------
		//Collections.sort(collRM, new RMComparator());

		// -----------------------------------------------------------------------------
		// delete duplicates with same NPV
		// -----------------------------------------------------------------------------
//		if (maxProjectsPerPeriod > 1) {
//			DuplicateCheck.deleteDuplicatesWithSameNPV(collRM);
//		}

		// -----------------------------------------------------------------------------
		// sort projects within period
		// -----------------------------------------------------------------------------
//		if (maxProjectsPerPeriod > 1) {
//			ProjectSorter.sortProjects(collRM);
//		}

		// -----------------------------------------------------------------------------
		// print output-data to excel file
		// -----------------------------------------------------------------------------
//		ExcelExporter.exportToExcel(collRM, excelFile, collProj.size());

		// -----------------------------------------------------------------------------
		// print output-data to console (only for future debugging purposes)
		// -----------------------------------------------------------------------------
		// for (Iterator<Roadmap> itRM = collRM.iterator(); itRM.hasNext();) {
		// Roadmap tempRoadmap = itRM.next();
		//  List<String> projectSequence = tempRoadmap.getProjectSequence();
		//  for (Iterator<String> itPS = projectSequence.iterator(); itPS.hasNext();) {
		//  System.out.print(itPS.next());
		//  System.out.print(" ");
		//  }
		//  System.out.print(" NPV: " + tempRoadmap.getNpv());
		//  System.out.println(" ");
		//  }
		 
		// MLe
		
//		System.out.println("OverallAmount:                        "+ RMRestrictionHandler.OverallAmount);
//		System.out.println("AmountisNoProjectInPeriodRestriction: "+ RMRestrictionHandler.AmountisNoProjectInPeriodRestriction);
//		System.out.println("AmountisTogetherWithRestriction:      "+ RMRestrictionHandler.AmountisTogetherWithRestriction);
//		System.out.println("AmountisNotTogetherWithRestriction:   "+ RMRestrictionHandler.AmountisNotTogetherWithRestriction);
//		System.out.println("AmountisPredecessorRestriction:       "+ RMRestrictionHandler.AmountisPredecessorRestriction);
//		System.out.println("AmountisSuccessorRestriction:         "+ RMRestrictionHandler.AmountisSuccessorRestriction);
//		System.out.println("AmountisEarliestAndLatest:            "+ RMRestrictionHandler.AmountisEarliestAndLatest);
//		System.out.println("AmountisMaxBudgetRestriction:         "+ RMRestrictionHandler.AmountisMaxBudgetRestriction);
//		System.out.println("AdmissibleAmount:                     "+ RMRestrictionHandler.AdmissibleAmount);	
//		System.out.println("DuplicateAmountQMIN:                  "+ DuplicateCheck.DuplicateAmountQMIN);
//		System.out.println("");	
//		System.out.println("Final Amount:                         "+ (RMRestrictionHandler.AdmissibleAmount - DuplicateCheck.DuplicateAmount));
		
		
		return RETURN_STRING_SUCCESSFUL;
	}
}
