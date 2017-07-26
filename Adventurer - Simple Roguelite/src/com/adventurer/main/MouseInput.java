package com.adventurer.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import com.adventurer.data.Camera;
import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.GameState;
import com.adventurer.gameobjects.Tile;

public class MouseInput implements MouseMotionListener, MouseListener {
	
	private Tile hoveringTile = null;
	
	private Coordinate calculateMousePosition(MouseEvent e) {
		
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		if(Camera.instance == null) return null;
			
		Rectangle camera = Camera.instance.getCameraBounds();
		
		// works fine with all camerazoom!
		int x = (mouseX / Game.CAMERAZOOM + camera.x);
		int y = (mouseY / Game.CAMERAZOOM + camera.y);
		
		return new Coordinate(x, y);
	}
	
	public void mousePressed(MouseEvent e) {
		
	    /*if(Game.instance.getGameState() == GameState.Loading) return;
	    
		if(e.getButton() == MouseEvent.BUTTON1) {
			
			Coordinate pos = calculateMousePosition(e);
			
			for(Tile tile : World.instance.GetTiles()) {
				if(tile.isInView()) {
					if(Game.CALCULATE_PLAYER_LOS == false || tile.isDiscovered()) {
						if(tile.GetBounds().contains(new Point(pos.getX(), pos.getY()))) {
							tile.toggleSelect();
							System.out.println(tile.GetInfo());
						}
					}
				}
			}
		}*/
	}
	
	public void mouseMoved(MouseEvent e) {
	
		// AWESOME MOUSE HOVER SCRIPT
		
		// only allow hover when in game.
		if(Game.instance.getGameState() == GameState.Loading) return;
		
		// get the mouse position in correct coordinates.
		Coordinate pos = calculateMousePosition(e);
		
		// do stupid null checks
		if(pos == null) return;
		if(World.instance == null) return;
		
		// get tiles
		List<Tile> tiles = World.instance.GetTiles();
		
		// flag if we didn't hover on anything.
		boolean notHoveringAnywhere = true;
		
		// select only one tile at a time.
		for(Tile tile : tiles) {
			if(tile.isInView()) {
				if(Game.CALCULATE_PLAYER_LOS == false || tile.isDiscovered()) {
					if(tile.GetBounds().contains(new Point(pos.getX(), pos.getY()))) {
						
						// if we get here,
						// then we are actually hovering on something.
						notHoveringAnywhere = false;
						
						// if we are hovering on a selected tile
						// just break out of the loop.
						// --> else we are on a new tile so deselect
						//     the currently selected tile.
						if(tile.isSelected()) break;
						else { if(hoveringTile != null) hoveringTile.Deselect(); }
						
						// cache newly selected tile.
						hoveringTile = tile;
						tile.Select();
						
						// TODO: update GUI here...
						System.out.println(tile.GetInfo());
					}
				}
			}
		}
		
		// if we didn't hover on anything 
		// e.g. non-discovered tile
		// just deselect our selection.
		if(notHoveringAnywhere) if(hoveringTile != null) hoveringTile.Deselect();
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
