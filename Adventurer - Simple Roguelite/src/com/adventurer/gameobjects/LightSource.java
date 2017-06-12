package com.adventurer.gameobjects;

import java.util.List;

import com.adventurer.main.Coordinate;
import com.adventurer.main.EffectCreator;
import com.adventurer.main.SpriteType;
import com.adventurer.main.Util;
import com.adventurer.main.World;

public class LightSource extends VanityItem {
	
	public LightSource(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype) {
		super(worldPos, tilePos, spritetype);
	}

	public void tick() {
		
		if(this.currentTile.discovered) {
			
			if(Util.GetRandomInteger() > 98) {
				EffectCreator.CreateSmokeEffect(this.currentTile, Util.GetRandomInteger(1, 3));
			}
			
			/*List<Tile> tiles = World.instance.GetSurroundingTiles(this.GetTilePosition());
			
			for(Tile t : tiles) {
				if(t != null) {
					if(Util.GetRandomInteger() > 99 && Util.GetRandomInteger() > 50) {
						t.toggleLit();
					}
				}
			}*/
		} 
	}
	
	public void render() {}
	
}
