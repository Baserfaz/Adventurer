package com.adventurer.main;

import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.Effect;
import com.adventurer.gameobjects.Tile;

public class EffectCreator {

	public static void CreateStaticHitEffect(Tile tile) {
		new Effect(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Hit01, 100);
	}
	
}