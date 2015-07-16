package com.v3pm_prototype.main;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.v3pm_prototype.excel.ExcelImporter;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

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
	private static RMGenerator rmGenerator;
	public static final String RETURN_STRING_SUCCESSFUL = "Kalkulation erfolgreich abgeschlossen";
	public static final String processLevel = "processLevel";
	
	public static void main(String[] args) {
		// -----------------------------------------------------------------------------
		// create the user interface
		// -----------------------------------------------------------------------------
		// callback method will trigger Main.processMainCalculation(File Excelfile)
		// exception handling happens in the SwingUI class
//		SwingUI sUI = new SwingUI();
//		sUI.createSwingUI();
	}

	public static String processMainCalculation(File excelFile) throws Exception {
		// -----------------------------------------------------------------------------
		// read excel input-data and write them into the collections and the static class variables
		// -----------------------------------------------------------------------------
		
		//RunConfiguration.standardConfig = new RunConfiguration();
		//ExcelImporter.importAllExcelData(excelFile, RunConfiguration.standardConfig);

		// -----------------------------------------------------------------------------
		// generate all possible roadmaps and filter them by restrictions
		// -----------------------------------------------------------------------------
		//RMGenerator rmGenerator = new RMGenerator();
		//List<Roadmap> collRM = rmGenerator.generateRoadmapCollection(collProj);

		double millisStart = System.currentTimeMillis();
		RMGenerator rmGenerator = new RMGenerator(RunConfiguration.standardConfig);
		//rmGenerator.call();
		
List<RoadMap> rmList = rmGenerator.getGeneratedRoadmaps();
		
		double millisFinish = System.currentTimeMillis();
		
//		for(RMContainer rmc : RMContainer.lstRMContainerSingle){
//			System.out.println("Single");
//			System.out.println("---"+rmc.getImplementedProjects()+"---");
//		for(com.v3pm_prototype.rmgeneration.RoadMap rm: rmc.getLstRM()){
//			System.out.println(rm);
//		}
//	}
//	
//	for(RMContainer rmc : RMContainer.lstRMContainerCombined){
//		System.out.println("Combined");
//		System.out.println("---"+rmc.getImplementedProjects()+"---");
//		for(com.v3pm_prototype.rmgeneration.RoadMap rm: rmc.getLstRM()){
//			System.out.println(rm);
//		}
//		
//	}
	
	//BEFORE PRE-COMBINEDCONTAINER CHECK: 28071 Roadmaps generated in 365.0ms
	//AFTER: 8850 Roadmaps generated in 221.0ms
	//AFTER: 7048 Roadmaps generated in 218.0ms
		
		// -----------------------------------------------------------------------------
		// calculate NPV of each roadmap
		// -----------------------------------------------------------------------------
		
		for(RoadMap rm : rmList){
			System.out.println(rm);
		}
		//System.out.println(rmList.size()+" Roadmaps generated in "+(millisFinish-millisStart)+"ms.   "+RMContainer.countRoadMapsGenerated+ " Roadmaps before Post-Gen Check");
		
		//Calculator.calculateNPVs(rmList, RunConfiguration.standardConfig.getLstProcesses(), RunConfiguration.standardConfig.getLstProjects(),RunConfiguration.standardConfig);

		// -----------------------------------------------------------------------------
		// sort roadmaps by NPV
		// -----------------------------------------------------------------------------
		Collections.sort(rmList);

		// -----------------------------------------------------------------------------
		// print output-data to excel file
		// -----------------------------------------------------------------------------
		//ExcelExporter.exportToExcel(rmList, excelFile, RunConfiguration.standardConfig.getLstProjects().size());

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
