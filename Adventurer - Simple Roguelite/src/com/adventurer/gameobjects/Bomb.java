package com.adventurer.gameobjects;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.adventurer.data.World;
import com.adventurer.enumerations.BombType;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.SpriteType;

public class Bomb extends Usable {

	private Map<DamageType, Integer> damage;
	private BombType bombType;
	
	private long liveTime = 0;
	private long liveTimer = 0;
	private boolean active = false;
	
	public Bomb(Tile tile, SpriteType spritetype, int liveTime, Map<DamageType, Integer> damage, BombType btype, String name, String description, int value) {
		super(tile, spritetype, name, description, value);
		
		this.damage = new LinkedHashMap<DamageType, Integer>(damage);
		this.liveTime = liveTime;
		this.bombType = btype;
	}
	
	public void tick() {
		
		if(active == false) return;
		
		if(System.currentTimeMillis() > liveTimer) {
			
			// do damage to items near this position
			List<Tile> tiles = World.instance.GetTilesInCardinalDirection(this.GetTilePosition());
			
			// add the current tile to the tiles list
			tiles.add(World.instance.GetTileAtPosition(this.GetTilePosition()));
			
			// TODO: FIX BOMBS
			
			/*for(Tile tile : tiles) {
				
				// Do damage:
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
				
			}*/
			
			// destroy this object
			Remove();
		}
	}
	
	public void use() { 
		this.liveTimer = System.currentTimeMillis() + this.liveTime;
		this.active = true;
		
		// TODO: remove from inventory and put down on a tile
	}
}
