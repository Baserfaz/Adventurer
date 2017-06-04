package com.adventurer.gameobjects;

import com.adventurer.main.*;

public class Trap extends Tile {

	private int damage = 0;
	
	public Trap(Coordinate worldPos, SpriteType spritetype, TileType type, Coordinate tilePos, int damage) {
		super(worldPos, spritetype, type, tilePos);
		
		this.setDamage(damage);
	}

	public void Activate() {
		
		// --------------------------
		// 1. get a random wall in some direction
		// 2. shoot an arrow from that wall towards the trap
		// --------------------------
		
		// 1.1 get a random direction
		Direction randomDir = Util.GetRandomCardinalDirection();
		
		// 1.2 get first wall in random direction
		Tile current = null;
		int xOffset = 0, yOffset = 0;
		
		do {
			
			// calculate coordinate
			Coordinate coord = new Coordinate(this.GetTilePosition().getX() + xOffset, this.GetTilePosition().getY() + yOffset);
			
			// get tile
			current = World.instance.GetTileFromDirection(coord, randomDir);
			
			// set offsets
			switch(randomDir) {
			case North:
				yOffset --;
				break;
			case South:
				yOffset ++;
				break;
			case East:
				xOffset++;
				break;
			case West:
				xOffset--;
				break;
			default:
				System.out.println("NOT A CARDINAL DIRECTION!");
				break;
			}
		} while(current.GetTileType() == TileType.Floor);
		
		// 2.1 get opposite direction for our projectile.
		Direction projectileDir = null;
		if(randomDir == Direction.North) projectileDir = Direction.South;
		else if(randomDir == Direction.South) projectileDir = Direction.North;
		else if(randomDir == Direction.East) projectileDir = Direction.West;
		else if(randomDir == Direction.West) projectileDir = Direction.East;
		
		// 2.2 shoot an arrow
		new Projectile(current.GetWorldPosition(), current.GetTilePosition(), SpriteType.Projectile01, this.damage, projectileDir);
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
