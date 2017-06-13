package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import com.adventurer.main.*;

public class Enemy extends Actor {

	private int moveCooldownBase = 2000;
	private long moveTimer = 0;
	
	private EnemyType enemyType;
	
	private boolean hasRangedAttack = false;
	private SpriteType projectileType;
	
	public Enemy(Coordinate worldPos, Coordinate tilePos, EnemyType enemytype, SpriteType spritetype, int maxHP, int damage) {
		super(worldPos, tilePos, spritetype, maxHP, damage);
		
		// declare ranged units here
		if(enemytype == EnemyType.Skeleton) {
			this.hasRangedAttack = true;
			this.projectileType = SpriteType.Spear01;
		}
		
		this.setEnemyType(enemytype);
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
				
			} else {
				
				// TODO: North & South sprites
				
				Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
				
			}
			
			renderDirectionArrow(g);
			
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
				
				// ***** Simple AI *****
				// check if player is nearby -> attack
				// else just move randomly.
				
				boolean canSeePlayer = false;
				Tile playerTile = null;
				
				List<Tile> surroundingTiles = World.instance.GetSurroundingTiles(this.GetTilePosition());
				
				for(Tile tile : surroundingTiles) {
					if(tile.GetActor() != null) {
						if(tile.GetActor() instanceof Player) {
							canSeePlayer = true;
							playerTile = tile;
							break;
						}
					}
				}
				
				if(canSeePlayer) {
					
					// --------------- ATTACK ----------------
					
					// TODO: gets stuck because of how World.GetSurroundingTiles works.
					//  	 -> doesn't check walls + always gives east/west first.
					Direction dir = World.instance.GetDirectionOfTileFromPoint(this.currentTile, playerTile);
					Move(dir);
					
				} else {
					// -------------- RANDOM -----------------
					
					// randomize direction
					Direction randomDir = Util.GetRandomCardinalDirection();
					
					// randomly shoot projectile
					if(Util.GetRandomInteger() > 80 && hasRangedAttack) {
						
						Shoot(this.GetTilePosition(), randomDir, projectileType);
						
					} else {
						
						// move
						Move(randomDir);
						
					}
					
				}
				
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
		
		// update facing
		/*if(dir == Direction.East || dir == Direction.West)*/ lookDir = dir;
		
		if((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null && tile.GetItem() == null) {
			
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

			// save our position
			this.setTile(tile);
			
			// hide ourselves if we are on a hidden tile.
			if(tile.isHidden()) {
				this.Hide();
			}
			
			// set off trap
			if(tile instanceof Trap) {
				((Trap)tile).Activate();
			}
			
		} else if(tile instanceof Door) {
			
			// open door
			((Door)tile).Open();
			
		} else if(tile.GetActor() != null) {
			
			if(tile.GetActor() instanceof Player) Attack(tile);
			
		}
	}

	public EnemyType getEnemyType() {
		return enemyType;
	}

	public void setEnemyType(EnemyType enemyType) {
		this.enemyType = enemyType;
	}
	
}
