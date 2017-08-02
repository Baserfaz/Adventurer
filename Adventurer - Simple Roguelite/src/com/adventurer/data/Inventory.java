package com.adventurer.data;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.enumerations.KeyType;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Item;
import com.adventurer.main.Game;
import com.adventurer.main.ItemCreator;

public class Inventory {

	private int maxInventorySpace;
	private List<Item> inventory = new ArrayList<Item>();
	
	public Inventory(Actor actor) {
		this.maxInventorySpace = Game.DEFAULT_INVENTORY_MAX_SIZE;
		
		// populate inventory with keys etc.
		for(int i = 0; i < Game.START_KEY_COUNT; i++) {
			this.addToInventory(ItemCreator.createKey(actor.getCurrentTile(), KeyType.Normal));
		}
	}
	
	public void addToInventory(Item item) {
		if(this.inventory.size() < this.maxInventorySpace) this.inventory.add(item);
		else System.out.println("Inventory is full!");
	}
	
	public void removeItemFromInventory(Item item) { this.inventory.remove(item); }
	
	public int getMaxSize() { return this.maxInventorySpace; }
	public List<Item> getInventoryItems() { return this.inventory; }
}
