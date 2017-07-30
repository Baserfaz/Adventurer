package com.adventurer.utilities;

// IO
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// XML
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

import com.adventurer.data.SaveFile;

public class FileReader {
	
	public static Map<String, String> readXMLGameData(String key) { return readXML("resources/data/gamedata.xml", key); }
	
	// https://stackoverflow.com/questions/428073/what-is-the-best-simplest-way-to-read-in-an-xml-file-in-java-application
	public static Map<String, String> readXML(String filename, String key) {
		
		Map<String, String> myMap = new HashMap<String, String>();	
		
		try {
			
			// ---------
			File xmlfile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(xmlfile);
		    doc.getDocumentElement().normalize();
			// ---------
		    
		    // TODO: are we looking for enemies/items etc. 
		    // NOW HARDCODED TO ONLY SEARCH FOR ENEMY DATA!
		    NodeList root = doc.getElementsByTagName("enemy");
		    
		    for(int i = 0; i < root.getLength(); i++) {
		    	
		    	Node n = root.item(i);
		    	if(n.getNodeType() == Node.ELEMENT_NODE) {
		    		
		    		Element e = (Element) n;
		    		
		    		if(e.getElementsByTagName("enemyType").item(0).getTextContent().equals(key)) {
		    		
		    			String name = e.getElementsByTagName("name").item(0).getTextContent(); 
		    			String enemyType = e.getElementsByTagName("enemyType").item(0).getTextContent(); 
		    			String health = e.getElementsByTagName("health").item(0).getTextContent();
		    			String damage = e.getElementsByTagName("damage").item(0).getTextContent();
		    			String isRanged = e.getElementsByTagName("isRanged").item(0).getTextContent();
		    			String movementSpeed = e.getElementsByTagName("movementSpeed").item(0).getTextContent();
		    			String movementCooldownBase = e.getElementsByTagName("movementCooldownBase").item(0).getTextContent();
		    			
		    			myMap.put("name", name);
		    			myMap.put("enemyType", enemyType);
		    			myMap.put("health", health);
		    			myMap.put("damage", damage);
		    			myMap.put("isRanged", isRanged);
		    			myMap.put("movementSpeed", movementSpeed);
		    			myMap.put("movementCooldownBase", movementCooldownBase);
		    		}
		    	}
		    }
		    
		} catch (ParserConfigurationException | SAXException | IOException e) { e.printStackTrace(); }
		return myMap;
	}
	
	public static String readSaveFile() { return readFile(SaveFile.SAVEFILENAME + ".txt"); }
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
		} catch (IOException e) { e.printStackTrace(); }
		return content;
	}
}
