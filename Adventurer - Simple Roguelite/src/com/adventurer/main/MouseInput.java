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
		
		// calculate position
		int x = (mouseX / Game.CAMERAZOOM + camera.x);
		int y = (mouseY / Game.CAMERAZOOM + camera.y);
		
		return new Coordinate(x, y);
	}
	
	public void mousePressed(MouseEvent e) {
		
		// run this only when the mainmenu is open.
	    if(Game.instance.getGameState() == GameState.MainMenu) {
	    	
	    	// calculate mouse position.
	    	Coordinate pos = new Coordinate(e.getX(), e.getY());
	    	
	    	// play-button
	    	Coordinate play_pos = new Coordinate(Game.WIDTH/5, 250);
	    	Coordinate play_size = new Coordinate(200, 50);
	    	Rectangle playRect = new Rectangle(play_pos.getX(), play_pos.getY(), play_size.getX(), play_size.getY());
	    	
	    	// exit-button
	    	Coordinate exit_pos = new Coordinate(Game.WIDTH/5, 350);
	    	Coordinate exit_size = new Coordinate(200, 50);
	    	Rectangle exitRect = new Rectangle(exit_pos.getX(), exit_pos.getY(), exit_size.getX(), exit_size.getY());
	    	
	    	// create mouse point
	    	Point mousepoint = new Point(pos.getX(), pos.getY());
	    	
	    	// check if we pressed play or exit buttons.
	    	if(playRect.contains(mousepoint)) Game.instance.startGame();
	    	else if(exitRect.contains(mousepoint)) System.exit(0);
	    }
	}
	
	public void mouseMoved(MouseEvent e) {
	
		// ---------------- AWESOME MOUSE HOVER SCRIPT ------------------
		
		// only allow hover when in game.
		if(Game.instance.getGameState() != GameState.InGame) return;
		
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
						
						// TODO: this is fishy as balls.
						Handler.instance.setHoverTile(hoveringTile);
						Handler.instance.setMousePosition(pos);
						
						// debug some info.
						//System.out.println(tile.GetInfo());
					}
				}
			}
		}
		
		// if we didn't hover on anything 
		// e.g. non-discovered tile
		// just deselect our selection.
		if(notHoveringAnywhere) {
			if(hoveringTile != null) hoveringTile.Deselect();
			Handler.instance.setHoverTile(null);
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
