package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.adventurer.data.Coordinate;
import com.adventurer.data.Health;
import com.adventurer.data.Inventory;
import com.adventurer.data.PredefinedMaps;
import com.adventurer.data.World;
import com.adventurer.enumerations.BombType;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.EnemyType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.*;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Actor extends GameObject {
	
	protected int targetx = this.GetWorldPosition().getX(), targety = this.GetWorldPosition().getY(); 
	
	protected BufferedImage directionArrow = null;
	
	protected Direction lookDir;
	protected Direction lastLookDir;
	
	protected boolean canMove = true;
	protected Health myHP;
	
	protected int meleeDamage = 0, rangedDamage = 0, magicDamage = 0;
	
	protected String name = "";
	protected int movementSpeed = 2; // how fast is the animation between tiles
	
	public Actor(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int maxHP, int meleeDamage, int rangedDamage, int magicDamage, String name, int movementSpeed) {
		super(worldPos, tilePos, spritetype);
		
		// set stuff here
		this.lookDir = Direction.West;

		// set damage types
		this.meleeDamage = meleeDamage;
		this.rangedDamage = rangedDamage;
		this.magicDamage = magicDamage;
		
		// set other stuff
		this.name = name;
		this.movementSpeed = movementSpeed;
		this.myHP = new Health(maxHP);
		
		// register to tile
		World.instance.GetTileAtPosition(tilePos).SetActor(this);
	}
	
	public void forceMove(Coordinate worldPos, Coordinate tilePos) {
		
		Tile tile = World.instance.GetTileAtPosition(tilePos);
		
		// update our tile position
		this.SetTilePosition(tilePos.getX(), tilePos.getY());
		
		// update our world position
		this.targetx = worldPos.getX();
		this.targety = worldPos.getY();
		
		this.SetWorldPosition(worldPos);
		
		// set the tile's actor to be this.
		tile.SetActor(this);
	}
	
	protected void renderDirectionArrow(Graphics g) {
		
		if(this instanceof Player) {
			
			if(Game.RENDER_PLAYER_DIRECTION_ARROW) {
				
				if(directionArrow == null) {
					directionArrow = SpriteCreator.instance.CreateSprite(SpriteType.DirectionArrow);
				}
				
				Renderer.RenderSprite(directionArrow, this.GetWorldPosition(), lookDir, g);
			}
			
		} else {
			
			if(Game.RENDER_ACTORS_DIRECTION_ARROW == false) return;
			
			if(directionArrow == null) {
				directionArrow = SpriteCreator.instance.CreateSprite(SpriteType.DirectionArrow);
			}
			
			Renderer.RenderSprite(directionArrow, this.GetWorldPosition(), lookDir, g);
		}
	}
	
	public void OnDeath(Tile tile) {
		
		if(this instanceof Enemy) {

			EnemyType enemyType = ((Enemy)this).getEnemyType();
			
			// remove gameobject
			Remove();
			
			//if(Game.MAGGOTS_SPAWN_EGGS_ON_DEATH) if(enemyType == EnemyType.Maggot) this.UseBomb(World.instance.GetTileAtPosition(this.GetTilePosition()));
			
		} else if(this instanceof Player) {
			
			ActorManager.RemovePlayer();
			World.instance.Remove();
			new World(PredefinedMaps.GetLobby());
			
		} else if(this instanceof Turret) Remove();
	}
	
	public void Remove() {
		
		// get tile
		Tile tile = World.instance.GetTileAtPosition(this.GetTilePosition());
		
		// set tile's actor to null
		// -> others can walk on the tile
		tile.SetActor(null);
		
		// remove this object from handler
		// -> no longer ticks
		Handler.instance.RemoveObject(this);
		
		// hide 
		Hide();
	}
	
	public void UseBomb(Tile tile) {
		
		if(Util.isTileValid(tile)) {
			
			if(this instanceof Player) {
				
				Player player = (Player) this;
				Inventory inv = player.getInventory();
				
				if(inv.getBombCount() > 0) {
					new Bomb(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Bomb01, 1500, 300, BombType.Normal);
					inv.addBombs(-1);
				} else {
					// TODO: ERROR EFFECT FOR NO BOMBS!
				}
				
			} else if(this instanceof Enemy) {
				
				Enemy enemy = (Enemy) this;
				
				/*if(enemy.getEnemyType() == EnemyType.Maggot) {
					
				    // TODO: bomb --> egg?
					new Bomb(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Bomb01, 900, 150, BombType.Gas);
					
				} else {
				
					new Bomb(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Bomb01, 1500, 300, BombType.Normal);
					
				}*/
			}
		}
	}
	
	public void Shoot(Coordinate originTilePos, Direction direction, SpriteType projSpriteType) {
		
		if(this instanceof Player) {
			
			Player player = (Player) this;
			Inventory inv = player.getInventory();
			
			if(inv.getProjectileCount() > 0) {
				
				Tile projStartTile = World.instance.GetTileAtPosition(originTilePos);
				new Projectile(projStartTile.GetWorldPosition(), projStartTile.GetTilePosition(), projSpriteType, rangedDamage, direction);
				
				inv.addProjectiles(-1);
			}
			
		} else {
			
			// enemies who can shoot dont lose projectiles.
			Tile projStartTile = World.instance.GetTileAtPosition(originTilePos);
			new Projectile(projStartTile.GetWorldPosition(), projStartTile.GetTilePosition(), projSpriteType, rangedDamage, direction);
			
		}
	}
	
	public void Attack(Tile tile) {
		
		// 1. try get actor
		GameObject object = tile.GetActor();
		
		if(object != null) DamageHandler.ActorTakeDamage(tile, meleeDamage);
		
		// 2. if actor is null 
		//    try get item
		/*if(object == null) {
			
			object = tile.GetItem();
			
			// 3. if the item is destructible
			if(object instanceof DestructibleItem) {
				
				// 4. object takes damage
				DamageHandler.ItemTakeDamage((DestructibleItem) object, meleeDamage);
			}
			
		} else {
			
			// 5. actor takes damage
			DamageHandler.ActorTakeDamage(tile, meleeDamage);
			
		}*/
	}
	
	protected void UpdatePosition() {
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
		// smooth movement
		if(x < targetx - movementSpeed || x > targetx + movementSpeed) {
			
			if(targetx < x) this.SetWorldPosition(x - movementSpeed, y);
			else if(targetx > x) this.SetWorldPosition(x + movementSpeed, y);
			
			canMove = false;
			
		} else if(y < targety - movementSpeed || y > targety + movementSpeed) {
			
			if(targety < y) this.SetWorldPosition(x, y - movementSpeed);
			else if(targety > y) this.SetWorldPosition(x, y + movementSpeed);
			
			canMove = false;
			
		} else {
			
			// force move the actor to the exact tile's position.
			this.SetWorldPosition(targetx, targety);
			
			canMove = true;
		}
	}
	
	public void render(Graphics g) {}
	public void tick() {}
	public void Move(Direction dir) {}
	
	public String toString() { return this.name; }
	
	public Direction GetLookDirection() { return this.lookDir; }
	public void SetLookDirection(Direction dir) { this.lookDir = dir; }	
	
	public void AddRangedDamage(int a) { this.rangedDamage += a; }
	public void SetRangedDamage(int a) { this.rangedDamage = a; }
	
	public void AddMagicDamage(int a) { this.magicDamage += a; }
	public void SetMagicDamage(int a) { this.magicDamage = a; }
	
	public void AddMeleeDamage(int a) { this.meleeDamage += a; }	
	public void SetMeleeDamage(int a) { this.meleeDamage = a;}
	
	public int GetMeleeDamage() { return this.meleeDamage; }
	public int GetRangedDamage() { return this.rangedDamage; }
	public int GetMagicDamage() { return this.magicDamage; }
	
	public String getName() { return this.name; }
	public Health GetHealth() { return this.myHP; }
	
	public Rectangle GetBounds() {
		return new Rectangle(this.GetWorldPosition().getX(), this.GetWorldPosition().getY(), Game.SPRITESIZE, Game.SPRITESIZE);
	}
}
