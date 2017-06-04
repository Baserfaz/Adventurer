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
		this.myHP = new Health(maxHP, this);
		
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
			VanityItemCreator.CreateVanityItem(tile, remainsSpriteType);
			
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
		
		Tile projStartTile = World.instance.GetTileFromDirection(originTilePos, direction);
		
		if((projStartTile.GetTileType() == TileType.Floor || projStartTile.GetTileType() == TileType.TrapTile ) &&
				projStartTile.GetActor() == null && projStartTile.GetItem() == null) {
			
			new Projectile(projStartTile.GetWorldPosition(), projStartTile.GetTilePosition(), SpriteType.Projectile01, damage, direction);
			
		} else if(projStartTile.GetActor() != null) {
			
			ActorManager.ActorTakeDamage(projStartTile, damage);
			
		} else {
			
			EffectCreator.CreateHitEffect(projStartTile);
			
		}
		
	}
	
	public void Attack(Tile tile) {
		
		GameObject object = tile.GetActor();
		if(object == null) return;
		
		// take damage
		ActorManager.ActorTakeDamage(tile, 100);
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
