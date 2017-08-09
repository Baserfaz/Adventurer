package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.data.World;
import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.ItemNames;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.Game;
import com.adventurer.main.ItemCreator;
import com.adventurer.main.SpriteCreator;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Chest extends Item {

	private boolean locked = false;
	
	private List<Item> itemsInside = new ArrayList<Item>();
	
	public Chest(Tile tile, SpriteType spritetype, boolean locked) {
		super(tile, spritetype, "Chest", "Holds valuable goodies.", 0);
		this.locked = locked;
		
		// put item(s) inside the chest.
		// TODO: randomize loot.
		
		itemsInside.add(ItemCreator.createGold(tile, Util.GetRandomInteger(1, 5)));
		
		Armor armor = ItemCreator.createArmor(tile, ItemNames.Leather, ArmorSlot.Chest);
		armor.getBonuses().setStrBonus(1);
		itemsInside.add(armor);
		
		Armor armor2 = ItemCreator.createArmor(tile, ItemNames.Leather,  ArmorSlot.Feet);
		armor2.getBonuses().setDexBonus(1);
		itemsInside.add(armor2);
		
		Armor armor3 = ItemCreator.createArmor(tile, ItemNames.Leather,  ArmorSlot.Hands);
		armor3.getBonuses().setIntBonus(1);
		itemsInside.add(armor3);
		
		Armor armor4 = ItemCreator.createArmor(tile, ItemNames.Leather,  ArmorSlot.Head);
		armor4.getBonuses().setDexBonus(1);
		itemsInside.add(armor4);
		
		Armor armor5 = ItemCreator.createArmor(tile, ItemNames.Leather,  ArmorSlot.Legs);
		armor5.getBonuses().setDexBonus(1);
		itemsInside.add(armor5);
		
		Weapon ba = ItemCreator.createWeapon(tile, ItemNames.Battleaxe);
		ba.getBonuses().setStrBonus(1);
		itemsInside.add(ba);
		
		Weapon oh = ItemCreator.createWeapon(tile, ItemNames.LightWoodenShield);
		oh.getBonuses().setVitBonus(2);
		itemsInside.add(oh);
		
		// register to tile
		tile.AddItem(this);
	}
	
	public void render(Graphics g) {
		if(hidden == false) {
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
		} else if(discovered == true && hidden == true) {
			Renderer.RenderSprite(Util.tint(sprite, true), this.GetWorldPosition(), g);
		}
	}
	
	public void Open() {
		
		// Add score to the current session.
		if(Game.instance.getCurrentSession() != null) {
			Game.instance.getCurrentSession().addScore(5);
		}
		
		// get tile
		Tile tile = World.instance.GetTileAtPosition(this.GetTilePosition());
		
		// dump all items onto ground
		for(Item t : this.itemsInside) { t.moveItemTo(tile); }
		
		// empty the list of items.
		this.itemsInside.clear();
		
		// remove chest
		this.Remove();	
	}
	
	public void Unlock() {
		this.locked = false;
		this.SetSprite(SpriteCreator.instance.CreateSprite(SpriteType.Chest02));
	}
	
	public void Lock() {
		this.locked = true;
		this.SetSprite(SpriteCreator.instance.CreateSprite(SpriteType.LockedChest01));
	}
	
	public void tick() {}
	public boolean isLocked() { return this.locked; }
}
