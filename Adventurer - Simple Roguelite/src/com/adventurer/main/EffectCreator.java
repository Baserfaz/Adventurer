package com.adventurer.main;

import java.util.List;

import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.Effect;
import com.adventurer.gameobjects.Gib;
import com.adventurer.gameobjects.Tile;

public class EffectCreator {

	public static void CreateStaticHitEffect(Tile tile) {
		// create effect.
		new Effect(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Hit01, 100);
	}
	
	public static void CreateGibs(Coordinate pos, int gibCount, SpriteType spritetype) {
		Tile tile = World.instance.GetTileAtPosition(pos);
		CreateGibs(tile, gibCount, spritetype);
	}
	
	public static void CreateGibs(Tile tile, int gibCount, SpriteType spritetype) {
		for(int i = 0; i < gibCount; i++) {
			
			// randomize sprite offset on the tile.
			int value = Game.SPRITESIZE / 4;
			int spriteOffsetX = Util.GetRandomInteger(-value, value);
			int spriteOffsetY = Util.GetRandomInteger(-value, value);
			
			// randomize timer
			int randomTTL = Util.GetRandomInteger(500, 1000);
			
			// calculate worldPosition
			Coordinate pos = new Coordinate(tile.GetWorldPosition().getX() + spriteOffsetX, tile.GetWorldPosition().getY() + spriteOffsetY);
			
			// create effect.
			new Gib(pos, tile.GetTilePosition(), spritetype, randomTTL);
		}
	}
	
	public static void CreateGasEffectArea(Tile tile, int gasPuffCount) {
		
		List<Tile> tiles = World.instance.GetSurroundingTiles(tile.GetTilePosition());
		
		for(Tile t : tiles) {
			
			// create effects
			EffectCreator.CreateGasEffect(t, Util.GetRandomInteger(3, 7));
		}
	}
	
	public static void CreateGasEffect(Tile tile, int gasPuffCount) {
		
		// create a smoke cloud by 
		// creating multiple smoke "puffs"
		// that are in random position
		// and have random live time.
		
		for(int i = 0; i < gasPuffCount; i++) {
			
			// randomize sprite offset on the tile.
			int value = Game.SPRITESIZE / 4;
			int spriteOffsetX = Util.GetRandomInteger(-value, value);
			int spriteOffsetY = Util.GetRandomInteger(-value, value);
			
			// randomize timer
			int randomTTL = Util.GetRandomInteger(1000, 1500);
			
			// calculate worldPosition
			Coordinate pos = new Coordinate(tile.GetWorldPosition().getX() + spriteOffsetX, tile.GetWorldPosition().getY() + spriteOffsetY);
			
			// create effect.
			new Effect(pos, tile.GetTilePosition(), SpriteType.GasCloud01, randomTTL, true);
		}
	}
	
	public static void CreateSmokeEffect(Tile tile, int smokePuffCount) {
		
		// create a smoke cloud by 
		// creating multiple smoke "puffs"
		// that are in random position
		// and have random live time.
		
		for(int i = 0; i < smokePuffCount; i++) {
			
			// randomize sprite offset on the tile.
			int value = Game.SPRITESIZE / 4;
			int spriteOffsetX = Util.GetRandomInteger(-value, value);
			int spriteOffsetY = Util.GetRandomInteger(-value, value);
			
			// randomize timer
			int randomTTL = Util.GetRandomInteger(500, 1000);
			
			// calculate worldPosition
			Coordinate pos = new Coordinate(tile.GetWorldPosition().getX() + spriteOffsetX, tile.GetWorldPosition().getY() + spriteOffsetY);
			
			// create effect.
			new Effect(pos, tile.GetTilePosition(), SpriteType.Smoke01, randomTTL, true);
		}
	}
}
