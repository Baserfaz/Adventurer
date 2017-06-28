package com.adventurer.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.adventurer.data.Camera;
import com.adventurer.data.World;
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
