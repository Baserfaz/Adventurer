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
		// get diamond key count from the file.
		int dcount = Integer.parseInt(data.substring(7, 8));
		this.setDiamondKeyCount(dcount);
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
