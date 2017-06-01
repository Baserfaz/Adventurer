package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.main.*;

public class VanityItem extends GameObject {
	
	private Tile tile;
	
	public VanityItem(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype) {
		super(worldPos, tilePos, spritetype);
		
		this.tile = World.instance.GetTileAtPosition(tilePos);
	}

	public void tick() {}
	public void render(Graphics g) {}
	public Rectangle GetBounds() { return null; }
	
	@Override
	public void Remove() {
		
		// set tile's actor to null
		// -> others can walk on the tile
		tile.RemoveVanityItem(this);
		
		// remove this object from handler
		// -> no longer ticks
		Game.instance.GetHandler().RemoveObject(this);
		
		// hide 
		Hide();
	}
	
}
