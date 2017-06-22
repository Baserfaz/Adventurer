package com.adventurer.gameobjects;

import com.adventurer.main.Coordinate;
import com.adventurer.main.ItemType;
import com.adventurer.main.SpriteType;

public class Chest extends Item {

	private boolean locked = false;
	
	public Chest(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, ItemType itemType, boolean locked) {
		super(worldPos, tilePos, spritetype, itemType);
		
		this.locked = locked;
	}
	
	public void Open() {
		
		// TODO: effects + gold etc.
		
	}
	
	public void Unlock() {
		this.locked = false;
	}
	
	public void Lock() {
		this.locked = true;
	}

}
