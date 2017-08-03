package com.adventurer.gameobjects;

import com.adventurer.enumerations.SpriteType;

public class Armor extends Item {

	protected int armor = 0;
	
	public Armor(Tile tile, SpriteType spritetype, String name, int value, int armor) {
		super(tile, spritetype, name, value);
		this.armor = armor;
	}

	public int getArmor() { return this.armor; }
	
}
