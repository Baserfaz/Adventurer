package com.adventurer.gameobjects;

import java.util.HashMap;
import java.util.Map;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.SpriteType;

public class Armor extends Item {

	private Map<DamageType, Integer> defenseValues;
	private ArmorSlot slot;
	
	public Armor(Tile tile, SpriteType spritetype, String name, int value, ArmorSlot armorSlot, Map<DamageType, Integer> defenseValues) {
		super(tile, spritetype, name, value);
		this.defenseValues = new HashMap<DamageType, Integer>(defenseValues);
		this.slot = armorSlot;
	}

	public Map<DamageType, Integer> getDefenseValues() { return this.defenseValues; }
	public ArmorSlot getSlot() { return this.slot; }
}
