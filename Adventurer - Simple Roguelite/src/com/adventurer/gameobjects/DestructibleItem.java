package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.data.Coordinate;
import com.adventurer.data.Health;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class DestructibleItem extends Item {

	private Health health;
	private boolean alive = true;
	
	public DestructibleItem(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int health) {
		super(worldPos, tilePos, spritetype, "Destructible Item");
		this.setHealth(new Health(health));
	}

	public void tick() {
		
		if(health == null) return;
		
		if(health.isDead()) {
			alive = false;
			this.Remove();
		}
	}
	
	public void render(Graphics g) {
		if(alive && hidden == false && discovered) {
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
		} else if(alive && discovered && hidden) {
			Renderer.RenderSprite(Util.tint(sprite, true), this.GetWorldPosition(), g);	
		}
	}
	
	public Health getHealth() { return health; }
	private void setHealth(Health health) { this.health = health; }
}
