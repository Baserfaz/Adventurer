package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.adventurer.main.*;

public class Projectile extends Item {
	
	private Direction direction;
	private boolean alive = true;
	private boolean tileDiscovered = true;
	
	private int damage = 0;
	private int movementSpeed = 2;
	
	private int targetx = this.GetWorldPosition().getX();
	private int targety = this.GetWorldPosition().getY();
	
	private boolean canMove = true;
	
	public Projectile(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int damage, Direction dir) {
		super(worldPos, tilePos, spritetype);
		this.direction = dir;
		this.damage = damage;
	}
	
	public void tick() {
		
		// "animate"
		UpdatePosition();
		
		// calculate next step 
		if(canMove) MoveForward();
		
		// if the tile is not yet discovered
		// hide this projectile.
		boolean f = World.instance.GetTileAtPosition(this.GetTilePosition()).discovered;
		if(f == false) this.tileDiscovered = false;
	}
	
	public void render(Graphics g) {
		
		if(alive && hidden == false) {
			
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), direction, g);
			
		} else if(tileDiscovered == false) { 
		
			// dont render 
			
		} else if(alive && discovered && hidden) {
			
			if(tintedSprite == null) {
				tintedSprite = Util.tint(sprite, true);
			}
			
			Renderer.RenderSprite(tintedSprite, this.GetWorldPosition(), direction, g);
		}
	}
	
	public Rectangle GetBounds() {
		return null;
	}
	
	private void UpdatePosition() {
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
		// smooth movement
		if(x < targetx - movementSpeed || x > targetx + movementSpeed) {
			
			if(targetx < x) this.SetWorldPosition(x - movementSpeed, y);
			else if(targetx > x) this.SetWorldPosition(x + movementSpeed, y);
			
			canMove = false;
			
		} else if(y < targety - movementSpeed || y > targety + movementSpeed) {
			
			if(targety < y) this.SetWorldPosition(x, y - movementSpeed);
			else if(targety > y) this.SetWorldPosition(x, y + movementSpeed);
			
			canMove = false;
			
		} else {
			
			// force move the actor to the exact tile's position.
			this.SetWorldPosition(targetx, targety);
			
			canMove = true;
		}
	}
	
	private void MoveForward() {
			
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), this.direction);
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null && tile.GetItem() == null) {
			
			// we are no longer on the last tile
			Tile lastTile = World.instance.GetTileAtPosition(this.GetTilePosition());
			lastTile.SetItem(null);
			
			int x = tile.GetTilePosition().getX();
			int y = tile.GetTilePosition().getY();
			
			// update our tile position
			this.SetTilePosition(x, y);
			
			// update our world position
			this.targetx = tile.GetWorldPosition().getX();
			this.targety = tile.GetWorldPosition().getY();
			
			// set the tile's actor to be this.
			tile.SetItem(this);
			
		} else if(tile.GetActor() != null) {
			
			DamageHandler.ActorTakeDamage(tile, damage);
			
			alive = false;
			Remove();
			
		} else if(tile.GetItem() != null) {
			
			Item item = tile.GetItem();
			
			if(item instanceof DestructibleItem) {
				DamageHandler.ItemTakeDamage((DestructibleItem) item, damage);
			}
			
			alive = false;
			Remove();
			
		} else {
			
			// create effect
			if(tile.isDiscovered()) EffectCreator.CreateStaticHitEffect(tile);
			
			alive = false;
			Remove();
		}
	}
	
	public int GetDamage() {
		return this.damage;
	}
}
