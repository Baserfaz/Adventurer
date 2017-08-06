package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.adventurer.enumerations.DamageType;

public class Offense {

	private Map<DamageType, Integer> meleeDmg;
	private int magicDmg;
	private int rangedDmg;
	
	public Offense() {
		meleeDmg = new LinkedHashMap<DamageType, Integer>();
		this.magicDmg = 0;
		this.rangedDmg = 0;
	}
	
	public Offense(int melee, int magic, int ranged) {
		meleeDmg = new LinkedHashMap<DamageType, Integer>();
		
		// populate melee dmg
		meleeDmg.put(DamageType.Physical, melee);
		
		this.magicDmg = magic;
		this.rangedDmg = ranged;
	}

	public int getMeleeDmgOfType(DamageType type) {
		int dmg = 0;
		if(meleeDmg.containsKey(type)) dmg = meleeDmg.get(type);
		return dmg;
	}
	
	public void setMeleeDmgOfType(DamageType type, int dmg) {
		if(meleeDmg.containsKey(type)) meleeDmg.put(type, meleeDmg.get(type) + dmg);
		else meleeDmg.put(type, dmg);
	}
	
	public Map<DamageType, Integer> getAllMeleeDamageTypes() { return meleeDmg; }
	
	public int getMagicDmg() { return magicDmg; }
	public void setMagicDmg(int magicDmg) { this.magicDmg = magicDmg; }
	
	public int getRangedDmg() { return rangedDmg; }
	public void setRangedDmg(int rangedDmg) { this.rangedDmg = rangedDmg; }
}
