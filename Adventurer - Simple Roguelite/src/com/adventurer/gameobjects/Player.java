package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.DoorType;
import com.adventurer.enumerations.RoomType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.*;

public class Player extends Actor {
	
	private LoSManager losmanager;
	private Inventory inventory;
	private Session currentSession;
	
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
			
			currentSession.saveSessionData();
			currentSession = null;
			
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
			
			if(door.isLocked() && door.getDoorType() == DoorType.Normal) {
				
				if(inventory.getKeyCount() > 0) {
					
					door.Unlock();
					
					// effects
					EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 7), SpriteType.LockedDoor01Gib01);
					
					inventory.addKeys(-1);
					
				} else {
					
					// TODO EFFECTS
					//EffectCreator.CreateErrorEffect();
				
				}
				
				
			} else if(door.isLocked() && door.getDoorType() == DoorType.Diamond) {
			
				if(inventory.getDiamondKeyCount() > 0) {
					
					door.Unlock();
					
					// effects
					EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 7), SpriteType.LockedDoor01Gib01);
					
					inventory.addDiamondKeyCount(-1);
					
				} else {
					
					// TODO EFFECTS
					//EffectCreator.CreateErrorEffect();
				
				}
				
			} else if(door.isLocked() == false) {
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
			
			Portal portal = (Portal) tile;
			
			if(portal.isExit()) {
				
				currentSession.saveSessionData();
				currentSession = null;
				
				this.losmanager.setRunning(false);
				
				World.instance.Remove();
				new World(PredefinedMaps.GetLobby());
				
				this.losmanager.setRunning(true);
				
			} else {
				
				currentSession = new Session("session_" + System.currentTimeMillis());
				
				this.losmanager.setRunning(false);
				
				World.instance.Remove();
				new World(RoomType.values()[Util.GetRandomInteger(0, RoomType.values().length)]);
				
				this.losmanager.setRunning(true);
				
			}
			
			
		} else if(tile.GetTileType() == TileType.DestructibleTile) {
			// TODO
		} 
	}
	
	public Session getSession() {
		return this.currentSession;
	}
	
	public LoSManager getLosManager() {
		return this.losmanager;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
}
