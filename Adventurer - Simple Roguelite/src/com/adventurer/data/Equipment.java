package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.enumerations.WeaponType;
import com.adventurer.gameobjects.Armor;
import com.adventurer.gameobjects.Equipable;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Weapon;
import com.adventurer.main.ActorManager;
import com.adventurer.utilities.Util;

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
	
	public void updateStats(Item item, boolean isAddition) {
		
		// cache vars
		Equipable eItem = (Equipable) item;
		Player player = ActorManager.GetPlayerInstance();
		Stats stats = player.getStats();
		ItemBonus ib = eItem.getBonuses();
		
		// update stats
		if(isAddition) {
			
			stats.addAddedVit(ib.getVitBonus());
			stats.addAddedStr(ib.getStrBonus());
			stats.addAddedInt(ib.getIntBonus());
			stats.addAddedDex(ib.getDexBonus());
			
		} else {
			
			stats.addAddedVit(-ib.getVitBonus());
			stats.addAddedStr(-ib.getStrBonus());
			stats.addAddedInt(-ib.getIntBonus());
			stats.addAddedDex(-ib.getDexBonus());
	
		}
		
		// update dmg/res
		if(item instanceof Armor) updateResistance(item, isAddition);
		else if(item instanceof Weapon) updateDamage(item, isAddition);
		
		// calculate stats + weapons
		updateDamageScaling();
		
		// update player health and mana pools.
		player.updateHPandMP();
	}
	
	private void updateDamageScaling() {
		
		Player player = ActorManager.GetPlayerInstance();
		Offense offense = player.getOffense();
		
		// update melee -> PHYSICAL scales with STR
		offense.setMeleeDmgOfType(DamageType.Physical, Util.calcMeleeDamage());
		
		// update magic -> ... scales with INT
		
		
		
		// update range -> ... scales with DEX
		
		
		
	}
	
	private void updateDamage(Item item, boolean isAddition) {
			
		Player player = ActorManager.GetPlayerInstance();
		Offense offense = player.getOffense();
		Weapon weapon = (Weapon) item;
		
		if(weapon.getWeaponSlot() == WeaponSlot.Mainhand) {
			
			if(weapon.getWeaponType() == WeaponType.Melee) {

				for(Entry<DamageType, Integer> entry : weapon.getBonuses().getDamage().entrySet()) {
					DamageType key = entry.getKey();
					int value = entry.getValue();
					
					if(isAddition) offense.setMeleeDmgOfType(key, value);
					else offense.setMeleeDmgOfType(key, offense.getMeleeDmgOfType(key) - value);
				}
				
			} else if(weapon.getWeaponType() == WeaponType.Magic) {
				// TODO: Add magic damage types
				
				
			} else if(weapon.getWeaponType() == WeaponType.Ranged) {
				
				// TODO: add ranged damage types
				
			}
			
		} else if(weapon.getWeaponSlot() == WeaponSlot.Offhand){
			
			// TODO: OFFHAND --> ONLY SHIELDS AND SPECIAL OFFHAND WEAPONS?
			
		}
		
	}
	
	private void updateResistance(Item item, boolean isAddition) {
			
		Player player = ActorManager.GetPlayerInstance();
		Resistances res = player.getResistances();
		Armor armor = (Armor) item;
		
			for(Entry<DamageType, Integer> d : armor.getBonuses().getResistances().entrySet()) {
			
			DamageType key = d.getKey();
			int val = d.getValue();
			
			if(isAddition) {
			
				switch(key) {
					case Fire: res.addFireResistance(val); break;
					case Frost: res.addFireResistance(val); break;
					case Holy: res.addHolyResistance(val); break;
					case Physical: res.addPhysicalResistance(val); break;
					case Shock: res.addShockResistance(val); break;
					default: break;
				}
				
			} else {
				
				switch(key) {
					case Fire: res.addFireResistance(-val); break;
					case Frost: res.addFireResistance(-val); break;
					case Holy: res.addHolyResistance(-val); break;
					case Physical: res.addPhysicalResistance(-val); break;
					case Shock: res.addShockResistance(-val); break;
					default: break;
				}
				
			}
		}
		
	}
	
	public void equipItem(Item item) {
		
		boolean success = false;
		
		if(item instanceof Armor) {
			
			Armor armor = (Armor) item;
			ArmorSlot armorSlot = armor.getSlot();
			
			// drop or inventory current item
			this.unequipSlot(armorSlot);
			
			// equip item
			switch(armorSlot) {
				case Amulet: this.setAmulet(armor); break;
				case Chest: this.setChest(armor); break;
				case Feet: this.setFeet(armor); break;
				case Head: this.setHead(armor); break;
				case Legs: this.setLegs(armor); break;
				case Ring: this.setRing(armor); break;
				default: System.out.println("INVALID ARMORSLOT IN EQUIPMENT.EQUIP: " + armorSlot); break;
			}
			
			// remove item from inventory.
			ActorManager.GetPlayerInstance().getInventory().removeItemFromInventory(armor);
			
			success = true;
			
		} else if(item instanceof Weapon) {
			
			Weapon weapon = (Weapon) item;
			WeaponSlot weaponSlot = weapon.getWeaponSlot();
			
			this.unequipSlot(weaponSlot);
			
			switch(weaponSlot) {
				case Mainhand: this.setMainHand(weapon); break;
				case Offhand: this.setOffHand(weapon); break;
				default: System.out.println("INVALID WEAPONSLOT IN EQUIPMENT.EQUIP: " + weaponSlot); break;
			}
			
			// remove item from inventory.
			ActorManager.GetPlayerInstance().getInventory().removeItemFromInventory(weapon);
			
			success = true;
			
		} else System.out.println("Item cannot be equipped!");
		
		// update stats
		if(success) updateStats(item, true);
	}
	
	public void unequipSlot(WeaponSlot slot) {
		Item item = null;
		switch(slot) {
			case Mainhand: item = this.getMainHand(); this.setMainHand(null); break;
			case Offhand: item = this.getOffHand(); this.setOffHand(null); break;
			default: System.out.println("INVALID WEAPONSLOT IN EQUIPMENT.UNEQUIP: " + slot); break;
		}
		
		moveItemToInventoryOrDrop(item);
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
			default: System.out.println("INVALID ARMORSLOT IN EQUIPMENT.UNEQUIP: " + slot); break;
		}
		
		moveItemToInventoryOrDrop(item);
	}
	
	private void moveItemToInventoryOrDrop(Item item) {
		if(item != null) {
			
			Player player = ActorManager.GetPlayerInstance();
			Inventory inv = player.getInventory();
			
			if(inv.isFull()) player.dropItem(item);
			else inv.addToInventory(item);
			updateStats(item, false);
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
