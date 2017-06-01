package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.adventurer.main.*;

public class Bomb extends Actor {

	private long liveTimer = 0;
	private boolean alive = false;
	
	public Bomb(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int liveTime, int damage) {
		super(worldPos, tilePos, spritetype, 100, damage);
		this.alive = true;
		liveTimer = System.currentTimeMillis() + liveTime;
	}
	
	public void tick() {
		
		if(System.currentTimeMillis() > liveTimer) {
			
			// explode
			alive = false;
			
			// do damage to items near this position
			List<Tile> tiles = World.instance.GetTilesInCardinalDirection(tilePosition.getX(), tilePosition.getY());
			
			// add the current tile to the tiles list
			tiles.add(World.instance.GetTileAtPosition(this.GetTilePosition()));
			
			for(Tile tile : tiles) {
				
				// create effect on tile.
				EffectCreator.CreateSmokeEffect(tile, Util.GetRandomInteger(4, 10));
				
				// Damage:
				// 1. destructible tiles
				// 2. doors
				// 3. actors
				if(tile instanceof DestructibleTile) {
					
					((DestructibleTile)tile).getTileHealth().TakeDamage(this.damage);
				
				} else if(tile instanceof Door) {
					
					// open door
					((Door) tile).Open();
					
				} else if(tile.GetActor() != null && tile.GetActor() != this) {
					
					// if there is another bomb in the radius
					if(tile.GetActor() instanceof Bomb) {
						
						// TODO: explode the other bomb too!
						
					} else {
						GameObject actorGo = tile.GetActor();
						((Actor)actorGo).GetHealth().TakeDamage(this.damage);
						
						// creates blood 
						VanityItemCreator.CreateSmallBlood(tile);
					}
				}
			}
			
			// destroy this object
			Remove();
		}
	}
	
	public void render(Graphics g) {
	
		if(alive && hidden == false) {
			
			g.drawImage(sprite, worldPosition.getX(), worldPosition.getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
			
		} else if(alive && discovered == true && hidden == true) {
			
			if(tintedSprite == null) {
				tintedSprite = Util.tint(sprite);
			}
			
			g.drawImage(tintedSprite, worldPosition.getX(), worldPosition.getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
			
		}
	}
	
	public Rectangle GetBounds() { return null; }
}
