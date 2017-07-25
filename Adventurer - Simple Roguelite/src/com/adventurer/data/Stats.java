package com.adventurer.data;

public class Stats {
	
	private int strength, intelligence, dexterity;
	
	// TODO: set default values from some constant?
	public Stats() {
		this.strength = 5;
		this.intelligence = 5;
		this.dexterity = 5;
	}
	
	public Stats(int str_, int int_, int dex_) {
		this.strength = str_;
		this.intelligence = int_;
		this.dexterity = dex_;
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
}
