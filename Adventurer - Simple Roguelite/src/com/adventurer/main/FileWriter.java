package com.adventurer.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWriter {

	public static void writeSaveFileData(SaveFile saveFile) {
		
		try {
			new File("data").mkdirs();
			
		    PrintWriter writer = new PrintWriter("data/" + saveFile.SAVEFILENAME + ".txt", "UTF-8");
		    
		    writer.println("Diamondkeys: " + saveFile.getDiamondKeyCount());
		    
		    writer.close();
		    
		} catch (IOException e) {
		   
			e.printStackTrace();
			System.exit(1);
			
		}
		
	}
	
	public static void writeSessionData(Session session) {
		
		try {
			new File("data").mkdirs();
			
		    PrintWriter writer = new PrintWriter("data/" + session.getSessionName() + ".txt", "UTF-8");
		    
		    writer.println("Session name: " + session.getSessionName());
		    writer.println("Score: " + session.getScore());
		    
		    writer.close();
		    
		} catch (IOException e) {
		   
			e.printStackTrace();
			System.exit(1);
			
		}
		
	}
	
	
}
