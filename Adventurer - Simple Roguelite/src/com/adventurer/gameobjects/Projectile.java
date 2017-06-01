package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.main.*;

public class Projectile extends GameObject {

	private int damage = 0;
	private Direction direction;
	private boolean isAlive = true;
	
	private int movementSpeed = 3;
	
	private int targetx = this.GetWorldPosition().getX();
	private int targety = this.GetWorldPosition().getY();
	
	private boolean canMove = false;
	
	public Projectile(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int damage, Direction dir) {
		super(worldPos, tilePos, spritetype);
		
		this.damage = damage;
		this.direction = dir;
	}

	public void tick() {
		
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
		// smooth movement
		if(x < targetx - movementSpeed || x > targetx + movementSpeed) {
			
			if(targetx < x) this.GetWorldPosition().decreaseX(movementSpeed);
			else if(targetx > x) this.GetWorldPosition().addX(movementSpeed);
			
			canMove = false;
			
		} else if(y < targety - movementSpeed || y > targety + movementSpeed) {
			
			if(targety < y) this.GetWorldPosition().decreaseY(movementSpeed);
			else if(targety > y) this.GetWorldPosition().addY(movementSpeed);
			
			canMove = false;
			
		} else {
			
			// force move the actor to the exact tile's position.
			this.GetWorldPosition().setX(targetx);
			this.GetWorldPosition().setY(targety);
			
			canMove = true;
		}
		
		// calculate next step 
		if(canMove)
			MoveForward();
	}

	public void render(Graphics g) {
		
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
		if(isAlive) {
			if(World.instance.GetTileAtPosition(this.GetTilePosition()).isHidden() == false) {
				g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			}
		}
	}
	
	public Rectangle GetBounds() {
		return null;
	}

	private void MoveForward() {
			
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), direction);
		World world = World.instance.GetWorld();
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null) {
			
			// tile is our new tile
			world.GetTileAtPosition(this.GetTilePosition()).SetActor(null);
			
			// update our tile position
			this.GetTilePosition().setX(tile.GetTilePosition().getX());
			this.GetTilePosition().setY(tile.GetTilePosition().getY());
			
			// update our world position
			targetx = world.ConvertTilePositionToWorld(tile.GetTilePosition().getX());
			targety = world.ConvertTilePositionToWorld(tile.GetTilePosition().getY());
			
			// set the tile's actor to be this.
			tile.SetActor(this);
			
			// hide ourselves if we are on a hidden tile.
			if(tile.isHidden()) {
				this.Hide();
			}
			
			
		} else if(tile.GetActor() != null) {
			
			GameObject go = tile.GetActor();
			
			if(go instanceof Actor) {
				
				EffectCreator.CreateHitEffect(tile);
				Actor actor = (Actor) go;
				actor.GetHealth().TakeDamage(damage);
			}
			
			isAlive = false;
			Remove();
			
		} else {
			isAlive = false;
			Remove();
		}
	}
	
	public int GetDamage() {
		return this.damage;
	}
}
