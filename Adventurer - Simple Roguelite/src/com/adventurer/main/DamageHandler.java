package com.adventurer.main;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.DestructibleItem;
import com.adventurer.gameobjects.Tile;

public class DamageHandler {

	public static void ActorTakeDamage(Actor actor, int damage) { actor.GetHealth().TakeDamage(damage); }
	public static void ActorTakeDamage(Tile tile, int damage) { ActorTakeDamage(tile.GetActor(), damage); }
	
	public static void ItemTakeDamage(Tile tile, int damage) { ItemTakeDamage((DestructibleItem)tile.GetItem(), damage); }
	public static void ItemTakeDamage(DestructibleItem item, int damage) { item.getHealth().TakeDamage(damage); }
}
