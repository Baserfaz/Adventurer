package com.adventurer.gameobjects;

import com.adventurer.enumerations.KeyType;
import com.adventurer.enumerations.SpriteType;

public class Key extends Item {

	private KeyType keyType;
	
	public Key(Tile tile, SpriteType spritetype, String name, KeyType keyType) {
		super(tile, spritetype, name);
		this.keyType = keyType;
	}
	
	public KeyType getKeyType() { return keyType; }
}