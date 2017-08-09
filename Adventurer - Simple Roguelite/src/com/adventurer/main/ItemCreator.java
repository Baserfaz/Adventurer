package com.adventurer.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.Effect;
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
import com.adventurer.gameobjects.Potion;
import com.adventurer.gameobjects.Projectile;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Weapon;
import com.adventurer.utilities.FileReader;
import com.adventurer.utilities.Util;

public class ItemCreator {

	public static Potion createHealthPotion(Tile tile, int amount) {
		Map<Effect, Integer> m = new LinkedHashMap<Effect, Integer>();
		m.put(Effect.Health, amount);
		return createPotion(tile, m);
	}
	
	public static Potion createManaPotion(Tile tile, int amount) {
		Map<Effect, Integer> m = new LinkedHashMap<Effect, Integer>();
		m.put(Effect.Mana, amount);
		return createPotion(tile, m);
	}
	
	public static Potion createRestorationPotion(Tile tile, int amount) {
		Map<Effect, Integer> m = new LinkedHashMap<Effect, Integer>();
		m.put(Effect.Mana, amount);
		m.put(Effect.Health, amount);
		return createPotion(tile, m);
	}
	
	public static Potion createPotion(Tile tile, Map<Effect, Integer> effects) {
		
		// vars
		String name = "Potion of ", description = "This potion ";
		int value = 0;
		SpriteType spritetype = null;
		
		// populate vars with using effect info.
		for(Entry<Effect, Integer> e : effects.entrySet()) {
			
			Effect key = e.getKey();
			int val = e.getValue();
			
			switch(key) {
				case Health: 
					name += "health";
					description += "heals";
					value += 15;
					spritetype = SpriteType.HealthPotion;
					break;
				case Mana:
					name += "mana";
					description += "regains mana";
					value += 20;
					spritetype = SpriteType.ManaPotion;
			}
			
			description += " and ";
			name += " and ";
		}
		
		// remove last ' and '.
		name = name.substring(0, name.length() - 5);
		description = description.substring(0, description.length() - 5) + ".";
		
		return new Potion(tile, spritetype, name, description, value, effects);
	}
	
