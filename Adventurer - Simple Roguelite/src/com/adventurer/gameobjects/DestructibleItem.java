package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.data.Coordinate;
import com.adventurer.data.Health;
import com.adventurer.enumerations.ItemType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.EffectCreator;
import com.adventurer.main.VanityItemCreator;
import com.adventurer.main.World;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class DestructibleItem extends Item {

	private Health health;
	private boolean alive = true;
	
	public DestructibleItem(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int health, ItemType itemType) {
		super(worldPos, tilePos, spritetype, itemType);
		
		this.setHealth(new Health(health));
	}

	public void tick() {
		
		if(health == null) return;
		
		if(health.isDead()) {
			alive = false;
			
			// TODO: create effects and vanity items here 
			// for all item types.
			
			switch(itemType) {
			case Pot:
				VanityItemCreator.CreateVanityItem(this.GetTilePosition(), SpriteType.PotRemains01, true);
				break;
			case Torch:
				VanityItemCreator.CreateVanityItem(this.GetTilePosition(), SpriteType.PotRemains01, true);
				break;
			default:
				VanityItemCreator.CreateVanityItem(this.GetTilePosition(), SpriteType.PotRemains01, true);
			}
			
			
			this.Remove();
		}
	}
	
	public void render(Graphics g) {
		if(alive && hidden == false && discovered) {
			
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
			
		} else if(alive && discovered && hidden) {
			
			if(tintedSprite == null) {
				tintedSprite = Util.tint(sprite, true);
			}
			
			Renderer.RenderSprite(tintedSprite, this.GetWorldPosition(), g);
			
		}
	}
	
	public Health getHealth() {
		return health;
	}

	private void setHealth(Health health) {
		this.health = health;
	}
	
}
