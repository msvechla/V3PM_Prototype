package com.processbalancing.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used to support the Calculator class. It is responsible for changing the values of the processes and projects according to the conducted
 * project.
 * 
 */
public class ProjectAndProcessModifier {

	/**
	 * loops through every process and modifies the values affected from the conducted project. Thereafter it loops through the project collection and modifies
	 * the values affected by the b-parameter.
	 */
	public static void modifyProcessesAndProjectsByProject(Collection<Process> tempCollPocess, Collection<Process> bufferedTempCollProcess,
			Project tempProject, Collection<Project> tempCollPoj_sorted, int projectNumberWithinPeriod, List<String> ProjectSequence) {
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// for each process
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// check each modification parameter of a project and modify the regarding processes
		for (Iterator<Process> itProcessModify = tempCollPocess.iterator(); itProcessModify.hasNext();) {
			Process tempProcess = itProcessModify.next();
			boolean projectRegardsProcess = false;
			if (tempProject.getI() == '*' || tempProject.getI() == tempProcess.getId()) {
				projectRegardsProcess = true;
			}
			
		
			// ------------------------------------------------------
			// a
			// ------------------------------------------------------
			if (projectRegardsProcess && tempProject.getA() != 0) {
				tempProcess.setOop(tempProcess.getOop() * tempProject.getA());
			}
			
			
			/*  MLe  alte Version konmplett auskommentiert
			// ------------------------------------------------------
			// e
			// ------------------------------------------------------
			if (projectRegardsProcess && tempProject.getE() != 0) {
				tempProcess.setT(tempProcess.getT() * tempProject.getE());
			} else {
				// degeneration-effect on time
				// differ between one or multiple projects per period
				if (Main.maxProjectsPerPeriod == 1) {
					tempProcess.setT(tempProcess.getT() * (1 + tempProcess.getV())); // degeneration
				} else {
					// check if it is the last handled project of a period.
					if (Main.maxProjectsPerPeriod == projectNumberWithinPeriod) {
						// check if there are any changes of t between handling the first and the last project in a period
						if (isTheSameValue(tempProcess.getT(), bufferedTempCollProcess, tempProcess.getId(), 't')) {
							tempProcess.setT(tempProcess.getT() * (1 + tempProcess.getV())); // degeneration
						}
					}
				}
			}
			
			*/
			
			// ------------------------------------------------------
			// e  mit Absolutem Effekt
			// ------------------------------------------------------
			if (projectRegardsProcess && tempProject.getE() != 0) {
				
				if (tempProject.getAbsRelt().equals("relativ")){
					
					tempProcess.setT(tempProcess.getT() * tempProject.getE());
				}
				
				else if (tempProject.getAbsRelt().equals("absolut")){
			
					tempProcess.setT(tempProcess.getT() + tempProject.getE());
					
					// check for time (time can't be lower than 0)	
					if (tempProcess.getT() < 0) {
						tempProcess.setT (0);
					}	
			
				}
				else if(!tempProject.getAbsRelt().equals("relativ") || !tempProject.getAbsRelt().equals("absolut")) {	
			
					System.out.println("Fehler, absoluter/relativer Effekt nicht korrekt angegeben bei " +tempProject.getName() );
		   			
			    }	
			
								
				// Test der Zeit bzgl. Effekt
				// System.out.println("time " +tempProcess.getT() );	
				
			}		
		
			  			
			
			else {
				// degeneration-effect on time
				// differ between one or multiple projects per period
				if (Main.maxProjectsPerPeriod == 1) {
					tempProcess.setT(tempProcess.getT() * (1 + tempProcess.getV())); // degeneration
				} else {
					// check if it is the last handled project of a period.
					if (Main.maxProjectsPerPeriod == projectNumberWithinPeriod) {
						// check if there are any changes of t between handling the first and the last project in a period
						if (isTheSameValue(tempProcess.getT(), bufferedTempCollProcess, tempProcess.getId(), 't')) {
							tempProcess.setT(tempProcess.getT() * (1 + tempProcess.getV())); // degeneration
						}
					}
				}
			}
			
			// ------------------------------------------------------
			// Platz für T MAX   Genau Zuordnung zu Roadmap ToDo
			// ------------------------------------------------------
						
			if (tempProcess.getT() > tempProcess.getTmax()) {
				
				System.out.println("Diese Roadmap verletzt TMAX " + ProjectSequence + "; Prozess: " + tempProcess.getName() + " hat die Grenze i.H.v. " + tempProcess.getTmax() + " mit dem Wert " + tempProcess.getT() +" überschritten.");
				//Test MLe
				tempProcess.setFixedCosts(10000000);
				
			}
		
			
			/*  MLe: 1zu1 Kopie der alten q Berechnung
			// ------------------------------------------------------
			// u
			// ------------------------------------------------------
			if (projectRegardsProcess && tempProject.getU() != 0) {
				tempProcess.setQ(tempProcess.getQ() * tempProject.getU());
			} else {
				// degeneration-effect on quality
				// differ between one or multiple projects per period
				if (Main.maxProjectsPerPeriod == 1) {
					tempProcess.setQ(tempProcess.getQ() * (1 - tempProcess.getD())); // degeneration
				} else {
					// check if it is the last handled project of a period.
					if (Main.maxProjectsPerPeriod == projectNumberWithinPeriod) {
						// check if there are any changes of q between handling the first and the last project in a period
						if (isTheSameValue(tempProcess.getQ(), bufferedTempCollProcess, tempProcess.getId(), 'q')) {
							tempProcess.setQ(tempProcess.getQ() * (1 - tempProcess.getD())); // degeneration
						}
					}
				}
			}
			*/
			
			// ------------------------------------------------------
			// u
			// ------------------------------------------------------
			if (projectRegardsProcess && tempProject.getU() != 0) {
				
				if (tempProject.getAbsRelq().equals("relativ")){
					
				tempProcess.setQ(tempProcess.getQ() * tempProject.getU());
			
				}
				
				else if (tempProject.getAbsRelq().equals("absolut")){
			
					tempProcess.setQ(tempProcess.getQ() + tempProject.getU());
					
					// check for quality (quality can't be lower than 0)	
					if (tempProcess.getQ() < 0) {
						tempProcess.setQ(0);
					}	
			
				}
				else if(!tempProject.getAbsRelq().equals("relativ") || !tempProject.getAbsRelq().equals("absolut")) {	
	
					System.out.println("Fehler, absoluter/relativer Effekt nicht korrekt angegeben bei " +tempProject.getName() );		   			
				}			
			
		
			}
			else {
				// degeneration-effect on quality
				// differ between one or multiple projects per period
				if (Main.maxProjectsPerPeriod == 1) {
					tempProcess.setQ(tempProcess.getQ() * (1 - tempProcess.getD())); // degeneration
				} else {
					// check if it is the last handled project of a period.
					if (Main.maxProjectsPerPeriod == projectNumberWithinPeriod) {
						// check if there are any changes of q between handling the first and the last project in a period
						if (isTheSameValue(tempProcess.getQ(), bufferedTempCollProcess, tempProcess.getId(), 'q')) {
							tempProcess.setQ(tempProcess.getQ() * (1 - tempProcess.getD())); // degeneration
						}
					}
				}
			}
				
			// check for qMax (q can't be higher than qMax)	
			if (tempProcess.getQ() >= tempProcess.getQmax()) {
				tempProcess.setQ(tempProcess.getQmax());
			}
	
		
			//Platz für q min
			//Abfrage, korrekt. Ausgabe enthält noch keine Information darüber, welche Roadmap betroffen ist.
			
				
			if (tempProcess.getQ() < tempProcess.getQmin()) {
				
				System.out.println("Diese Roadmap verletzt QMIN " + ProjectSequence + "; Prozess: " + tempProcess.getName() + " hat die Grenze i.H.v. " + tempProcess.getQmin() + " mit dem Wert " + tempProcess.getQ() +" unterschritten.");
				//Test MLe
				tempProcess.setFixedCosts(10000000);
			

			}
			
			
			/// MLe Ende
			
			// ------------------------------------------------------
			// m
			// ------------------------------------------------------
		/*   Ursprüngliche Variante auskommentiert!
			if (projectRegardsProcess && tempProject.getM() != 0) {
				tempProcess.setOop(tempProcess.getOop() * tempProject.getM());
			}
		}
		
		
		*/
		
		// Neu MLe  Oop mit absolutem Effekt - Scheint korrekt zu sein. Ausführlicher Test erforderlich
		
		if (projectRegardsProcess && tempProject.getM() != 0) {
			
			if (tempProject.getAbsRelOop().equals("relativ")){
			
						tempProcess.setOop(tempProcess.getOop() * tempProject.getM());
			}
			
			else if (tempProject.getAbsRelOop().equals("absolut")){
				
						tempProcess.setOop(tempProcess.getOop() + tempProject.getM());
						
						// check for Oop (Oop can't be lower than 0)	
						if (tempProcess.getOop() < 0) {
							tempProcess.setOop(0);
						}	
				
			}
			else{
				System.out.println("Fehler, absoluter/relativer Effekt nicht korrekt angegeben bei " +tempProject.getName() );
				
			}
				
				
			}
		
		// ------------------------------------------------------
		// FixedCosts  MLe
		// ------------------------------------------------------	
		
		
		if (tempProject.getType().equals("processLevel") && tempProject.getFixedCostEffect()!= 0)
			{
				
			tempProcess.setFixedCosts(tempProcess.getFixedCosts() + tempProject.getFixedCostEffect());
				
			// check for FixedCosts (FixedCosts can't be lower than 0)	
			if (tempProcess.getFixedCosts() < 0) {
				tempProcess.setFixedCosts(0);
			
			}
				
		
		}
	
		}
		
		// ------------------------------------------------------
		// b
		// ------------------------------------------------------
		if (tempProject.getB() != 0) {
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// for each project
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// check all future projects for process-level-projects
			for (Iterator<Project> itProjB = tempCollPoj_sorted.iterator(); itProjB.hasNext();) {
				Project tempProjectFutureB = itProjB.next();
				if (tempProjectFutureB.getType().equals(Main.processLevel) && tempProject.getPeriod() < tempProjectFutureB.getPeriod()) {
					tempProjectFutureB.setOinv(tempProjectFutureB.getOinv() * tempProject.getB());
				}
			}
		}

	
		
		
	}

	/**
	 * checks if q/t of two given processes have the same values
	 * 
	 * @param dimension_value
	 *            (value of q or t)
	 * @param dimension
	 *            (q or t)
	 */
	private static boolean isTheSameValue(double dimension_value, Collection<Process> bufferedTempCollProcess, char processId, char dimension) {
		for (Iterator<Process> itProcessBuffer = bufferedTempCollProcess.iterator(); itProcessBuffer.hasNext();) {
			Process tempProcessBuffer = itProcessBuffer.next();
			if (dimension == 't') {
				if (tempProcessBuffer.getId() == processId && tempProcessBuffer.getT() == dimension_value) {
					return true;
				}
			} else if (dimension == 'q') {
				if (tempProcessBuffer.getId() == processId && tempProcessBuffer.getQ() == dimension_value) {
					return true;
				}
			}
		}
		return false;
	}

}