	public static Armor createArmor(Tile tile, String itemName) {
		
		Map<String, String> iteminfo = FileReader.readXMLGameData(itemName, RootElement.armor);
		Armor armor = null;
		
		if(iteminfo.isEmpty()) {
			
			System.out.println("ARMOR NOT CREATED - DIDNT FIND ARMOR WITH NAME: " + itemName);
			return null;
			
		} else {
			
			// variables
			String name = "";
			String description = "";
			int value = 0;
			ArmorSlot armorSlot = null;
			Map<DamageType, Integer> defenseValues = new HashMap<DamageType, Integer>();
			
			// parse item's info entry by entry.
			for(Map.Entry<String, String> entry : iteminfo.entrySet()) {
				
				String key = entry.getKey().toUpperCase();
				String val = entry.getValue().toUpperCase();
				
				if(key.equals("NAME")) name = Util.Capitalize(val);
				else if(key.equals("DESCRIPTION")) description = Util.Capitalize(val);
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
			armor = new Armor(tile, SpriteType.GenericItem, name, description, value, armorSlot, defenseValues);
		}
		return armor;
	}
	
	public static Weapon createWeapon(Tile tile, String itemName) {
		
		Map<String, String> iteminfo = FileReader.readXMLGameData(itemName, RootElement.weapon);
		Weapon weapon = null;
		
		if(iteminfo.isEmpty()) {
			
			System.out.println("ARMOR NOT CREATED - DIDNT FIND ARMOR WITH NAME: " + itemName);
			return null;
			
		} else {
		
			// variables
			String name = "";
			String description = "";
			int value = 0;
			WeaponSlot weaponSlot = null;
			WeaponType weaponType = null;
			Map<DamageType, Integer> damageValues = new HashMap<DamageType, Integer>();
			
			// parse item's info entry by entry.
			for(Map.Entry<String, String> entry : iteminfo.entrySet()) {
							
				String key = entry.getKey().toUpperCase();
				String val = entry.getValue().toUpperCase();
				
				if(key.equals("NAME")) name = Util.Capitalize(val);
				else if(key.equals("DESCRIPTION")) description = Util.Capitalize(val);
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
			weapon = new Weapon(tile, SpriteType.GenericItem, name, description, value, damageValues, weaponType, weaponSlot);
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
		int value = 5;
		
		if(keyType == KeyType.Normal) {
			name = "Normal Key";
			type = SpriteType.Key;
		} else if (keyType == KeyType.Diamond) {
			name = "Diamond key";
			type = SpriteType.DiamondKey;
		} else {
			System.out.println("NOT VALID KEY TYPE: " + keyType);
			return null;
		}
		
		// TODO: static fields for key values
		return new Key(tile, type, name, keyType, value);
	}
	
	public static Projectile createProjectile(Tile tile, DamageType projtype, int damageAmount, Direction dir) {
		
		// vars
		String name = "", description = "";
		Map<DamageType, Integer> dmg = new LinkedHashMap<DamageType, Integer>();
		int value = 0;
		
		// TODO: now hardcoded to shoot always north
		if(dir == null) dir = Direction.North;
		
		// damage
		switch(projtype) {
			case Fire: 
				name = "Fire arrow";
				description = "Does fire damage on hit.";
				dmg.put(DamageType.Fire, damageAmount);
				value = 15;
				break;
			case Frost: 
				name = "Frost arrow";
				description = "Does frost damage on hit.";
				dmg.put(DamageType.Frost, damageAmount);
				value = 15;
				break;
			case Holy: 
				name = "Holy arrow";
				description = "Does holy damage on hit.";
				dmg.put(DamageType.Holy, damageAmount);
				value = 15;
				break;
			case Physical: 
				name = "Arrow";
				description = "Does physical damage on hit.";
				dmg.put(DamageType.Physical, damageAmount);
				value = 3;
				break;
			case Shock: 
				name = "Shock arrow";
				description = "Does shock damage on hit.";
				dmg.put(DamageType.Shock, damageAmount);
				value = 15;
				break;
			default: break;
		}
		
		// create projectile.
		return new Projectile(tile, SpriteType.Arrow01, name, description, value, dmg, dir);
	}
	
	public static Bomb createBomb(Tile tile, DamageType bombtype) {
		
		// vars
		String name = "";
		String description = "";
		int value = 0;
		Map<DamageType, Integer> dmg = new LinkedHashMap<DamageType, Integer>();
		
		// populate vars
		switch(bombtype) {
			case Frost:
				name = "Frost Bomb";
				description = "Does frost damage in AOE.";
				value = 15;
				dmg.put(DamageType.Frost, 15);
				break;
			case Physical:
				name = "Bomb";
				description = "Does physical damage in AOE.";
				value = 5;
				dmg.put(DamageType.Physical, 5);
				break;
			case Fire:
				name = "Fire bomb";
				description = "Does fire damage in AOE.";
				value = 15;
				dmg.put(DamageType.Fire, 5);
				break;
			case Holy:
				name = "Holy bomb";
				description = "Does holy damage in AOE.";
				value = 15;
				dmg.put(DamageType.Holy, 5);
				break;
			case Shock:
				name = "Shock bomb";
				description = "Does shock damage in AOE.";
				value = 15;
				dmg.put(DamageType.Shock, 5);
				break;
			default: System.out.println("NOT VALID BOMBTYPE: " + bombtype); return null;
		}
		
		// create an instance of bomb.
		return new Bomb(tile, SpriteType.Bomb01, 1000, dmg, name, description, value);
	}
	
	public static Gold createGold(Tile tile, int amount) { return new Gold(tile, SpriteType.Gold01, amount); }
}
