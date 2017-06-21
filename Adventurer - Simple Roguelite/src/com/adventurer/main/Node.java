package com.adventurer.main;

public class Node {

	private double fScore = Double.POSITIVE_INFINITY;
	private double gScore = Double.POSITIVE_INFINITY;
	
	public double getfScore() {
		return fScore;
	}
	
	public void setfScore(double fScore) {
		this.fScore = fScore;
	}
	
	public double getgScore() {
		return gScore;
	}
	
	public void setgScore(double gScore) {
		this.gScore = gScore;
	}

	public void reset() {
		this.setfScore(Double.POSITIVE_INFINITY);
		this.setgScore(Double.POSITIVE_INFINITY);
	}
	
}
