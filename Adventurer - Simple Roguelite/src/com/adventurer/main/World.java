package com.adventurer.main;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.gameobjects.DestructibleTile;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Trap;

public class World {

	private int worldHeight;
	private int worldWidth;
	
	private int roomHeight;
	private int roomWidth;
	
	private List<Tile> tiles;
	private List<Room> rooms;
	
	public static final int TILESIZE = 16;	// should be same as Game.SPRITESIZE
	public static final int TILEGAP = 2;	// gap between each tile.
	public static final int ROOMGAP = 0; 	// gap between rooms
	
	public static World instance;
	
	public World(int wwidth, int wheight, int rwidth, int rheight) {
		
		if(instance != null) return;
		
		World.instance = this;
		
		this.worldHeight = wheight;
		this.worldWidth = wwidth;
		
		this.roomHeight = rheight;
		this.roomWidth = rwidth;
		
		this.tiles = new ArrayList<Tile>();
		this.rooms = new ArrayList<Room>();
		
		CreateWorld();
	}
	
	public Tile GetTileAtPosition(Coordinate pos) {
		return GetTileAtPosition(pos.getX(), pos.getY());
	}
	
	public Tile GetTileAtPosition(int x, int y) {
		
		Tile retTile = null;
		
		for(int i = 0; i < tiles.size(); i++) {
			Tile tile = tiles.get(i);
			Coordinate position = tile.GetTilePosition();
			
			if(position.getX() == x && position.getY() == y) {
				retTile = tile;
				break;
			}
		}
		return retTile;
	}
	
	public List<Tile> GetTilesInCardinalDirection(Coordinate pos) {
		return GetTilesInCardinalDirection(pos.getX(), pos.getY());
	}
	
	public List<Tile> GetTilesInCardinalDirection(int posx, int posy) {
		
		List<Tile> foundTiles = new ArrayList<Tile>();
		Tile current = null;
		
		for(int y = -1; y < 2; y++) {
			for(int x = -1; x < 2; x++) {
				
				if((x == -1 || x == 1) && y == 0) {
					
					// # # # 
					// 1 @ 2
					// # # #
					current = GetTileAtPosition(posx + x, posy + y);
					if(current == null) continue;
					foundTiles.add(current);
					
				} else if((y == -1 || y == 1) && x == 0) {
					
					// # 1 # 
					// # @ #
					// # 2 #
					current = GetTileAtPosition(posx + x, posy + y);
					if(current == null) continue;
					foundTiles.add(current);
					
				}
			}
		}
		
		return foundTiles;
	}
	
	public List<Tile> GetSurroundingTiles(Coordinate pos) {
		return GetSurroundingTiles(pos.getX(), pos.getY());
	}
	
	public List<Tile> GetSurroundingTiles(int posx, int posy) {
		List<Tile> foundTiles = new ArrayList<Tile>();
		for(int y = -1; y < 2; y++) {
			for(int x = -1; x < 2; x++) {
				foundTiles.add(GetTileAtPosition(posx + x, posy + y));
			}
		}
		return foundTiles;
	}
	
	public Tile GetTileFromDirection(Coordinate pos, Direction dir) {
		return GetTileFromDirection(pos.getX(), pos.getY(), dir);
	}
	
	public Tile GetTileFromDirection(int x, int y, Direction dir) {
		
		Tile tile = null;
		
		switch(dir) {
		case North:
			tile = GetTileAtPosition(x, y - 1);
			break;
		case South:
			tile = GetTileAtPosition(x, y + 1);
			break;
		case West:
			tile = GetTileAtPosition(x - 1, y);
			break;
		case East:
			tile = GetTileAtPosition(x + 1, y);
			break;
		default:
			System.out.println("GetTileFromDirection: NOT A CARDINAL DIRECTION!");
			break;
		}
		
		return tile;
	}
	
