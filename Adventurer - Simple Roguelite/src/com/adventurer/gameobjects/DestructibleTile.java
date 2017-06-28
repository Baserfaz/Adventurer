package com.adventurer.gameobjects;

import java.awt.Rectangle;

import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.*;

public class DestructibleTile extends Tile {

	private Health tileHealth = null;
	
	public DestructibleTile(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type, int health) {
		super(worldPos, tilePos, spritetype, type);
		
		this.tileHealth = new Health(health);
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
			
			int x = this.GetWorldPosition().getX();
			int y = this.GetWorldPosition().getY();
			
			if(camera != null) {
				if(camera.contains(this.GetWorldPosition().getX() + Game.SPRITESIZE / 2, this.GetWorldPosition().getY() + Game.SPRITESIZE / 2)) {
					inView = true;
					Show();
				} else {
					Hide();
					inView = false;
					return;
				}
			}
			
			UpdatePosition(x, y);
		}
	}
	
	public Health getTileHealth() {
		return tileHealth;
	}

	public void setTileHealth(Health tileHealth) {
		this.tileHealth = tileHealth;
	}

}
