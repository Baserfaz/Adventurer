package com.adventurer.gameobjects;

import com.adventurer.main.Coordinate;
import com.adventurer.main.SpriteType;
import com.adventurer.main.TileType;

public class Portal extends Tile {

	private boolean exit = false;
	
	public Portal(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type, boolean exit) { 
		super(worldPos, tilePos, spritetype, type);
		
		this.exit = exit;
	}
	
	public boolean isExit() {
		return this.exit;
	}
}
