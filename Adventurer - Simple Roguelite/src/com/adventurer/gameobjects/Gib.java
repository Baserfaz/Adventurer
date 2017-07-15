package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.VanityItemCreator;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Gib extends Effect {
	
	private Direction dir;
	private int max_y;
	private int min_y;
	private boolean falling = false;
	private double angle = 0;
	private double angleValue = 6.0;
	
	public Gib(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int timeToLive) {
		super(worldPos, tilePos, spritetype, timeToLive);
		
		Direction[] dirs = new Direction[] { Direction.West, Direction.East, Direction.North };
		this.dir = dirs[Util.GetRandomInteger(0, dirs.length)];
		
		this.max_y = worldPos.getY() - Util.GetRandomInteger(5, 10);
		this.min_y = worldPos.getY() + Util.GetRandomInteger(5, 10);
		
		if(Util.GetRandomInteger() > 50) angleValue = -angleValue;
	}
	
	public void render(Graphics g) {
		if(isAlive) Renderer.RenderSpriteWithRotation(sprite, this.GetWorldPosition(), this.angle, g);
	}
	
	public void tick() {
		
		angle += angleValue;
		
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
		if(y < max_y) falling = true;
		
		if(falling == false) {
			
			int x_val = 1;
			if(Util.GetRandomInteger() > 50) x_val = 0;
			
			if(dir == Direction.East) this.SetWorldPosition(x + x_val, y - 1);
			else if(dir == Direction.West) this.SetWorldPosition(x - x_val, y - 1);
			else this.SetWorldPosition(x, y - 1);
			
		} else {
			
			int x_val = 1;
			if(Util.GetRandomInteger() > 50) x_val = 0;
			
			if(dir == Direction.East) this.SetWorldPosition(x + x_val, y + 1);
			else if(dir == Direction.West) this.SetWorldPosition(x - x_val, y + 1);
			else this.SetWorldPosition(x, y + 1);
			
			if(y > min_y) {
				
				Tile tile = World.instance.GetTileWithWorldPosition(this.GetWorldPosition());
				
				// TODO: check if the tile is wall, different sprite?
				
				// create vanity items
				if(tile != null) VanityItemCreator.CreateVanityItem(tile, spriteType, false);
				
				// destroy effect gib
				isAlive = false;
				Remove();
			}
		}
	}
}
