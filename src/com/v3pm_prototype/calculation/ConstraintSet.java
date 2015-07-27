package com.v3pm_prototype.calculation;

import java.util.ArrayList;
import java.util.List;

import com.v3pm_prototype.database.DBConstraint;

public class ConstraintSet {
	private List<DBConstraint> lstConstraints;
	
	//Mandatory
	private List<DBConstraint> lstMandatory = new ArrayList<DBConstraint>();
	private List<Integer> lstMandatoryIDs = new ArrayList<Integer>();
	
	//LocMutDep
	private List<DBConstraint> lstLocMutDep = new ArrayList<DBConstraint>();
	
	//LocMutEx
	private List<DBConstraint> lstLocMutEx = new ArrayList<DBConstraint>();
	
	//GloMutDep
	private List<DBConstraint> lstGloMutDep = new ArrayList<DBConstraint>();
		
	//GloMutEx
	private List<DBConstraint> lstGloMutEx = new ArrayList<DBConstraint>();
	
	//GloMutEx
	private List<DBConstraint> lstPreSuc = new ArrayList<DBConstraint>();
	
	//Earliest
	private List<DBConstraint> lstEarliest = new ArrayList<DBConstraint>();
	
	//Latest
	private List<DBConstraint> lstLatest = new ArrayList<DBConstraint>();
	
	//TimeMax
	private List<DBConstraint> lstTimeMax = new ArrayList<DBConstraint>();
	
	//QualMin
	private List<DBConstraint> lstQualMin = new ArrayList<DBConstraint>();
	
	//Budget
	private List<DBConstraint> lstBudget = new ArrayList<DBConstraint>();
	
	//BudBPM
	private List<DBConstraint> lstBudBPM = new ArrayList<DBConstraint>();
	
	//BudPro
	private List<DBConstraint> lstBudPro = new ArrayList<DBConstraint>();
	
	
	public ConstraintSet(List<DBConstraint> lstConstraints) {
		this.lstConstraints = new ArrayList<DBConstraint>();
		
		for(DBConstraint dbConstraint : lstConstraints){
			this.lstConstraints.add((DBConstraint) dbConstraint.clone());
		}
		
		//Pre-Process Mandatory
		for(DBConstraint constraint : this.lstConstraints){
			
			switch (constraint.getType()) {

			case DBConstraint.TYPE_MANDATORY:
				lstMandatory.add(constraint);
				lstMandatoryIDs.add(constraint.getS().getId());
				break;
			
			case DBConstraint.TYPE_LOCMUTDEP:
				lstLocMutDep.add(constraint);
				break;
			
			case DBConstraint.TYPE_LOCMUTEX:
				lstLocMutEx.add(constraint);
				break;
			
			case DBConstraint.TYPE_GLOMUTDEP:
				lstGloMutDep.add(constraint);
				break;
			
			case DBConstraint.TYPE_GLOMUTEX:
				lstGloMutEx.add(constraint);
				break;
				
			case DBConstraint.TYPE_PRESUC:
				lstPreSuc.add(constraint);
				break;
				
			case DBConstraint.TYPE_EARLIEST:
				lstEarliest.add(constraint);
				break;
				
			case DBConstraint.TYPE_LATEST:
				lstLatest.add(constraint);
				break;
				
			case DBConstraint.Type_TIMEMAX:
				lstTimeMax.add(constraint);
				break;
				
			case DBConstraint.TYPE_QUALMIN:
				lstQualMin.add(constraint);
				break;
				
			case DBConstraint.TYPE_BUDGET:
				lstBudget.add(constraint);
				break;
				
			case DBConstraint.TYPE_BUDBPM:
				lstBudBPM.add(constraint);
				break;
				
			case DBConstraint.TYPE_BUDPRO:
				lstBudPro.add(constraint);
				break;
			
			}
			
			
			
		}
	}

	
	//-------------------- GETTERS AND SETTERS --------------------
	public List<DBConstraint> getLstConstraints() {
		return lstConstraints;
	}

	public void setLstConstraints(List<DBConstraint> lstConstraints) {
		this.lstConstraints = lstConstraints;
	}

	public List<DBConstraint> getLstMandatory() {
		return lstMandatory;
	}

	public void setLstMandatory(List<DBConstraint> lstMandatory) {
		this.lstMandatory = lstMandatory;
	}

	public List<Integer> getLstMandatoryIDs() {
		return lstMandatoryIDs;
	}

	public void setLstMandatoryIDs(List<Integer> lstMandatoryIDs) {
		this.lstMandatoryIDs = lstMandatoryIDs;
	}

	public List<DBConstraint> getLstLocMutDep() {
		return lstLocMutDep;
	}
	
	public List<DBConstraint> getLstLocMutEx() {
		return lstLocMutEx;
	}

	public List<DBConstraint> getLstGloMutDep() {
		return lstGloMutDep;
	}

	public List<DBConstraint> getLstGloMutEx() {
		return lstGloMutEx;
	}

	public List<DBConstraint> getLstPreSuc() {
		return lstPreSuc;
	}


	public List<DBConstraint> getLstEarliest() {
		return lstEarliest;
	}

	public List<DBConstraint> getLstLatest() {
		return lstLatest;
	}


	public List<DBConstraint> getLstTimeMax() {
		return lstTimeMax;
	}

	public List<DBConstraint> getLstQualMin() {
		return lstQualMin;
	}

	public List<DBConstraint> getLstBudget() {
		return lstBudget;
	}


	public List<DBConstraint> getLstBudBPM() {
		return lstBudBPM;
	}


	public List<DBConstraint> getLstBudPro() {
		return lstBudPro;
	}
	
		
	
}
