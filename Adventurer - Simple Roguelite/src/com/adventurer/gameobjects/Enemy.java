package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.adventurer.main.*;

public class Enemy extends Actor {

	private int moveCooldownBase = 2000;
	private long moveTimer = 0;
	
	private EnemyType enemyType;
	
	
	public Enemy(Coordinate worldPos, Coordinate tilePos, EnemyType type, SpriteType spritetype, int maxHP, int damage) {
		super(worldPos, tilePos, spritetype, maxHP, damage);
		
		this.setEnemyType(type);
		this.moveTimer = System.currentTimeMillis();
	}

	public void render(Graphics g) {
		
		if(hidden == false) {
			
			if(lookDir == Direction.East) {
				
				if(flippedSpriteHor == null) {
					flippedSpriteHor = SpriteCreator.instance.FlipSpriteHorizontally(sprite);
				}
				
				Renderer.RenderSprite(flippedSpriteHor, this.GetWorldPosition(), g);
				
			} else if(lookDir == Direction.West) {
				
				Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
				
			}
			
		} else if(hidden == true && discovered == true){
			
			// create ghost image?
			
		}
	}
	
	public void tick() {
		
		if(myHP.isDead() == false) {
		
			// only calculate discovered enemies behavior.
			if(this.discovered == false) return;
			
			long current = System.currentTimeMillis();
			
			if(current > moveTimer && canMove) {
				
				// randomize direction
				Direction randomDir = Util.GetRandomCardinalDirection();
				
				// update facing
				if(randomDir == Direction.East || randomDir == Direction.West) lookDir = randomDir;
				
				// move
				Move(randomDir);
				
				// update timer
				moveTimer = current + moveCooldownBase + Util.GetRandomInteger(0, 500);
			}
			
			UpdatePosition();
			
		} else {
			OnDeath(World.instance.GetTileAtPosition(this.GetTilePosition()));
		}
	}
	
	public void Move(Direction dir) {
		
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), dir);
		World world = World.instance.GetWorld();
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null) {
			
			// tile is our new tile
			world.GetTileAtPosition(this.GetTilePosition()).SetActor(null);
			
			int x = tile.GetTilePosition().getX();
			int y = tile.GetTilePosition().getY();
			
			// update our tile position
			this.SetTilePosition(x, y);
			
			// update our world position
			targetx = tile.GetWorldPosition().getX();
			targety = tile.GetWorldPosition().getY();
			
			// set the tile's actor to be this.
			tile.SetActor(this);
			
			// hide ourselves if we are on a hidden tile.
			if(tile.isHidden()) {
				this.Hide();
			}
			
			// set off trap
			if(tile instanceof Trap) {
				((Trap)tile).Activate();
			}
			
			
		} else if(tile.GetTileType() == TileType.Door) {
			
			// open door
			// TODO: if the enemy can open door
			// tile.ActivateTile();
			
		} else if(tile.GetActor() != null) {
			
			if(tile.GetActor() instanceof Player)
				Attack(tile);
			
		}
	}

	public EnemyType getEnemyType() {
		return enemyType;
	}

	public void setEnemyType(EnemyType enemyType) {
		this.enemyType = enemyType;
	}
	
}
