package com.adventurer.gameobjects;

import com.adventurer.main.*;

public class Door extends Tile {

	protected boolean locked = false;
	
	public Door(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type, boolean locked) {
		super(worldPos, tilePos, spritetype, type);
		
		this.locked = locked;
	}
	
	public void Open() {
		World.instance.ReplaceTile(this, TileType.Floor, SpriteType.FloorTile01);
		
		//this.type = TileType.Floor;
		//this.sprite = SpriteCreator.instance.CreateSprite(SpriteType.FloorTile01);
		//this.SetTintedSprite(null);
	}

	public void Unlock() {
		this.locked = false;
		World.instance.ChangeTile(this, TileType.Door, SpriteType.Door01);
	}
	
	public void Lock() {
		this.locked = true;
		World.instance.ChangeTile(this, TileType.LockedDoor, SpriteType.LockedDoor01);
	}
	
	public boolean isLocked() {
		return this.locked;
	}
	
}
