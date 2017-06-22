package com.adventurer.gameobjects;

import com.adventurer.main.Coordinate;
import com.adventurer.main.EffectCreator;
import com.adventurer.main.ItemType;
import com.adventurer.main.SpriteCreator;
import com.adventurer.main.SpriteType;
import com.adventurer.main.Util;
import com.adventurer.main.VanityItemCreator;

public class Chest extends Item {

	private boolean locked = false;
	
	public Chest(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, ItemType itemType, boolean locked) {
		super(worldPos, tilePos, spritetype, itemType);
		
		this.locked = locked;
	}
	
	public void Open() {
		// TODO: effects + gold etc.
		
		EffectCreator.CreateGibs(this.GetTilePosition(), Util.GetRandomInteger(3, 7), SpriteType.GoldCoin01);
		VanityItemCreator.CreateVanityItem(this.GetTilePosition(), SpriteType.OpenChest01, true);
		
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

}
