package com.adventurer.utilities;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.data.Coordinate;
import com.adventurer.data.Room;
import com.adventurer.data.World;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.RoomType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.Tile;
import com.adventurer.main.Game;

public class DungeonGeneration {
	
	/*
	 * Creates Rooms, world walls and maze, in that order.
	 * 1. Rooms are inside the world space
	 * 2. Rooms are not overlapping
	 * 3. Walls are created around the world edge.
	 * 4. Maze is generated randomly
	 * 5. Doors are created to connect the maze with other rooms.
	 */
	
	public static List<Tile> createDungeon(int roomcount) {
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("Generating dungeon...");
		
		List<Tile> allTiles = new ArrayList<Tile>();
		List<Room> allRooms = new ArrayList<Room>();
		
		for(int i = 0; i < roomcount; i++) {
			
			RoomType randType = RoomType.values()[Util.GetRandomInteger(0, RoomType.values().length)];
			
			int width, height, tryCount = 0;
			Coordinate startTilePos;
			boolean failure = false;
			
			// This loop tries to fit different sized rooms 
			// to the dungeon. For every room it tries this multiple times.
			do {
				// randomize room size
				width = Util.GetRandomInteger(Game.ROOM_MIN_WIDHT, Game.ROOM_MAX_WIDTH);
				height = Util.GetRandomInteger(Game.ROOM_MIN_HEIGHT, Game.ROOM_MAX_HEIGHT);
				
				// randomize start TILE position
				startTilePos = new Coordinate(
						Util.GetRandomInteger(0, Game.WORLDWIDTH), 
						Util.GetRandomInteger(0, Game.WORLDHEIGHT));
			
				tryCount += 1;
				
				if(tryCount >= Game.ROOM_TRY_GENERATION_COUNT) {
					failure = true;
					break;
				}
				
				// the room should not be inside other rooms 
				// and it should be inside the world space.
			} while (checkOverlap(startTilePos, width, height, allTiles) || checkInsideWorld(startTilePos, width, height) == false);
			
			// if the room fails to fit anywhere,
			// then we won't create a room.
			if(failure) continue;
			
			// create room
			Room room = createRoom(width, height, randType, startTilePos);
			
			// save room to all rooms
			allRooms.add(room);
			
			// save tiles to all tiles
			allTiles.addAll(room.getTiles());
		}
		
		System.out.println("Created: " + allRooms.size() + " rooms.");
		
		// adds world walls to the list.
		// modifies the allTiles list.
		allTiles = createWorldWalls(allTiles);
		
		// contains only "empty" i.e. error tiles.
		// creates a new list of error tiles.
		List<Tile> modTiles = fillEmptyWithErrorTiles(allTiles);
		
		// generate maze.
		modTiles = MazeGeneration.generateMaze_v3(modTiles);
		allTiles.addAll(modTiles);
		
		// create doors 
		allTiles = createDoorways(allRooms, allTiles);
		
		// fill in the maze's dead-ends.
		allTiles = fillDeadEnds(allTiles);
		
		// ----------- END OF GENERATION ------------------
		
		int genTime = (int) (System.currentTimeMillis() - startTime);
		
		System.out.println("World consists of " + allTiles.size() + " tiles.");
		System.out.println("World generated in : " + genTime + " milliseconds.");
		
		// just to be sure that there are no error tiles left in the dungeon.
		return Util.replaceAllErrorTiles(allTiles);
	}
	
