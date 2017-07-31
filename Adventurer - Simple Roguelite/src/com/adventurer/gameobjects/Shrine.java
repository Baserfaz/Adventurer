package com.adventurer.gameobjects;

import java.util.List;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.ShrineType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;

public class Shrine extends Tile {

	private boolean used = false;
	private int amount = 0;
	private ShrineType shrineType;
	
	public Shrine(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type, ShrineType shrineType, int amount) {
		super(worldPos, tilePos, spritetype, type);
		
		this.amount = amount;
		this.shrineType = shrineType;
	}
	
	public void activate() {
		if(used == false) {
			
			// do 8 tile AOE
			if(shrineType == ShrineType.healing) {
				
				List<Tile> tiles = World.instance.GetSurroundingTiles(this.GetTilePosition());
				
				for(Tile tile : tiles) {
					if(tile.GetActor() != null) tile.actor.GetHealth().healDamage(this.amount);
				}
				
				
			} else {
				System.out.print("NOT YET IMPLEMENTED SHRINETYPE.");
				return;
			}
			
			used = true;
		}
	}
	
	public int getAmount() { return amount; }
	public void setAmount(int amount) { this.amount = amount; }

	public ShrineType getShrineType() {return shrineType; }
	public void setShrineType(ShrineType shrineType) { this.shrineType = shrineType; }
}
