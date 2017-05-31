package com.adventurer.main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Enemy extends Actor {

	private int moveCooldownBase = 2000;
	private long moveTimer = 0;
	
	private boolean canMove = false;
	
	private EnemyType enemyType;
	
	private BufferedImage unknownActorSprite = null;
	
	private boolean blinking = false;
	private long spriteBlinkTimer = 0;
	private int spriteBlinkCooldown = 1000;
	
	private int lastXpos = 0;
	private int lastYpos = 0;
	
	public Enemy(Coordinate worldPos, Coordinate tilePos, int maxHP, EnemyType type, SpriteType spritetype, int damage) {
		super(worldPos, tilePos, spritetype, maxHP, damage);
		
		this.setEnemyType(type);
		this.moveTimer = System.currentTimeMillis();
		
		this.unknownActorSprite = SpriteCreator.instance.CreateSprite(SpriteType.UnknownActor);
	}

	public void render(Graphics g) {
		
		int x = worldPosition.getX();
		int y = worldPosition.getY();
		
		if(hidden == false) {
			
			if(lookDir == Direction.East) {
				
				if(flippedSpriteHor == null) {
					flippedSpriteHor = SpriteCreator.instance.FlipSpriteHorizontally(sprite);
				}
				
				g.drawImage(flippedSpriteHor, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			} else if(lookDir == Direction.West) {
				g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			}
			
			// update last seen positions
			lastXpos = x;
			lastYpos = y;
			
		} else if(hidden == true && discovered == true){
			
			/*if(System.currentTimeMillis() > spriteBlinkTimer) {
				if(blinking == false) blinking = true;
				else blinking = false;
				spriteBlinkTimer = System.currentTimeMillis() + spriteBlinkCooldown;
			}
			if(blinking) g.drawImage(unknownActorSprite, lastXpos, lastYpos, Game.SPRITESIZE, Game.SPRITESIZE, null);
			*/
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
			
		} else {
			OnDeath(World.instance.GetTileAtPosition(tilePosition));
		}
	}
	
	public int[] GetLastSeenPosition() {
		return new int[] { lastXpos, lastYpos };
	}
	
	public void Move(Direction dir) {
		
		Tile tile = null;
		World world = Game.instance.GetWorld();
		
		int tilex = this.tilePosition.getX();
		int tiley = this.tilePosition.getY();
		
		switch(dir) {
		case North:
			tile = world.GetTileAtPosition(tilex, tiley - 1);
			break;
		case South:
			tile = world.GetTileAtPosition(tilex, tiley + 1);
			break;
		case East:
			tile = world.GetTileAtPosition(tilex + 1, tiley);
			break;
		case West:
			tile = world.GetTileAtPosition(tilex - 1, tiley);
			break;
		default:
			break;
		}
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null) {
			
			// tile is our new tile
			world.GetTileAtPosition(tilePosition).SetActor(null);
			
			// update our tile position
			tilePosition.setX(tile.GetTilePosition().getX());
			tilePosition.setY(tile.GetTilePosition().getY());
			
			// update our world position
			targetx = tile.GetWorldPosition().getX();
			targety = tile.GetWorldPosition().getY();
			
			// set the tile's actor to be this.
			tile.SetActor(this);
			
			// hide ourselves if we are on a hidden tile.
			if(tile.isHidden()) {
				this.Hide();
			}
			
		} else if(tile.GetTileType() == TileType.Door) {
			
			// open door
			// TODO: if the enemy can open door
			// tile.ActivateTile();
			
		} else if(tile.GetActor() != null) {
			
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
