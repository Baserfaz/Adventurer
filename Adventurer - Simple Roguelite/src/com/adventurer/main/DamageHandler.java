package com.adventurer.main;
import java.util.Map;
import java.util.Map.Entry;

import com.adventurer.data.Resistances;
import com.adventurer.enumerations.DamageType;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Tile;

public class DamageHandler {

	public static void ActorHeal(Actor actor, int amount) {
		actor.getHealth().healDamage(amount);
		EffectCreator.CreateHealEffect(actor.getCurrentTile());
	}
	
	public static void ActorTakeDamage(Tile tile, Map<DamageType, Integer> dmg) { ActorTakeDamage(tile.GetActor(), dmg); }
	
	public static void ActorTakeDamage(Actor actor, Map<DamageType, Integer> dmg) {
		
		// ------------------------------------------
		// calculates damage and considers resistances
		
		Resistances res = actor.getResistances();
		
		// calculate resistances - damage here
		for(Entry<DamageType, Integer> d : dmg.entrySet()) {
			
			DamageType key = d.getKey();
			int val = d.getValue();
			int calcdmg = 0;
			
			switch(key) {
				case Fire: calcdmg = val - res.getFireResistance(); break;
				case Frost: calcdmg = val - res.getFrostResistance(); break;
				case Holy: calcdmg = val - res.getHolyResistance(); break;
				case Physical: calcdmg = val - res.getPhysicalResistance(); break;
				case Shock: calcdmg = val - res.getShockResistance(); break;
				default: break;
			}
			
			// if the resistances absorb all damage
			// then do just one point of damage.
			// --> can kill everything in the game.
			if(calcdmg > 0) {
				if(actor.getHealth().isDead() == false) actor.getHealth().TakeDamage(calcdmg);
			} else {
				if(actor.getHealth().isDead() == false) actor.getHealth().TakeDamage(1);
			}
		}
		
		// TODO: every damagetype has own effect?
		EffectCreator.CreateHitEffect(actor.getCurrentTile());
	}
	
}
