package com.adventurer.gameobjects;

import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.Coordinate;

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
