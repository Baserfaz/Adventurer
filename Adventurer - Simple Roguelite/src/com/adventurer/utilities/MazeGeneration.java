package com.adventurer.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.gameobjects.Tile;

public class MazeGeneration {
	
	// uses recursive backtracking algorithm, based on Jamis Buck's work.
	// http://www.jamisbuck.org/presentations/rubyconf2011/index.html#recursive-backtracker-demo
	public static List<Tile> generateMaze_v3(List<Tile> tiles) {
		
		List<Tile> tiles_ 		 = new ArrayList<Tile>(tiles);  // contains all tiles
		Stack<Tile> visited 	 = new Stack<Tile>();			// visited tiles
		List<Tile> concretePath  = new ArrayList<Tile>();		// floor tiles
		List<Tile> concreteWalls = new ArrayList<Tile>();		// wall tiles
		
		// randomize starting tile
		Tile current = tiles_.get(Util.GetRandomInteger(0, tiles_.size()));
		visited.push(current);
		
		System.out.println("Maze started from tile: " + current.GetInfo());
		
		Tile cameFrom = null, neighbor = null;
		
		int c = 0; // TODO: this is ugly.
		boolean backtracking = false;
		
		// ----------------------------------- START MAZE GEN
		
		// if the stack is empty, we are done.
		while(visited.isEmpty() == false) {
			
			current = visited.pop();
			concretePath.add(current);
			
			do { // random walker
		
				neighbor = getRandomNeighboringTile(current, tiles_);
				
				if(
						(backtracking == false &&
						neighbor != null && 
						visited.contains(neighbor) == false && 
						concretePath.contains(neighbor) == false &&
						concreteWalls.contains(neighbor) == false) 
						
				) {
					
					// If we accidentally chose the same tile where we came from,
					// then just continue and don't count this as a failure.
					if(neighbor.equals(cameFrom)) continue;
						
					// other neighbors are walls.
					for(Tile t : getNeighboringTiles(current, tiles_)) {
						if(
								t != null && 
								t.equals(neighbor) == false &&
								visited.contains(t) == false &&
								concretePath.contains(t) == false &&
								concreteWalls.contains(t) == false
								
						) concreteWalls.add(t);
					}
					
					visited.push(neighbor);
					cameFrom = current; // cache the previous tile
					current = neighbor;
					c = 0;
					
				} else { c++; }
			} while(c < 10);
		}
		
		// ------------------------------------ END OF MAZE GEN 
		
		// draw floor
		for(Tile t : concretePath) {
			tiles_.remove(t);
			Tile newt = replaceTile(t, TileType.Floor, SpriteType.FloorTile01);
			tiles_.add(newt);
		}
		
		// draw walls
		for(Tile t : concreteWalls) {
			tiles_.remove(t);
			Tile newt = replaceTile(t, TileType.Wall, SpriteType.Wall01);
			tiles_.add(newt);
		}
		
		// TODO: fill in walls if there are more error-tiles.
		return tiles_;//createMazeWalls(tiles_);
	}
	
	// replaces all error tiles with walls.
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
	
	private static Tile getRandomNeighboringTile(Tile tile, List<Tile> tiles) {
		Tile chosen = null;
		
		Direction[] ds = new Direction[] { Direction.North, Direction.East, Direction.South, Direction.West };
		List<Direction> dds = new ArrayList<Direction>();
		for(Direction d : ds) {
			dds.add(d);
		}
		
		// randomize the search order.
		Collections.shuffle(dds);
		
		// get first neighboring tile.
		for(Direction d : dds) {
			Tile n = getNeighboringTile(tile, d, tiles);
			if(n != null) chosen = n;
		}
		return chosen;
	}
	
	private static Tile getNeighboringTile(Tile tile, Direction dir, List<Tile> tiles) {
		Tile chosen = null;
		
		int x = tile.GetTilePosition().getX();
		int y = tile.GetTilePosition().getY();
		
		switch(dir) {
		case East:
			chosen = getTileAt(new Coordinate(x + 1, y), tiles);
			break;
		case North:
			chosen = getTileAt(new Coordinate(x, y - 1), tiles);
			break;
		case South:
			chosen = getTileAt(new Coordinate(x, y + 1), tiles);
			break;
		case West:
			chosen = getTileAt(new Coordinate(x - 1, y), tiles);
			break;
		}
		return chosen;
	}
	
	private static Tile getTileAt(Coordinate pos, List<Tile> tiles) {
		Tile tile = null;
		for(Tile t : tiles) {
			if(t.GetTilePosition().getX() == pos.getX() && t.GetTilePosition().getY() == pos.getY()) {
				tile = t;
				break;
			}
		}
		return tile;
	}
	
	private static List<Tile> getNeighboringTiles(Tile tile, List<Tile> tiles) {
		List<Tile> neighbors = new ArrayList<Tile>();
		
		int x = tile.GetTilePosition().getX();
		int y = tile.GetTilePosition().getY();
		
		// neighbors
		Coordinate top = new Coordinate(x, y - 1);
		Coordinate bottom = new Coordinate(x, y + 1);
		Coordinate left = new Coordinate(x - 1, y);
		Coordinate right = new Coordinate(x + 1, y);
		
		for(Tile t : tiles) {
			
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
