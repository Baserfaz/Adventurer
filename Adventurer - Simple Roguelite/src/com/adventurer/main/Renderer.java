package com.adventurer.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Renderer {

	// render without rotation
	public static void RenderSprite(BufferedImage sprite, Coordinate pos, Graphics g) {
		g.drawImage(sprite, pos.getX(), pos.getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
	}
	
	// render with rotation
	public static void RenderSprite(BufferedImage sprite, Coordinate pos, Direction dir, Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		int x = pos.getX();
		int y = pos.getY();
		int halfOfSpriteSize = Game.SPRITESIZE / 2;
		int xcenter = x + halfOfSpriteSize;
		int ycenter = y + halfOfSpriteSize;
		double angle = 0.0;
		
		if(dir == Direction.East) angle = 180.0;
		else if(dir == Direction.West) angle = 0.0;
		else if(dir == Direction.South) angle = 270.0;
		else if(dir == Direction.North) angle = 90.0;
		
		double rot = Math.toRadians(angle);
		
		g2d.rotate(rot, xcenter, ycenter);
		g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
		g2d.rotate(-rot, xcenter, ycenter);
	}
	
}
