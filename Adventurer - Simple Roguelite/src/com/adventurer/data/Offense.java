package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.adventurer.enumerations.DamageType;

public class Offense {

	private Map<DamageType, Integer> meleeDmg;
	private Map<DamageType, Integer> magicDmg;
	private Map<DamageType, Integer> rangedDmg;
	
	public Offense() {
		this.meleeDmg = new LinkedHashMap<DamageType, Integer>();
		this.magicDmg = new LinkedHashMap<DamageType, Integer>();
		this.rangedDmg = new LinkedHashMap<DamageType, Integer>();
	}
	
	public Offense(int melee, int magic, int ranged) {
		meleeDmg = new LinkedHashMap<DamageType, Integer>();
		magicDmg = new LinkedHashMap<DamageType, Integer>();
		rangedDmg = new LinkedHashMap<DamageType, Integer>();
		
		// populate
		meleeDmg.put(DamageType.Physical, melee);
		//magicDmg.put(DamageType.Physical, magic);
		//rangedDmg.put(DamageType.Physical, ranged);
	}

	public int getMagicDmgOfType(DamageType type) {
		int dmg = 0;
		if(magicDmg.containsKey(type)) dmg = magicDmg.get(type);
		return dmg;
	}
	
	public int getRangedDmgOfType(DamageType type) {
		int dmg = 0;
		if(rangedDmg.containsKey(type)) dmg = rangedDmg.get(type);
		return dmg;
	}
	
	public int getMeleeDmgOfType(DamageType type) {
		int dmg = 0;
		if(meleeDmg.containsKey(type)) dmg = meleeDmg.get(type);
		return dmg;
	}
	
	public void setMeleeDmgOfType(DamageType type, int dmg) {
		//if(meleeDmg.containsKey(type)) meleeDmg.put(type, meleeDmg.get(type) + dmg);
		//else meleeDmg.put(type, dmg);
		meleeDmg.put(type, dmg);
	}
	
	public void setMagicDmgOfType(DamageType type, int dmg) {
		//if(magicDmg.containsKey(type)) magicDmg.put(type, magicDmg.get(type) + dmg);
		//else magicDmg.put(type, dmg);
		magicDmg.put(type, dmg);
	}
	
	public void setRangedDmgOfType(DamageType type, int dmg) {
		//if(rangedDmg.containsKey(type)) rangedDmg.put(type, rangedDmg.get(type) + dmg);
		//else rangedDmg.put(type, dmg);
		rangedDmg.put(type, dmg);
	}
	
	public Map<DamageType, Integer> getAllMeleeDamageTypes() { return meleeDmg; }
	public Map<DamageType, Integer> getAllMagicDamageTypes() { return magicDmg; }
	public Map<DamageType, Integer> getAllRangedDamageTypes() { return rangedDmg; }
}
