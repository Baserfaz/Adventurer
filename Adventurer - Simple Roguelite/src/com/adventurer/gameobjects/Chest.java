package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.enumerations.ItemType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.ActorManager;
import com.adventurer.main.Coordinate;
import com.adventurer.main.EffectCreator;
import com.adventurer.main.Renderer;
import com.adventurer.main.SpriteCreator;
import com.adventurer.main.Util;
import com.adventurer.main.VanityItemCreator;

public class Chest extends Item {

	private boolean locked = false;
	
	public Chest(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, ItemType itemType, boolean locked) {
		super(worldPos, tilePos, spritetype, itemType);
		
		this.locked = locked;
	}
	
	public void render(Graphics g) {
		
		// TODO: This should perhaps be the Item classes' render version.
		
		if(hidden == false) {
			
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
			
		} else if(discovered == true && hidden == true) {
			
			if(tintedSprite == null) {
				tintedSprite = Util.tint(sprite, true);
			}
			
			Renderer.RenderSprite(tintedSprite, this.GetWorldPosition(), g);
			
		}
	}
	
	public void tick() {}
	
	public void Open() {
		// TODO: effects + gold etc.
		
		EffectCreator.CreateGibs(this.GetTilePosition(), Util.GetRandomInteger(3, 7), SpriteType.GoldCoin01);
		VanityItemCreator.CreateVanityItem(this.GetTilePosition(), SpriteType.OpenChest01, false);
		
		int amount = Util.GetRandomInteger(0, 5);
		
		ActorManager.GetPlayerInstance().getSession().addScore(amount);
		
		this.Remove();
		
	}
	
	public void Unlock() {
		this.locked = false;
		
		this.SetSprite(SpriteCreator.instance.CreateSprite(SpriteType.Chest01));
		this.SetTintedSprite(null);
		
		EffectCreator.CreateGibs(this.GetTilePosition(), Util.GetRandomInteger(3, 7), SpriteType.LockedDoor01Gib01);
	}
	
	public void Lock() {
		this.locked = true;
		
		this.SetSprite(SpriteCreator.instance.CreateSprite(SpriteType.LockedChest01));
		this.SetTintedSprite(null);
	}

	public boolean isLocked() {
		return this.locked;
	}
	
}
