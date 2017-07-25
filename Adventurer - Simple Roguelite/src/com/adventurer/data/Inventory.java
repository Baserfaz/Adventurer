package com.adventurer.data;

import com.adventurer.main.Game;

public class Inventory {

	private int keyCount, diamondKeyCount, bombCount, projectileCount;
	
	public Inventory(int keys, int bombs, int projectiles) {
		this.setKeyCount(keys);
		this.setDiamondKeyCount(Game.instance.getCurrentSaveFile().getDiamondKeyCount()); 
		this.setBombCount(bombs);
		this.setProjectileCount(projectiles);
	}
	
	// TODO: equipment?
	
	public void addDiamondKeyCount(int a) {
		this.diamondKeyCount += a;
		Game.instance.getCurrentSaveFile().addDiamondKeyCount(a);
	}
	
	public void addKeys(int a) { this.keyCount += a; }
	public void addBombs(int b) { this.bombCount += b; }
	public void addProjectiles(int p) { this.projectileCount += p; }
	
	public void setKeyCount(int keyCount) { this.keyCount = keyCount; }
	public void setBombCount(int bombCount) { this.bombCount = bombCount; }
	public void setProjectileCount(int projectileCount) { this.projectileCount = projectileCount; }
	public void setDiamondKeyCount(int diamondKeyCount) { this.diamondKeyCount = diamondKeyCount; }
	
	public int getKeyCount() { return keyCount; }
	public int getBombCount() { return bombCount; }
	public int getProjectileCount() { return projectileCount; }
	public int getDiamondKeyCount() { return diamondKeyCount; }
}
