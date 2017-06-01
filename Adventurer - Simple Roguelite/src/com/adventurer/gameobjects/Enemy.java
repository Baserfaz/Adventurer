package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.adventurer.main.*;

public class Enemy extends Actor {

	private int moveCooldownBase = 2000;
	private long moveTimer = 0;
	
	private EnemyType enemyType;
	
	private BufferedImage unknownActorSprite = null;
	
	private boolean blinking = false;
	private long spriteBlinkTimer = 0;
	private int spriteBlinkCooldown = 1000;
	
	private int lastXpos = 0;
	private int lastYpos = 0;
	
	public Enemy(Coordinate worldPos, Coordinate tilePos, EnemyType type, SpriteType spritetype, int maxHP, int damage) {
		super(worldPos, tilePos, spritetype, maxHP, damage);
		
		this.setEnemyType(type);
		this.moveTimer = System.currentTimeMillis();
		
		this.unknownActorSprite = SpriteCreator.instance.CreateSprite(SpriteType.UnknownActor);
	}

	public void render(Graphics g) {
		
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
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
			
			UpdatePosition();
			
		} else {
			OnDeath(World.instance.GetTileAtPosition(this.GetTilePosition()));
		}
	}
	
	public int[] GetLastSeenPosition() {
		return new int[] { lastXpos, lastYpos };
	}
	
	public void Move(Direction dir) {
		
		Tile tile = World.instance.GetTileFromDirection(this.GetTilePosition(), dir);
		World world = World.instance.GetWorld();
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null) {
			
			// tile is our new tile
			world.GetTileAtPosition(this.GetTilePosition()).SetActor(null);
			
			// update our tile position
			this.GetTilePosition().setX(tile.GetTilePosition().getX());
			this.GetTilePosition().setY(tile.GetTilePosition().getY());
			
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
