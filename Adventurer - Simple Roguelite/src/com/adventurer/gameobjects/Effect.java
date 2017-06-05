package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.main.Coordinate;
import com.adventurer.main.Game;
import com.adventurer.main.Handler;
import com.adventurer.main.SpriteType;
import com.adventurer.main.Util;

public class Effect extends GameObject {
	
	private long liveTimer = 0;
	private boolean isAlive = true;
	private boolean allowMovement = false;
	
	public Effect(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int timeToLiveInMs, boolean allowMovement) {
		super(worldPos, tilePos, spritetype);
		
		this.liveTimer = System.currentTimeMillis() + timeToLiveInMs;
		this.allowMovement = allowMovement;
	}

	public void tick() {
		if(System.currentTimeMillis() > liveTimer) {
			isAlive = false;
			Remove();
		} else {
			
			int x = this.GetWorldPosition().getX();
			int y = this.GetWorldPosition().getY();
			
			if(allowMovement) {
				if(Util.GetRandomInteger() > 90) this.SetWorldPosition(x, y - 1);
			}
		}
	}
	
	public void Remove() {
		
		// remove this object from handler
		// -> no longer ticks
		Handler.instance.RemoveObject(this);
		
		// hide 
		Hide();
	}
	
	public void render(Graphics g) {
		if(isAlive) RenderSprite(sprite, this.GetWorldPosition(), g);
	}

	public Rectangle GetBounds() {
		return null;
	}
}
