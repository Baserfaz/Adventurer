package com.adventurer.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.adventurer.gameobjects.Tile;

public class MouseInput implements MouseMotionListener, MouseListener {

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}
	
	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			
			int mouseX = e.getX();
			int mouseY = e.getY();
			
			Rectangle camera = Camera.instance.getCameraBounds();
			
			// calculates tile position (doesnt work 100% correctly)
			//int x = (int) Math.floor((mouseX + camera.x) /  (double) (Game.SPRITESIZE * Game.CAMERAZOOM));
			//int y = (int) Math.floor((mouseY + camera.y) / (double) (Game.SPRITESIZE * Game.CAMERAZOOM));
			
			// works fine with camerazoom = 1
			//int x = mouseX + camera.x;
			//int y = mouseY + camera.y;
			
			// works fine with all camerazoom!
			int x = (mouseX / Game.CAMERAZOOM + camera.x);
			int y = (mouseY / Game.CAMERAZOOM + camera.y);
			
			for(Tile tile : World.instance.GetTiles()) {
				if(tile.isInView()) {
					
					if(Game.CALCULATE_PLAYER_LOS == false || tile.isDiscovered()) {
						if(tile.GetBounds().contains(new Point(x, y))) {
							tile.toggleSelect();
							System.out.println(tile.GetInfo());
						}
					}
				}
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
}
