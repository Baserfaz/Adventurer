package com.adventurer.gameobjects;

import com.adventurer.main.*;

public class Door extends Tile {

	public Door(Coordinate worldPos, SpriteType spritetype, TileType type, Coordinate tilePos) {
		super(worldPos, spritetype, type, tilePos);
	}
	
	public void Open() {
		this.type = TileType.Floor;
		this.sprite = SpriteCreator.instance.CreateSprite(SpriteType.FloorTile01);
		this.discoveredSprite = null;
	}

}
