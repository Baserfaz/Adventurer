package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.*;

public class VanityItem extends GameObject {
	
	public VanityItem(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype) {
		super(worldPos, tilePos, spritetype);
		
		// register created vanity item to the tile given.
		World.instance.GetTileAtPosition(this.GetTilePosition()).AddVanityItem(this);
	}

	public void tick() {}
	public void render(Graphics g) {}
	public Rectangle GetBounds() { return null; }
	
	public void Remove() {
		
		Tile tile = World.instance.GetTileAtPosition(this.GetTilePosition());
		
		// set tile's actor to null
		// -> others can walk on the tile
		tile.RemoveVanityItem(this);
		
		// remove this object from handler
		// -> no longer ticks
		Handler.instance.RemoveObject(this);
		
		// hide 
		Hide();
	}
	
}
