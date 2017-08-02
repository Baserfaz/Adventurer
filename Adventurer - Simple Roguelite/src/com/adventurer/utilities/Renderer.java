package com.adventurer.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adventurer.data.Camera;
import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.GameState;
import com.adventurer.enumerations.WorldType;
import com.adventurer.gameobjects.Player;
import com.adventurer.main.ActorManager;
import com.adventurer.main.Game;
import com.adventurer.main.Handler;

public class Renderer {

	public static void renderMainMenu(Graphics g) {
		
	    Graphics2D g2d = (Graphics2D) g;
	    
	    // get background image..
	    if(Game.instance.getBackgroundImage() == null) {
	    	try { Game.instance.setBackgroundImage(ImageIO.read(Renderer.class.getClass().getResourceAsStream("/" + Game.BACKGROUNDNAME))); }
			catch (IOException e) { e.printStackTrace(); }
	    }
	    
	    // render background image
		Renderer.FillScreenWithImage(g, Game.instance.getBackgroundImage());
	    
		// the position of all GUI elements
		// --> cleaner look.
		int xPos = Game.WIDTH / 5;
		
        // title
        Renderer.renderString(
        		"Adventurer - Roguelike",
                new Coordinate(xPos, 100), Color.white, 36, g2d);
        
        // creator info
        Renderer.renderString(
        		"by Heikki Heiskanen",
                new Coordinate(xPos, 150), Color.white, 21, g2d);
        
        // draw play button
        Renderer.renderButton("Play", new Coordinate(xPos, 250), new Coordinate(200, 50), Color.black, Color.white, 21, true, g2d);
        
        // draw exit button
        Renderer.renderButton("Exit", new Coordinate(xPos, 350), new Coordinate(200, 50), Color.black, Color.white, 21, true, g2d);
	}
	
	public static void renderLoading(Graphics g) {
	    
		// get game state
		GameState gameState = Game.instance.getGameState();
		
	    Graphics2D g2d = (Graphics2D) g;
        
        // set background
        Renderer.FillScreen(g, Color.black);
	    
        // title
        Renderer.renderString("Adventurer - Roguelike",
                new Coordinate(Game.WIDTH / 3, 100), Color.white, 36, g2d);
        
        // creator info
        Renderer.renderString("by Heikki Heiskanen",
                new Coordinate(Game.WIDTH / 3, 150), Color.gray, 21, g2d);
        
        // information about the dungeon
        if(World.instance.getWorldType() == WorldType.Random) {
            
            // dungeon generation states
            Renderer.renderString(">> Generating " + DungeonGeneration.state, 
                    new Coordinate(Game.WIDTH / 3, 300), Color.white, 32, g2d);
            
            // dungeon settings
            Renderer.renderString("Dungeon size: " + Game.WORLDWIDTH + "x" + Game.WORLDHEIGHT +
                    "\nMax room count: " + Game.ROOM_COUNT +
                    "\nMax doors per room: " + Game.ROOM_DOOR_MAX_COUNT,
                    new Coordinate(Game.WIDTH / 3, 350), Color.white, 18, g2d);
            
        } else if(World.instance.getWorldType() == WorldType.Predefined) {
            
            if(gameState == GameState.Loading) {
                Renderer.renderString("Creating world...", new Coordinate(Game.WIDTH / 3, 300), Color.white, 32, g2d);
            } else if(gameState == GameState.Ready) {
                // TODO: the name of the world/predefined should not be hard coded.
                Renderer.renderString("World created. \n>>Lobby", new Coordinate(Game.WIDTH / 3, 300), Color.white, 32, g2d);
            }
            
        }
        
        // print finished 
        if(gameState == GameState.Ready) {
            Renderer.renderString("Press any key to continue...", 
                    new Coordinate(Game.WIDTH / 3, 500), Color.white, 18, g2d);
        }
	}
	
	public static void renderInGame(Graphics g) {
	    
	    Graphics2D g2d = (Graphics2D) g;
	    
        // set background
        Renderer.FillScreen(g, Color.black);
        
        // zoom
        g2d.scale(Game.CAMERAZOOM, Game.CAMERAZOOM);
        
        // camera follow
        Player player = ActorManager.GetPlayerInstance();
        if(player != null) {
    
            Coordinate pos = Util.calculateCameraPos(player);
            int x = pos.getX(); 
            int y = pos.getY();
            
            // translate
            g.translate(x, y);
            
            // update camera's position
            Camera.instance.Update(new Coordinate(-x, -y), 1273 / Game.CAMERAZOOM, 690 / Game.CAMERAZOOM);
        
            if(Game.DRAW_CAMERA) {
                g.setColor(Color.red);
                g.drawRect((int) Camera.instance.getCameraBounds().getX(), 
                        (int) Camera.instance.getCameraBounds().getY(),
                        (int) Camera.instance.getCameraBounds().getWidth(),
                        (int) Camera.instance.getCameraBounds().getHeight());
            }
            
        } else Renderer.FillScreen(g, Color.black);
        
        // render objects and GUI
        Handler.instance.render(g); 
	}
	
	
	// https://stackoverflow.com/questions/11367324/how-do-i-scale-a-bufferedimage
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
