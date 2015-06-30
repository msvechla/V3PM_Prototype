package com.processbalancing.exceptions;

public class ProjectIsNotInRoadmapException extends Exception {

	// Auto generated serialVersionUID
	private static final long serialVersionUID = 6377584913027502615L;

	public ProjectIsNotInRoadmapException() {
		super(
				"Das gesuchte Projekt existiert nicht in dieser Roadmap. Fehler tritt bei dem Anwenden von Restriktionen in der RoadmapRestrictionHandler Klasse auf.");
	}
}
