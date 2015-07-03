package com.v3pm_prototype.excel;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.v3pm_prototype.rmgeneration.RoadMap;

/**
 * This class is responsible for exporting the data to the Excel file and . The Apache POI 3.10 library is used for the work with Excel files. For more
 * information about the POI-Library see: http://poi.apache.org/
 */
public class ExcelExporter {

	private static int START_ROW_FOR_FULL_ROADMAP_PRINT = 11; // defines in which row the printing begins
	private static int START_CELL_FOR_FULL_ROADMAP_PRINT = 0; // defines in which column the printing begins
	private static final String TX_HEADER_EXCEL_SHEET = "Kalkulations-Output "; // String for the header of the Excel sheet
	private static final String TX_ROADMAP_HEADER = "ROADMAP"; // String for the header of roadmaps-column
	private static final String TX_NPV_HEADER = "NPV"; // String for the header of NPV-column
	private static final int MaxWritableRows = 60020; // xls-files only support 65535 rows
	private static HSSFWorkbook workbook;
	private static StringBuilder SB = new StringBuilder();
	private static Cell cell;
	private static Row row;
	private static HSSFSheet sheet;
	private static int rowcounter;

	public static void exportToExcel(List<RoadMap> collRM, File excelFile, int numberOfProjects) throws Exception {
		FileInputStream file = null;
		try {
			file = new FileInputStream(excelFile);
			workbook = new HSSFWorkbook(file);

			// ---------------------------------------------------------------------------------------
			// copy a sheet and set the sheet-name with a timestamp
			// ---------------------------------------------------------------------------------------
			sheet = workbook.cloneSheet(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String dateTime = sdf.format(new Date());
			workbook.setSheetName(workbook.getSheetIndex(sheet), dateTime);
			row = sheet.createRow(0);
			cell = row.createCell(4);
			cell.setCellValue(TX_HEADER_EXCEL_SHEET + dateTime);
			cell.setCellStyle(getBoldStyle());

			// ---------------------------------------------------------------------------------------
			// delete rows with text and variable explanation
			// ---------------------------------------------------------------------------------------
			deleteRowsWithTextAndVariableExplanations();

			// ---------------------------------------------------------------------------------------
			// write header for the full print of the roadmap
			// ---------------------------------------------------------------------------------------
			writeHeaderForRoadmapList(numberOfProjects);

			// ---------------------------------------------------------------------------------------
			// write the list of roadmaps and NPVs in descending order
			// ---------------------------------------------------------------------------------------
			writeRoadmapAndNPVListDesc(collRM, numberOfProjects);

			// ---------------------------------------------------------------------------------------
			// write the list of roadmaps and NPVs in ascending order
			// ---------------------------------------------------------------------------------------
			writeRoadmapAndNPVListAsc(collRM, numberOfProjects);

			// Set new sheet as active sheet -> if excel file gets opened, it will show this sheet
			workbook.setActiveSheet(workbook.getSheetIndex(sheet));
			// Set the focus to the first cell-> if excel file gets opened, it will show the sheet scrolled to the top/left
			sheet.showInPane(0, 0);

		} finally {
			file.close();
		}
		FileOutputStream outFile = null;
		outFile = new FileOutputStream(excelFile);
		workbook.write(outFile);
		outFile.close(); // no finally block necessary because if the the file is open, the closing is not possible anyway
	}

	/**
	 * removes the content of the rows filled with explanatory text and moves the necessary rows further up to the place where the content was deleted
	 */
	private static void deleteRowsWithTextAndVariableExplanations() {
		for (int i = 0; i < 19; i++) {
			sheet.shiftRows(12, sheet.getLastRowNum(), -1);
		}
		sheet.shiftRows(2, sheet.getLastRowNum(), -1);
		sheet.shiftRows(2, sheet.getLastRowNum(), -1);
		sheet.shiftRows(2, sheet.getLastRowNum(), -1);
		sheet.shiftRows(2, sheet.getLastRowNum(), -1);
		for (int i = 27; i < 150; i++) {
			if (sheet.getRow(i) != null) {
				sheet.removeRow(sheet.getRow(i));
			}
		}
	}

	private static void writeHeaderForRoadmapList(int numberOfProjects) {
		row = sheet.createRow(START_ROW_FOR_FULL_ROADMAP_PRINT + numberOfProjects);
		cell = row.createCell(START_CELL_FOR_FULL_ROADMAP_PRINT);
		cell.setCellValue(TX_ROADMAP_HEADER);
		cell.setCellStyle(getBoldStyle());
		cell = row.createCell(START_CELL_FOR_FULL_ROADMAP_PRINT + 1);
		cell.setCellValue(TX_NPV_HEADER + "(desc)");
		cell.setCellStyle(getBoldStyle());
		cell = row.createCell(START_CELL_FOR_FULL_ROADMAP_PRINT + 3);
		cell.setCellValue(TX_ROADMAP_HEADER);
		cell.setCellStyle(getBoldStyle());
		cell = row.createCell(START_CELL_FOR_FULL_ROADMAP_PRINT + 3 + 1);
		cell.setCellValue(TX_NPV_HEADER + "(asc)");
		cell.setCellStyle(getBoldStyle());
	}

	private static void writeRoadmapAndNPVListDesc(List<RoadMap> collRM, int numberOfProjects) {
		rowcounter = START_ROW_FOR_FULL_ROADMAP_PRINT + numberOfProjects + 1;
		for (ListIterator<RoadMap> listItRM = collRM.listIterator(collRM.size()); listItRM.hasPrevious();) {
			RoadMap tempRoadmap = listItRM.previous();
			if (rowcounter < MaxWritableRows) {
				row = sheet.createRow(rowcounter); // to avoid nullpointer caused by empty rows
				printOneRoadmapAndNPV(rowcounter, tempRoadmap, 0);
			}
			rowcounter++;
		}
	}

	private static void writeRoadmapAndNPVListAsc(List<RoadMap> collRM, int numberOfProjects) {
		rowcounter = START_ROW_FOR_FULL_ROADMAP_PRINT + numberOfProjects + 1;
		for (Iterator<RoadMap> itRM = collRM.iterator(); itRM.hasNext();) {
			RoadMap tempRoadmap = itRM.next();
			if (rowcounter < MaxWritableRows) {
				printOneRoadmapAndNPV(rowcounter, tempRoadmap, 3);
			}
			rowcounter++;
		}
	}

	private static void printOneRoadmapAndNPV(int rowcounter, RoadMap tempRoadmap, int additionalColums) {
		row = sheet.getRow(rowcounter);
		cell = row.createCell(START_CELL_FOR_FULL_ROADMAP_PRINT + additionalColums);
		cell.setCellValue(tempRoadmap.toString());
		cell = row.createCell(START_CELL_FOR_FULL_ROADMAP_PRINT + additionalColums + 1);
		cell.setCellValue(tempRoadmap.getNpv());
	}

	/**
	 * defines a style for printing in bold and returns it
	 */
	private static HSSFCellStyle getBoldStyle() {
		// set Style to write bold in some excel-cells
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle styleBold = workbook.createCellStyle();
		styleBold.setFont(font);
		return styleBold;
	}

}
