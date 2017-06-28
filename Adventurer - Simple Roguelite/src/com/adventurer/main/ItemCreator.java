package com.adventurer.main;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.ItemType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.Chest;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.LightSource;
import com.adventurer.gameobjects.Tile;
import com.adventurer.utilities.Util;

public class ItemCreator {

	public static Item CreateItem(Coordinate pos, SpriteType spriteType, boolean setOffset, ItemType itemType) {
		return CreateItem(World.instance.GetTileAtPosition(pos), spriteType, setOffset, itemType);
	}
	
	public static Item CreateItem(Tile tile, SpriteType spriteType, boolean setOffset, ItemType itemType) {
		
		// randomize sprite offset on the tile.
		Coordinate pos = CalculatePositionWithOffset(tile, setOffset);
		
		// create vanity item
		return new Item(pos, tile.GetTilePosition(), spriteType, itemType);
	}
	
	public static Chest CreateChest(Tile tile, boolean setOffset, boolean locked) {
		
		// randomize sprite offset on the tile.
		Coordinate pos = CalculatePositionWithOffset(tile, setOffset);
		
		SpriteType st = null;
		
		if(locked) st = SpriteType.LockedChest01;
		else st = SpriteType.Chest01;
		
		return new Chest(pos, tile.GetTilePosition(), st, ItemType.Chest, locked);
	}
	
	public static LightSource CreateLightSource(Tile tile, SpriteType spriteType, boolean setOffset) {
		// randomize sprite offset on the tile.
		Coordinate pos = CalculatePositionWithOffset(tile, setOffset);
		
		// create vanity item
		return new LightSource(pos, tile.GetTilePosition(), spriteType, ItemType.Torch);
	}
	
	private static Coordinate CalculatePositionWithOffset(Tile tile, boolean setOffset) {
		
		int value = Game.SPRITESIZE / 4;
		Coordinate offsets = GetOffsets(value, -value);
		Coordinate pos = null;
		
		if(setOffset) {
			pos = new Coordinate(tile.GetWorldPosition().getX() + offsets.getX(), tile.GetWorldPosition().getY() + offsets.getY());
		} else {
			pos = tile.GetWorldPosition();
		}
		return pos;
	}
	
	private static Coordinate GetOffsets(int max, int min) {
		return new Coordinate(Util.GetRandomInteger(min, max), Util.GetRandomInteger(min, max));
	}
	
}
