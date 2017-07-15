package com.adventurer.gameobjects;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.ItemType;
import com.adventurer.enumerations.SpriteType;

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
