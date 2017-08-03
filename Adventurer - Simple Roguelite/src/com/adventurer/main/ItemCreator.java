package com.adventurer.main;

import java.util.HashMap;
import java.util.Map;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.KeyType;
import com.adventurer.enumerations.RootElement;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.Armor;
import com.adventurer.gameobjects.Chest;
import com.adventurer.gameobjects.Gold;
import com.adventurer.gameobjects.Key;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Weapon;
import com.adventurer.utilities.FileReader;

public class ItemCreator {

	public static Armor createArmor(Tile tile, String itemName) {
		
		System.out.println("CREATING ARMOR");
		
		Map<String, String> iteminfo = FileReader.readXMLGameData(itemName, RootElement.armor);
		Armor armor = null;
		
		if(iteminfo.isEmpty()) {
			
			System.out.println("ARMOR NOT CREATED - DIDNT FIND ARMOR: " + itemName);
			return null;
			
		} else {
			
			// variables
			String name = "";
			int value = 0;
			ArmorSlot armorSlot = null;
			Map<DamageType, Integer> defenseValues = new HashMap<DamageType, Integer>();
			
			// parse item's info
			for(Map.Entry<String, String> entry : iteminfo.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue();
				
				System.out.println(key + ": " + val);
				
				if(key == "name") name = val;
				else if(key == "value") value = Integer.parseInt(val);
				else if(key == "armorSlot") {
					
					switch(val) {
						case "head": armorSlot = ArmorSlot.Head; break;
						case "chest": armorSlot = ArmorSlot.Chest; break;
						case "legs": armorSlot = ArmorSlot.Legs; break;
						case "feet": armorSlot = ArmorSlot.Feet; break;
						case "amulet": armorSlot = ArmorSlot.Amulet; break;
						case "ring": armorSlot = ArmorSlot.Ring; break;
						default: System.out.println("COULDNT GET ARMORSLOT: " + key);
					}
					
				}
				else if(key == "defenseValues") {
					
					switch(val) {
						case "Physical": defenseValues.put(DamageType.Physical, Integer.parseInt(val)); break;
						case "Fire": defenseValues.put(DamageType.Fire, Integer.parseInt(val)); break;
						case "Frost": defenseValues.put(DamageType.Frost, Integer.parseInt(val)); break;
						case "Shock": defenseValues.put(DamageType.Shock, Integer.parseInt(val)); break;
						case "Holy": defenseValues.put(DamageType.Holy, Integer.parseInt(val)); break;
						default: System.out.println("DAMAGETYPE NOT AVAILABLE: " + val);
					}
					
				} else {
					
					System.out.println("FAILED TO PARSE: " + key);
					return null;
					
				}
			}
			
			// create new armor with the info.
			armor = new Armor(tile, SpriteType.GenericItem, name, value, armorSlot, defenseValues);
		}
		return armor;
	}
	
	public static Weapon createWeapon(Tile tile, String itemName) {
		
		
		// TODO: 
		
		
		
		return null;
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
		
		return new Key(tile, type, name, keyType, value);
	}
	
	public static Gold createGold(Tile tile, int amount) {
		return new Gold(tile, SpriteType.Gold01, amount);
	}
}
