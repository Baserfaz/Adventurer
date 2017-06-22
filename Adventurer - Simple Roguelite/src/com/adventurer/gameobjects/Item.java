package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.main.Coordinate;
import com.adventurer.main.Handler;
import com.adventurer.main.ItemType;
import com.adventurer.main.SpriteType;
import com.adventurer.main.World;

public class Item extends GameObject {

	protected ItemType itemType;
	
	public Item(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, ItemType itemType) {
		super(worldPos, tilePos, spritetype);
		
		this.itemType = itemType;
		
		// register to tile
		World.instance.GetTileAtPosition(tilePos).SetItem(this);
	}
	
	public void tick() {}
	
	public void render(Graphics g) {}
	
	public Rectangle GetBounds() {
		return null;
	}
	
	public void Remove() {
		
		// get tile
		Tile tile = World.instance.GetTileAtPosition(this.GetTilePosition());
		
		tile.SetItem(null);
		
		// remove this object from handler
		// -> no longer ticks
		Handler.instance.RemoveObject(this);
		
		// hide 
		Hide();
	}
}
