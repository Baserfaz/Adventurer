package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.adventurer.main.*;

public class Actor extends GameObject {
	
	protected int targetx = this.GetWorldPosition().getX(), targety = this.GetWorldPosition().getY(); 
	protected int movementSpeed = 2;
	
	protected BufferedImage flippedSpriteHor = null;
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
		World.instance.GetTileAtPosition(this.GetTilePosition()).SetActor(this);
		
	}
	
	public void render(Graphics g) {}
	public void tick() {}
	public void Move(Direction dir) {}
	
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
			
		} else {
			System.out.println("Player ded");
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
	
	public void Shoot(Coordinate originTilePos, Direction direction) {
		Tile projStartTile = World.instance.GetTileAtPosition(originTilePos);
		new Projectile(projStartTile.GetWorldPosition(), projStartTile.GetTilePosition(), SpriteType.Projectile01, damage, direction);
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
