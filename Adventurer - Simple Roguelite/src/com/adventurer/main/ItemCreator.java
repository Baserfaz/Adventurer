package com.adventurer.main;

import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.Chest;
import com.adventurer.gameobjects.Gold;
import com.adventurer.gameobjects.Tile;

public class ItemCreator {

	public static Chest CreateChest(Tile tile, boolean locked) {
		SpriteType st = null;
		if(locked) st = SpriteType.LockedChest02;
		else st = SpriteType.Chest02;
		return new Chest(tile, st, locked);
	}
	
	public static Gold createGold(Tile tile, int amount) {
		return new Gold(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.GenericItem, amount);
	}
}
