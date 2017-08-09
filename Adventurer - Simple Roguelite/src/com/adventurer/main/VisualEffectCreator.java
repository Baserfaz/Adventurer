package com.adventurer.main;

import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.VisualEffect;
import com.adventurer.gameobjects.Tile;

public class VisualEffectCreator {

	public static void CreateHitEffect(Tile tile) {
		new VisualEffect(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Hit01, 150);
	}
	
	public static void CreateHealEffect(Tile tile) {
		new VisualEffect(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Heal, 200);
	}
	
}