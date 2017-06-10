package com.adventurer.main;

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
			
			int x = (int) Math.floor((mouseX + camera.getX()) / 400); //(Game.SPRITESIZE * Game.CAMERAZOOM));
			int y = (int) Math.floor((mouseY + camera.getY()) / 400);//(Game.SPRITESIZE * Game.CAMERAZOOM));
			
			Tile tile = World.instance.GetTileAtPosition(x, y);
			
			System.out.println("clicked: " + x + ", " + y + ", camera pos: " + camera.getX() + ", " + camera.getY());
			
			if(tile != null) {
				tile.toggleSelect();
				System.out.println(tile.GetInfo());
			}
			
		}
		
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
}
