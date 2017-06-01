package com.adventurer.main;

import com.adventurer.gameobjects.*;

public class ActorManager {

	private static Player playerInstance = null;
	private static Enemy[] enemyInstances = null;
	
	public static Player CreatePlayerInstance(int maxHP, int damage) {
		
		if(playerInstance != null) {
			System.out.println("Player is already instantiated!");
			return null;
		}
		
		World world = World.instance.GetWorld();
		
		// get position
		Coordinate[] pos = world.GetFreePosition();
		
		// create coordinates
		Coordinate playerWorldPos = pos[0];
		Coordinate playerTilePos = pos[1];
		
		// create player and add it to our handler.
		playerInstance = new Player(playerWorldPos, playerTilePos, SpriteType.Player, maxHP, damage);
		
		return playerInstance;
	}
	
	public static void CreateEnemies(int count) {
		for(int i = 0; i < count; i++) {
			EnemyType randomType = EnemyType.values()[Util.GetRandomInteger(0, EnemyType.values().length)];
			CreateEnemy(300, 100, randomType);
		}
	}
	
	public static Enemy CreateEnemy(int maxHP, int damage, EnemyType enemyType) {
		
		// get position
		Coordinate[] pos = World.instance.GetFreePosition();
		
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
		
		Coordinate enemyWorldPos = pos[0];
		Coordinate enemyTilePos = pos[1];
		
		// create enemy object
		return new Enemy(enemyWorldPos, enemyTilePos, enemyType, spriteType, 300, 100);
	}
	
	public static Enemy[] GetEnemyInstances() {
		return enemyInstances;
	}
	
	public static Player GetPlayerInstance() {
		return playerInstance;
	}
	
}
