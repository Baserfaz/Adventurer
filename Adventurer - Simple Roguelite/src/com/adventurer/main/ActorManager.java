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
	
	public static Player CreatePlayerInstance(int maxHp, int damage, Tile tile) {
		
		if(playerInstance != null) {
			System.out.println("Player is already instantiated!");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		// create player and add it to our handler.
		playerInstance = new Player(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Player, maxHp, damage);
		
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
	
	public static Player CreatePlayerInstance(int maxHP, int damage) {
		
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
		
		// create player and add it to our handler.
		playerInstance = new Player(playerWorldPos, playerTilePos, SpriteType.Player, maxHP, damage);
		
		return playerInstance;
	}
	
	public static void CreateEnemies(int count, List<Tile> tiles) {
		for(int i = 0; i < count; i++) {
			
			EnemyType randomType = EnemyType.values()[Util.GetRandomInteger(0, EnemyType.values().length)];
			
			int damage = 0;
			int health = 0;
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
				//else System.out.println("Not yet implemented: " + key);
				
			}
			CreateEnemy(name, health, damage, randomType, tiles, isRanged);
		}
	}
	
	public static Enemy CreateEnemy(String name, int maxHP, int damage,
		EnemyType enemyType, List<Tile> roomTiles, boolean isRanged) {
		
		// get position
		int[] pos = World.instance.GetFreePosition(roomTiles);
		
		SpriteType spriteType = null;
		
		switch(enemyType) {
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
		}
		
		Coordinate enemyWorldPos = new Coordinate(pos[0], pos[1]);
		Coordinate enemyTilePos = new Coordinate(pos[2], pos[3]);
		
		// create enemy object
		return new Enemy(enemyWorldPos, enemyTilePos, enemyType, spriteType, maxHP, damage, name, isRanged);
	}
	
	public static Enemy[] GetEnemyInstances() { return enemyInstances; }
	public static Player GetPlayerInstance() { return playerInstance; }
}
