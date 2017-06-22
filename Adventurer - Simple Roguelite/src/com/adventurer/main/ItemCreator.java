package com.adventurer.main;

import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.LightSource;
import com.adventurer.gameobjects.Tile;

public class ItemCreator {

	public static Item CreateItem(Coordinate pos, SpriteType spriteType, boolean setOffset, ItemType itemType) {
		return CreateItem(World.instance.GetTileAtPosition(pos), spriteType, setOffset, itemType);
	}
	
	public static Item CreateItem(Tile tile, SpriteType spriteType, boolean setOffset, ItemType itemType) {
		
		// randomize sprite offset on the tile.
		int value = Game.SPRITESIZE / 4;
		Coordinate offsets = GetOffsets(value, -value);
		Coordinate pos = null;
		
		if(setOffset) {
			pos = new Coordinate(tile.GetWorldPosition().getX() + offsets.getX(), tile.GetWorldPosition().getY() + offsets.getY());
		} else {
			pos = tile.GetWorldPosition();
		}
		
		// create vanity item
		Item item = new Item(pos, tile.GetTilePosition(), spriteType, itemType);
		
		// register created vanity item to the tile given.
		tile.SetItem(item);
		
		return item;
	}
	
	public static LightSource CreateLightSource(Tile tile, SpriteType spriteType, boolean setOffset) {
		// randomize sprite offset on the tile.
		int value = Game.SPRITESIZE / 4;
		Coordinate offsets = GetOffsets(value, -value);
		Coordinate pos = null;
		
		if(setOffset) {
			pos = new Coordinate(tile.GetWorldPosition().getX() + offsets.getX(), tile.GetWorldPosition().getY() + offsets.getY());
		} else {
			pos = tile.GetWorldPosition();
		}
		
		// create vanity item
		LightSource lightsource = new LightSource(pos, tile.GetTilePosition(), spriteType, ItemType.Torch);
		
		// register created vanity item to the tile given.
		tile.SetItem(lightsource);
		
		return lightsource;
	}
	
	private static Coordinate GetOffsets(int max, int min) {
		return new Coordinate(Util.GetRandomInteger(min, max), Util.GetRandomInteger(min, max));
	}
	
}
