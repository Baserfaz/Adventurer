package com.adventurer.gameobjects;

import java.awt.Rectangle;

import com.adventurer.main.*;

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
			Rectangle camera = Camera.instance.getCameraBounds();
			
			if(camera != null) {
				if(camera.contains(this.GetWorldPosition().getX() + World.tileSize / 2, this.GetWorldPosition().getY() + World.tileSize / 2)) {
					inView = true;
					Show();
				} else {
					Hide();
					inView = false;
					return;
				}
			}
			
			// move the tile
			if(this.GetWorldPosition().getY() < targety + fallingSpeed) {
				if(this.GetWorldPosition().getY() < targety) this.GetWorldPosition().addY(fallingSpeed);
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
