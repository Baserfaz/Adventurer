package com.adventurer.gameobjects;

import java.util.Map;

import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.enumerations.WeaponType;

public class Weapon extends Equippable {
	
	private WeaponType weaponType;
	private WeaponSlot weaponSlot;
	
	public Weapon(Tile tile, SpriteType spritetype, String name, String description, 
			int value, ItemRarity itemRarity, Map<DamageType, Integer> damageValues, 
			WeaponType weaponType, WeaponSlot weaponSlot) {
		super(tile, spritetype, name, description, value, itemRarity, damageValues, false);
		
		this.weaponType = weaponType;
		this.weaponSlot = weaponSlot;
		
		// because weapons are set to do damage
		// therefore we need to swap damage to resistance.
		// ---> in a case of SHIELD.
		if(this.weaponSlot == WeaponSlot.Offhand) { this.getBonuses().swapDmgAndRes(true); }
		
	}
	
	public WeaponType getWeaponType() { return weaponType; }
	public WeaponSlot getWeaponSlot() { return weaponSlot; }
}
