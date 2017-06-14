package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.adventurer.main.*;

public abstract class GameObject {

	private Coordinate worldPosition; // this is WORLD position, in pixels.
	private Coordinate tilePosition; // this is TILE position in the grid.
	
	protected boolean hidden = true;
	protected boolean discovered = false;
	
	protected SpriteType spriteType;
	protected BufferedImage sprite;
	protected BufferedImage tintedSprite = null;

	//protected Tile currentTile;
	
	public GameObject(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype) {
		
		// create world coordinates
		this.worldPosition = worldPos;
		
		// create tile coordinates
		this.tilePosition = tilePos;
		
		// cache tile information
		//this.currentTile = World.instance.GetTileAtPosition(this.GetTilePosition());
		
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
	
	public String GetInfo() {
		return "GameObject: " + this.toString() + ", tilePos: (" + this.GetTilePosition().getX() + ", " + this.GetTilePosition().getY() +"), "
				+ "worldPos: (" + this.GetWorldPosition().getX() + ", " + this.GetWorldPosition().getY() + ")" + ", hidden: " + this.hidden + ", discovered: " + this.discovered;
	}
	
	/*public Tile getTile() {
		return this.currentTile;
	}
	
	public void setTile(Tile tile) {
		this.currentTile = tile;
	}*/
	
	public boolean isDiscovered() {
		return this.discovered;
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
	
	public void SetWorldPosition(int x, int y) {
		this.worldPosition = new Coordinate(x, y);
	}
	
	public void SetTilePosition(int x, int y) {
		this.tilePosition = new Coordinate(x, y);
	}
	
	public void SetWorldPosition(Coordinate pos) {
		this.worldPosition = pos;
	}
	
	public void SetTilePosition(Coordinate pos) {
		this.tilePosition = pos;
	}
	
	public Coordinate GetWorldPosition() {
		return this.worldPosition;
	}
	
	public Coordinate GetTilePosition() {
		return this.tilePosition;
	}
}
