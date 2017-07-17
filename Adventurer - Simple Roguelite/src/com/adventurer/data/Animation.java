package com.adventurer.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// TODO: add this implementation.

public class Animation {

	private List<BufferedImage> sprites = new ArrayList<BufferedImage>();
	private int current = 0;
	private BufferedImage currentSprite = null;
	
	public Animation(BufferedImage[] imgs) { 
		for(BufferedImage b : imgs) this.sprites.add(b); 
		this.currentSprite = sprites.get(current);
	}
	
	// this is called on every tick.
	public void update() {
		currentSprite = sprites.get(current);
		current += 1;
		if(current % sprites.size() == 0) current = 0;
	}
	
	public BufferedImage getCurrentSprite() { return this.currentSprite; }
	public BufferedImage[] getSprites() { return this.sprites.toArray(new BufferedImage[sprites.size()]); }
}