	public Tile GetTileFromDirection(Tile current, Direction dir) {
		
		Tile tile = null;
		
		int x = current.GetTilePosition().getX();
		int y = current.GetTilePosition().getY();
		
		switch(dir) {
		case North:
			tile = GetTileAtPosition(x, y - 1);
			break;
		case South:
			tile = GetTileAtPosition(x, y + 1);
			break;
		case West:
			tile = GetTileAtPosition(x - 1, y);
			break;
		case East:
			tile = GetTileAtPosition(x + 1, y);
			break;
		default:
			System.out.println("GetTileFromDirection: NOT A CARDINAL DIRECTION!");
			break;
		}
		
		return tile;
	}
	
	public int[] GetFreePosition() {
		
		List<Tile> possibleTiles = new ArrayList<Tile>();
		int[] position = new int[4];
		
		// get all possible tiles
		for(int i = 0; i < tiles.size(); i++) {
			
			Tile tile = tiles.get(i);
			
			if(tile.GetTileType() == TileType.Floor && tile.GetActor() == null) {
				possibleTiles.add(tile);
			}
		}
		
		// get a random number
		int random = Util.GetRandomInteger(0, possibleTiles.size());
		
		// get a random tile from possible tiles.
		Tile randomTile = possibleTiles.get(random);
		
		// get the position of random tile.
		Coordinate randTilePos = randomTile.GetTilePosition();
		
		// world-x
		position[0] = randomTile.GetWorldPosition().getX();
		
		// world-y
		position[1] = randomTile.GetWorldPosition().getY();
		
		// tile-x
		position[2] = randTilePos.getX();
		
		// tile-y
		position[3] = randTilePos.getY();
		
		return position;
	}
	
	public int ConvertTilePositionToWorld(int pos) {
		return (pos * TILESIZE + TILEGAP * pos);
	}
	
	// creates a new TILE and destroys the old one.
	public Tile ReplaceTile(Tile old, TileType newType, SpriteType newSprite) {
		
		Tile newTile = null;
		
		// create new tile
		if(newType == TileType.TrapTile) {
			newTile = (Trap) new Trap(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.TrapTile01, TileType.TrapTile, 100);
		} else if(newType == TileType.Door) {
			newTile = (Door) new Door(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.Door01, TileType.Door);
		} else if(newType == TileType.Floor) {
			newTile = new Tile(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.FloorTile01, TileType.Floor);
		} else {
			System.out.println("WORLD.REPLACETILE: TILETYPE NOT YET IMPLEMENTED!");
		}
		
		// add new tile to our list of tiles.
		AddToTiles(newTile);
		
		// remove old tile
		old.Remove();
		
		return newTile;
	}
	
	// Changes tiletype and spritetype of OLD tile.
	public void ChangeTile(Tile oldTile, TileType newTileType, SpriteType newSpriteType) {
		
		// 1. change the data of old tile.
		oldTile.SetTileType(newTileType);
		
		// 2. change the graphics of old tile.
		oldTile.SetSprite(SpriteCreator.instance.CreateSprite(newSpriteType));
	}
	
	public List<Tile> GetTilesOfType(TileType type) {
		List<Tile> foundTiles = new ArrayList<Tile>();
		for(int y = 0; y < worldHeight; y++) {
			for (int x = 0; x < worldWidth; x++) {
				Tile current = GetTileAtPosition(x, y);
				if(current.GetTileType() == type) {
					foundTiles.add(current);
				}
			}
		}
		return foundTiles;
	}
	
