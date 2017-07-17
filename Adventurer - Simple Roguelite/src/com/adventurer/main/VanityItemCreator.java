package com.adventurer.main;

import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.VanityItem;
import com.adventurer.utilities.Util;
import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.SpriteType;

public class VanityItemCreator {

	private static Coordinate GetOffsets(int max, int min) {
		return new Coordinate(Util.GetRandomInteger(min, max), Util.GetRandomInteger(min, max));
	}
	
	/*public static VanityItem CreateVanityItem(Coordinate pos, SpriteType spriteType, boolean setOffset) {
		return CreateVanityItem(World.instance.GetTileAtPosition(pos), spriteType, setOffset);
	}
	
	public static VanityItem CreateVanityItem(Tile tile, SpriteType spriteType, boolean setOffset) {
		
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
		VanityItem vanityItem = new VanityItem(pos, tile.GetTilePosition(), spriteType);
		
		// register created vanity item to the tile given.
		tile.AddVanityItem(vanityItem);
		
		return vanityItem;
	}
	
	public static VanityItem CreateSmallBlood(Tile tile) {
		
		// randomize sprite offset on the tile.
		int value = Game.SPRITESIZE / 4;
		Coordinate offsets = GetOffsets(value, -value);
		
		// create specifically a blood vanity item.
		VanityItem vanityItem = new VanityItem(new Coordinate(tile.GetWorldPosition().getX() + offsets.getX(),
				tile.GetWorldPosition().getY() + offsets.getY()),
				tile.GetTilePosition(), SpriteType.SmallBlood01);
		
		// register created vanity item to the tile given.
		tile.AddVanityItem(vanityItem);
		
		return vanityItem;
	}*/
}
