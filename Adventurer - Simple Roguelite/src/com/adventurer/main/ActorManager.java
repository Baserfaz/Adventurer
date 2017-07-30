package com.adventurer.main;

import java.util.List;
import java.util.Map;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.EnemyType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.*;
import com.adventurer.utilities.FileReader;
import com.adventurer.utilities.Util;

public class ActorManager {

	private static Player playerInstance = null;
	private static Enemy[] enemyInstances = null;
	
	public static Player CreatePlayerInstance(Tile tile) {
		
		if(playerInstance != null) {
			System.out.println("Player is already instantiated!");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		// create player and add it to our handler.
		playerInstance = new Player(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Player);
		
		return playerInstance;
	}
	
	public static void RemovePlayer() {
		if(playerInstance != null) {
			playerInstance.Remove();
			playerInstance = null;
		}
	}
	
	public static void ForceMoveActor(Tile tile, Actor actor) {
		Handler.instance.RemoveObject(actor);
		actor.forceMove(tile.GetWorldPosition(), tile.GetTilePosition());
		Handler.instance.AddObject(actor);
	}
	
	public static void ForceMovePlayerToFreePosition() {
		World world = World.instance.GetWorld();
		
		// get position
		int[] pos = world.GetFreePosition();
		
		// create coordinates
		Coordinate playerWorldPos = new Coordinate(pos[0], pos[1]);
		Coordinate playerTilePos = new Coordinate(pos[2], pos[3]);
		
		playerInstance.forceMove(playerWorldPos, playerTilePos);
	}
	
	public static Player CreatePlayerInstance() {
		
		if(playerInstance != null) {
			System.out.println("Player is already instantiated!");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		World world = World.instance.GetWorld();
		
		// get position
		int[] pos = world.GetFreePosition();
		
		// create coordinates
		Coordinate playerWorldPos = new Coordinate(pos[0], pos[1]);
		Coordinate playerTilePos = new Coordinate(pos[2], pos[3]);
		
		// create player
		playerInstance = new Player(playerWorldPos, playerTilePos, SpriteType.Player);
		
		return playerInstance;
	}
	
	public static void CreateEnemies(int count, List<Tile> tiles) {

	    count = Math.min(count, Game.ROOM_MIN_HEIGHT * Game.ROOM_MIN_WIDHT);
		for(int i = 0; i < count; i++) {
		    
			EnemyType randomType = EnemyType.values()[Util.GetRandomInteger(0, EnemyType.values().length)];
			
	        int damage = 0, health = 0, movementSpeed = 0, movementCooldownBase = 0;
	        String name = "";
	        boolean isRanged = false;
	        
			// read enemy data
			Map<String, String> retval = FileReader.readXMLGameData(randomType.toString());
			for(Map.Entry<String, String> entry : retval.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue();
				
				if(key == "damage") damage = Integer.parseInt(val);
				else if(key == "health") health = Integer.parseInt(val);
				else if(key == "name") name = val;
				else if(key == "isRanged") isRanged = Boolean.parseBoolean(val);
				else if(key == "movementSpeed") movementSpeed = Integer.parseInt(val);
				else if(key == "movementCooldownBase") movementCooldownBase = Integer.parseInt(val);
			}
			CreateEnemy(name, health, damage, randomType, tiles, isRanged, movementSpeed, movementCooldownBase);
		}
	}
	
	public static Enemy CreateEnemy(String name, int maxHP, int damage,
		EnemyType enemyType, List<Tile> tiles_, boolean isRanged, int movementSpeed, int movementCooldownBase) {
	    
		// get position
		int[] pos = World.instance.GetFreePosition(tiles_);
		
		SpriteType spriteType = null;
		
		/*switch(enemyType) {
		case Skeleton:
			spriteType = SpriteType.Skeleton01;
			break;
		case Zombie:
			spriteType = SpriteType.Zombie01;
			break;
		case Maggot:
			spriteType = SpriteType.Maggot01;
			break;
		default:
			System.out.println("SPRITETYPE NOT FOUND FOR ENEMYTYPE: " + enemyType);
			break;
		}*/
		
		// TODO: switch cases for all different enemies.
		// ---> now every enemy is shown as generic.
		spriteType = SpriteType.GenericEnemy;
		
		Coordinate enemyWorldPos = new Coordinate(pos[0], pos[1]);
		Coordinate enemyTilePos = new Coordinate(pos[2], pos[3]);
		
		// create enemy object
		// TODO: refactor melee, ranged and magic damage...
		return new Enemy(enemyWorldPos, enemyTilePos, enemyType, spriteType, maxHP, damage, damage, damage, name, isRanged, movementSpeed, movementCooldownBase);
	}
	
	public static Enemy[] GetEnemyInstances() { return enemyInstances; }
	public static Player GetPlayerInstance() { return playerInstance; }
}
