package com.adventurer.main;

import com.adventurer.enumerations.KeyType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.Chest;
import com.adventurer.gameobjects.Gold;
import com.adventurer.gameobjects.Key;
import com.adventurer.gameobjects.Tile;

public class ItemCreator {

	public static Chest CreateChest(Tile tile, boolean locked) {
		SpriteType st = null;
		if(locked) st = SpriteType.LockedChest02;
		else st = SpriteType.Chest02;
		return new Chest(tile, st, locked);
	}
	
	public static Key createKey(Tile tile, KeyType keyType) {
		
		String name = "";
		SpriteType type = null;
		
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
		
		return new Key(tile, type, name, keyType);
	}
	
	public static Gold createGold(Tile tile, int amount) {
		return new Gold(tile, SpriteType.Gold01, amount);
	}
}
