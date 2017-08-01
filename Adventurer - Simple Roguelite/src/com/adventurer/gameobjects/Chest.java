package com.adventurer.gameobjects;

import java.awt.Graphics;
import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.Game;
import com.adventurer.main.SpriteCreator;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Chest extends Item {

	private boolean locked = false;
	
	public Chest(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, boolean locked) {
		super(worldPos, tilePos, spritetype);
		this.locked = locked;
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
		Game.instance.getCurrentSession().addScore(Util.GetRandomInteger(0, 5));
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
