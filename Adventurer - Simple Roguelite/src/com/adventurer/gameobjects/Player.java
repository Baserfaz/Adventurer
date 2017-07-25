package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.data.Coordinate;
import com.adventurer.data.Inventory;
import com.adventurer.data.PredefinedMaps;
import com.adventurer.data.Session;
import com.adventurer.data.Stats;
import com.adventurer.data.World;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.DoorType;
import com.adventurer.enumerations.GameState;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.*;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Player extends Actor {
	
	private LoSManager losmanager;
	private Inventory inventory;
	private Stats stats;
	
	public Player(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int maxHP, int damage) {
		super(worldPos, tilePos, spritetype, maxHP, damage, "Player");
		
		this.losmanager = new LoSManager();
		this.inventory = new Inventory(Game.START_KEY_COUNT, Game.START_BOMB_COUNT, Game.START_PROJECTILE_COUNT);
		this.stats = new Stats();
	}
	
	public void tick() {
		if(myHP == null) return;
		if(myHP.isDead() == false) {
		    
			UpdatePosition();
			if(Game.CALCULATE_PLAYER_LOS && losmanager != null) losmanager.CalculateLos(this.GetTilePosition());
			
		} else {
		    
		    // save current session data.
			if(Game.instance.getCurrentSession() != null) {
			    Game.instance.getCurrentSession().saveSessionData();
			    Game.instance.setCurrentSession(null);
			} else System.out.println("No currentSession (Player.Tick).");
			
			// die...
			OnDeath(World.instance.GetTileAtPosition(this.GetTilePosition()));
		}
	}
	
	public void render(Graphics g) {
		Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
		renderDirectionArrow(g);
	}
	
	public void Move(Direction dir) {
		
		if(canMove == false) return;
		
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), dir);
		
		if(tile == null) {
			System.out.println("TILE IS NULL!");
			return;
		}
		
		if(Util.isTileValid(tile)) {
			
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
			if(tile instanceof Trap) ((Trap)tile).Activate();
			
		} else if(tile instanceof Door) {
			
			Door door = (Door) tile;
			
			if(door.isLocked() && door.getDoorType() == DoorType.Normal && door.GetTileType() == TileType.LockedDoor) {
				
				if(inventory.getKeyCount() > 0) {
					door.Unlock();
					inventory.addKeys(-1);
				} 
				
			} else if(door.isLocked() && door.getDoorType() == DoorType.Diamond) {
			
				if(inventory.getDiamondKeyCount() > 0) {
					door.Unlock();
					inventory.addDiamondKeyCount(-1);
				}
				
			} else if(door.isLocked() == false) door.Open();
			
		} else if(tile.GetActor() != null) {
			
			if(tile.GetActor() instanceof Enemy || tile.GetActor() instanceof Turret) Attack(tile);
			
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
	        
			    // exit dungeon -> return to lobby.
			    
			    // set game state to loading immediately.
			    Game.instance.setGameState(GameState.Loading);
			    
		        // save session
			    Game.instance.getCurrentSession().saveSessionData();
			    Game.instance.setCurrentSession(null);
                
                // delete current world
                World.instance.Remove();
                
                // create lobby.
                new World(PredefinedMaps.GetLobby());
				
			} else {
				
				if(Game.instance.getCurrentSession() == null) { 
				    Game.instance.setCurrentSession(new Session("session_" + System.currentTimeMillis()));
				} else Game.instance.getCurrentSession().addDungeonLevel(1);
				
				World.instance.Remove();
				new World(Game.ROOM_COUNT);
			}	
			
		} else if(tile.GetTileType() == TileType.DestructibleTile) {
			// TODO: interaction with destructible tiles.
		}
	}
	public LoSManager getLosManager() { return this.losmanager; }
	public Inventory getInventory() { return this.inventory; }
	public Stats getStats() { return this.stats; }
}
