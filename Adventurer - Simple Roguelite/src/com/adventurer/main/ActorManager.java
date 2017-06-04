package com.adventurer.main;

import com.adventurer.gameobjects.*;

public class ActorManager {

	private static Player playerInstance = null;
	private static Enemy[] enemyInstances = null;
	
	public static void ActorTakeDamage(Actor actor, int damage) {
		ActorTakeDamage(World.instance.GetTileAtPosition(actor.GetTilePosition()), damage);
	}
	
	public static void ActorTakeDamage(Tile tile, int damage) {
		
		// TODO: check for null actor here?
		
		// damage
		tile.GetActor().GetHealth().TakeDamage(damage);
		
		// effects
		if(tile.GetDiscovered() && tile.isHidden() == false) EffectCreator.CreateHitEffect(tile);
		
		// creates blood 
		// TODO: enemy types 
		VanityItemCreator.CreateSmallBlood(tile);
	}
	
	public static Player CreatePlayerInstance(int maxHP, int damage) {
		
		if(playerInstance != null) {
			System.out.println("Player is already instantiated!");
			return null;
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
	
	public static void CreateEnemies(int count) {
		for(int i = 0; i < count; i++) {
			EnemyType randomType = EnemyType.values()[Util.GetRandomInteger(0, EnemyType.values().length)];
			CreateEnemy(300, 100, randomType);
		}
	}
	
	public static Enemy CreateEnemy(int maxHP, int damage, EnemyType enemyType) {
		
		// get position
		int[] pos = World.instance.GetFreePosition();
		
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
		return new Enemy(enemyWorldPos, enemyTilePos, enemyType, spriteType, 300, 100);
	}
	
	public static Enemy[] GetEnemyInstances() {
		return enemyInstances;
	}
	
	public static Player GetPlayerInstance() {
		return playerInstance;
	}
	
}
