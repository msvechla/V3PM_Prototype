package com.v3pm_prototype.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.v3pm_prototype.main.Main;
import com.v3pm_prototype.main.Process;
import com.v3pm_prototype.main.Project;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

/**
 * This class is responsible for importing the data from the Excel file and writing them it into the collections and variables of the Main class. The Apache POI
 * 3.10 library is used for the work with Excel files. For more information about the POI-Library see: http://poi.apache.org/
 */
public class ExcelImporter {

	private static int START_ROW_FOR_PROJECT_SEARCH = 34; // defines in which row the search for projects begins
	private static int START_CELL_FOR_PROJECT_SEARCH = 0; // defines in which column the search for projects begins

	private static int START_ROW_FOR_PROCESS_SEARCH = 34; // defines in which row the search for processes begins
	private static int START_CELL_FOR_PROCESS_SEARCH = 23; // defines in which column the search for processes begins

	private static int START_ROW_FOR_GENERAL_VALUES_SEARCH = 6; // defines in which row the search for general data begins
	private static int START_CELL_FOR_GENERAL_VALUES_SEARCH = 3; // defines in which column the search general data begins

	private static int START_ROW_FOR_BUDGET_RESTRICTION_VALUES_SEARCH = 6; // defines in which row the search for budget restrictions for each period begins
	private static int START_CELL_FOR_BUDGET_RESTRICTION_VALUES_SEARCH = 24; // defines in which column the search budget restrictions for each period begins

	private static Cell cell;

	/**
	 * this is the main method of this class and sums up the other methods by calling them one after another
	 * @param standardConfig 
	 */
	public static void importAllExcelData(File excelFile, RunConfiguration standardConfig) throws Exception {
		FileInputStream file = null;
		try {
			// load excel file
			file = new FileInputStream(excelFile);
			// get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			// get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			// call methods to import data for Projects, Processes and general static values
			generateProjects(sheet, standardConfig);
			importExcelDataAndFillProjectCollection(sheet, standardConfig);
			importExcelDataAndFillProcessCollection(sheet, standardConfig);
			importExcelDataAndFillGeneralStaticValues(sheet, standardConfig);
			importExcelDataAndFillMaximumBudgetPerPeriodList(sheet, standardConfig);
		} finally {
			file.close();
		}
	}
	
	/**
	 * Generates placeholders for all Projects beforehand, so Suc, Pre restrictions can be set with type Project
	 * @param sheet
	 * @param standardConfig
	 */
	private static void generateProjects(HSSFSheet sheet, RunConfiguration standardConfig){
		int rowCount = START_ROW_FOR_PROJECT_SEARCH;
		cell = sheet.getRow(rowCount).getCell(START_CELL_FOR_PROJECT_SEARCH);
		while (cell != null) {
			if(cell.getStringCellValue().equals("")){
				break; // reached the end of the projects-list
			}
			
			int tempID = standardConfig.getLstProjects().size()+1;
			Project project = new Project(tempID, cell.getStringCellValue(), 1, "", '0', 0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, null, 0, "", "", "");
			standardConfig.getLstProjects().add(project);
			
			// Initialize cell for next iteration
			rowCount++;
			cell = sheet.getRow(rowCount).getCell(START_CELL_FOR_PROJECT_SEARCH);
		}
	}
		
		

