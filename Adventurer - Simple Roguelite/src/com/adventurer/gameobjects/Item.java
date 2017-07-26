package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.ItemType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.Handler;

public class Item extends GameObject {

	protected String itemName;
	protected ItemType itemType;
	
	public Item(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, ItemType itemType) {
		super(worldPos, tilePos, spritetype);
		
		this.itemType = itemType;
		this.itemName = "";
		
		// register to tile
		World.instance.GetTileAtPosition(tilePos).SetItem(this);
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

	public void tick() {}
	public void render(Graphics g) {}
	public Rectangle GetBounds() { return null; }

	public String toString() {
		return this.itemName + ", " + this.itemType;
	}
}
