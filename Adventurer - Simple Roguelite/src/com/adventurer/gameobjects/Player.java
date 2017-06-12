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
			
			if(Game.CALCULATE_PLAYER_LOS) {
				if(losmanager != null) losmanager.CalculateLos(this.GetTilePosition());
			}
			
		} else {
			
			OnDeath(World.instance.GetTileAtPosition(this.GetTilePosition()));
			
		}
	}
	
	public void render(Graphics g) {
		
		if(lookDir == Direction.East) {
			
			if(flippedSpriteHor == null) {
				flippedSpriteHor = SpriteCreator.instance.FlipSpriteHorizontally(sprite);
			}
			
			Renderer.RenderSprite(flippedSpriteHor, this.GetWorldPosition(), g);
			
		} else if(lookDir == Direction.West) {
			
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
			
		} else {
			
			// TODO: up & down
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
			
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
			
			// save our position
			this.setTile(tile);
			
			// set off trap
			if(tile instanceof Trap) {
				((Trap)tile).Activate();
			}
			
		} else if(tile instanceof Door) {
			
			Door door = (Door) tile;
			if(door.isLocked()) {
				
				// TODO: KEYS
				door.Unlock();
				
				// effects
				EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 7), SpriteType.LockedDoor01Gib01);
				
			} else {
				door.Open();
			}
			
		} else if(tile.GetActor() != null) {
			
			if(tile.GetActor() instanceof Enemy)
				Attack(tile);
			
		} else if(tile.GetItem() != null) {
			
			if(tile.GetItem() instanceof DestructibleItem)
				Attack(tile);
			
		} else if(tile.GetTileType() == TileType.DestructibleTile) {
			// TODO
		}
	}
}
