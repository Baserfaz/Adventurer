package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.Handler;

public class Item extends GameObject {

	protected String itemName;
	
	public Item(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype) {
		super(worldPos, tilePos, spritetype);
		
		this.itemName = "";
		
		// register to tile
		World.instance.GetTileAtPosition(tilePos).AddItem(this);
	}
	
	public void Remove() {
		
		// get tile
		Tile tile = World.instance.GetTileAtPosition(this.GetTilePosition());
		
		tile.RemoveItem(this);
		
		// remove this object from handler
		// -> no longer ticks
		Handler.instance.RemoveObject(this);
		
		// hide 
		Hide();
	}

	public void tick() {}
	public void render(Graphics g) {}
	public Rectangle GetBounds() { return null; }
}
