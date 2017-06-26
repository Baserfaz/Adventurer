package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.main.*;

public class Player extends Actor {
	
	private LoSManager losmanager;
	private Inventory inventory;
	
	public Player(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int maxHP, int damage) {
		super(worldPos, tilePos, spritetype, maxHP, damage);
		
		this.losmanager = new LoSManager();
		this.inventory = new Inventory(Game.START_KEY_COUNT, Game.START_BOMB_COUNT, Game.START_PROJECTILE_COUNT);
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
		
		renderDirectionArrow(g);
		
	}
	
	public void Move(Direction dir) {
		
		if(canMove == false) return;
		
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), dir);
		
		if(tile == null) {
			System.out.println("TILE IS NULL!");
			return;
		}
		
		if(tile.isWalkable() && tile.GetActor() == null && tile.GetItem() == null) {
			
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
			
		} else if(tile instanceof Door) {
			
			Door door = (Door) tile;
			
			if(door.isLocked()) {
				
				if(inventory.getKeyCount() > 0) {
					
					door.Unlock();
					
					// effects
					EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 7), SpriteType.LockedDoor01Gib01);
					
					inventory.addKeys(-1);
					
				} else {
					// TODO EFFECTS
					//EffectCreator.CreateErrorEffect();
				}
				
				
			} else {
				door.Open();
			}
			
		} else if(tile.GetActor() != null) {
			
			if(tile.GetActor() instanceof Enemy || tile.GetActor() instanceof Turret) {
				Attack(tile);
			}
			
		} else if(tile.GetItem() != null) {
			
			if(tile.GetItem() instanceof DestructibleItem) {
				
				Attack(tile);
				
			} else if(tile.GetItem() instanceof Chest) {
				
				Chest chest = (Chest) tile.GetItem();
				
				if(chest.isLocked() && inventory.getKeyCount() > 0) {
					chest.Unlock();
					inventory.addKeys(-1);
				}
				else if(chest.isLocked() == false) chest.Open();
				
			}
			
		} else if(tile instanceof Portal) {
			
			ActorManager.RemovePlayer();
			World.instance.Remove();
			new World(Game.WORLDWIDTH, Game.WORLDHEIGHT, Game.ROOMWIDTH, Game.ROOMHEIGHT);
			
		} else if(tile.GetTileType() == TileType.DestructibleTile) {
			// TODO
		} 
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
}
