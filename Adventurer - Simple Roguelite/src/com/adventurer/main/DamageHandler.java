package com.adventurer.main;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Tile;

public class DamageHandler {

	public static void ActorTakeDamage(Tile tile, int damage) { ActorTakeDamage(tile.GetActor(), damage); }
	public static void ActorTakeDamage(Actor actor, int damage) { 
		
		// do damage
		actor.getHealth().TakeDamage(damage); 
		
		// play effects
		
		
		
	}
	
}
