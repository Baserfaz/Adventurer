package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.BombType;
import com.adventurer.enumerations.ItemType;
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
	
	public Bomb(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int liveTime, int damage, BombType btype) {
		super(worldPos, tilePos, spritetype, ItemType.Bomb);
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
			
			//int gibAmount = Util.GetRandomInteger(4, 10);
			//Tile currentTile = World.instance.GetTileAtPosition(this.GetTilePosition());
			
			// create gib effect
			/*if(bombType == BombType.Normal) {
				EffectCreator.CreateGibs(currentTile, gibAmount, SpriteType.BombGib01);
			} else if(bombType == BombType.Gas){
				EffectCreator.CreateGibs(currentTile, gibAmount, SpriteType.GasBarrelGib01);
			}*/
			
			for(Tile tile : tiles) {
				
				//int amount = Util.GetRandomInteger(4, 10);
				
				// create smoke effect on tile.
				/*if(bombType == BombType.Normal) {
					EffectCreator.CreateSmokeEffect(tile, amount);
				} else if(bombType == BombType.Gas) {
					EffectCreator.CreateGasEffect(tile, amount);
				}*/
				
				// Damage:
				// 1. destructible tiles
				// 2. doors
				// 3. actors
				if(tile instanceof DestructibleTile) {
					
					((DestructibleTile)tile).getTileHealth().TakeDamage(this.damage);
				
					// effects
					//EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 7), SpriteType.Wall01Gib01);
					
				} else if(tile instanceof Door) {
					
					if(tile.GetTileType() == TileType.LockedDoor) {
						
						// dont do anything.
						
					} else {
						// open door
						((Door) tile).Open();
						//EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 6), SpriteType.PotGib01);
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
