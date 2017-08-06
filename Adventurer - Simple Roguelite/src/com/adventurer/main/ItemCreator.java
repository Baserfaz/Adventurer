package com.adventurer.main;

import java.util.HashMap;
import java.util.Map;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.BombType;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.KeyType;
import com.adventurer.enumerations.RootElement;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.enumerations.WeaponType;
import com.adventurer.gameobjects.Armor;
import com.adventurer.gameobjects.Bomb;
import com.adventurer.gameobjects.Chest;
import com.adventurer.gameobjects.Gold;
import com.adventurer.gameobjects.Key;
import com.adventurer.gameobjects.Projectile;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Weapon;
import com.adventurer.utilities.FileReader;
import com.adventurer.utilities.Util;

public class ItemCreator {

	public static Armor createArmor(Tile tile, String itemName) {
		
		//System.out.println("CREATING ARMOR");
		
		Map<String, String> iteminfo = FileReader.readXMLGameData(itemName, RootElement.armor);
		Armor armor = null;
		
		if(iteminfo.isEmpty()) {
			
			System.out.println("ARMOR NOT CREATED - DIDNT FIND ARMOR WITH NAME: " + itemName);
			return null;
			
		} else {
			
			// variables
			String name = "";
			int value = 0;
			ArmorSlot armorSlot = null;
			Map<DamageType, Integer> defenseValues = new HashMap<DamageType, Integer>();
			
			// parse item's info entry by entry.
			for(Map.Entry<String, String> entry : iteminfo.entrySet()) {
				
				String key = entry.getKey().toUpperCase();
				String val = entry.getValue().toUpperCase();
				
				//System.out.println(key + ": " + val);
				
				if(key.equals("NAME")) name = Util.Capitalize(val);
				else if(key.equals("VALUE")) value = Integer.parseInt(val);
				else if(key.equals("ARMORSLOT")) {
					
					switch(val) {
						case "HEAD": armorSlot = ArmorSlot.Head; break;
						case "CHEST": armorSlot = ArmorSlot.Chest; break;
						case "LEGS": armorSlot = ArmorSlot.Legs; break;
						case "FEET": armorSlot = ArmorSlot.Feet; break;
						case "AMULET": armorSlot = ArmorSlot.Amulet; break;
						case "RING": armorSlot = ArmorSlot.Ring; break;
						default: System.out.println("COULDNT GET ARMORSLOT: " + key);
					}
					
				} 
				else if(key.equals("PHYSICAL")) defenseValues.put(DamageType.Physical, Integer.parseInt(val)); 
				else if(key.equals("FIRE")) defenseValues.put(DamageType.Fire, Integer.parseInt(val));
				else if(key.equals("FROST")) defenseValues.put(DamageType.Frost, Integer.parseInt(val));
				else if(key.equals("SHOCK")) defenseValues.put(DamageType.Shock, Integer.parseInt(val));
				else if(key.equals("HOLY")) defenseValues.put(DamageType.Holy, Integer.parseInt(val));
				else {
					System.out.println("FAILED TO PARSE: " + key);
					return null;
				}
			}
			
			// create new armor with the info.
			armor = new Armor(tile, SpriteType.GenericItem, name, value, armorSlot, defenseValues);
			//System.out.println("CREATION COMPLETE: " + armor.getName());
		}
		return armor;
	}
	
	public static Weapon createWeapon(Tile tile, String itemName) {
		
		//System.out.println("CREATING WEAPON");
		
		Map<String, String> iteminfo = FileReader.readXMLGameData(itemName, RootElement.weapon);
		Weapon weapon = null;
		
		if(iteminfo.isEmpty()) {
			
			System.out.println("ARMOR NOT CREATED - DIDNT FIND ARMOR WITH NAME: " + itemName);
			return null;
			
		} else {
		
			// variables
			String name = "";
			int value = 0;
			WeaponSlot weaponSlot = null;
			WeaponType weaponType = null;
			Map<DamageType, Integer> damageValues = new HashMap<DamageType, Integer>();
			
			// parse item's info entry by entry.
			for(Map.Entry<String, String> entry : iteminfo.entrySet()) {
							
				String key = entry.getKey().toUpperCase();
				String val = entry.getValue().toUpperCase();
				
				//System.out.println(key + ": " + val);
				
				if(key.equals("NAME")) name = Util.Capitalize(val);
				else if(key.equals("VALUE")) value = Integer.parseInt(val);
				else if(key.equals("WEAPONSLOT")) {
					
					switch(val) {
						case "MAINHAND": weaponSlot = WeaponSlot.Mainhand; break;
						case "OFFHAND": weaponSlot = WeaponSlot.Offhand; break;
						default: System.out.println("NO WEAPONSLOT: " + val + " EXISTS!"); return null;
					}
					
				} else if(key.equals("WEAPONTYPE")) {
					
					switch(val) {
						case "MELEE": weaponType = WeaponType.Melee; break;
						case "MAGIC": weaponType = WeaponType.Magic; break;
						case "RANGED": weaponType = WeaponType.Ranged; break;
						default: System.out.println("NO WEAPONTYPE: " + val + " EXISTS!"); return null;
					}
					
				} 
				else if(key.equals("PHYSICAL")) damageValues.put(DamageType.Physical, Integer.parseInt(val)); 
				else if(key.equals("FIRE")) damageValues.put(DamageType.Fire, Integer.parseInt(val)); 
				else if(key.equals("FROST")) damageValues.put(DamageType.Frost, Integer.parseInt(val)); 
				else if(key.equals("SHOCK")) damageValues.put(DamageType.Shock, Integer.parseInt(val)); 
				else if(key.equals("HOLY")) damageValues.put(DamageType.Holy, Integer.parseInt(val)); 
				else {
					System.out.println("FAILED TO PARSE: " + key);
					return null;
				}
			}
			
			// create weapon with data.
			weapon = new Weapon(tile, SpriteType.GenericItem, name, value, damageValues, weaponType, weaponSlot);
			//System.out.println("CREATION COMPLETE: " + weapon.getName());
		}
		return weapon;
	}
	
	public static Chest CreateChest(Tile tile, boolean locked) {
		SpriteType st = null;
		if(locked) st = SpriteType.LockedChest02;
		else st = SpriteType.Chest02;
		return new Chest(tile, st, locked);
	}
	
	public static Key createKey(Tile tile, KeyType keyType) {
		
		String name = "";
		SpriteType type = null;
		int value = 0;
		
		if(keyType == KeyType.Normal) {
			name = "Normal Key";
			type = SpriteType.Key;
		} else if (keyType == KeyType.Diamond) {
			name = "Diamond key";
			type = SpriteType.DiamondKey;
		} else {
			name = "???? KEY ????";
			type = SpriteType.Key;
		}
		
		// TODO: static fields for key values
		return new Key(tile, type, name, keyType, value);
	}
	
	/*public static Projectile createProjectile() {
		new Projectile();
	}*/
	
	/*public static Bomb createBomb(Tile tile, BombType bombtype) {
		
		String name = "";
		int value = 0;
		
		switch(bombtype) {
			case Gas: name = "Gasbomb";  value = 10; break;
			case Normal: name = "Bomb";  value = 15; break;
			default: name = "???? BOMB ????"; break;
		}
		
		// TODO: static field for bomb time, damage and values
		return new Bomb(tile, SpriteType.Bomb01, 1000, 50, bombtype, name, value);
	}*/
	
	public static Gold createGold(Tile tile, int amount) {
		return new Gold(tile, SpriteType.Gold01, amount);
	}
}
