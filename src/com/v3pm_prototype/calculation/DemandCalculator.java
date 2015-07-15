package com.v3pm_prototype.calculation;

import com.v3pm_prototype.exceptions.NoValidThetaIDException;

/**
 * This class is is used from the Calculator class in order to calculate the demand of a process for a specific period.
 * 
 */
public class DemandCalculator {

	/**
	 * calculates demand n according to the parameters of the process
	 */
	public static double calculateN(Process p) throws NoValidThetaIDException {
		double n = 0; // demand
		double tempTetaQ; // demand-function for quality
		double tempTetaT; // demand-function for time

		tempTetaQ = calculateTeta(p.getThetaID_q(), p.getQ());
		tempTetaT = calculateTeta(p.getThetaID_t(), p.getT());

		// Formular for n
		n = p.getRoh() + p.getLamda() * (p.getAlpha() * tempTetaQ + p.getBeta() * tempTetaT);

		return n;
	}

	/**
	 * calculates the theta depending on the selected theta-ID. This method can be used to calculate theta depending on t or q. (The value
	 * tOrQValue is used for that)
	 * 
	 * @throws NoValidThetaIDException
	 */
	private static double calculateTeta(int Theta_ID, double tOrQValue) throws NoValidThetaIDException {
		switch (Theta_ID) {
		case 0:
			return 0;
		case 1:
			return tOrQValue;
		case 2:
			return Math.log(tOrQValue);
		case 3:
			return Math.exp(1 / tOrQValue);
		default:
			throw new NoValidThetaIDException();

		}

	}

}
