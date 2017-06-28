package com.adventurer.gameobjects;

import java.util.List;

import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.enumerations.TrapType;
import com.adventurer.main.*;

public class Trap extends Tile {

	private int damage = 0;
	private TrapType trapType;
	
	public Trap(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType tiletype, TrapType traptype, int damage) {
		super(worldPos, tilePos, spritetype, tiletype);
		
		this.setDamage(damage);
		this.trapType = traptype;
	}

	public void Activate() {
		
		switch(this.trapType) {
		case Projectile:
			ActivateProjectileTrap();
			break;
		case Gas:
			ActivateGasTrap();
			break;
		}
		
	}
	
	private void ActivateGasTrap() {
		
		List<Tile> tiles = World.instance.GetSurroundingTiles(this.GetTilePosition());
		for(Tile tile : tiles) {
			if (tile.GetActor() != null) {
				if(tile.GetActor() instanceof Enemy || tile.GetActor() instanceof Player) {
					DamageHandler.ActorTakeDamage(tile.GetActor(), damage);
				}
			}
		}
		
		EffectCreator.CreateGasEffectArea(this, Util.GetRandomInteger(3, 7));
	}
	
	private void ActivateProjectileTrap() {
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
				new Exception().printStackTrace();
				System.exit(1);
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
		new Projectile(current.GetWorldPosition(), current.GetTilePosition(), SpriteType.Arrow01, this.damage, projectileDir);
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
