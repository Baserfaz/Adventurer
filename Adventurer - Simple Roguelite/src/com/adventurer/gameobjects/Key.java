package com.adventurer.gameobjects;

import com.adventurer.enumerations.KeyType;
import com.adventurer.enumerations.SpriteType;

public class Key extends Item {

	private KeyType keyType;
	
	public Key(Tile tile, SpriteType spritetype, String name, KeyType keyType, int value) {
		super(tile, spritetype, name, "Opens doors to heavenly places.", value);
		this.keyType = keyType;
	}
	
	public KeyType getKeyType() { return keyType; }
}
