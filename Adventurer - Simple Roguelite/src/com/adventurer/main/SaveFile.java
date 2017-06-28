package com.adventurer.main;


/*
 * This class gets the information about the 
 * permanent state of the game.
 * such as diamond keys that the player has picked up
 * during their runs.
 */

public class SaveFile {

	public static final String SAVEFILENAME = "savefile";
	
	private int diamondKeyCount;
	
	public SaveFile() {
		
		String data = FileReader.readSaveFile();
		
		// TODO: read save file or create a new one
		
		this.setDiamondKeyCount(0);
		
	}

	public int getDiamondKeyCount() {
		return diamondKeyCount;
	}

	public void setDiamondKeyCount(int diamondKeyCount) {
		this.diamondKeyCount = diamondKeyCount;
	}
	
	// writes the update to file
	public void addDiamondKeyCount(int a) {
		this.diamondKeyCount += a;
		FileWriter.writeSaveFileData(this);
	}
	
}
