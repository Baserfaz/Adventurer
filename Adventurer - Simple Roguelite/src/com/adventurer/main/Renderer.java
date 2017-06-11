package com.adventurer.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Renderer {

	// https://stackoverflow.com/questions/11367324/how-do-i-scale-a-bufferedimage
	private static BufferedImage getScaledImage(BufferedImage sprite, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.drawImage(sprite, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}
	
	public static void RenderSpriteWithTint(BufferedImage sprite, Coordinate pos, Graphics g, Color tint) {
		BufferedImage img = Util.tintWithColor(sprite, tint);
		g.drawImage(img, pos.getX(), pos.getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
	}
	
	// render without rotation
	public static void RenderSprite(BufferedImage sprite, Coordinate pos, Graphics g) {
		g.drawImage(sprite, pos.getX(), pos.getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
	}
	
	// render with 90 degree rotation 
	public static void RenderSprite(BufferedImage sprite, Coordinate pos, Direction dir, Graphics g) {
		
		double angle = 0.0;
		if(dir == Direction.East) angle = 180.0;
		else if(dir == Direction.West) angle = 0.0;
		else if(dir == Direction.South) angle = 270.0;
		else if(dir == Direction.North) angle = 90.0;
		
		RenderSpriteWithRotation(sprite, pos, angle, g);
	}
	
	// render with free angle
	public static void RenderSpriteWithRotation(BufferedImage sprite, Coordinate pos, double angle, Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		double rot = Math.toRadians(angle);
		int x = pos.getX();
		int y = pos.getY();
		int halfOfSpriteSize = Game.SPRITESIZE / 2;
		int xcenter = x + halfOfSpriteSize;
		int ycenter = y + halfOfSpriteSize;
		
		g2d.rotate(rot, xcenter, ycenter);
		g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
		g2d.rotate(-rot, xcenter, ycenter);
		
	}
}
