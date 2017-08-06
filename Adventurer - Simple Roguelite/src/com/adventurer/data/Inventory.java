package com.adventurer.data;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.enumerations.BombType;
import com.adventurer.enumerations.KeyType;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Gold;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Key;
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
		
		/*for(int i = 0; i < Game.START_DIAMOND_KEY_COUNT; i++) {
			
		}*/
		
		/*for(int i = 0; i < Game.START_BOMB_COUNT; i++) {
			this.addToInventory(ItemCreator.createBomb(actor.getCurrentTile(), BombType.Normal));
		}
		
		for(int i = 0; i < Game.START_PROJECTILE_COUNT; i++) {
			//this.addToInventory(ItemCreator.createProjectile());
		}*/
		
	}
	
	public void addToInventory(Item item) {
		
		// stack gold.
		if(item instanceof Gold) {
			Gold currentGold = this.getGold();
			if(currentGold != null) {
				currentGold.addAmount(((Gold)item).getAmount());
				return;
			}
		}
		
		if(this.inventory.size() < this.maxInventorySpace) this.inventory.add(item);
		else System.out.println("Inventory is full!");
	}
	
	public Gold getGold() {
		Gold gold = null;
		for(Item item : inventory) {
			if(item instanceof Gold) {
				gold = (Gold) item;
				break;
			}
		}
		return gold;
	}
	
	public Key getKey(KeyType keyType) {
		Key key = null;
		for(Item item : inventory) {
			if(item instanceof Key && ((Key)item).getKeyType() == keyType) {
				key = (Key) item;
				break;
			}
		}
		return key;
	}
	
	public void removeItemFromInventory(Item item) { this.inventory.remove(item); }
	
	public boolean isFull() { if(this.inventory.size() >= this.maxInventorySpace) return true; else return false; }
	public int getMaxSize() { return this.maxInventorySpace; }
	public List<Item> getInventoryItems() { return this.inventory; }
}
