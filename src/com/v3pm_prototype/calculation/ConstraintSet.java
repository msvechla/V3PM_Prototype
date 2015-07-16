package com.v3pm_prototype.calculation;

import java.util.ArrayList;
import java.util.List;

import com.v3pm_prototype.database.DBConstraint;

public class ConstraintSet {
	private List<DBConstraint> lstConstraints;
	
	//Mandatory
	private int amountMandatory;
	private List<Integer> lstMandatory = new ArrayList<Integer>();
	
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
	
	
	public ConstraintSet(List<DBConstraint> lstConstraints) {
		this.lstConstraints = lstConstraints;
		this.amountMandatory = 0;
		
		//Pre-Process Mandatory
		for(DBConstraint constraint : lstConstraints){
			
			switch (constraint.getType()) {

			case DBConstraint.TYPE_MANDATORY:
				amountMandatory++;
				lstMandatory.add(constraint.getS().getId());
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

	public int getAmountMandatory() {
		return amountMandatory;
	}

	public void setAmountMandatory(int amountMandatory) {
		this.amountMandatory = amountMandatory;
	}

	public List<Integer> getLstMandatory() {
		return lstMandatory;
	}

	public void setLstMandatory(List<Integer> lstMandatory) {
		this.lstMandatory = lstMandatory;
	}

	public List<DBConstraint> getLstLocMutDep() {
		return lstLocMutDep;
	}
	
	public List<DBConstraint> getLstLocMutEx() {
		return lstLocMutDep;
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
	
		
	
}
