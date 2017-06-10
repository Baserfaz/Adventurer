package com.adventurer.main;

import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.DestructibleItem;
import com.adventurer.gameobjects.Enemy;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Tile;

public class DamageHandler {

	public static void ActorTakeDamage(Actor actor, int damage) {
		
		Tile tile = World.instance.GetTileAtPosition(actor.GetTilePosition());
		
		// damage
		actor.GetHealth().TakeDamage(damage);
		
		// effects
		if(actor.isDiscovered()) EffectCreator.CreateStaticHitEffect(tile);
		
		// creates blood
		if(actor instanceof Player) {
			
			//VanityItemCreator.CreateSmallBlood(tile);
			EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 5), SpriteType.BloodGib01);
			
		} else {
			
			SpriteType spritetype = SpriteType.BloodGib01;
			
			switch(((Enemy)actor).getEnemyType()) {
			case Skeleton:
				spritetype = SpriteType.BoneGib01;
				break;
			case Maggot:
				spritetype = SpriteType.BloodGib01;
				break;
			case Zombie:
				spritetype = SpriteType.BloodGib01;
				break;
			default:
				System.out.println("ENEMYTYPE NOT YET IMPLEMENTED!");
				new Exception().printStackTrace();
				System.exit(1);
			}
			
			//VanityItemCreator.CreateVanityItem(tile, spritetype, true);
			EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 5), spritetype);
		}
	}
	
	public static void ActorTakeDamage(Tile tile, int damage) {
		ActorTakeDamage(tile.GetActor(), damage);
	}
	
	public static void ItemTakeDamage(DestructibleItem item, int damage) {
		
		Tile tile = World.instance.GetTileAtPosition(item.GetTilePosition());
		
		// damage
		item.getHealth().TakeDamage(damage);
		
		// effects
		if(item.isDiscovered()) EffectCreator.CreateStaticHitEffect(tile);
		
		// create gibs
		EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 7), SpriteType.PotGib01);
		
	}
	
	public static void ItemTakeDamage(Tile tile, int damage) {
		ItemTakeDamage((DestructibleItem)tile.GetItem(), damage);
	}
	
}
