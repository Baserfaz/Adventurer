package com.adventurer.gameobjects;

import java.util.HashMap;
import java.util.Map;

import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.enumerations.WeaponType;

public class Weapon extends Item {

	private Map<DamageType, Integer> damageValues;
	private WeaponType weaponType; // which stat scales with the damage.
	private WeaponSlot weaponSlot;
	
	public Weapon(Tile tile, SpriteType spritetype, String name, int value, Map<DamageType, Integer> damageValues, WeaponType weaponType, WeaponSlot weaponSlot) {
		super(tile, spritetype, name, value);
		this.weaponType = weaponType;
		this.weaponSlot = weaponSlot;
		this.damageValues = new HashMap<DamageType, Integer>(damageValues);
	}

	public Map<DamageType, Integer> getDamage() { return this.damageValues; }
	public WeaponType getWeaponType() { return weaponType; }
	public WeaponSlot getWeaponSlot() { return weaponSlot; }
}
