package com.v3pm_prototype.exceptions;

public class NoValidThetaIDException extends Exception {

	// Auto-generated serialVersionUID
	private static final long serialVersionUID = 7669777381595083847L;

	public NoValidThetaIDException() {
		super("Fehler bei der Verarbeitung der Nachfrageparameter. Möglicherweise wurde eine Theta-ID ausgewählt die nicht existiert.");
	}
}
