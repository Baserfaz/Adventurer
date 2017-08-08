package com.adventurer.gameobjects;

import java.util.Map;

import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.enumerations.WeaponType;

public class Weapon extends Equipable {
	
	private WeaponType weaponType;
	private WeaponSlot weaponSlot;
	
	public Weapon(Tile tile, SpriteType spritetype,
			String name, String description, int value, Map<DamageType, Integer> damageValues, WeaponType weaponType, WeaponSlot weaponSlot) {
		super(tile, spritetype, name, description, value, damageValues, false);
		
		this.weaponType = weaponType;
		this.weaponSlot = weaponSlot;
	}
	
	public WeaponType getWeaponType() { return weaponType; }
	public WeaponSlot getWeaponSlot() { return weaponSlot; }
}
