package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.data.ItemBonus;
import com.adventurer.data.World;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.Handler;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Item extends GameObject {

	protected String itemName;
	protected int value;
	
	protected ItemBonus bonus;
	
	public Item(Tile tile, SpriteType spritetype, String name, int value) {
		super(tile.GetWorldPosition(), tile.GetTilePosition(), spritetype);
		this.itemName = name;
		this.value = value;
		
		this.bonus = new ItemBonus();
	}
	
	public void Remove() {
		
		// get tile
		Tile tile = World.instance.GetTileAtPosition(this.GetTilePosition());
		
		tile.RemoveItem(this);
		
		// remove this object from handler
		// -> no longer ticks
		Handler.instance.RemoveObject(this);
		
		// hide 
		Hide();
	}

	public String getName() { return this.itemName; }
	
	public void moveItemTo(Tile tile) {
		this.SetTilePosition(tile.GetTilePosition()); 
		this.SetWorldPosition(tile.GetWorldPosition());
		tile.AddItem(this);
	}
	
	public void render(Graphics g) {
		if(hidden == false) {
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
		} else if(discovered == true && hidden == true) {
			Renderer.RenderSprite(Util.tint(sprite, true), this.GetWorldPosition(), g);
		}
	}
	
	public int getValue() { return this.value; }
	
	public void tick() {}
	public Rectangle GetBounds() { return null; }
}
