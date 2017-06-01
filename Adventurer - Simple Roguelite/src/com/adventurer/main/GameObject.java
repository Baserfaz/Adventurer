package com.adventurer.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adventurer.gameobjects.Tile;

public abstract class GameObject {

	protected Coordinate worldPosition; // this is WORLD position, in pixels.
	protected Coordinate tilePosition; // this is TILE position in the grid.
	
	protected boolean hidden = true;
	protected boolean discovered = false;
	
	protected SpriteType spriteType;
	protected BufferedImage sprite;
	protected BufferedImage tintedSprite = null;
	
	public GameObject(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype) {
		
		// create world coordinates
		this.worldPosition = worldPos;
		
		// create tile coordinates
		this.tilePosition = tilePos;
		
		// cache sprite type
		this.spriteType = spritetype;
		
		// create sprite
		this.sprite = SpriteCreator.instance.CreateSprite(spritetype);
		
		// add to handler
		Handler.instance.AddObject(this);
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle GetBounds();
	
	public void Remove() {
		
		// get tile
		Tile tile = World.instance.GetTileAtPosition(this.tilePosition);
		
		// set tile's actor to null
		// -> others can walk on the tile
		tile.SetActor(null);
		
		// remove this object from handler
		// -> no longer ticks
		Handler.instance.RemoveObject(this);
		
		// hide 
		Hide();
	}
	
	public void Discover() { 
		this.discovered = true; 
	}
	
	public void Show() {
		this.hidden = false;
	}
	
	public void Hide() {
		this.hidden = true;
	}
	
	public boolean isHidden() {
		return this.hidden;
	}
	
	public BufferedImage GetTintedSprite() {
		return this.tintedSprite;
	}
	
	public void SetTintedSprite(BufferedImage b) {
		this.tintedSprite = b;
	}
	
	public void SetSprite(BufferedImage i) {
		this.sprite = i;
	}
	
	public BufferedImage GetSprite() {
		return this.sprite;
	}
	
	public Coordinate GetWorldPosition() {
		return this.worldPosition;
	}
	
	public Coordinate GetTilePosition() {
		return this.tilePosition;
	}
}
