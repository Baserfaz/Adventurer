package com.adventurer.utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.gameobjects.Tile;

public class MazeGeneration {

	public static List<Tile> generateMaze_v1 (List<Tile> tiles) {
		
		System.out.println("Creating maze...");
		
		List<Tile> tiles_ = new ArrayList<Tile>(tiles);
		
		List<Tile> closedSet = new ArrayList<Tile>(); 		// visited tiles
		Queue<Tile> openSet = new ArrayDeque<Tile>();		// discovered tiles
		
		// get a random tile
		Tile current = tiles.get(Util.GetRandomInteger(0, tiles.size()));
		
		closedSet.add(current);
		openSet.addAll(getNeighboringTiles(current, tiles, openSet, closedSet));
		
		while(openSet.isEmpty() == false) {
			
			current = openSet.remove();
			
			List<Tile> neighbors = getNeighboringTiles(current, tiles, openSet, closedSet);
			
			int floorCount = 0;
			
			for(Tile t : neighbors) {
				if(closedSet.contains(t)) {
					floorCount += 1;
				}
			}
			
			if(floorCount < 2) {
				
				closedSet.add(current);
				
				for(Tile t : neighbors) {
					if(closedSet.contains(t) || openSet.contains(t)) continue;
					else openSet.add(t);
				}
				
			}
		}
		
		// replace error tiles with floor tiles.
		for(Tile t : closedSet)  {
			tiles_.remove(t);
			Tile tile_ = replaceTile(t, TileType.Floor, SpriteType.FloorTile01);
			tiles_.add(tile_);
		}
		
		System.out.println("Maze created.");
		
		return createMazeWalls(tiles_);
	}
	
	public static List<Tile> generateMaze_v2(List<Tile> tiles) {
		List<Tile> tiles_ = new ArrayList<Tile>();
		
		
		
		
		return tiles_;
	}
	
	private static List<Tile> createMazeWalls(List<Tile> tiles) {
		
		List<Tile> tiles_ = new ArrayList<Tile>(tiles);
		
		for(Tile t : tiles) {
			if(t.GetTileType() == TileType.Error) {
				tiles_.remove(t);
				Tile tile_ = replaceTile(t, TileType.Wall, SpriteType.Wall01);
				tiles_.add(tile_);
			}
		}
		return tiles_;
	}
	
	private static Tile replaceTile(Tile old, TileType newType, SpriteType newSpriteType) {
		Tile tile_ = new Tile(old.GetWorldPosition(), old.GetTilePosition(), newSpriteType, newType);
		old.Remove();
		return tile_;
	}
	
	private static List<Tile> getNeighboringTiles(Tile tile, List<Tile> tiles, Queue<Tile> openSet, List<Tile> closedSet) {
		List<Tile> neighbors = new ArrayList<Tile>();
		
		int x = tile.GetTilePosition().getX();
		int y = tile.GetTilePosition().getY();
		
		// neighbors
		Coordinate top = new Coordinate(x, y - 1);
		Coordinate bottom = new Coordinate(x, y + 1);
		Coordinate left = new Coordinate(x - 1, y);
		Coordinate right = new Coordinate(x + 1, y);
		
		for(Tile t : tiles) {
			// we want ALL tiles, no filtering here yet.
			//if(openSet.contains(t) || closedSet.contains(t)) continue;
			
			int x_ = t.GetTilePosition().getX();
			int y_ = t.GetTilePosition().getY();
			
			// check if this tile is a neighbor of the current tile.
			// tiles contains only tile type of error tiles.
			
			if(
				(top.getX() == x_ && top.getY() == y_) 			||
				(bottom.getX() == x_ && bottom.getY() == y_) 	||
				(left.getX() == x_ && left.getY() == y_) 		||
				(right.getX() == x_ && right.getY() == y_)
			) { neighbors.add(t); }
			
		}
		return neighbors;
	}
}