	public void CreateVanityItems() {
		
		List<Tile> floorTiles = GetTilesOfType(TileType.Floor);
		List<Tile> wallTiles = GetTilesOfType(TileType.Wall);
		
		// create an array of random vanity item sprites
		SpriteType[] floorVanitySpriteTypes = { SpriteType.Pot01, SpriteType.PotRemains01 };
		SpriteType[] wallVanitySpriteTypes = { SpriteType.Torch01 };
		
		// create floor vanity items
		for(Tile tile : floorTiles) {
			
			if(Util.GetRandomInteger() > 95) {
				
				// randomize spritetype
				SpriteType st = floorVanitySpriteTypes[Util.GetRandomInteger(0, floorVanitySpriteTypes.length)];
				
				// create vanity item
				VanityItemCreator.CreateVanityItem(tile, st, true);
			}
		}
		
		// create wall vanity items
		for(Tile tile : wallTiles) {
			
			if(Util.GetRandomInteger() > 90) {
				
				// randomize spritetype
				SpriteType st = wallVanitySpriteTypes[Util.GetRandomInteger(0, wallVanitySpriteTypes.length)];
				
				// create vanity item
				VanityItemCreator.CreateVanityItem(tile, st, false);
			}
		}
	}
	
	public void CreateTraps() {
		
		List<Tile> floorTiles = GetTilesOfType(TileType.Floor);
		
		for(Tile tile : floorTiles) {
			
			if(Util.GetRandomInteger() > 98) {
				
				// change tile type & sprite type
				ReplaceTile(tile, TileType.TrapTile, SpriteType.TrapTile01);
			}
		}
		
	}
	
	public void CreateIndestructibleWalls() {
		// TODO
	}
	
	// creates doors randomly
	public void CreateDoors(List<Tile> roomTiles) {
		
		for(Tile current : roomTiles) {
			
			// limit the door count
			//if(Util.GetRandomInteger() < 50) continue;
			
			// tile should be floor.
			if(current.GetTileType() != TileType.Floor) continue;
			
			// get tile's cardinal direction tiles
			List<Tile> cardinalTiles = GetTilesInCardinalDirection(current.GetTilePosition());
			
			// save the direction data
			List<Direction> dirData = new ArrayList<Direction>();
			
			boolean valid = true;
			
			// get the direction data of wall tiles.
			for(Tile t : cardinalTiles) {
				
				// Take these tile types in count.
				// when looking for neighbours.
				if(t.GetTileType() == TileType.Wall || t.GetTileType() == TileType.OuterWall) {
					
					// this is direction of t from current.
					Direction dirToCurrent = GetDirectionOfTileFromPoint(current, t);
					
					if(dirToCurrent == null) continue;
					
					// save data to array.
					dirData.add(dirToCurrent);
					
				} else if(t.GetTileType() == TileType.DestructibleObject || t.GetTileType() == TileType.Door) {
					
					// this is not a valid spot for a door!
					valid = false;
					break;
				}
			}
			
			if(valid == false) continue;
			
			// at this point we should have only 
			// two walls that we need to check.
			if(dirData.size() != 2) continue;
			Direction first_dir = dirData.get(0);
			Direction second_dir = dirData.get(1);
			
			Tile newDoorTile = null;
			
			switch(first_dir) {
			case North:
				if(second_dir == Direction.South) newDoorTile = ReplaceTile(current, TileType.Door, SpriteType.Door01);
				break;
			case East:
				if(second_dir == Direction.West) newDoorTile = ReplaceTile(current, TileType.Door, SpriteType.Door01);
				break;
			case South:
				if(second_dir == Direction.North) newDoorTile = ReplaceTile(current, TileType.Door, SpriteType.Door01);
				break;
			case West:
				if(second_dir == Direction.East) newDoorTile = ReplaceTile(current, TileType.Door, SpriteType.Door01);
				break;
			}
			
			if(newDoorTile != null && Game.PRINT_DOOR_CREATED) System.out.println("Created door: " + newDoorTile.GetInfo());
		}
	}
	
