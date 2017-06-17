package com.adventurer.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.gameobjects.DestructibleItem;
import com.adventurer.gameobjects.DestructibleTile;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.Portal;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Trap;
import com.adventurer.gameobjects.Turret;

public class World {

	private int worldHeight;
	private int worldWidth;
	
	private int roomHeight;
	private int roomWidth;
	
	private List<Tile> tiles;
	private List<Room> rooms;
	
	public static final int TILEGAP = 2;	// gap between each tile.
	public static final int ROOMGAP = 0; 	// gap between rooms
	
	public static World instance;
	
	// creates a predefined map
	public World(char[][] map) {
		
		if(instance != null) {
			System.out.println("WORLD ALREADY EXISTS!");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		World.instance = this;
		
		this.tiles = new ArrayList<Tile>();
		
		Tile spawnTile = CreatePredefinedMap(map);
		
		// create player
		if(ActorManager.GetPlayerInstance() == null) ActorManager.CreatePlayerInstance(300, 100, spawnTile);
		//else ActorManager.ForceMovePlayerToFreePosition();
	}
	
	// creates a random level
	public World(int wwidth, int wheight, int rwidth, int rheight) {
		
		if(instance != null) {
			System.out.println("WORLD ALREADY EXISTS!");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		World.instance = this;
		
		this.worldHeight = wheight;
		this.worldWidth = wwidth;
		
		this.roomHeight = rheight;
		this.roomWidth = rwidth;
		
		this.tiles = new ArrayList<Tile>();
		this.rooms = new ArrayList<Room>();
		
		CreateWorld();
		
		// create player
		if(ActorManager.GetPlayerInstance() == null) ActorManager.CreatePlayerInstance(300, 100);
		//else ActorManager.ForceMovePlayerToFreePosition();
	}
	
	public Tile GetTileAtPosition(Coordinate pos) {
		return GetTileAtPosition(pos.getX(), pos.getY());
	}
	
	public Tile GetTileAtPosition(int x, int y) {
		Tile retTile = null;
		for(Tile tile : tiles) {
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
	
	public Tile GetTileWithWorldPosition(Coordinate wpos) {
		
		Tile ret = null;
		
		for(Tile tile : tiles) {
			if(tile.GetBounds().contains(new Point(wpos.getX(), wpos.getY()))) {
				ret = tile;
				break;
			}
		}
		
		
		return ret;
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
			
			if(tile.GetTileType() == TileType.Floor && tile.GetActor() == null && tile.GetItem() == null) {
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
		return (pos * Game.SPRITESIZE + TILEGAP * pos);
	}
	
	// creates a new TILE and destroys the old one.
	public Tile ReplaceTile(Tile old, TileType newType, SpriteType newSprite) {
		
		Tile newTile = null;
		
		// create new tile
		if(newType == TileType.Trap) {
			
			// randomize trap type
			TrapType randTrapType = TrapType.values()[Util.GetRandomInteger(0, TrapType.values().length)];
			
			switch(randTrapType) {
			case Projectile:
				newTile = new Trap(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.TrapTile01, TileType.Trap, TrapType.Projectile, 100);
				break;
			case Gas:
				newTile = new Trap(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.TrapTile01, TileType.Trap, TrapType.Gas, 25);
				break;
			}
		
		} else if(newType == TileType.Door) {
			newTile = new Door(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.Door01, TileType.Door, false);
		} else if(newType == TileType.LockedDoor) {
			newTile = new Door(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.LockedDoor01, TileType.Door, true);
		} else if(newType == TileType.Floor) {
			newTile = new Tile(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.FloorTile01, TileType.Floor);
		} else if(newType == TileType.DestructibleTile) {
			newTile = new DestructibleTile(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.DestructibleWall, TileType.DestructibleTile, 300);
		} else {
			System.out.println("WORLD.REPLACETILE: TILETYPE NOT YET IMPLEMENTED!");
			new Exception().printStackTrace();
			System.exit(1);
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
		
		// 3. reset tinted sprite
		oldTile.SetTintedSprite(null);
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
	
	public List<Tile> GetTilesOfType(TileType type, List<Tile> tiles) {
		List<Tile> foundTiles = new ArrayList<Tile>();
		for(Tile tile : tiles) {
			if(tile.GetTileType() == type) {
				foundTiles.add(tile);
			}
		}
		return foundTiles;
	}
	
	public void CreateDestructibleItemsInsideRoom(List<Tile> roomTiles) {
		
		for(Tile tile : roomTiles) {
			
			if(Util.GetRandomInteger() > 90) {
				if(tile.GetTileType() == TileType.Floor && tile.GetItem() == null) {
					
					DestructibleItem item = new DestructibleItem(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Pot01, 200);
					
				}
			}
		}
	}
	
	public void CreateVanityItemsInsideRoom(List<Tile> roomTiles) {
		
		List<Tile> floorTiles = GetTilesOfType(TileType.Floor, roomTiles);
		List<Tile> wallTiles = GetTilesOfType(TileType.Wall, roomTiles);
		
		// create an array of random vanity item sprites
		SpriteType[] floorVanitySpriteTypes = { SpriteType.PotRemains01 };
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
				VanityItemCreator.CreateLightSource(tile, st, false);
			}
		}
	}
	
	public void CreateTrapsInsideRoom(List<Tile> roomTiles) {
		
		List<Tile> floorTiles = GetTilesOfType(TileType.Floor, roomTiles);
		
		for(Tile tile : floorTiles) {
			
			if(Util.GetRandomInteger() > 98) {
				
				// change tile type & sprite type
				ReplaceTile(tile, TileType.Trap, SpriteType.TrapTile01);
			}
		}
	}
	
	public void CreateDestructibleWallsInsideRoom(List<Tile> roomTiles) {
		
		List<Tile> wallTiles = GetTilesOfType(TileType.Wall, roomTiles);
		
		for(Tile tile : wallTiles) {
			if(Util.GetRandomInteger() > 95) {
				
				// change tile type & sprite type
				ReplaceTile(tile, TileType.DestructibleTile, SpriteType.DestructibleWall);
				
			}
		}
	}
	
	// creates doors randomly
	public void CreateDoorsInsideRoom(List<Tile> roomTiles) {
		
		for(Tile current : roomTiles) {
			
			// limit the door count
			if(Util.GetRandomInteger() < 70) continue;
			
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
					
				} else if(t.GetTileType() == TileType.DestructibleTile || t.GetTileType() == TileType.Door) {
					
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
	
	private void CreateTurretsInsideRoom(List<Tile> roomTiles) {
		
		for(Tile tile : roomTiles) {
		
			if(tile.GetTileType() == TileType.Floor && tile.GetActor() == null && tile.GetItem() == null && Util.GetRandomInteger() > 98) {
			
				Turret turret = new Turret(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.ArrowTurretNorth, 100);
				
			}
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
		
		// TODO: SOMETIMES CREATES 1 EXTRA TILE SOMEWHERE!
		
		int roomOffsetY = 0;
		int roomOffsetX = 0;
		
		int roomCount = 0;
		int roomMaxCount = worldHeight * worldWidth;
		
		if(Game.PRINT_WORLD_CREATION_START_END) System.out.println("Begin world creation.");
		
		for(int y = 0; y < worldHeight; y++) {
			for(int x = 0; x < worldWidth; x++) {
				
				Room currentRoom = CreateRoom(roomOffsetX, roomOffsetY, x, y);
				rooms.add(currentRoom);
				roomOffsetX += (roomWidth * Game.SPRITESIZE + roomWidth * TILEGAP) + ROOMGAP;
				
				roomCount++;
				
				if(Game.PRINT_WORLD_CREATION_PERCENTAGE_DONE) {
					double percentageDone = ((double) roomCount / roomMaxCount) * 100;
					System.out.printf("Creating world: %.0f \n", percentageDone);
				}
				
			}
			roomOffsetX = 0;
			roomOffsetY += (roomHeight * Game.SPRITESIZE + roomHeight * TILEGAP) + ROOMGAP;
		}
		
		if(Game.PRINT_WORLD_CREATION_START_END) System.out.println("World created! (Tile count: " + tiles.size() + ")");
	}
	
	
	// TODO: refactor this spaghetti method... (._.)
	private Room CreateRoom(int roomOffsetX, int roomOffsetY, int roomStartX, int roomStartY) {
		
		List<Tile> roomTiles = new ArrayList<Tile>();
		
		int offsetY = 0;
		int offsetX = 0;
		
		boolean horizontalDoor = false;
		boolean verticalDoor = false;
		
		for(int y = 0; y < roomHeight; y++) {
			for (int x = 0; x < roomWidth; x++) {
				
				Tile tile = null;
				
				Coordinate tilePos = new Coordinate(x + (roomStartX * roomWidth), y + (roomStartY * roomHeight));
				Coordinate worldPos = new Coordinate(x * Game.SPRITESIZE + offsetX + roomOffsetX, y * Game.SPRITESIZE + offsetY + roomOffsetY);
				
				if(GetTileAtPosition(tilePos) != null) {
					System.out.println("TILE ALREADY EXISTS ON THIS COORDINATE: " + tilePos.getX() + ", " + tilePos.getY());
					new Exception().printStackTrace();
					System.exit(1);
				}
				
				// OUTERWALL CALCULATIONS
				if (y == 0) { // TOP TILES
						
					// ----- creates horizontal doors between rooms
					if(roomStartY > 0 && (x != 0 && x != roomWidth - 1)) {
						if(horizontalDoor == false) {
							
							// force create door on the last tile that can be a door
							// if a door has not yet been created
							if(x == roomWidth - 2) {
								
								tile = new Door(worldPos, tilePos, SpriteType.Door01, TileType.Door, false);
								horizontalDoor = true;
								
							} else {
								
								// randomize the location of a door
								if(Util.GetRandomInteger() > 80) {
									
									// randomize if the door is locked
									if(Util.GetRandomInteger() > 75) {
										tile = new Door(worldPos, tilePos, SpriteType.LockedDoor01, TileType.LockedDoor, true);
									} else {
										tile = new Door(worldPos, tilePos, SpriteType.Door01, TileType.Door, false);
									}
									
									horizontalDoor = true;
									
								} else {
									tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
								}
								
							}
							
						} else {
							// door is already created for this set of walls
							tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						}
					} else {
						// corner pieces cant be doors.
						tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
					}
					// ------
					
				} else if(y == roomHeight - 1) { // BOTTOM TILES
					
					if(roomStartX == worldWidth - 1 && roomStartY == worldWidth - 1) { // BOTTOM RIGHT ROOM
						
						tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						
					} else if(roomStartX == worldWidth - 1) { // RIGHTMOST ROOM
						
						if(x == 0 || x == roomWidth - 1) {
							tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						} else {
							tile = RandomizeTileFloorWall(worldPos, tilePos);
						}
						
					} else if(roomStartY == worldHeight - 1) { // BOTTOM-MOST ROOM
						
						tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						
					} else { // OTHER ROOM
						
						if(x == 0) {
							tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						} else {
							tile = RandomizeTileFloorWall(worldPos, tilePos);
						}
						
					}
					
				} else if(x == 0) { // LEFT TILES
					
					// ----- creates vertical doors between rooms
					if(roomStartX > 0 && (y != 0 && y != roomHeight - 1)) {
						if(verticalDoor == false) {
							
							// force create door on the last tile that can be a door
							// if a door has not yet been created
							if(y == roomHeight - 2) {
								
								tile = new Door(worldPos, tilePos, SpriteType.Door01, TileType.Door, false);
								verticalDoor = true;
								
							} else {
								
								// randomize the location of a door
								if(Util.GetRandomInteger() > 80) {
									
									// randomize if the door is locked
									if(Util.GetRandomInteger() > 50) {
										tile = new Door(worldPos, tilePos, SpriteType.LockedDoor01, TileType.LockedDoor, true);
									} else {
										tile = new Door(worldPos, tilePos, SpriteType.Door01, TileType.Door, false);
									}
									
									verticalDoor = true;
									
								} else {
									tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
								}
							}
							
						} else {
							// one door is already created on this set of tiles.
							tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						}
					} else {
						// corner pieces cant be doors.
						tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
					}
					// ------
					
				} else if(x == roomWidth - 1) { // RIGHT TILES
					
					if(roomStartX == worldWidth - 1 && roomStartY == worldWidth - 1) { // BOTTOM RIGHT ROOM
						
						tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						
					} else if(roomStartX == worldWidth - 1) { // RIGHTMOST ROOM
						
						tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						
					} else if(roomStartY == worldHeight - 1) { // BOTTOM-MOST ROOM
						
						tile = RandomizeTileFloorWall(worldPos, tilePos);
						
					} else { // OTHER ROOM
						
						if(y == 0) {
							tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
						} else {
							tile = RandomizeTileFloorWall(worldPos, tilePos);
						}
						
					}
				
				// OTHER TILE TYPE CALCULATIONS
				} else tile = RandomizeTileFloorWall(worldPos, tilePos);
				
				if(tile == null) {
					System.out.print("WORLD.CREATEROOM: TILE IS NULL! \n");
					new Exception().printStackTrace();
					System.exit(1);
				}
				
				// add to tiles list
				roomTiles.add(tile);
				tiles.add(tile);
				
				offsetX += TILEGAP;
				
			}
			offsetX = 0;
			offsetY += TILEGAP;
		}
		
		CreateDoorsInsideRoom(roomTiles);
		CreateTrapsInsideRoom(roomTiles);
		CreateTurretsInsideRoom(roomTiles);
		CreateDestructibleWallsInsideRoom(roomTiles);
		CreateDestructibleItemsInsideRoom(roomTiles);
		CreateVanityItemsInsideRoom(roomTiles);
		
		// create enemies
		ActorManager.CreateEnemies(5);
		
		// create room object
		Room room = new Room(roomWidth, roomHeight, new Coordinate(roomStartX, roomStartY), roomTiles);
		
		return room;
	}
	
	// randomly creates either floor or wall tile.
	public Tile RandomizeTileFloorWall(Coordinate worldPos, Coordinate tilePos) {
		Tile tile = null;
		if(Util.GetRandomInteger() > 20) tile = new Tile(worldPos, tilePos, SpriteType.FloorTile01, TileType.Floor);
		else tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.Wall);
		return tile;
	}
	
	private Tile CreatePredefinedMap(char[][] map) {
		
		int offsetY = 0;
		int offsetX = 0;
		
		Tile spawnTile = null;
		
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[y].length; x++) {
				
				Tile tile = null;
				
				Coordinate tilePos = new Coordinate(x + roomWidth, y + roomHeight);
				Coordinate worldPos = new Coordinate(x * Game.SPRITESIZE + offsetX, y * Game.SPRITESIZE + offsetY);
				
				// read map and create a tile
				char mapChar = map[y][x];
				
				switch(mapChar) {
				case '#':
					tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall);
					break;
				case '.':
					tile = new Tile(worldPos, tilePos, SpriteType.FloorTile01, TileType.Floor);
					break;
				case '@':
					tile = new Tile(worldPos, tilePos, SpriteType.FloorTile01, TileType.Floor);
					spawnTile = tile;
					break;
				case 'W':
					tile = new Tile(worldPos, tilePos, SpriteType.Wall01, TileType.Wall);
					break;
				case 'D':
					tile = new Door(worldPos, tilePos, SpriteType.Door01, TileType.Door, false);
					break;
				case 'L':
					tile = new Door(worldPos, tilePos, SpriteType.LockedDoor01, TileType.LockedDoor, true);
					break;
				case 'P':
					tile = new Portal(worldPos, tilePos, SpriteType.Portal01, TileType.Portal);
					break;
				default:
					System.out.println("INVALID CHARACTER AT CreatePredefinedMap.");
					new Exception().printStackTrace();
					System.exit(1);
					break;
				}
				
				tiles.add(tile);
				
				offsetX += TILEGAP;
			}
			offsetX = 0;
			offsetY += TILEGAP;
		}
		return spawnTile;
	}
	
	public void Remove() {
		
		List<Tile> temp = new ArrayList<Tile>(tiles);
		
		for(Tile tile : temp) {
			tile.Remove();
		}
		
		World.instance = null;
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
