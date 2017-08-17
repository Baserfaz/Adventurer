package com.adventurer.data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ParseData {

	private List<String> string;
	private List<Color> color;
	private List<int[]> positions;
	
	public ParseData() {
		this.string = new ArrayList<String>();
		this.color = new ArrayList<Color>();
		this.positions = new ArrayList<int[]>();
	}
	
	public ParseData(String string, Color color, int[] positions) {
		this.string = new ArrayList<String>();
		this.string.add(string);
		
		this.color = new ArrayList<Color>();
		this.color.add(color);
		
		this.positions = new ArrayList<int[]>();
		this.positions.add(positions);
	}

	public List<String> getStrings() { return this.string; }
	public List<Color> getColors() { return this.color; }
	public List<int[]> getPositions() { return this.positions; }
	
	public void addString(String string) { this.string.add(string); }
	public void addColor(Color color) { this.color.add(color); }
	public void addPositions(int[] positions) { this.positions.add(positions); }
	
}
