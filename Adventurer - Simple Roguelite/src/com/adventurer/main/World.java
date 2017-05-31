package com.adventurer.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class World {

	private int height;
	private int width;
	private List<Tile> tiles;
	
	public static final int tileSize = 16;
	public static final int tileGap = 2;
	
	public int tileCount = 0;
	
	public static World instance;
	
	public World(int width, int height) {
		
		if(instance != null) return;
		
		World.instance = this;
		
		this.height = height;
		this.width = width;
		this.tiles = new ArrayList<Tile>();
		
		this.tileCount = width * height;
		
		CreateWorld();
	}
	
	public Tile GetTileAtPosition(Coordinate pos) {
		
		Tile retTile = null;
		
		int x = pos.getX();
		int y = pos.getY();
		
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
	
	public Tile[] GetSurroundingTiles(Coordinate pos) {
		return GetSurroundingTiles(pos.getX(), pos.getY());
	}
	
	public Tile[] GetSurroundingTiles(int posx, int posy) {
		
		Tile[] foundTiles = new Tile[9];
		
		int tileCounter = 0;
		
		for(int y = -1; y < 2; y++) {
			for(int x = -1; x < 2; x++) {
				foundTiles[tileCounter] = GetTileAtPosition(posx + x, posy + y);
				tileCounter++;
			}
		}
		return foundTiles;
	}
	
	public Tile GetTileFromDirection(Coordinate pos, Direction dir) {
		
		Tile tile = null;
		
		int x = pos.getX();
		int y = pos.getY();
		
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
		int random = ThreadLocalRandom.current().nextInt(0, possibleTiles.size());
		
		// get a random tile from possible tiles.
		Tile randomTile = possibleTiles.get(random);
		
		// get the position of random tile.
		Coordinate randTilePos = randomTile.GetTilePosition();
		
		// we have to change the tile position to 
		// world position.
		
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
		return (pos * tileSize + tileGap * pos);
	}
	
	// creates a new TILE and destroys the old one.
	public Tile ReplaceTile(Tile old, TileType newType, SpriteType newSprite) {
		
		Tile newTile = null;
		
		// create new tile
		if(newType == TileType.TrapTile) {
			
			newTile = (Trap) new Trap(
					old.GetWorldPosition(),
					SpriteType.TrapTile01,
					TileType.TrapTile, 
					old.GetTilePosition(),
					100);
			
		} else if(newType == TileType.Door) {
			
			newTile = (Door) new Door(
					old.GetWorldPosition(),
					SpriteType.Door01,
					TileType.Door, 
					old.GetTilePosition());
			
		} else if(newType == TileType.Floor) {
			
			newTile = (Tile) new Tile(
					old.GetWorldPosition(),
					SpriteType.FloorTile01,
					TileType.Floor, 
					old.GetTilePosition());
			
		} else {
			System.out.println("WORLD.REPLACETILE: TILETYPE NOT YET IMPLEMENTED!!");
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
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
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
		
		// create an array of random vanity item sprites
		SpriteType[] vanitySpriteTypes = { SpriteType.Pot01, SpriteType.PotRemains01 };
		
		for(Tile tile : floorTiles) {
			
			if(Util.GetRandomInteger() > 95) {
				
				// randomize spritetype
				SpriteType st = vanitySpriteTypes[Util.GetRandomInteger(0, vanitySpriteTypes.length)];
				
				// create vanity item
				VanityItemCreator.CreateVanityItem(tile, st);
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
	
	public void CreateDoors() {
		
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				// limit the door count
				if(Util.GetRandomInteger() < 50) continue;
				
				// get current tile
				Tile current = GetTileAtPosition(x, y);
				
				// tile should be floor.
				if(current.GetTileType() != TileType.Floor) continue;
				
				// get tile's cardinal direction tiles
				List<Tile> cardinalTiles = GetTilesInCardinalDirection(x, y);
				
				// save the direction data
				List<Direction> dirData = new ArrayList<Direction>();
				
				boolean valid = true;
				
				// get the direction data of wall tiles.
				for(int i = 0; i < cardinalTiles.size(); i++) {
					
					Tile t = cardinalTiles.get(i);
					
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
				
				// this spot is not valid, because there
				// are some tile(s) next to it that 
				// are not legitimate!
				// e.g. door or destructible object.
				if(valid == false) continue;
				
				// at this point we should have only 
				// two walls that we need to check.
				if(dirData.size() != 2) continue;
				Direction first_dir = dirData.get(0);
				Direction second_dir = dirData.get(1);
				
				switch(first_dir) {
				case North:
					if(second_dir == Direction.South) ReplaceTile(current, TileType.Door, SpriteType.Door01);
					break;
				case East:
					if(second_dir == Direction.West) ReplaceTile(current, TileType.Door, SpriteType.Door01);
					break;
				case South:
					if(second_dir == Direction.North) ReplaceTile(current, TileType.Door, SpriteType.Door01);
					break;
				case West:
					if(second_dir == Direction.East) ReplaceTile(current, TileType.Door, SpriteType.Door01);
					break;
				}
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
		
		//System.out.print("from: " + fromX + ", " + fromY + "; to: " + toX + ", " + toY + "; dir: " + dir + "\n");
		
		return dir;
	}
	
	private void CreateWorld() {
		
		int offsetY = 0;
		int offsetX = 0;
		
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				Tile tile = null;
				
				Coordinate tilePos = new Coordinate(x, y);
				Coordinate worldPos = new Coordinate(x * tileSize + offsetX, y * tileSize + offsetY);
				
				// create outer wall
				if(y == 0 || y == height - 1 || x == 0 || x == width - 1) {
							
					tile = new Tile(worldPos, SpriteType.Wall01, TileType.OuterWall, tilePos);
					
				} else {
					
					if(Util.GetRandomInteger() > 20) {
						
						// create floor 
						tile = new Tile(worldPos, SpriteType.FloorTile01, TileType.Floor, tilePos);
						
					} else {
						
						// create wall
						if(Util.GetRandomInteger() > 90) {
							
							// destructible wall
							tile = (DestructibleTile) new DestructibleTile(worldPos, 
									SpriteType.DestructibleWall, TileType.DestructibleObject, tilePos, 300);
							
						} else {
							
							// indestructible wall
							tile = new Tile(worldPos, SpriteType.Wall01, TileType.Wall, tilePos);
							
						}
					}
				}
				
				// add to tiles list
				tiles.add(tile);
				
				offsetX += tileGap;
				
			}
			offsetX = 0;
			offsetY += tileGap;
		}
		
		CreateDoors();
		CreateTraps();
		CreateVanityItems();
	}
	
	public int GetHeight() {
		return this.height;
	}
	
	public int GetWidth() {
		return this.width;
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
}