	private static List<Tile> fillDeadEnds(List<Tile> tiles) {
		List<Tile> tiles_  = new ArrayList<Tile>(tiles); // contains all tiles
		List<Tile> remove_ = new ArrayList<Tile>();		 // contains all "to be removed"-tiles
		List<Tile> add_    = new ArrayList<Tile>();		 // contains all "to be added"-tiles
		
		// fills in all "dead-ends".
		while(true) {
			
			for(Tile tile : tiles_) {
				if(tile.isWalkable()) {
					
					int neighboringWallCount = 0;
					boolean hasDoor = false;
					
					for(Tile t : Util.getNeighboringTiles(tile, tiles_)) {
						if(t instanceof Door) {
							hasDoor = true;
							break;
						}
						
						if(t.GetTileType() == TileType.OuterWall || t.GetTileType() == TileType.Wall) neighboringWallCount += 1;
					}
					
					if(neighboringWallCount >= 3 && hasDoor == false) {
						remove_.add(tile);
						Tile newt = new Tile(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Wall01, TileType.Wall);//Util.replaceTile(tile, TileType.Floor, SpriteType.FloorTile01);
						add_.add(newt);
					}
				}	
			}
			
			tiles_.removeAll(remove_);
			tiles_.addAll(add_);
			
			// if there is no dead ends left -> done.
			if(remove_.isEmpty()) break;
			
			// reset lists
			remove_.clear();
			add_.clear();
		}
			
		return tiles_;
	}
	
	private static List<Tile> createDoorways(List<Room> rooms, List<Tile> tiles) {
		List<Tile> tiles_ = new ArrayList<Tile>(tiles);
		
		// for each room create doors.
		for(Room room : rooms) {			
			int doorCount = 0, y = 0, x = 0;
			List<Tile> doorSpotCandidates = new ArrayList<Tile>();
			
			for(int i = 0; i < room.getTiles().size(); i++) {
				
				// get current tile.
				Tile tile = room.getTiles().get(i);
				
				// 1. check if the tile is valid
				// --> tile should be wall
				// --> tile should have two floor neighbors
				// 2. replace tile with a door tile
				
				// Calculate room tile x & y coordinates from the current tile count.
				if(i != 0) x += 1;
				if(x == 0) /* do nothing*/ ;
				else if(x % (room.getRoomWidth() + 2) == 0) {
					y += 1;
					x = 0;
				}
				
				// room walls are type of outerwall.
				if(tile.GetTileType() == TileType.OuterWall) {
					
					boolean createDoor = false;
					
					// get all neighboring tiles
					Tile up    = Util.getNeighboringTile(tile, Direction.North, tiles_);
					Tile down  = Util.getNeighboringTile(tile, Direction.South, tiles_);
					Tile left  = Util.getNeighboringTile(tile, Direction.West, tiles_);
					Tile right = Util.getNeighboringTile(tile, Direction.East, tiles_);
					
					// vertical doors
					if(up != null && up.isWalkable() && down != null && down.isWalkable()) {
						if(y == 0 || y == room.getRoomHeight() + 1) createDoor = true;
					// horizontal doors
					} else if(left != null && left.isWalkable() && right != null && right.isWalkable()) {
						if(x == 0 || x == room.getRoomWidth() + 1) createDoor = true;
					}
				
					// can't have doors neighboring each other.
					if(doorSpotCandidates.contains(up) || doorSpotCandidates.contains(down) ||
					   doorSpotCandidates.contains(left) || doorSpotCandidates.contains(right)
					) createDoor = false;
							
					// if we found a tile which could be a door
					// then it's valid candidate spot for a door. 
					if(createDoor) doorSpotCandidates.add(tile);	
				}
			}
			
			// debug/error information ---->
			if(doorSpotCandidates.isEmpty()) {
				System.out.println("ERROR: EntrySpots is empty!");
				continue;
			} // <----
			
			// choose random spots to be doors.
			List<Tile> chosenSpots = new ArrayList<Tile>();
			while(doorCount < Math.max(Game.ROOM_DOOR_MAX_COUNT, chosenSpots.size())) {
				Tile chosen = doorSpotCandidates.get(Util.GetRandomInteger(0, doorSpotCandidates.size()));
				if(chosenSpots.contains(chosen)) continue;
				chosenSpots.add(chosen);
				doorCount += 1;
			}
			
			// create doors.
			for(Tile t : chosenSpots) {
				tiles_.remove(t);
				tiles_.add(Util.replaceTileWithDoor(t, false));
			}	
		}
		return tiles_;
	}
	
