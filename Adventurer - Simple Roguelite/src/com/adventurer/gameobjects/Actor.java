package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.adventurer.main.*;

public class Actor extends GameObject {
	
	protected int targetx = worldPosition.getX(), targety = worldPosition.getY(); 
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
		World.instance.GetTileAtPosition(tilePosition).SetActor(this);
		
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
	
	public void Attack(Tile tile) {
		
		GameObject object = tile.GetActor();
		if(object == null) return;
		
		// take damage
		((Actor)object).GetHealth().TakeDamage(damage);
		
		// effects etc.
		EffectCreator.CreateHitEffect(tile);
		VanityItemCreator.CreateSmallBlood(tile); // TODO: skeleton blood
	}
	
	protected void UpdatePosition() {
		int x = worldPosition.getX();
		int y = worldPosition.getY();
		
		// smooth movement
		if(x < targetx - movementSpeed || x > targetx + movementSpeed) {
			
			if(targetx < x) worldPosition.decreaseX(movementSpeed);
			else if(targetx > x) worldPosition.addX(movementSpeed);
			
			canMove = false;
			
		} else if(y < targety - movementSpeed || y > targety + movementSpeed) {
			
			if(targety < y) worldPosition.decreaseY(movementSpeed);
			else if(targety > y) worldPosition.addY(movementSpeed);
			
			canMove = false;
			
		} else {
			
			// force move the actor to the exact tile's position.
			worldPosition.setX(targetx);
			worldPosition.setY(targety);
			
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
		return new Rectangle(worldPosition.getX(), worldPosition.getY(), Game.SPRITESIZE, Game.SPRITESIZE);
	}
}
