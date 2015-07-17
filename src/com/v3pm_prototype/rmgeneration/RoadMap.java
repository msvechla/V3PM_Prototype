package com.v3pm_prototype.rmgeneration;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.database.DBProject;
import com.v3pm_prototype.main.Main;

public class RoadMap implements Comparable<RoadMap>{
	private Project[][] rmArray;
	public HashSet<Integer> implementedProjectIDs;
	private double npv;
	private boolean restrictionBroken = false;
	public static int equalsCalls = 0;

	public RoadMap(Project[][] rmArray, HashSet<Integer> implementedProjectIDs) {
		this.rmArray = rmArray;
		this.implementedProjectIDs = implementedProjectIDs;
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		for(int period=0; period < rmArray.length;period++){
			sb.append("[ ");
			for(int slot=0; slot< rmArray[period].length;slot++){
				sb.append(rmArray[period][slot]+" ");
			}
			sb.append("] ");
		}
		
		DecimalFormat df = new DecimalFormat("#,###.00 €");
		sb.append(df.format(npv));
		
		return sb.toString();
	}
	
	public Project[][] getRMArray(){
		return this.rmArray;
	}
	
	public Collection<Project> createCollection(RunConfiguration config){
		
		Collection<Project> collProjects = new LinkedHashSet<Project>();
		HashSet<Project> implementedProjects = new HashSet<Project>();
		
		for(int period = 0; period < config.getPeriods(); period++){
			for(int slot = 0; slot < config.getSlotsPerPeriod();slot++){
				Project p = this.rmArray[period][slot];
				if(p != null){
					Project copy = new Project(p.getId(), p.getName(), p.getNumberOfPeriods(), p.getType(), p.getFixedCosts(), p.getOinv(),p.getI(), p.getA(), p.getB(), p.getE(), p.getU(),
							p.getM(), p.getAbsRelQ(), p.getAbsRelT(), p.getAbsRelOop());
					copy.setPeriod(period);
					
					//Set the starting period
					if(implementedProjects.contains(copy)){
						for(Project i : implementedProjects){
							if(i.getId() == copy.getId()){
								copy.setStartPeriod(i.getStartPeriod());
								break;
							}
						}
					}else{
						copy.setStartPeriod(period);
						implementedProjects.add(copy);
					}
					
					collProjects.add(copy);
				}else{
					Project emptyProject = new Project(-1, "Empty", 0, "", 0, 0, 0, 0, 0, 0, 0, 0, null, null, null);
					emptyProject.setPeriod(period);
					collProjects.add(emptyProject);
				}
			}
		}
		return collProjects;
	}
	
	
//	public Project[][] createTempRMArrayCopy(RunConfiguration config){
//		
//		Project[][] rmArrayCopy = new Project[config.getCountPeriods()][config.getCountProjectsMaxPerPeriod()];
//		HashSet<Project> implementedProjects = new HashSet<Project>();
//		
//		for(int period = 0; period < this.rmArray.length; period++){
//			for(int slot = 0; slot < rmArray[period].length;slot++){
//				Project p = this.rmArray[period][slot];
//				if(p != null){
//					Project copy = new Project(p.getId(), p.getName(), p.getNumberOfPeriods(), p.getType(), p.getI(), p.getOinv(), p.getA(), p.getB(), p.getE(), p.getU(),
//							p.getM(), p.getEarliestImplementationPeriod(), p.getLatestImplementationPeriod(), p.getPredecessorProject(),
//							p.getSuccessorProject(), p.getTogetherInPeriodWith(), p.getNotTogetherInPeriodWith(), p.getFixedCostEffect(), p.getAbsRelq(), p.getAbsRelt(), p.getAbsRelOop());
//					copy.setPeriod(period);
//					
//					//Set the starting period
//					if(implementedProjects.contains(copy)){
//						for(Project i : implementedProjects){
//							if(i.getId() == copy.getId()){
//								copy.setStartPeriod(i.getStartPeriod());
//								break;
//							}
//						}
//					}else{
//						copy.setStartPeriod(period);
//						implementedProjects.add(copy);
//					}
//					
//					rmArrayCopy[period][slot] = copy;
//				}
//			}
//		}
//		return rmArrayCopy;
//	}
	
	
//	public void calculateNPV(RunConfiguration config){
//		
//		double outflows = 0;
//		double fixedCostsOA = 0;
//		
//		Project[][] tmpRMArray = this.createTempRMArrayCopy(config);
//		
//		for(int period = 0; period < config.getCountPeriods(); period++){
//			
//			// (2) Book fixedCostsOA once at beginning of period
//			fixedCostsOA = fixedCostsOA + (Main.overarchingFixedOutflows / (Math.pow(1 + Main.discountRatePerPeriod, period)));
//			
//			for(int slot = 0; slot < config.getCountProjectsMaxPerPeriod(); slot++){
//				Project p = tmpRMArray[slot][period];
//				if(p != null){
//					
//					//TODO: DiscountRate in config
//					// (1) Add-up investment outflows of every project
//					outflows = outflows + (p.getOinv() / (Math.pow(1 + Main.discountRatePerPeriod, period)));
//					
//					// (2) Manipulating fixedCostsOA by finished BPM-Level Projects
//					if(p.getType().equals("bpmLevel")){
//						//If project is finishing this period
//						if(period == (p.getStartPeriod() + p.getNumberOfPeriods()-1) ){
//							fixedCostsOA = fixedCostsOA + p.getFixedCostEffect();
//						}
//					}
//				}
//
//			}
//			
//			
//		}
//		
//	}


	public void setNpv(double npv) {
		this.npv = npv;
	}


	public double getNpv() {
		return this.npv;
	}


	public boolean isRestrictionBroken() {
		return restrictionBroken;
	}


	public void setRestrictionBroken(boolean restrictionBroken) {
		this.restrictionBroken = restrictionBroken;
	}


	@Override
	public int compareTo(RoadMap rm) {
		return Double.compare(this.getNpv(), rm.getNpv());
	}
	
}
