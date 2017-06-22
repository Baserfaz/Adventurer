package com.adventurer.gameobjects;

import java.util.List;

import com.adventurer.main.Coordinate;
import com.adventurer.main.EffectCreator;
import com.adventurer.main.ItemType;
import com.adventurer.main.SpriteType;
import com.adventurer.main.Util;
import com.adventurer.main.World;

public class LightSource extends DestructibleItem {
	
	public LightSource(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, ItemType itemType) {
		super(worldPos, tilePos, spritetype, 100, itemType);
	}

	public void tick() {
		
		super.tick();
		
		//Tile currentTile = World.instance.GetTileAtPosition(this.GetTilePosition());
		
		/*if(currentTile.discovered) {
			
			if(Util.GetRandomInteger() > 98) {
				EffectCreator.CreateSmokeEffect(currentTile, Util.GetRandomInteger(1, 3));
			}
			
			List<Tile> tiles = World.instance.GetSurroundingTiles(this.GetTilePosition());
			
			for(Tile t : tiles) {
				if(t != null) {
					if(Util.GetRandomInteger() > 99 && Util.GetRandomInteger() > 25) {
						t.toggleLit();
					}
				}
			}
		} */
	}
	
	public void render() {}
	
}
