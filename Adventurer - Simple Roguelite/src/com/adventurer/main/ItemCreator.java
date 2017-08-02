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
		
		if(keyType == KeyType.Normal) name = "Key";
		else if (keyType == KeyType.Diamond) name = "Diamond key";
		else name = "???? KEY ????";
		
		return new Key(tile, SpriteType.GenericItem, name, keyType);
	}
	
	public static Gold createGold(Tile tile, int amount) {
		return new Gold(tile, SpriteType.GenericItem, amount);
	}
}
