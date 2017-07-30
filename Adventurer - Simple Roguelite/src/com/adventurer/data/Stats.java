package com.adventurer.data;

import com.adventurer.main.Game;

public class Stats {
	
	private int strength, intelligence, dexterity, vitality;
	
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
	
	public int getStrength() { return strength; }
	public void setStrength(int strength) { this.strength = strength; }
	public void addStrength(int a) { this.strength += a; }
	
	public int getIntelligence() { return intelligence; }
	public void setIntelligence(int intelligence) { this.intelligence = intelligence; }
	public void addIntelligence(int a ) { this.intelligence += a; }
	
	public int getDexterity() { return dexterity; }
	public void setDexterity(int dexterity) { this.dexterity = dexterity; }
	public void addDexterity(int a) { this.dexterity += a; }

	public int getVitality() { return vitality; }
	public void setVitality(int vitality) { this.vitality = vitality; }
	public void addVitality(int a) { this.vitality += a; }
}
