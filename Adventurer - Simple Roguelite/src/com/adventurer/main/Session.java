package com.adventurer.main;

public class Session {

	private int score;
	private String sessionName;
	
	public Session(String sessionName) {
		this.setSessionName(sessionName);
		this.setScore(0);
	}
	
	// this is called when the "sessions/run" is over.
	public void saveSessionData() {
		FileWriter.writeSessionData(this);
		FileWriter.writeSaveFileData(Game.instance.getCurrentSaveFile());
	}
	
	public void addScore(int a) {
		this.setScore(this.getScore() + a);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	
}
