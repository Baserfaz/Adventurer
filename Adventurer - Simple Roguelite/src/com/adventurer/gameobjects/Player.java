package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.main.*;

public class Player extends Actor {
	
	private LoSManager losmanager;
	
	public Player(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int maxHP, int damage) {
		super(worldPos, tilePos, spritetype, maxHP, damage);
		
		this.losmanager = new LoSManager();
	}
	
	public void tick() {
		
		if(myHP.isDead() == false) {
		
			UpdatePosition();
			
			// update LOS
			if(losmanager != null) losmanager.CalculateLos(tilePosition);
			
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
		
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), dir);
		World world = World.instance.GetWorld();
		
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
