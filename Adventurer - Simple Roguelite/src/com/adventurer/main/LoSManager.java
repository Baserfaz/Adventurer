package com.adventurer.main;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.gameobjects.*;

public class LoSManager {
	
	public LoSManager() {}
	
	private List<Tile> CalculateLosToDirection(Coordinate pos, Direction dir) {
		
		List<Tile> retTiles = new ArrayList<Tile>();
		
		int x = pos.getX();
		int y = pos.getY();
		World world = World.instance.GetWorld();
		
		Tile tile = null;
		int offset = 1;
		
		// add the tile that we are currently standing on.
		retTiles.add(world.GetTileAtPosition(pos));
		
		do {	
			switch(dir) {
			case North:
				tile = world.GetTileAtPosition(x, y - offset);
				break;
			case South:
				tile = world.GetTileAtPosition(x, y + offset);
				break;
			case West:
				tile = world.GetTileAtPosition(x - offset, y);
				break;
			case East:
				tile = world.GetTileAtPosition(x + offset, y);
				break;
			case NorthEast:
				tile = world.GetTileAtPosition(x + offset, y - offset);
				break;
			case NorthWest:
				tile = world.GetTileAtPosition(x - offset, y - offset);
				break;
			case SouthEast:
				tile = world.GetTileAtPosition(x + offset, y + offset);
				break;
			case SouthWest:
				tile = world.GetTileAtPosition(x - offset, y + offset);
				break;
			}
			
			if(tile != null) retTiles.add(tile);
			
			offset ++;
			
		} while ((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null);
		
		return retTiles;
	}
	
	public void CalculateLos(Coordinate position) {
		
		// list of tiles that are visible
		List<Tile> foundTiles = new ArrayList<Tile>();
		
		World world = World.instance.GetWorld();
		
		// 1. hide everything
		List<Tile> allTiles = world.GetTiles();
		for(int i = 0; i < allTiles.size(); i++) {
			allTiles.get(i).Hide();
		}
		
		// 2. calculate FOV
		// ------------------------
		
		// straight line
		foundTiles.addAll(CalculateLosToDirection(position, ActorManager.GetPlayerInstance().GetLookDirection()));
		
		// surrounding tiles
		/*for(Tile tile : world.GetSurroundingTiles(position)) {
			foundTiles.add(tile);
		}*/
		
		// ------------------------
		// 3. show tiles
		for(Tile tile : foundTiles) {
			tile.Discover();
			tile.Show();
		}
	}
}
