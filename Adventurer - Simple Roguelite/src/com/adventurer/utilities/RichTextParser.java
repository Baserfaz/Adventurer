package com.adventurer.utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.adventurer.data.ParseData;

public class RichTextParser {

	// Parser supports command syntax:
	// <color="200,200,200"> ... </color>
	
	public static ParseData parseStringColor(String inputString) {
		
		ParseData data = new ParseData();
		
		boolean startCmd = false, hasStartCmdEnded = false, hasStartCmdTerminated = false,
				endCmd = false, hasEndCmdEnded = false,
				paramStarted = false, paramEnded = false;
		
		int count = -1, /*startCommandPos = 0, endCommandPos = 0, */ index = 0;
		
		List<Integer> startCommandPositions = new ArrayList<Integer>();
		List<Integer> endCommandPositions = new ArrayList<Integer>();
		
		// dictionaries 
		Map<Integer, ArrayList<Character>> cmd2 = new LinkedHashMap<Integer, ArrayList<Character>>();
		Map<Integer, ArrayList<Character>> endcmd2 = new LinkedHashMap<Integer, ArrayList<Character>>();
		Map<Integer, ArrayList<Character>> parameter2 = new LinkedHashMap<Integer, ArrayList<Character>>();
		Map<Integer, ArrayList<Character>> content2 = new LinkedHashMap<Integer, ArrayList<Character>>();
		
		// instantiate arrays inside map
		cmd2.put(index, new ArrayList<Character>());
		endcmd2.put(index, new ArrayList<Character>());
		parameter2.put(index, new ArrayList<Character>());
		content2.put(index, new ArrayList<Character>());
		
		// parsing happens here
		for(char c : inputString.toCharArray()) {
			
			count ++;
			
			if(hasEndCmdEnded) {
				
				//break;
				index += 1;
				
				// create new arrays
				cmd2.put(index, new ArrayList<Character>());
				endcmd2.put(index, new ArrayList<Character>());
				parameter2.put(index, new ArrayList<Character>());
				content2.put(index, new ArrayList<Character>());
				
				// reset 
				startCmd = false; 
				hasStartCmdEnded = false;
				hasStartCmdTerminated = false;
				endCmd = false;
				hasEndCmdEnded = false;
				paramStarted = false;
				paramEnded = false;
				
				continue;
			}
			
			// parse end command
			if(endCmd && hasEndCmdEnded == false) {
				
				if(c == '>') {
					hasEndCmdEnded = true;
					//endCommandPos = count + 1;
					endCommandPositions.add(count + 1);
				} else {
					//endcmd.add(c);
					
					endcmd2.get(index).add(c);
					
				}

				continue;
				
			}
			
			// content
			if(hasStartCmdTerminated && endCmd == false) {
				
				if(c == '<') endCmd = true;
				else {
					//content.add(c);
					content2.get(index).add(c);
				}
				
				continue;
			}
			
			// end start command
			if(c == '>' && paramEnded) {
				hasStartCmdTerminated = true;
				
				continue;
			}
			
			// parse start command
			if(startCmd && hasStartCmdEnded == false) {
				
				if(c == '=') hasStartCmdEnded = true;
				else {
					//cmd.add(c);
					cmd2.get(index).add(c);
				}
				
				continue;
				
			} else if(startCmd && hasStartCmdEnded) {
				
				// parse command parameter
				if(c == '"' && paramStarted == false) paramStarted = true; // first '"'
				else if(c == '"' && paramStarted) paramEnded = true;	   // closing '"'
				else if(paramEnded == false) {							   // cache parameter
					//parameter.add(c);
					parameter2.get(index).add(c);
				}
				
				continue;
			}
			
			
			// check whether we should start parsing a command
			if(c == '<' && startCmd == false) {
				startCmd = true;
				//startCommandPos = count;
				startCommandPositions.add(count);
			}
		}
		
		// loop through all commands in the string.
		for(int i = 0; i < index; i++) {
			
			if(cmd2.get(i) == null || endcmd2.get(i) == null || content2.get(i) == null || parameter2.get(i) == null) {
				System.out.println("wat");
				continue;
			}
			
			if(cmd2.get(i).isEmpty() || endcmd2.get(i).isEmpty() || content2.get(i).isEmpty() || parameter2.get(i).isEmpty()) {
				System.out.println("wut");
				continue;
			}
			
			// cache the current commands data
			List<Character> cmd = new ArrayList<Character>(cmd2.get(i));
			List<Character> endcmd = new ArrayList<Character>(endcmd2.get(i));
			List<Character> content = new ArrayList<Character>(content2.get(i));
			List<Character> parameter = new ArrayList<Character>(parameter2.get(i));
			
			int startCommandPos = startCommandPositions.get(i);
			int endCommandPos = endCommandPositions.get(i);
			
			// after parsing the string, we have to test if the cmd is the same as endcmd.
			// if the commands are same, it's a complete and valid command.
			
			// remove the ending '/'
			endcmd.remove(0);
			
			if(cmd.equals(endcmd)) {
				
				// create parameter string
				String param = "";
				for(char c : parameter) { param += c; }
				
				//https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
				param = param.replaceAll("\\s+", ""); 
				
				// get color channels
				String[] params = param.split(",");
				int r = Integer.parseInt(params[0]);
				int g = Integer.parseInt(params[1]);
				int b = Integer.parseInt(params[2]);
				
				// modify color with parsed data
				Color color = new Color(r, g, b, 255);
				
				// TODO: PROBLEM HERE IN STRING BUILDING!
				
				// create a string that doesn't have the rich-text commands.
				StringBuilder builder = new StringBuilder(inputString);            // create a stringbuilder 
				builder = builder.delete(startCommandPos, endCommandPos);	       // delete the whole command, other parts of the string are left untouched.
				String content_ = "";										       // the content will be cached here
				for(char c : content) content_ += c;						       // create content string
				String ss = builder.insert(startCommandPos, content_).toString();  // insert the content in right position
				
				// get the start and end positions of the string we want to color differently.
				int[] pos = new int[] { startCommandPos - 1, startCommandPos + content_.length() };
				
				// add color, string and positions to data class
				data.setColor(color);
				data.setString(ss);
				data.setPositions(pos);
				
			} else data = null;
		
		}
		
		// return parsed color and content
		return data;
	}
	
}
