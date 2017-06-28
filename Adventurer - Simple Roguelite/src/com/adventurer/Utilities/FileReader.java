package com.adventurer.Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import com.adventurer.main.SaveFile;

public class FileReader {

	public static String readFile(String filename) {
		String content = "";
		try { content = new String(Files.readAllBytes(Paths.get("data/" + filename))); } 
		catch (NoSuchFileException e) { 
			
			if(filename == SaveFile.SAVEFILENAME + ".txt") {
				
				// there is no savefile created yet!
				// --> create empty file
				FileWriter.createSaveFile();
				
				// read the default file.
				content = readSaveFile();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	public static String readSaveFile() {
		return readFile(SaveFile.SAVEFILENAME + ".txt");
	}
}
