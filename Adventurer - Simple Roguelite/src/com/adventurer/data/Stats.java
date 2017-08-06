package com.adventurer.data;

import com.adventurer.main.Game;

public class Stats {
	
	private int strength, intelligence, dexterity, vitality;
	private int addedStr, addedInt, addedDex, addedVit;
	
	public Stats() {
		this.strength     = Game.DEFAULT_STRENGTH_PLAYER;
		this.intelligence = Game.DEFAULT_INTELLIGENCE_PLAYER;
		this.dexterity 	  = Game.DEFAULT_DEXTERITY_PLAYER;
		this.vitality     = Game.DEFAULT_VITALITY_PLAYER;
	}
	
	public Stats(int str_, int int_, int dex_, int vit_) {
		this.strength     = str_;
		this.intelligence = int_;
		this.dexterity    = dex_;
		this.vitality     = vit_;
	}
	
	public int getSumStr() { return strength + addedStr; }
	public int getSumInt() { return intelligence + addedInt; }
	public int getSumDex() { return dexterity + addedDex; }
	public int getSumVit() { return vitality + addedVit; }
	
	public void setAddedStr(int a) { this.addedStr = a; }
	public void setAddedInt(int a) { this.addedInt = a; }
	public void setAddedDex(int a) { this.addedDex = a; }
	public void setAddedVit(int a) { this.addedVit = a; }
	
	public void addAddedStr(int a) { this.addedStr += a; }
	public void addAddedInt(int a) { this.addedInt += a; }
	public void addAddedDex(int a) { this.addedDex += a; }
	public void addAddedVit(int a) { this.addedVit += a; }
	
	public int getBaseStrength() { return strength; }
	public void setBaseStrength(int strength) { this.strength = strength; }
	public void addBaseStrength(int a) { this.strength += a; }
	
	public int getBaseIntelligence() { return intelligence; }
	public void setBaseIntelligence(int intelligence) { this.intelligence = intelligence; }
	public void addBaseIntelligence(int a ) { this.intelligence += a; }
	
	public int getBaseDexterity() { return dexterity; }
	public void setBaseDexterity(int dexterity) { this.dexterity = dexterity; }
	public void addBaseDexterity(int a) { this.dexterity += a; }

	public int getBaseVitality() { return vitality; }
	public void setBaseVitality(int vitality) { this.vitality = vitality; }
	public void addBaseVitality(int a) { this.vitality += a; }
}