	/**
	 * iterates through the project-specific, non-empty rows of the Excel file, read the data and after each row, creates a project-instance and saves it in the
	 * project collection
	 * @param standardConfig 
	 */
	private static void importExcelDataAndFillProjectCollection(HSSFSheet sheet, RunConfiguration standardConfig) {
		int rowCount = START_ROW_FOR_PROJECT_SEARCH;
		cell = sheet.getRow(rowCount).getCell(START_CELL_FOR_PROJECT_SEARCH);
		int id = 0;
		while (cell != null) {
			if(cell.getStringCellValue().equals("")){
				break; // reached the end of the projects-list
			}
			
			id++;
			Project project = standardConfig.getProject(id);		
			
			// set Number of Periods
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				project.setNumberOfPeriods((int)cell.getNumericCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				project.setNumberOfPeriods((Integer.parseInt(cell.getStringCellValue())));
			}
			
			//Set Mandatory
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.SetMandaytory(cell.getStringCellValue());
			
			// set type
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setType(cell.getStringCellValue());
			// set i
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				project.setI(Double.toString(cell.getNumericCellValue()).toCharArray()[0]);
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				project.setI(cell.getStringCellValue().toCharArray()[0]);
			}
			// set Oinv
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setOinv(cell.getNumericCellValue());
			// set a
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setA(cell.getNumericCellValue());
			// set b
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setB(cell.getNumericCellValue());
			// set e
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setE(cell.getNumericCellValue());
			// set u
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setU(cell.getNumericCellValue());
			// set m
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setM(cell.getNumericCellValue());
			// set EIP (Earliest Implementation Period)
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setEarliestImplementationPeriod((int) cell.getNumericCellValue()-1);
			// set LIP (Latest Implementation Period)
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setLatestImplementationPeriod((int) cell.getNumericCellValue()-1);
			
			// set Predecessor-Project
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if((int)cell.getNumericCellValue() != 0){
					project.setPredecessorProject(standardConfig.getProject((int)cell.getNumericCellValue()));
				}
			}
			
