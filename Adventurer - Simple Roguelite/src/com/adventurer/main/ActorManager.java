package com.adventurer.main;

import java.util.List;

import com.adventurer.gameobjects.*;

public class ActorManager {

	private static Player playerInstance = null;
	private static Enemy[] enemyInstances = null;
	
	public static Player CreatePlayerInstance(int maxHp, int damage, Tile tile) {
		
		if(playerInstance != null) {
			
			// TODO: move the player to a position or delete?
			
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
		actor.forceMove(tile.GetWorldPosition(), tile.GetTilePosition());
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
			int damage = 100;
			int health = 300;
			
			// TODO: enemy type specific damage and health
			
			CreateEnemy(health, damage, randomType, tiles);
		}
	}
	
	public static Enemy CreateEnemy(int maxHP, int damage, EnemyType enemyType, List<Tile> roomTiles) {
		
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
		return new Enemy(enemyWorldPos, enemyTilePos, enemyType, spriteType, maxHP, damage);
	}
	
	public static Enemy[] GetEnemyInstances() {
		return enemyInstances;
	}
	
	public static Player GetPlayerInstance() {
		return playerInstance;
	}
	
}
