package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.adventurer.enumerations.DamageType;

public class ItemBonus {

	private Map<DamageType, Integer> bonusDamage = null;
	private Map<DamageType, Integer> bonusResistance = null;
	
	private int strBonus, dexBonus, intBonus, vitBonus;
	
	public ItemBonus() {
		this.bonusDamage = new LinkedHashMap<DamageType, Integer>();
		this.bonusResistance = new LinkedHashMap<DamageType, Integer>();
		this.strBonus = 0;
		this.dexBonus = 0;
		this.intBonus = 0;
		this.vitBonus = 0;
	}

	public Map<DamageType, Integer> getBonusDamage() { return bonusDamage; }
	public void setBonusDamage(Map<DamageType, Integer> bonusDamage) { this.bonusDamage = bonusDamage; }
	public Map<DamageType, Integer> getBonusResistance() { return bonusResistance; }
	public void setBonusResistance(Map<DamageType, Integer> bonusResistance) { this.bonusResistance = bonusResistance; }
	public int getStrBonus() { return strBonus; }
	public void setStrBonus(int strBonus) { this.strBonus = strBonus; }
	public int getDexBonus() { return dexBonus; }
	public void setDexBonus(int dexBonus) { this.dexBonus = dexBonus; }
	public int getIntBonus() { return intBonus; }
	public void setIntBonus(int intBonus) { this.intBonus = intBonus;}
	public int getVitBonus() { return vitBonus; }
	public void setVitBonus(int vitBonus) { this.vitBonus = vitBonus; }
	
}
