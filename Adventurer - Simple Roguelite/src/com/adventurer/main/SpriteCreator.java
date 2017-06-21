package com.adventurer.main;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteCreator {

	private String path;
	private int height;
	private int width;
	private int[] pixels;
	
	public static SpriteCreator instance;
	
	public SpriteCreator(String path) {
		
		if(instance != null) return;
		
		instance = this;
		
		BufferedImage image = null;
		
		// get the sprite sheet
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/" + path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// set vars
		this.path = path;
		this.height = image.getHeight();
		this.width = image.getWidth();
		
		// load the color data
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}
	
	// https://stackoverflow.com/questions/9558981/flip-image-with-graphics2d
	public BufferedImage FlipSpriteVertically(BufferedImage img) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -img.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage image = op.filter(img, null);
		
		return image;
	}
	
	// https://stackoverflow.com/questions/9558981/flip-image-with-graphics2d
	public BufferedImage FlipSpriteHorizontally(BufferedImage img) {
		
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-img.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage retimage = op.filter(img, null);
		
		return retimage;
	}
	
	public BufferedImage CreateSprite(SpriteType type) {
		
		int row = 0, column = 0;
		BufferedImage sprite = new BufferedImage(Game.SPRITESIZE, Game.SPRITESIZE, BufferedImage.TYPE_INT_ARGB);
		int[] spritePixelData = new int[sprite.getWidth() * sprite.getHeight()];
		
		// get tile position
		switch(type) {
		case Player:
			row = 0;
			column = 0;
			break;
		case FloorTile01:
			row = 0;
			column = 1;
			break;
		case Wall01:
			row = 0;
			column = 2;
			break;
		case Door01:
			row = 0;
			column = 3;
			break;
		case DestructibleWall:
			row = 0;
			column = 4;
			break;
		case Zombie01:
			row = 1;
			column = 0;
			break;
		case Blood01:
			row = 1;
			column = 1;
			break;
		case Hit01:
			row = 1;
			column = 2;
			break;
		case Arrow01:
			row = 1;
			column = 3;
			break;
		case Spear01:
			row = 4;
			column = 1;
			break;
		case Key01:
			row = 1;
			column = 5;
			break;
		case LockedDoor01:
			row = 0;
			column = 6;
			break;
		case Maggot01:
			row = 2;
			column = 0;
			break;
		case Pot01:
			row = 1;
			column = 4;
			break;
		case Skeleton01:
			row = 2;
			column = 1;
			break;
		case SkeletonRemains01:
			row = 2;
			column = 2;
			break;
		case TrapTile01:
			row = 0;
			column = 5;
			break;
		case PotRemains01:
			row = 2;
			column = 3;
			break;
		case UnknownActor:
			row = 2;
			column = 4;
			break;
		case Bomb01:
			row = 2;
			column = 5;
			break;
		case Smoke01:
			row = 2;
			column = 6;
			break;
		case SmallBlood01:
			row = 3;
			column = 0;
			break;
		case Torch01:
			row = 0;
			column = 7;
			break;
		case SmallSkeletonRemains01:
			row = 3;
			column = 1;
			break;
		case BloodGib01:
			row = 3;
			column = 2;
			break;
		case BoneGib01:
			row = 3;
			column = 3;
			break;
		case PotGib01:
			row = 3;
			column = 4;
			break;
		case LockedDoor01Gib01:
			row = 3;
			column = 5;
			break;
		case Wall01Gib01:
			row = 3;
			column = 6;
			break;
		case PlayerGib01:
			row = 3;
			column = 7;
			break;
		case PlayerRemains01:
			row = 4;
			column = 0;
			break;
		case GasCloud01:
			row = 2;
			column = 7;
			break;
		case ArrowTurretNorth:
			row = 1;
			column = 10;
			break;
		case ArrowTurretWest:
			row = 1;
			column = 9;
			break;
		case ArrowTurretSouth:
			row = 1;
			column = 11;
			break;
		case DirectionArrow:
			row = 4;
			column = 2;
			break;
		case Portal01:
			row = 0;
			column = 10;
			break;
		case GoldCoin01:
			row = 2;
			column = 8;
			break;
		case Error:
			row = 4;
			column = 3;
			break;
		case Chest01:
			row = 1;
			column = 6;
			break;
		case LockedChest01:
			row = 1;
			column = 8;
			break;
		case GasBarrel01:
			row = 1;
			column = 7;
			break;
		case GasBarrelGib01:
			row = 3;
			column = 8;
			break;
		case Egg01:
			row = 4;
			column = 4;
			break;
		case BombGib01:
			row = 3;
			column = 9;
			break;
		default:
			System.out.println("SPRITETYPE NOT FOUND: " +  type);
			break;
		}
		
		// the tiles are 16x16
		// calculate tile's pixel locations.
		int startX = column * 16;
		int endX = startX + 16;
		int startY = row * 16;
		int endY = startY + 16;
		
		int currentPixel = 0;
		
		// get the pixel array
		for(int y = startY; y < endY; y++) {
			for (int x = startX; x < endX; x++) {
				
				// get the pixel data from the sprite sheet.
				spritePixelData[currentPixel] = pixels[y * width + x];
			
				currentPixel ++;
			}
		}
		
		// set pixels
		for (int y = 0; y < sprite.getHeight(); y++) {
		    for (int x = 0; x < sprite.getWidth(); x++) {
		    	sprite.setRGB(x, y, spritePixelData[y * sprite.getWidth() + x]);
		    }
		}
		
		return sprite;
	}
	
	public String GetPath() {
		return this.path;
	}
	
	public int[] GetPixelArray() {
		return this.pixels;
	}
	
	public int GetWidth() {
		return this.width;
	}
	
	public int GetHeight() {
		return this.height;	
	}
}
