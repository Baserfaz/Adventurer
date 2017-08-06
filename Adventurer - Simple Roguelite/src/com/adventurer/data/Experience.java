package com.adventurer.data;

public class Experience {

	private int currentExp = 0;
	private int currentLevel = 1;
	
	public Experience() {
		
		// TODO: calculate experience needed for all levels.
		
	}
	
	public int getCurrentExp() { return currentExp; }
	public void setCurrentExp(int currentExp) { this.currentExp = currentExp; }

	public int getCurrentLevel() { return currentLevel; }
	public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
}
