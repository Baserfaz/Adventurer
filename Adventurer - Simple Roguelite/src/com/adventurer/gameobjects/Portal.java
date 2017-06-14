package com.adventurer.gameobjects;

import com.adventurer.main.Coordinate;
import com.adventurer.main.SpriteType;
import com.adventurer.main.TileType;

public class Portal extends Tile {

	// TODO: target??
	
	public Portal(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type) { // TODO: take target argument: which world are we going to create.
		super(worldPos, tilePos, spritetype, type);
	}
}
