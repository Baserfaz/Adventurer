package com.adventurer.main;

public class EffectCreator {

	public static void CreateHitEffect(Tile tile) {
		// create effect.
		new Effect(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Hit01, 100, false);
	}
	
	public static void CreateHitEffect(Coordinate worldPos, Coordinate tilePos) {
		// create effect.
		new Effect(worldPos, tilePos, SpriteType.Hit01, 100, false);
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
