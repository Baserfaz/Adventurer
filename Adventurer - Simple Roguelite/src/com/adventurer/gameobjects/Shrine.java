package com.adventurer.gameobjects;

import java.util.List;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.ShrineType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.SpriteCreator;

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
			
			if(shrineType == ShrineType.healing) {
				
				if(this.actor != null) this.actor.GetHealth().healDamage(amount);
				System.out.println("Healed " + this.actor.toString() + " for " + amount + ".");
				
			} else {
				System.out.print("NOT YET IMPLEMENTED SHRINETYPE.");
				return;
			}
			
			// change sprite.
			this.SetSprite(SpriteCreator.instance.CreateSprite(SpriteType.UsedShrine));
			
			used = true;
		}
	}
	
	public int getAmount() { return amount; }
	public void setAmount(int amount) { this.amount = amount; }

	public ShrineType getShrineType() {return shrineType; }
	public void setShrineType(ShrineType shrineType) { this.shrineType = shrineType; }
}
