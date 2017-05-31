package com.adventurer.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {

	private int damage = 0;
	private Direction direction;
	private boolean isAlive = true;
	
	private int movementSpeed = 3;
	
	private int targetx = worldPosition.getX();
	private int targety = worldPosition.getY();
	
	private boolean canMove = false;
	
	public Projectile(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int damage, Direction dir) {
		super(worldPos, tilePos, spritetype);
		
		this.damage = damage;
		this.direction = dir;
	}

	public void tick() {
		
		int x = worldPosition.getX();
		int y = worldPosition.getY();
		
		// smooth movement
		if(x < targetx - movementSpeed || x > targetx + movementSpeed) {
			
			if(targetx < x) worldPosition.decreaseX(movementSpeed);
			else if(targetx > x) worldPosition.addX(movementSpeed);
			
			canMove = false;
			
		} else if(y < targety - movementSpeed || y > targety + movementSpeed) {
			
			if(targety < y) worldPosition.decreaseY(movementSpeed);
			else if(targety > y) worldPosition.addY(movementSpeed);
			
			canMove = false;
			
		} else {
			
			// force move the actor to the exact tile's position.
			worldPosition.setX(targetx);
			worldPosition.setY(targety);
			
			canMove = true;
		}
		
		// calculate next step 
		if(canMove)
			MoveForward();
	}

	public void render(Graphics g) {
		
		int x = worldPosition.getX();
		int y = worldPosition.getY();
		
		if(isAlive) {
			if(World.instance.GetTileAtPosition(tilePosition.getX(), tilePosition.getY()).isHidden() == false) {
				g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			}
		}
	}
	
	public Rectangle GetBounds() {
		return null;
	}

	private void MoveForward() {
			
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), direction);
		World world = Game.instance.GetWorld();
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null) {
			
			// tile is our new tile
			world.GetTileAtPosition(tilePosition.getX(), tilePosition.getY()).SetActor(null);
			
			// update our tile position
			tilePosition.setX(tile.GetTilePosition().getX());
			tilePosition.setY(tile.GetTilePosition().getY());
			
			// update our world position
			targetx = world.ConvertTilePositionToWorld(tile.tilePosition.getX());
			targety = world.ConvertTilePositionToWorld(tile.tilePosition.getY());
			
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
