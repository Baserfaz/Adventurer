package com.adventurer.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Session {

	private int score;
	private String sessionName;
	
	public Session(String sessionName) {
		this.sessionName = sessionName;
		this.score = 0;
	}
	
	public void saveSessionData() {
		
		try {
			
			new File("data").mkdirs();
			
		    PrintWriter writer = new PrintWriter("data/" + sessionName + ".txt", "UTF-8");
		    
		    writer.println("Session name: " + sessionName);
		    writer.println("Score: " + score);
		    
		    writer.close();
		    
		} catch (IOException e) {
		   
			e.printStackTrace();
			System.exit(1);
			
		}
		
	}
	
	public void addScore(int a) {
		this.score += a;
	}
	
}
