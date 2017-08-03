package com.adventurer.gameobjects;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.SpriteType;

public class Armor extends Item {

	protected int armor = 0;
	protected ArmorSlot slot;
	
	public Armor(Tile tile, SpriteType spritetype, String name, int value, int armor, ArmorSlot armorSlot) {
		super(tile, spritetype, name, value);
		this.armor = armor;
		this.slot = armorSlot;
	}

	public int getArmor() { return this.armor; }
	public ArmorSlot getSlot() { return this.slot; }
}
