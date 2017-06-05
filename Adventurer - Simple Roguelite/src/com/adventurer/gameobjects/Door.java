package com.adventurer.gameobjects;

import com.adventurer.main.*;

public class Door extends Tile {

	public Door(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type) {
		super(worldPos, tilePos, spritetype, type);
	}
	
	public void Open() {
		this.type = TileType.Floor;
		this.sprite = SpriteCreator.instance.CreateSprite(SpriteType.FloorTile01);
		this.discoveredSprite = null;
	}

}
