package com.adventurer.data;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.gameobjects.Item;
import com.adventurer.main.Game;

public class Inventory {

	private int keyCount, diamondKeyCount, bombCount, projectileCount, maxInventorySpace;
	private List<Item> inventory = new ArrayList<Item>();
	
	public Inventory(int keys, int bombs, int projectiles) {
		this.setKeyCount(keys);
		this.setDiamondKeyCount(Game.instance.getCurrentSaveFile().getDiamondKeyCount()); 
		this.setBombCount(bombs);
		this.setProjectileCount(projectiles);
		
		this.maxInventorySpace = Game.DEFAULT_INVENTORY_MAX_SIZE;
	}
	
	public void addToInventory(Item item) {
		if(this.inventory.size() < this.maxInventorySpace) this.inventory.add(item);
		else System.out.println("Inventory is full!");
	}
	
	public void removeItemFromInventory(Item item) { this.inventory.remove(item); }
	
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
	
	public int getMaxSize() { return this.maxInventorySpace; }
	public List<Item> getInventoryItems() { return this.inventory; }
	public int getKeyCount() { return keyCount; }
	public int getBombCount() { return bombCount; }
	public int getProjectileCount() { return projectileCount; }
	public int getDiamondKeyCount() { return diamondKeyCount; }
}
