package com.adventurer.gameobjects;

import com.adventurer.enumerations.SpriteType;

public abstract class Usable extends Item {

	public Usable(Tile tile, SpriteType spritetype, String name, String description, int value) {
		super(tile, spritetype, name, description, value);
	}
	
	public abstract void use();
	
}
