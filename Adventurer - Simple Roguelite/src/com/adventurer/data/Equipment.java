package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.adventurer.gameobjects.Item;

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
	
	public Item getMainHand() { return mainHand; }
	public void setMainHand(Item mainHand) { this.mainHand = mainHand; }
	public Item getOffHand() { return offHand; }
	public void setOffHand(Item offHand) { this.offHand = offHand; }
	public Item getHead() { return head; }
	public void setHead(Item head) { this.head = head; }
	public Item getChest() { return chest; }
	public void setChest(Item chest) { this.chest = chest; }
	public Item getLegs() { return legs; }
	public void setLegs(Item legs) { this.legs = legs; }
	public Item getFeet() { return feet; }
	public void setFeet(Item feet) { this.feet = feet; }
	public Item getAmulet() { return amulet; }
	public void setAmulet(Item amulet) { this.amulet = amulet; }
	public Item getRing() { return ring; }
	public void setRing(Item ring) { this.ring = ring; }
}
