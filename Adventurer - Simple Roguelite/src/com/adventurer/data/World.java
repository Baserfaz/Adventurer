package com.adventurer.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.DoorType;
import com.adventurer.enumerations.RoomType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.enumerations.TrapType;
import com.adventurer.gameobjects.DestructibleTile;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.Portal;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Trap;
import com.adventurer.main.ActorManager;
import com.adventurer.main.Game;
import com.adventurer.main.SpriteCreator;
import com.adventurer.utilities.DungeonGeneration;
import com.adventurer.utilities.Util;

public class World {

	private int worldHeight;
	private int worldWidth;
	
	private List<Tile> tiles;
	private List<Room> rooms;
	
	public static World instance;
	
	// creates a predefined map
	public World(char[][] map) {
		
		if(ActorManager.GetPlayerInstance() != null) {
			ActorManager.GetPlayerInstance().getLosManager().calculateLOS = false;
		}
		
		if(instance != null) {
			System.out.println("WORLD ALREADY EXISTS!");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		World.instance = this;
		
		this.tiles = new ArrayList<Tile>();
		
		Tile spawnTile = CreatePredefinedMap(map);
		
		if(ActorManager.GetPlayerInstance() == null) ActorManager.CreatePlayerInstance(300, 100, spawnTile);
		else ActorManager.ForceMoveActor(spawnTile, ActorManager.GetPlayerInstance());
		
		ActorManager.GetPlayerInstance().getLosManager().calculateLOS = true;
	}
	
	// picks a random room from predefined rooms that has a specific room type
	public World(RoomType roomType) {
		
		if(ActorManager.GetPlayerInstance() != null) {
			ActorManager.GetPlayerInstance().getLosManager().calculateLOS = false;
		}
		
		if(instance != null) {
			System.out.println("WORLD ALREADY EXISTS!");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		World.instance = this;
		
		this.tiles = new ArrayList<Tile>();
		
		Tile spawnTile = CreatePredefinedMap(PredefinedMaps.GetRandomRoomOfType(roomType));
		
		// randomize enemy count 
		int minEnemyCount = 0, maxEnemyCount = 1;
		switch(roomType) {
		case Large:
			minEnemyCount = 10;
			maxEnemyCount = 20;
			break;
		case Normal:
			minEnemyCount = 5;
			maxEnemyCount = 10;
			break;
		case Small:
			minEnemyCount = 2;
			maxEnemyCount = 5;
			break;
		case Treasure:
			minEnemyCount = 0;
			maxEnemyCount = 1;
			break;
		}
		
		// create enemies
		ActorManager.CreateEnemies(Util.GetRandomInteger(minEnemyCount, maxEnemyCount), tiles);
		
		// create player
		if(ActorManager.GetPlayerInstance() == null) ActorManager.CreatePlayerInstance(300, 100, spawnTile);
		else ActorManager.ForceMoveActor(spawnTile, ActorManager.GetPlayerInstance());
		
		ActorManager.GetPlayerInstance().getLosManager().calculateLOS = true;
	}
	
	// create a randomized dungeon level
	public World(int roomcount) {
		
		if(instance != null) {
			System.out.println("WORLD ALREADY EXISTS!");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		World.instance = this;
		
		this.tiles = DungeonGeneration.createDungeon(roomcount);
		
		if(ActorManager.GetPlayerInstance() == null) ActorManager.CreatePlayerInstance(300, 100);
	}
	
	private Tile CreatePredefinedMap(char[][] map) {
		
		int offsetY = 0;
		int offsetX = 0;
		
		Tile spawnTile = null;
		
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[y].length; x++) {
				
				Tile tile = null;
				
				Coordinate tilePos = new Coordinate(x, y);
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
				case 'd':
					tile = new Door(worldPos, tilePos, SpriteType.Door01, TileType.Door, false, DoorType.Normal);
					break;
				case 'L':
					tile = new Door(worldPos, tilePos, SpriteType.LockedDoor01, TileType.LockedDoor, true, DoorType.Normal);
					break;
				case 'P':
					tile = new Portal(worldPos, tilePos, SpriteType.Portal01, TileType.Portal, false);
					break;
				case 'E':
					tile = new Portal(worldPos, tilePos, SpriteType.Portal02, TileType.Portal, true);
					break;
				case 'D':
					tile = new Door(worldPos, tilePos, SpriteType.LockedDoorDiamond01, TileType.LockedDoor, true, DoorType.Diamond);
					break;
				default:
					System.out.println("INVALID CHARACTER AT CreatePredefinedMap.");
					new Exception().printStackTrace();
					System.exit(1);
					break;
				}
				
				tiles.add(tile);
				
				offsetX += Game.TILEGAP;
			}
			offsetX = 0;
			offsetY += Game.TILEGAP;
		}
		return spawnTile;
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
	
	public int[] GetFreePosition(List<Tile> _tiles) {
		
		List<Tile> possibleTiles = new ArrayList<Tile>();
		int[] position = new int[4];
		
		// get all possible tiles
		for(int i = 0; i < _tiles.size(); i++) {
			
			Tile tile = _tiles.get(i);
			
			if(tile.isWalkable() && tile.GetActor() == null && tile.GetItem() == null) {
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
	
	public Coordinate ConvertTilePositionToWorld(Coordinate pos) {
		return new Coordinate(pos.getX() * Game.SPRITESIZE + Game.TILEGAP * pos.getX(), 
				pos.getY() * Game.SPRITESIZE + Game.TILEGAP * pos.getY());
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
			newTile = new Door(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.Door01, TileType.Door, false, DoorType.Normal);
		} else if(newType == TileType.LockedDoor) {
			newTile = new Door(old.GetWorldPosition(), old.GetTilePosition(), SpriteType.LockedDoor01, TileType.Door, true, DoorType.Normal);
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
	// doesnt remove tiles.
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
	
	public int distanceBetweenTiles(Tile start, Tile goal) {
		int dist_x = Math.abs(start.GetTilePosition().getX() - goal.GetTilePosition().getX());
		int dist_y = Math.abs(start.GetTilePosition().getY() - goal.GetTilePosition().getY());
		int dist = dist_x + dist_y;
		return dist;
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
	
	public void Remove() {
		List<Tile> temp = new ArrayList<Tile>(tiles);
		for(Tile tile : temp) { tile.Remove(); }
		World.instance = null;
	}

	public List<Tile> GetTilesInCardinalDirection(Tile tile) {
		return GetTilesInCardinalDirection(tile.GetTilePosition().getX(), tile.GetTilePosition().getY());
	}
	
	public List<Tile> GetTilesInCardinalDirection(Coordinate pos) {
		return GetTilesInCardinalDirection(pos.getX(), pos.getY());
	}
	
	public void addToRooms(Room room) { this.rooms.add(room); }
	public void AddToTiles(Tile t) { this.tiles.add(t); }
	public void RemoveTiles(Tile t) { if(tiles.contains(t)) this.tiles.remove(t); }
	public List<Tile> GetTiles() { return this.tiles; }
	public World GetWorld() { return this; }
	public int GetHeight() { return this.worldHeight; }
	public int GetWidth() { return this.worldWidth; }
}
