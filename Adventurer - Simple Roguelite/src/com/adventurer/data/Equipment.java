package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.gameobjects.Armor;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Weapon;
import com.adventurer.main.ActorManager;

public class Equipment {

	private Item mainHand, offHand, head, chest, legs, feet, amulet, ring;

	public Equipment() {
		this.mainHand = null;
		this.offHand = null;
		this.head = null;
		this.chest = null;
		this.legs = null;
		this.feet = null;
		this.amulet = null;
		this.ring = null;
	}
	
	public Equipment(Item mh, Item of, Item head, Item chest, Item legs, Item feet, Item amulet, Item ring) {
		this.mainHand = mh;
		this.offHand = of;
		this.head = head;
		this.chest = chest;
		this.legs = legs;
		this.feet = feet;
		this.amulet = amulet;
		this.ring = ring;
	}
	
	public Map<String, Item> getAllEquipment() {
		
		Map<String, Item> eq = new LinkedHashMap<String, Item>();
		
		eq.put("MainHand", mainHand);
		eq.put("OffHand", offHand);
		eq.put("Head", head);
		eq.put("Chest", chest);
		eq.put("Legs", legs);
		eq.put("Feet", feet);
		eq.put("Amulet", amulet);
		eq.put("Ring", ring);
		
		return eq;
	}
	
	public void equipItem(Item item) {
		
		ArmorSlot armorSlot = null;
		
		if(item instanceof Armor) {
			
			Armor armor = (Armor) item;
			armorSlot = armor.getSlot();
			
			// drop or inventory current item 
			// and equip the new one.
			this.unequipSlot(armorSlot);
			
			// equip item
			switch(armorSlot) {
				case Amulet: this.setAmulet(armor); break;
				case Chest: this.setChest(armor); break;
				case Feet: this.setFeet(armor); break;
				case Head: this.setHead(armor); break;
				case Legs: this.setLegs(armor); break;
				case Ring: this.setRing(armor); break;
				default: break;
			}
			
			// remove item from inventory.
			ActorManager.GetPlayerInstance().getInventory().removeItemFromInventory(armor);
			
		} else if(item instanceof Weapon) {
			
			
			
			
		} else {
			
			System.out.println("Item cannot be equipped!");
			
		}
		
		// TODO: update stats!
		
	}
	
	public void unequipSlot(ArmorSlot slot) {
		Item item = null;
		switch(slot) {
			case Head: item = this.getHead(); this.setHead(null); break;
			case Chest: item = this.getChest(); this.setChest(null); break;
			case Legs: item = this.getLegs(); this.setLegs(null); break;
			case Feet: item = this.getFeet(); this.setFeet(null); break;
			case Amulet: item = this.getAmulet(); this.setAmulet(null); break;
			case Ring: item = this.getRing(); this.setRing(null); break;
			default: break;
		}
		
		// move the item to inventory
		if(item != null) {
			
			Player player = ActorManager.GetPlayerInstance();
			Inventory inv = player.getInventory();
			
			if(inv.isFull()) {
				
				// inventory is full 
				// -> drop the item on the ground.
				player.dropItem(item);
				
			} else {
				
				// put item into inventory.
				inv.addToInventory(item);
			}
		}
	}
	
	public Item getMainHand() { return mainHand; }
	private void setMainHand(Item mainHand) { this.mainHand = mainHand; }
	public Item getOffHand() { return offHand; }
	private void setOffHand(Item offHand) { this.offHand = offHand; }
	public Item getHead() { return head; }
	private void setHead(Item head) { this.head = head; }
	public Item getChest() { return chest; }
	private void setChest(Item chest) { this.chest = chest; }
	public Item getLegs() { return legs; }
	private void setLegs(Item legs) { this.legs = legs; }
	public Item getFeet() { return feet; }
	private void setFeet(Item feet) { this.feet = feet; }
	public Item getAmulet() { return amulet; }
	private void setAmulet(Item amulet) { this.amulet = amulet; }
	public Item getRing() { return ring; }
	private void setRing(Item ring) { this.ring = ring; }
}
