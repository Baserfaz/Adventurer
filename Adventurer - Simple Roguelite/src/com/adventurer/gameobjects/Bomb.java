package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.BombType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.*;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Bomb extends Item {

	private int damage = 0;
	private BombType bombType;
	private long liveTimer = 0;
	private boolean alive = false;
	
	public Bomb(Tile tile, SpriteType spritetype, int liveTime, int damage, BombType btype) {
		super(tile, spritetype, "Bomb");
		this.alive = true;
		this.damage = damage;
		this.liveTimer = System.currentTimeMillis() + liveTime;
		this.bombType = btype;
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
				
				// Damage:
				// 1. destructible tiles
				// 2. doors
				// 3. actors
				if(tile instanceof DestructibleTile) {
					
					((DestructibleTile)tile).getTileHealth().TakeDamage(this.damage);
					
				} else if(tile instanceof Door) {
					
					if(tile.GetTileType() == TileType.LockedDoor) {
						
						// dont do anything.
						
					} else {
						
						((Door) tile).Open();
					}
					
				} else if(tile.GetActor() != null) DamageHandler.ActorTakeDamage(tile, damage);
				
			}
			
			// destroy this object
			Remove();
		}
	}
	
	public void render(Graphics g) {
		if(alive && hidden == false) {
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
		} else if(alive && discovered == true && hidden == true) {
			Renderer.RenderSprite(Util.tint(sprite, true), this.GetWorldPosition(), g);
		}
	}
	
	public Rectangle GetBounds() { return null; }
}
