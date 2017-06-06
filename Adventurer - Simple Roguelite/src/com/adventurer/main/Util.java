package com.adventurer.main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.concurrent.ThreadLocalRandom;

public class Util {
	
	public static Direction GetRandomCardinalDirection() {
		return Direction.values()[Util.GetRandomInteger(0, 4)];
	}
	
	public static int GetRandomInteger() {
		return ThreadLocalRandom.current().nextInt(0, 101);
	}
	
	public static int GetRandomInteger(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	// http://stackoverflow.com/questions/16656651/does-java-have-a-clamp-function
	public static int clamp(int val, int min, int max) {
	    return Math.max(min, Math.min(max, val));
	}
	
	// http://www.java-gaming.org/index.php?topic=34706.0
	public static float lerp(float point1, float point2, float alpha) {
	    return point1 + alpha * (point2 - point1);
	}
	
	// http://www.java-gaming.org/index.php?topic=34706.0
	public static int lerp(int point1, int point2, float alpha) {
	    return Math.round(point1 + alpha * (point2 - point1));
	}
	
	// http://stackoverflow.com/questions/4248104/applying-a-tint-to-an-image-in-java
	public static BufferedImage tint(BufferedImage image, boolean darker) {
		
		// copy the image 
		BufferedImage tintedImage = Util.deepCopy(image);
		
		// loop through all pixels
		for(int x = 0; x < tintedImage.getWidth(); x++) {
			for (int y = 0; y < tintedImage.getHeight(); y++) {
				
				// second parameter is if there is alpha channel.
				Color color = new Color(tintedImage.getRGB(x, y), true);
				
				Color tintedColor = null;
				
				// make the pixel's color darker.
				if(darker) tintedColor = color.darker().darker();
				else tintedColor = color.brighter();
					
				// apply color to new image.
				tintedImage.setRGB(x, y, tintedColor.getRGB());
			}
		}
		return tintedImage;
	}
	
	// http://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
}
