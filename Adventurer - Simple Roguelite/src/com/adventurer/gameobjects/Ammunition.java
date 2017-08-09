package com.adventurer.gameobjects;

import com.adventurer.enumerations.SpriteType;

public abstract class Ammunition extends Item {

	public Ammunition(Tile tile, SpriteType spritetype, String name, String description, int value) {
		super(tile, spritetype, name, description, value);
	}

}
