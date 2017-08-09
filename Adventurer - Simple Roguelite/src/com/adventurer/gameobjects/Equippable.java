package com.adventurer.gameobjects;

import java.util.Map;

import com.adventurer.data.ItemBonus;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.SpriteType;

public class Equippable extends Item {

	protected ItemBonus bonus;
	
	public Equippable(Tile tile, SpriteType spritetype, String name, String description, int value, 
			Map<DamageType, Integer> myMap, boolean isResistance) {
		super(tile, spritetype, name, description, value);
		
		this.bonus = new ItemBonus(myMap, isResistance);
	}
	
	public Equippable(Tile tile, SpriteType spritetype, String name, String description, int value, 
			Map<DamageType, Integer> resistances, Map<DamageType, Integer> damage) {
		super(tile, spritetype, name, description, value);
		
		this.bonus = new ItemBonus(resistances, damage);
	}

	public ItemBonus getBonuses() { return this.bonus; }
	
}
