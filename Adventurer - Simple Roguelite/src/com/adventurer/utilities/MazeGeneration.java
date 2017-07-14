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
		
		Tile cameFrom = null, neighbor = null;
		int c = 0; // TODO: this is ugly and has side effects.
		boolean backtracking = false;
				
		// ----------------------------------- START MAZE GEN
		// if the stack is empty, we are done.		
		
		while(visited.isEmpty() == false) {
			
			current = visited.pop();
			concretePath.add(current);
			
			do { // random walker
				
				if(backtracking) {
					
					neighbor = Util.getRandomNeighborWall(current, tiles_, concreteWalls);
					
					if(neighbor != null) {
						
						// our neighbor candidate should have error neighbors
						// --> it should not be in walls or floors
						for(Tile t : Util.getNeighboringTiles(neighbor, tiles_)) {
							
							if(		
								concretePath.contains(t) == false &&
								concreteWalls.contains(t) == false &&
								visited.contains(t) == false
							) {
						
								concreteWalls.remove(neighbor);
								
								visited.push(neighbor);
								cameFrom = current;
								current = neighbor;
								c = 0;
								
								backtracking = false;
								break;
							}
							
						}
						
					} else {
						// there is no walls adjacent to 
						// this current tile.
						continue;
					}
					
				} else {
				
					neighbor = Util.getRandomNeighboringTile(current, tiles_);
					
					if(
							
						(neighbor != null && 
						visited.contains(neighbor) == false && 
						concretePath.contains(neighbor) == false &&
						concreteWalls.contains(neighbor) == false) 
							
					) {
						
						// If we accidentally chose the same tile where we came from,
						// then just continue and don't count this as a failure.
						if(neighbor.equals(cameFrom)) continue;
							
						// other neighbors are walls.
						for(Tile t : Util.getNeighboringTiles(current, tiles_)) {
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
				
				}
					
			} while(c < 20); // TODO: This is bad.
			
			// random walker got stuck.
			// --> backtrack.
			backtracking = true;
		}
		
		// ------------------------------------ END OF MAZE GEN 
		
		// draw floor
		for(Tile t : concretePath) {
			tiles_.remove(t);
			Tile newt = Util.replaceTile(t, TileType.Floor, SpriteType.FloorTile01);
			tiles_.add(newt);
		}
		
		// draw walls
		for(Tile t : concreteWalls) {
			tiles_.remove(t);
			Tile newt = Util.replaceTile(t, TileType.Wall, SpriteType.Wall01);
			tiles_.add(newt);
		}
		
		return tiles_;
	}
	
	// replaces all error tiles with walls.
	private static List<Tile> createMazeWalls(List<Tile> tiles) {
		List<Tile> tiles_ = new ArrayList<Tile>(tiles);
		for(Tile t : tiles) {
			if(t.GetTileType() == TileType.Error) {
				tiles_.remove(t);
				Tile tile_ = Util.replaceTile(t, TileType.Wall, SpriteType.Wall01);
				tiles_.add(tile_);
			}
		}
		return tiles_;
	}
}
