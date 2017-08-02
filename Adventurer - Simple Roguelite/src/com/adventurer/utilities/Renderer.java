package com.adventurer.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.Direction;
import com.adventurer.main.Game;

public class Renderer {

	// https://stackoverflow.com/questions/11367324/how-do-i-scale-a-bufferedimage
	@SuppressWarnings("unused")
	private static BufferedImage getScaledImage(BufferedImage sprite, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.drawImage(sprite, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}
	
	public static void FillScreen(Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
	}
	
	public static void FillScreenWithImage(Graphics g, Image img) {
		g.drawImage(img, 0, 0, Game.WIDTH, Game.HEIGHT, null);
	}
	
	public static void RenderSpriteWithBorder(BufferedImage sprite, Coordinate pos, Graphics g, Color borderColor) {
		BufferedImage img = Util.highlightTileBorders(sprite, borderColor);
		g.drawImage(img, pos.getX(), pos.getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
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
	
	public static void renderButton(String txt, Coordinate pos, Coordinate size, Color fontCol, Color rectColor, int fontSize, boolean fill, Graphics2D g2d) {
		
		// font
        Font font = new Font("Consolas", Font.PLAIN, fontSize);
        
        // font settings
        g2d.setFont(font);
        
        // positions
        int y = pos.getY();
        int x = pos.getX();
		int width = size.getX();
		int height = size.getY();
        
		// -------------------------- RENDER ------------------------
		
		// set color for the rectangle
		g2d.setColor(rectColor);
		
        // render rectangle and fill it
		g2d.drawRect(x, y, width, height);
        if(fill) { g2d.fillRect(x, y, width, height); }
        
        // calculate text position inside the button
        Coordinate txtpos = new Coordinate(x + width/2 - 25, y + height/2 - 20);
        
        // render string
        Renderer.renderString(txt, txtpos, fontCol, fontSize, g2d);
	}
	
	// https://stackoverflow.com/questions/4413132/problems-with-newline-in-graphics2d-drawstring
	public static void renderString(String txt, Coordinate pos, Color color, int fontSize, Graphics2D g2d) {
	    
        // font
        Font font = new Font("Consolas", Font.PLAIN, fontSize);
        
        // font settings
        g2d.setFont(font);
        g2d.setColor(color);
        
        int y = pos.getY();
        int x = pos.getX();
        
        // render
        for (String line : txt.split("\n")) {
            y += g2d.getFontMetrics().getHeight();
            g2d.drawString(line, x, y);
        }
	}
}
