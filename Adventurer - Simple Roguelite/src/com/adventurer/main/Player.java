package com.adventurer.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Player extends Actor {
	
	protected boolean canMove = true;
	
	private LoSManager losmanager;
	
	public Player(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int maxHP, int damage) {
		super(worldPos, tilePos, spritetype, maxHP, damage);
		
		this.losmanager = new LoSManager(this);
	}
	
	public void tick() {
		
		if(myHP.isDead() == false) {
		
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
			
			// update LOS
			losmanager.CalculateLos(tilePosition);
			
		} else {
			
			OnDeath(World.instance.GetTileAtPosition(tilePosition));
			
		}
	}
	
	public void render(Graphics g) {
		
		int x = worldPosition.getX();
		int y = worldPosition.getY();
		
		if(lookDir == Direction.East) {
			
			if(flippedSpriteHor == null) {
				flippedSpriteHor = SpriteCreator.instance.FlipSpriteHorizontally(sprite);
			}
			
			g.drawImage(flippedSpriteHor, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
		} else if(lookDir == Direction.West) {
			g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
		} else {
			g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
		}
	}
	
	public void Move(Direction dir) {
		
		if(canMove == false) return;
		
		Tile tile = null;
		World world = Game.instance.GetWorld();
		
		int tilex = this.tilePosition.getX();
		int tiley = this.tilePosition.getY();
		
		switch(dir) {
		case North:
			tile = world.GetTileAtPosition(tilex, tiley - 1);
			break;
		case South:
			tile = world.GetTileAtPosition(tilex, tiley + 1);
			break;
		case East:
			tile = world.GetTileAtPosition(tilex + 1, tiley);
			break;
		case West:
			tile = world.GetTileAtPosition(tilex - 1, tiley);
			break;
		default:
			break;
		}
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null) {
			
			// tile is our new tile
			world.GetTileAtPosition(this.tilePosition).SetActor(null);
			
			// update our tile position
			tilePosition.setX(tile.GetTilePosition().getX());
			tilePosition.setY(tile.GetTilePosition().getY());
			
			// update our world position
			targetx = tile.GetWorldPosition().getX();
			targety = tile.GetWorldPosition().getY();
			
			// set the tile's actor to be this.
			tile.SetActor(this);
			
			// set off trap
			if(tile instanceof Trap) {
				Trap trap = (Trap) tile;
				trap.Activate();
			}
			
		} else if(tile.GetTileType() == TileType.Door) {
			
			((Door)tile).Open();
			
		} else if(tile.GetActor() != null) {
			
			if(tile.GetActor() instanceof Enemy)
				Attack(tile);
			
		} else if(tile.GetTileType() == TileType.DestructibleObject) {
			
			// TODO: Something with the desctructible objects.
			//new Effect(tile.GetX(), tile.GetY(), tile.GetPosition()[0], tile.GetPosition()[1], SpriteType.Hit01, 100);
			//tile.GetTileHealth().TakeDamage(100);
			
		}
	}
}