	// works only with cardinal tiles!
	public Direction GetDirectionOfTileFromPoint(Tile from, Tile to) {
		
		Direction dir = null;
		
		int fromX = from.GetTilePosition().getX();
		int fromY = from.GetTilePosition().getY();
		int toX = to.GetTilePosition().getX();
		int toY = to.GetTilePosition().getY();
		
		if(fromX < toX) dir = Direction.East;
		else if(fromX > toX) dir = Direction.West;
		else if(fromY < toY) dir = Direction.South;
		else if(fromY > toY) dir = Direction.North;
		
		return dir;
	}
	
	private void CreateWorld() {
		
		int roomOffsetY = 0;
		int roomOffsetX = 0;
		
		int roomCount = 0;
		int roomMaxCount = worldHeight * worldWidth;
		
		if(Game.PRINT_WORLD_CREATION_START_END) System.out.println("Begin world creation.");
		
		for(int y = 0; y < worldHeight; y++) {
			for(int x = 0; x < worldWidth; x++) {
				Room currentRoom = CreateRoom(roomOffsetX, roomOffsetY, x, y);
				rooms.add(currentRoom);
				roomOffsetX += (roomWidth * TILESIZE + roomWidth * TILEGAP) + ROOMGAP;
				
				roomCount++;
				
				if(Game.PRINT_WORLD_CREATION_PERCENTAGE_DONE) {
					double percentageDone = ((double) roomCount / roomMaxCount) * 100;
					System.out.println("Creating world: " + percentageDone);
				}
				
			}
			roomOffsetX = 0;
			roomOffsetY += (roomHeight * TILESIZE + roomHeight * TILEGAP) + ROOMGAP;
		}
		
		if(Game.PRINT_WORLD_CREATION_START_END) System.out.println("End world creation.");
	}
	
	private Room CreateRoom(int roomOffsetX, int roomOffsetY, int roomStartX, int roomStartY) {
		
		List<Tile> roomTiles = new ArrayList<Tile>();
		
		int offsetY = 0;
		int offsetX = 0;
		
		for(int y = 0; y < roomHeight; y++) {
			for (int x = 0; x < roomWidth; x++) {
				
				Tile tile = null;
				
				Coordinate tilePos = new Coordinate(x + (roomStartX * roomWidth), y + (roomStartY * roomHeight));
				Coordinate worldPos = new Coordinate(x * TILESIZE + offsetX + roomOffsetX, y * TILESIZE + offsetY + roomOffsetY);
				
				// create outer wall
				if(y == 0 || y == roomHeight - 1 || x == 0 || x == roomWidth - 1) {
							
					tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
					
				} else {
					
					if(Util.GetRandomInteger() > 20) {
						
						// create floor 
						tile = new Tile(worldPos, tilePos, SpriteType.FloorTile01, TileType.Floor);
						
					} else {
						
						// create wall
						tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.Wall);
					}
				}
				
				// add to tiles list
				roomTiles.add(tile);
				tiles.add(tile);
				
				offsetX += TILEGAP;
				
			}
			offsetX = 0;
			offsetY += TILEGAP;
		}
		
		CreateDoors(roomTiles);
		//CreateTraps();
		//CreateIndestructibleWalls();
		//CreateVanityItems();
		
		Room room = new Room(roomWidth, roomHeight, new Coordinate(roomStartX, roomStartY), roomTiles);
		
		return room;
	}
	
	public World GetWorld() {
		return this;
	}
	
	public int GetHeight() {
		return this.worldHeight;
	}
	
	public int GetWidth() {
		return this.worldWidth;
	}
	
	public List<Tile> GetTiles() {
		return this.tiles;
	}
	
	public void AddToTiles(Tile t) {
		this.tiles.add(t);
	}
	
	public void RemoveTiles(Tile t) {
		this.tiles.remove(t);
	}

	public int getRoomHeight() {
		return roomHeight;
	}

	public void setRoomHeight(int roomHeight) {
		this.roomHeight = roomHeight;
	}

	public int getRoomWidth() {
		return roomWidth;
	}

	public void setRoomWidth(int roomWidth) {
		this.roomWidth = roomWidth;
	}
}
