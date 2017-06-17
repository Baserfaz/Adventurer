package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.adventurer.main.*;

public class Bomb extends Item {

	private int damage = 0;
	
	private long liveTimer = 0;
	private boolean alive = false;
	
	public Bomb(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int liveTime, int damage) {
		super(worldPos, tilePos, spritetype);
		this.alive = true;
		this.damage = damage;
		this.liveTimer = System.currentTimeMillis() + liveTime;
	}
	
	public void tick() {
		
		if(System.currentTimeMillis() > liveTimer) {
			
			// explode
			alive = false;
			
			// do damage to items near this position
			List<Tile> tiles = World.instance.GetTilesInCardinalDirection(this.GetTilePosition());
			
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
				
					// effects
					EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 7), SpriteType.Wall01Gib01);
					
				} else if(tile instanceof Door) {
					
					if(tile.GetTileType() == TileType.LockedDoor) {
						
						// dont do anything.
						
					} else {
						// open door
						((Door) tile).Open();
						EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 6), SpriteType.PotGib01);
					}
					
				} else if(tile.GetActor() != null && tile.GetItem() != this && tile.GetItem() == null) {
					
					// if there is another bomb in the radius
					if(tile.GetItem() instanceof Bomb) {
						
						// TODO: explode the other bomb too!
						
					} else {
						
						DamageHandler.ActorTakeDamage(tile, damage);
					}
					
				} else if(tile.GetItem() != null) {
					
					if(tile.GetItem() instanceof DestructibleItem) {
						DamageHandler.ItemTakeDamage((DestructibleItem)tile.GetItem(), damage);
					}
					
				}
			}
			
			// destroy this object
			Remove();
		}
	}
	
	public void render(Graphics g) {
		
		if(alive && hidden == false) {
			
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
			
		} else if(alive && discovered == true && hidden == true) {
			
			if(tintedSprite == null) {
				tintedSprite = Util.tint(sprite, true);
			}
			
			Renderer.RenderSprite(tintedSprite, this.GetWorldPosition(), g);
			
		}
	}
	
	public Rectangle GetBounds() { return null; }
}
