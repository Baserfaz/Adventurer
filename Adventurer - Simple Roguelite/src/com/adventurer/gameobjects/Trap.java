package com.adventurer.gameobjects;

import com.adventurer.main.*;

public class Trap extends Tile {

	private int damage = 0;
	
	public Trap(Coordinate worldPos, SpriteType spritetype, TileType type, Coordinate tilePos, int damage) {
		super(worldPos, spritetype, type, tilePos);
		
		this.setDamage(damage);
	}

	public void Activate() {
		
		// 1. get a random wall in some direction
		// 2. shoot an arrow from that wall towards the trap
		//--------------------------
		
		// 1.1 get a random direction
		Direction randomDir = Util.GetRandomCardinalDirection();
		
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
