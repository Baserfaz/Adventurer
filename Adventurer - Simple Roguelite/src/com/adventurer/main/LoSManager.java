package com.adventurer.main;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Tile;

public class LoSManager {
	
	private List<Tile> foundTiles = new ArrayList<Tile>();
	
	private Player player;
	
	public LoSManager(Player player) {
		this.player = player;
	}
	
	private void CalculateLosToDirection(World world, int x, int y, Direction dir) {
		Tile tile = null;
		int offset = 1;
		
		// add the tile that we are currently standing on.
		foundTiles.add(world.GetTileAtPosition(x, y));
		
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
			
			// add to found tiles.
			foundTiles.add(tile);
			
			offset ++;
			
		} while ((tile.GetTileType() == TileType.Floor || tile.GetTileType() == TileType.TrapTile) && tile.GetActor() == null);
	}
	
	public void CalculateLos(Coordinate position) {
		
		int x = position.getX();
		int y = position.getY();
		
		World world = Game.instance.GetWorld();
		
		// 1. hide everything
		List<Tile> allTiles = world.GetTiles();
		for(int i = 0; i < allTiles.size(); i++) {
			allTiles.get(i).Hide();
		}
		
		// 2. calculate fov
		CalculateLosToDirection(world, x, y, player.GetLookDirection());
		
		// add surrounding tiles to view too.
		for(Tile tile : World.instance.GetSurroundingTiles(position)) {
			foundTiles.add(tile);
		}
		
		// 3. show tiles
		for(Tile tile : foundTiles) {
			tile.Discover();
			tile.Show();
		}
		
		// clear the array
		foundTiles.clear();
	}
}