			// set Successor-Project
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if((int)cell.getNumericCellValue() != 0){
					project.setSuccessorProject(standardConfig.getProject((int)cell.getNumericCellValue()));
				}
			}
			
			// set TogetherInPeriodWithProject
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				project.setTogetherInPeriodWith(standardConfig.getProject((int) cell.getNumericCellValue()));
			} 
			
			// set NotTogetherInPeriodWithProject
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				project.setNotTogetherInPeriodWith(standardConfig.getProject((int) cell.getNumericCellValue()));
			}
			
			// set GloMutEx
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				project.setGloMutEx(standardConfig.getProject((int) cell.getNumericCellValue()));
			} 
			
			// set GloMutDep
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				project.setGloMutDep(standardConfig.getProject((int) cell.getNumericCellValue()));
			}
			
			// set Fixkosteneffekt  Neu MLe
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setFixedCostEffect(cell.getNumericCellValue());
			
			// set absolut/relativ q  Neu MLe
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setAbsRelq(cell.getStringCellValue());
	
			// set absolut/relativ t  Neu MLe
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setAbsRelt(cell.getStringCellValue());
			
			// set absolut/relativ Oop  Neu MLe
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			project.setAbsRelOop(cell.getStringCellValue());
			
			project.adjustForMultiPeriodScenario();
			
			// Initialize cell for next iteration
			rowCount++;
			cell = sheet.getRow(rowCount).getCell(START_CELL_FOR_PROJECT_SEARCH);
		}
	}

	/**
	 * iterates through the the process-specific, non-empty rows of the Excel file, read the data and after each row, creates a process-instance and saves it in
	 * the process collection
	 * @param standardConfig 
	 */
	private static void importExcelDataAndFillProcessCollection(HSSFSheet sheet, RunConfiguration standardConfig) {
		int rowCount = START_ROW_FOR_PROCESS_SEARCH;
		cell = sheet.getRow(rowCount).getCell(START_CELL_FOR_PROCESS_SEARCH);
		while (cell != null) {
			if(cell.getStringCellValue().equals("")){
				break; // reached the end of the process-list
			}
			Process process = new Process(cell.getStringCellValue(), '0', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			// set ID
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				process.setId(Double.toString(cell.getNumericCellValue()).toCharArray()[0]);
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				process.setId(cell.getStringCellValue().toCharArray()[0]);
			}
			// set q
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setQ(cell.getNumericCellValue());
			// set qmax
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setQmax(cell.getNumericCellValue());
			// set t
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			// if (cell != null) {
			process.setT(cell.getNumericCellValue());
			// }
			// set p
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setP(cell.getNumericCellValue());
			// set Oop
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setOop(cell.getNumericCellValue());
			// set d
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setD(cell.getNumericCellValue());
			// set v
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setV(cell.getNumericCellValue());
			// set FixedCosts MLe
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setFixedCosts(cell.getNumericCellValue());			
			// set qmin MLe
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setQmin(cell.getNumericCellValue());				
			// set tmax MLe
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setTmax(cell.getNumericCellValue());				
			// set roh
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setRoh(cell.getNumericCellValue());
			// set lamda
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setLamda(cell.getNumericCellValue());
			// set alpha
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setAlpha(cell.getNumericCellValue());
			// set thetaID_q
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setThetaID_q((int) cell.getNumericCellValue());
			// set beta
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setBeta(cell.getNumericCellValue());
			// set thetaID_t
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
			process.setThetaID_t((int) cell.getNumericCellValue());
			
			// add process to standard config and initialize cell for next iteration
			standardConfig.getLstProcesses().add(process);
			rowCount++;
			cell = sheet.getRow(rowCount).getCell(START_CELL_FOR_PROCESS_SEARCH);
		}
	}

	/**
	 * iterates through the the cells of the Excel file, read the data and writes them into the according variables of the Main class
	 * @param standardConfig 
	 */
	private static void importExcelDataAndFillGeneralStaticValues(HSSFSheet sheet, RunConfiguration standardConfig) {
		int rowCount = START_ROW_FOR_GENERAL_VALUES_SEARCH;
		
		cell = sheet.getRow(rowCount++).getCell(START_CELL_FOR_GENERAL_VALUES_SEARCH);
		int periodsUnderInvestigation = (int) cell.getNumericCellValue();
		
		cell = sheet.getRow(rowCount++).getCell(cell.getColumnIndex());
		standardConfig.setCountProjectsMaxPerPeriod((int) cell.getNumericCellValue());
		standardConfig.setCountPeriods(periodsUnderInvestigation / standardConfig.getCountProjectsMaxPerPeriod());
		
		cell = sheet.getRow(rowCount++).getCell(cell.getColumnIndex());
		standardConfig.setDiscountRate(cell.getNumericCellValue());
		
		cell = sheet.getRow(rowCount++).getCell(cell.getColumnIndex());
		standardConfig.setPeriodWithNoScheduledProjects((int) cell.getNumericCellValue());

		cell = sheet.getRow(rowCount++).getCell(cell.getColumnIndex());
		standardConfig.setBudgetMaxPerPeriod((int) cell.getNumericCellValue());
		
		//Neu MLe
		cell = sheet.getRow(rowCount).getCell(cell.getColumnIndex());
		standardConfig.setBudgetMaxPerPeriod((int) cell.getNumericCellValue());
	}

	/**
	 * iterates through the the cells of the MaximumBudgetPerPeriod-table of the Excel file, read the data and writes them into the Array-List of the Main class
	 * @param standardConfig 
	 */
	private static void importExcelDataAndFillMaximumBudgetPerPeriodList(HSSFSheet sheet, RunConfiguration standardConfig) {
		
		standardConfig.setBudgetMaxforEachPeriod(new ArrayList<Double>());

		int maxProjectsAllowedInExcelFile = getMaxProjectsAllowedInExcelFile(sheet);
		cell = sheet.getRow(START_ROW_FOR_BUDGET_RESTRICTION_VALUES_SEARCH + 1).getCell(START_CELL_FOR_BUDGET_RESTRICTION_VALUES_SEARCH);
		for (int i = 0; i < maxProjectsAllowedInExcelFile; i++) {
			// check if there is a number in this cell because it could be not null or empty
			if (cell.getNumericCellValue() != 0) {
				// add maximum Budget for a Period to the ArrayList
				standardConfig.getBudgetMaxforEachPeriod().add(cell.getNumericCellValue());
			} else {
				standardConfig.getBudgetMaxforEachPeriod().add(0.0);
			}
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
		}
	}

	/**
	 * checks how many fields the table for MaximumBudgetPerPeriod has. Therefore it iterates through the headline of the table and counts the filled cells
	 */
	private static int getMaxProjectsAllowedInExcelFile(HSSFSheet sheet) {
		cell = sheet.getRow(START_ROW_FOR_BUDGET_RESTRICTION_VALUES_SEARCH).getCell(START_CELL_FOR_BUDGET_RESTRICTION_VALUES_SEARCH);
		int maxProjectsAllowedInExcelFile = 0;
		while (cell != null) {
			// check if there is a number in this cell because cell could be not null but empty
			if (cell.getNumericCellValue() != 0) {
				maxProjectsAllowedInExcelFile++;
			}
			cell = sheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex() + 1);
		}
		return maxProjectsAllowedInExcelFile;
	}

}