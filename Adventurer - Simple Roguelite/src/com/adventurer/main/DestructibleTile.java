package com.adventurer.main;

import java.awt.Rectangle;

public class DestructibleTile extends Tile {

	private Health tileHealth = null;
	
	public DestructibleTile(Coordinate worldPos, SpriteType spritetype, TileType type, Coordinate tilePos, int health) {
		super(worldPos, spritetype, type, tilePos);
		
		this.tileHealth = new Health(health, this);
	}

	public void tick() {
		
		// sometimes the health is null,
		// for some reason (?).
		if(this.tileHealth == null) return;
		
		// check health
		if(this.tileHealth.isDead() == true) {
			World.instance.ReplaceTile(this, TileType.Floor, SpriteType.FloorTile01);
		} else {
			
			// check if the tile is in the camera's view
			Rectangle camera = Game.instance.camera;
			
			if(camera != null) {
				if(camera.contains(worldPosition.getX() + World.tileSize / 2, worldPosition.getY() + World.tileSize / 2)) {
					inView = true;
					Show();
				} else {
					Hide();
					inView = false;
					return;
				}
			}
			
			// move the tile
			if(worldPosition.getY() < targety + fallingSpeed) {
				if(worldPosition.getY() < targety) worldPosition.addY(fallingSpeed);
			}
		}
	}
	
	public Health getTileHealth() {
		return tileHealth;
	}

	public void setTileHealth(Health tileHealth) {
		this.tileHealth = tileHealth;
	}

}
