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
		
		if(myHP == null) return;
		
		if(myHP.isDead() == false) {
		
			UpdatePosition();
			
			// update LOS
			//if(losmanager != null) losmanager.CalculateLos(this.GetTilePosition());
			
		} else {
			
			OnDeath(World.instance.GetTileAtPosition(this.GetTilePosition()));
			
		}
	}
	
	public void render(Graphics g) {
		
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
		if(lookDir == Direction.East) {
			
			if(flippedSpriteHor == null) {
				flippedSpriteHor = SpriteCreator.instance.FlipSpriteHorizontally(sprite);
			}
			
			g.drawImage(flippedSpriteHor, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			
		} else if(lookDir == Direction.West) {
			
			g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			
		} else {
			
			// TODO: up & down
			g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			
		}
	}
	
	public void Move(Direction dir) {
		
		if(canMove == false) return;
		
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), dir);
		World world = World.instance.GetWorld();
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null && tile.GetItem() == null) {
			
			// we are no longer on the last tile
			Tile lastTile = world.GetTileAtPosition(this.GetTilePosition());
			lastTile.SetActor(null);
			
			// update our tile position
			this.GetTilePosition().setX(tile.GetTilePosition().getX());
			this.GetTilePosition().setY(tile.GetTilePosition().getY());
			
			// update our world position
			this.targetx = tile.GetWorldPosition().getX();
			this.targety = tile.GetWorldPosition().getY();
			
			// set the tile's actor to be this.
			tile.SetActor(this);
			
			// set off trap
			if(tile instanceof Trap) {
				((Trap)tile).Activate();
			}
			
		} else if(tile.GetTileType() == TileType.Door) {
			
			((Door)tile).Open();
			
		} else if(tile.GetActor() != null) {
			
			if(tile.GetActor() instanceof Enemy)
				Attack(tile);
			
		} else if(tile.GetTileType() == TileType.DestructibleObject) {
			// TODO
		}
	}
}
