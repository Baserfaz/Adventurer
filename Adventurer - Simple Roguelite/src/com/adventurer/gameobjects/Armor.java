package com.adventurer.gameobjects;

import java.util.Map;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;

public class Armor extends Equippable {
	
	private ArmorSlot slot;
	
	public Armor(Tile tile, SpriteType spritetype, String name, String description, 
			int value, ItemRarity itemRarity, ArmorSlot armorSlot, Map<DamageType, Integer> defenseValues) {
		super(tile, spritetype, name, description, value, itemRarity, defenseValues, true);
		
		this.slot = armorSlot;
	}

	public ArmorSlot getSlot() { return this.slot; }
}
