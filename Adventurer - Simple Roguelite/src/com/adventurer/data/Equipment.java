package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.enumerations.WeaponType;
import com.adventurer.gameobjects.Armor;
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
		
		Player player = ActorManager.GetPlayerInstance();
		Stats stats = player.getStats();
		Resistances res = player.getResistances();
		Offense offense = player.getOffense();
		
		ItemBonus ib = item.getBonuses();
		
		// ---------------- UPDATE WITH ITEM BONUS STATS --------------------
		
		stats.addAddedDex(ib.getDexBonus()); 
		stats.addAddedInt(ib.getIntBonus());
		stats.addAddedStr(ib.getStrBonus());
		stats.addAddedVit(ib.getVitBonus());
		
		// TODO: resistance + damage bonuses?
		
		// ---------------- UPDATE WITH ITEM BASE STATS ---------------------
		if(item instanceof Armor) {
			
			Armor armor = (Armor) item;
			
			// update resistances
			for(Entry<DamageType, Integer> d : armor.getDefenseValues().entrySet()) {
				
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
			
		} else if(item instanceof Weapon) {
			
			Weapon weapon = (Weapon) item;
			
			if(weapon.getWeaponSlot() == WeaponSlot.Mainhand) {
				
				// MAINHAND
				
				if(weapon.getWeaponType() == WeaponType.Melee) {
					
					// calculate damage based off stats.
					int meleeDmg = Util.calcMeleeDamage();
					
					// add calculated damage values to weapon damage values
					// and lastly cache the sum in offense.
					for(Entry<DamageType, Integer> e : weapon.getDamage().entrySet()) {
						DamageType key = e.getKey();
						int val = e.getValue();
						
						// physical damage is affected by characters strength stat.
						if(key == DamageType.Physical) val += meleeDmg;
						
						if(isAddition) offense.setMeleeDmgOfType(key, val);
						else offense.setMeleeDmgOfType(key, -val);
					}
					
				} else if(weapon.getWeaponType() == WeaponType.Magic) {
					
					int magicDmg = Util.calcMagicDamage();
					// TODO: Add magic damage types
					
					
				} else if(weapon.getWeaponType() == WeaponType.Ranged) {
					
					int rangedDmg = Util.calcRangedDamage();
					// TODO: add ranged damage types
					
				}
				
			} else if(weapon.getWeaponSlot() == WeaponSlot.Offhand){
				
				// TODO: OFFHAND --> ONLY SHIELDS AND SPECIAL OFFHAND WEAPONS?
				
			}
			
		}
		
		// update health and mana based on stats
		player.updateHPandMP();
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
