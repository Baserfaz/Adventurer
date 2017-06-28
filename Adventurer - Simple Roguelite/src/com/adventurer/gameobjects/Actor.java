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

public class Actor extends GameObject {
	
	protected int targetx = this.GetWorldPosition().getX(), targety = this.GetWorldPosition().getY(); 
	protected int movementSpeed = 2;
	
	protected BufferedImage flippedSpriteHor = null;
	protected BufferedImage directionArrow = null;
	
	protected Direction lookDir;
	protected Direction lastLookDir;
	
	protected boolean canMove = true;
	protected Health myHP;
	protected int damage = 100;
	
	public Actor(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int maxHP, int damage) {
		super(worldPos, tilePos, spritetype);
		
		this.lookDir = Direction.West;
		this.damage = damage;
		this.myHP = new Health(maxHP);
		
		// register to tile
		World.instance.GetTileAtPosition(tilePos).SetActor(this);
	}
	
	public void render(Graphics g) {}
	public void tick() {}
	public void Move(Direction dir) {}
	
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
			
			// which kind of blood/remains are we going to spawn
			SpriteType remainsSpriteType = null;
			
			EnemyType enemyType = ((Enemy)this).getEnemyType();
			
			switch(enemyType) {
			case Skeleton:
				remainsSpriteType = SpriteType.SkeletonRemains01;
				break;
			default:
				remainsSpriteType = SpriteType.Blood01;
				break;
			}
			
			// create vanity item 
			VanityItemCreator.CreateVanityItem(tile, remainsSpriteType, true);
			
			// remove gameobject
			Remove();
			
			if(enemyType == EnemyType.Maggot) {
				// TODO: randomize chance of dropping "an exploding egg".
				this.UseBomb(World.instance.GetTileAtPosition(this.GetTilePosition()));
			}
			
		} else if(this instanceof Player) {
			
			// create vanity item 
			VanityItemCreator.CreateVanityItem(tile, SpriteType.PlayerRemains01, true);
			
			ActorManager.RemovePlayer();
			World.instance.Remove();
			new World(PredefinedMaps.GetLobby());
			
		} else if(this instanceof Turret) {
			
			// create vanity item 
			VanityItemCreator.CreateVanityItem(tile, SpriteType.PotRemains01, true);
			
			Remove();
		}
		
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
		
		if(tile.isWalkable() && tile.GetActor() == null && tile.GetItem() == null) {
			
			
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
				
				if(enemy.getEnemyType() == EnemyType.Maggot) {
					
					new Bomb(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Egg01, 900, 150, BombType.Gas);
					
				} else {
				
					new Bomb(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Bomb01, 1500, 300, BombType.Normal);
					
				}
				
				
			}
		}
	}
	
	public void Shoot(Coordinate originTilePos, Direction direction, SpriteType projSpriteType) {
		
		if(this instanceof Player) {
			
			Player player = (Player) this;
			Inventory inv = player.getInventory();
			
			if(inv.getProjectileCount() > 0) {
				
				Tile projStartTile = World.instance.GetTileAtPosition(originTilePos);
				new Projectile(projStartTile.GetWorldPosition(), projStartTile.GetTilePosition(), projSpriteType, damage, direction);
				
				inv.addProjectiles(-1);
				
			} else {
				
				// TODO: ERROR EFFECT NO PROJECTILES
				
			}
			
		} else {
			
			// enemies who can shoot dont lose projectiles.
			Tile projStartTile = World.instance.GetTileAtPosition(originTilePos);
			new Projectile(projStartTile.GetWorldPosition(), projStartTile.GetTilePosition(), projSpriteType, damage, direction);
			
		}
	}
	
	public void Attack(Tile tile) {
		
		// 1. try get actor
		GameObject object = tile.GetActor();
		
		// 2. if actor is null 
		//    try get item
		if(object == null) {
			
			object = tile.GetItem();
			
			// 3. if the item is destructible
			if(object instanceof DestructibleItem) {
				
				// 4. object takes damage
				DamageHandler.ItemTakeDamage((DestructibleItem) object, damage);
			}
			
		} else {
			
			// 5. actor takes damage
			DamageHandler.ActorTakeDamage(tile, damage);
			
		}
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
	
	public Direction GetLookDirection() {
		return this.lookDir;
	}
	
	public void SetLookDirection(Direction dir) {
		this.lookDir = dir;
	}
	
	public void DecreaseDamage(int a) {
		this.damage -= a;
	}
	
	public void AddDamage(int a) {
		this.damage += a;
	}
	
	public void SetDamage(int a) {
		this.damage = a;
	}
	
	public int GetDamage() {
		return this.damage;
	}
	
	public Health GetHealth() {
		return this.myHP;
	}
	
	public Rectangle GetBounds() {
		return new Rectangle(this.GetWorldPosition().getX(), this.GetWorldPosition().getY(), Game.SPRITESIZE, Game.SPRITESIZE);
	}
}
