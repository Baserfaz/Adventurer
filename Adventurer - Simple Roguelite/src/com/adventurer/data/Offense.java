package com.adventurer.data;

public class Offense {

	private int meleeDmg;
	private int magicDmg;
	private int rangedDmg;
	
	public Offense() {
		this.meleeDmg = 0;
		this.magicDmg = 0;
		this.rangedDmg = 0;
	}
	
	public Offense(int melee, int magic, int ranged) {
		this.meleeDmg = melee;
		this.magicDmg = magic;
		this.rangedDmg = ranged;
	}

	public int getMeleeDmg() { return meleeDmg; }
	public void setMeleeDmg(int meleeDmg) { this.meleeDmg = meleeDmg;}
	public int getMagicDmg() { return magicDmg; }
	public void setMagicDmg(int magicDmg) { this.magicDmg = magicDmg; }
	public int getRangedDmg() { return rangedDmg; }
	public void setRangedDmg(int rangedDmg) { this.rangedDmg = rangedDmg; }
}
