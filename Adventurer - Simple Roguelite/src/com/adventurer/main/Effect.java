package com.adventurer.main;

import java.awt.Graphics;
import java.awt.Rectangle;

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
			
			if(allowMovement) {
				if(Util.GetRandomInteger() > 90)
					this.GetWorldPosition().decreaseY(1);
			}
		}
	}

	@Override
	public void Remove() {
		
		// remove this object from handler
		// -> no longer ticks
		Game.instance.GetHandler().RemoveObject(this);
		
		// hide 
		Hide();
	}
	
	public void render(Graphics g) {
		if(isAlive)
			g.drawImage(sprite, worldPosition.getX(), worldPosition.getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
	}

	public Rectangle GetBounds() {
		return null;
	}
}
