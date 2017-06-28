package com.adventurer.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {

	public static String readFile(String filename) {
		String content = "";
		try { content = new String(Files.readAllBytes(Paths.get(filename))); } 
		catch (IOException e) { e.printStackTrace(); }
		return content;
	}
	
	public static String readSaveFile() {
		return readFile(SaveFile.SAVEFILENAME + "0" + ".txt");
	}
}
