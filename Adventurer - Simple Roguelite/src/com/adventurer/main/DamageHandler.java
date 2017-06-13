package com.adventurer.main;

import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.DestructibleItem;
import com.adventurer.gameobjects.Enemy;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Turret;

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
			EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 5), SpriteType.PlayerGib01);
			
		} else if(actor instanceof Enemy) {
			
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
			
			EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 5), spritetype);
		} else if(actor instanceof Turret) {
			
			EffectCreator.CreateGibs(tile, Util.GetRandomInteger(3, 5), SpriteType.PotGib01);
			
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