	private static Room createRoom(int width, int height, RoomType roomtype, Coordinate startTilePos) {
		
		int offsetY = 0, offsetX = 0;
		List<Tile> tiles = new ArrayList<Tile>();
		
		for(int y = 0; y < height + 2; y++) {
			for(int x = 0; x < width + 2; x++) {
				
				// calculate tile position
				Coordinate tilePos = new Coordinate(x + startTilePos.getX(), y + startTilePos.getY());
				
				// calculate world position
				Coordinate worldPos = World.instance.ConvertTilePositionToWorld(tilePos);
				
				// variables etc.
				TileType tileType = null;
				SpriteType spriteType = null;
				
				// decide tiletype
				// TODO: room types
				if(y == 0 || x == 0 || y == height + 1 || x == width + 1) {
					tileType = TileType.OuterWall;
					spriteType = SpriteType.Wall01;
				} else {
					tileType = TileType.Floor;
					spriteType = SpriteType.FloorTile01;
				}
				
				// create tile object
				Tile tile = new Tile(worldPos, tilePos, spriteType, tileType);
				
				// add tiles to list
				tiles.add(tile);
				
				offsetX += Game.TILEGAP;
			}
			offsetX = 0;
			offsetY += Game.TILEGAP;
		}
		
		return new Room(width, height, startTilePos, tiles, roomtype);
	}
	
	private static List<Tile> createWorldWalls(List<Tile> tiles) {
		
		// old tiles + new tiles?
		List<Tile> tiles_ = new ArrayList<Tile>(tiles);
		
		for(int y = 0; y < Game.WORLDHEIGHT; y++) {
			for (int x = 0; x < Game.WORLDWIDTH; x++) {
				
				boolean empty = true;
				
				for(Tile tile : tiles) {
					
					Coordinate pos = tile.GetTilePosition();
					
					if(pos.getX() == x && pos.getY() == y) {
						empty = false;
						break;
					}
				}
				
				if(empty && (y == 0 || y == Game.WORLDHEIGHT - 1 || x == 0 || x == Game.WORLDWIDTH - 1)) {
					
					Coordinate tilePos = new Coordinate(x, y);	
					
					// calculate world position
					Coordinate worldPos = World.instance.ConvertTilePositionToWorld(tilePos);
					
					Tile t = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
					
					tiles_.add(t);
				}
			}
		}
		return tiles_;
	}
	
	private static List<Tile> fillEmptyWithErrorTiles(List<Tile> tiles) {
		
		List<Tile> tiles_ = new ArrayList<Tile>();
		
		for(int y = 0; y < Game.WORLDHEIGHT; y++) {
			for (int x = 0; x < Game.WORLDWIDTH; x++) {
				
				boolean empty = true;
				
				for(Tile tile : tiles) {
					
					Coordinate pos = tile.GetTilePosition();
					
					if(pos.getX() == x && pos.getY() == y) {
						empty = false;
						break;
					}
				}
				
				if(empty) {
					
					Coordinate tilePos = new Coordinate(x, y);	
					
					// calculate world position
					Coordinate worldPos = World.instance.ConvertTilePositionToWorld(tilePos);
					
					Tile t = new Tile(worldPos, tilePos, SpriteType.Error, TileType.Error);
					
					tiles_.add(t);
				}
			}
		}
		return tiles_;
	}
	
	private static boolean checkInsideWorld(Coordinate startTilePos, int width, int height) {
		
		int x_ = startTilePos.getX() + width + 1;
		int y_ = startTilePos.getY() + height + 1;
		
		if(x_ > Game.WORLDWIDTH - 1 || y_ > Game.WORLDHEIGHT - 1) return false;
		else return true;
	}
	
	private static boolean checkOverlap(Coordinate startTilePos, int width, int height, List<Tile> tiles) {	
	
		// leave one tile between rooms
		// returns true if rooms are overlapping.
		
		for(int y = -3; y < height + 3; y++) {
			for (int x = -3; x < width + 3; x++) {
				
				// calculate the tile position of the tile
				// and check if there is already a tile on that spot.
				// if any tile overlaps -> return.
				
				int x_ = x + startTilePos.getX();
				int y_ = y + startTilePos.getY();
				
				for(Tile tile : tiles) {
					
					Coordinate pos = tile.GetTilePosition();
					
					// overlapping
					if(pos.getX() == x_ && pos.getY() == y_) return true;
					
				}
			}
		}
		return false;
	}
}
