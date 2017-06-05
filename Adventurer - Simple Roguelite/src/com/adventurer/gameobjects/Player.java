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
			if(losmanager != null) losmanager.CalculateLos(this.GetTilePosition());
			
		} else {
			
			OnDeath(World.instance.GetTileAtPosition(this.GetTilePosition()));
			
		}
	}
	
	public void render(Graphics g) {
		
		if(lookDir == Direction.East) {
			
			if(flippedSpriteHor == null) {
				flippedSpriteHor = SpriteCreator.instance.FlipSpriteHorizontally(sprite);
			}
			
			RenderSprite(flippedSpriteHor, this.GetWorldPosition(), g);
			
		} else if(lookDir == Direction.West) {
			
			RenderSprite(sprite, this.GetWorldPosition(), g);
			
		} else {
			
			// TODO: up & down
			RenderSprite(sprite, this.GetWorldPosition(), g);
			
		}
	}
	
	public void Move(Direction dir) {
		
		if(canMove == false) return;
		
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), dir);
		
		if(tile == null) {
			System.out.println("TILE IS NULL!");
			return;
		}
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null && tile.GetItem() == null) {
			
			// we are no longer on the last tile
			Tile lastTile = World.instance.GetTileAtPosition(this.GetTilePosition());
			lastTile.SetActor(null);
			
			int x = tile.GetTilePosition().getX();
			int y = tile.GetTilePosition().getY();
			
			// update our tile position
			this.SetTilePosition(x, y);
			
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
