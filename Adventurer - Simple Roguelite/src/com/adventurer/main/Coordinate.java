package com.adventurer.main;

public class Coordinate {

	private int x;
	private int y;
	
	public Coordinate(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public void decreaseX(int x) {
		this.x -= x;
	}
	
	public void decreaseY(int y) {
		this.y -= y;
	}
	
	public void addX(int x) {
		this.x += x;
	}
	
	public void addY(int y) {
		this.y += y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
