package com.adventurer.main;

public class Inventory {

	private int keyCount = 0;
	private int bombCount = 0;
	private int projectileCount = 0;
	
	public Inventory(int keys, int bombs, int projectiles) {
		this.setKeyCount(keys);
		this.setBombCount(bombs);
		this.setProjectileCount(projectiles);
	}
	
	public void addKeys(int a) {
		this.keyCount += a;
	}
	
	public void addBombs(int b) {
		this.bombCount += b;
	}
	
	public void addProjectiles(int p) {
		this.projectileCount += p;
	}
	
	public int getKeyCount() {
		return keyCount;
	}
	
	public void setKeyCount(int keyCount) {
		this.keyCount = keyCount;
	}
	
	public int getBombCount() {
		return bombCount;
	}
	
	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}
	
	public int getProjectileCount() {
		return projectileCount;
	}
	
	public void setProjectileCount(int projectileCount) {
		this.projectileCount = projectileCount;
	}
}
