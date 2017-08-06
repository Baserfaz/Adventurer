package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.adventurer.data.Equipment;
import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.GameState;
import com.adventurer.enumerations.GuiState;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;

public class KeyInput extends KeyAdapter {
	
	public KeyInput() {}
	
	public void keyPressed(KeyEvent e) {
		GameState currentState = Game.instance.getGameState();
	    if(currentState == GameState.InGame || currentState == GameState.Ready) inGameKeys(e);
	}
	
	private void inGameKeys(KeyEvent e) {
		// get the pressed key 
		int key = e.getKeyCode();
		
		// when loading is finished, wait for player input to start game.
		if(Game.instance.getGameState() == GameState.Ready) {
		    Game.instance.setGameState(GameState.InGame);
		    return;
		}
		
		Player player = ActorManager.GetPlayerInstance();
		if(player.getHealth().isDead()) return;
		
		// -------------- HANDLE INPUTS ------------------
		
		// cache in which gui state are we in.
		GuiState guiState = Game.instance.getGuiState();
		
		// Toggle inventory state
		if(key == KeyEvent.VK_I) {
			if(Game.instance.getGuiState() == GuiState.None) {
				
				// reset inventory cursor position
				Handler.instance.setInventoryCursorPos(0);
				Game.instance.setGuiState(GuiState.Inventory);
				
			}
		}
		
		// Toggle equipment state
		if(key == KeyEvent.VK_E) {
			if(Game.instance.getGuiState() == GuiState.None) {
				
				// reset inventory cursor position
				Handler.instance.setEquipmentCursorPos(0);
				Game.instance.setGuiState(GuiState.Equipment);
				
			}
		}
		
		// decide what actions to do when
		// in different GUI-states.
		if(guiState == GuiState.None) {
		
			// movement
			if(key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) player.Move(Direction.North);
			else if(key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) player.Move(Direction.South);
			else if(key == KeyEvent.VK_A || key == KeyEvent.VK_NUMPAD4) player.Move(Direction.West);
			else if(key == KeyEvent.VK_D || key == KeyEvent.VK_NUMPAD6) player.Move(Direction.East);
		
		} else if(guiState == GuiState.Inventory) {
			
			boolean success = false;
			
			// move cursor in inventory
			if(key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) Handler.instance.moveInvCursorUp();
			else if(key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) Handler.instance.moveInvCursorDown();
			
			// escape from inventory mode
			if(key == KeyEvent.VK_ESCAPE) Game.instance.setGuiState(GuiState.None);
			
			// drop item
			if(key == KeyEvent.VK_R) {
				// get the item that is selected i.e. cursor position
				Item item = player.getInventory().getItemOnPosition(Handler.instance.getInventoryCursorPos());
				if(item != null) {
					success = true;
					player.dropItem(item);
				}
			}
			
			// equip item
			if(key == KeyEvent.VK_E) {
				Item item = player.getInventory().getItemOnPosition(Handler.instance.getInventoryCursorPos());
				if(item != null) {
					success = true;
					player.getEquipment().equipItem(item);
				}
			}
			
			// exit inventory mode automatically
			if(Game.AUTOMATICALLY_ESCAPE_FROM_INV_MODE_AFTER_SUCCESS && success) Game.instance.setGuiState(GuiState.None);
		
		} else if(guiState == GuiState.Equipment) {
			
			boolean success = false;
			
			// move cursor in inventory
			if(key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) Handler.instance.moveEquipmentCursorUp();
			else if(key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) Handler.instance.moveEquipmentCursorDown();
			
			// unequip item
			if(key == KeyEvent.VK_E) {
			
				int pos = Handler.instance.getEquipmentCursorPos();
				Equipment eq = player.getEquipment();
				
				// TODO: HARDCODED TO MATCH EQUIPMENT GUI!!!!
				if(pos == 0) {
					eq.unequipSlot(WeaponSlot.Mainhand);
					success = true;
				} else if(pos == 1) {
					eq.unequipSlot(WeaponSlot.Offhand);
					success = true;
				} else if(pos == 2) {
					eq.unequipSlot(ArmorSlot.Head);
					success = true;
				} else if(pos == 3) {
					eq.unequipSlot(ArmorSlot.Chest);
					success = true;
				} else if(pos == 4) {
					eq.unequipSlot(ArmorSlot.Legs);
					success = true;
				} else if(pos == 5) {
					eq.unequipSlot(ArmorSlot.Feet);
					success = true;
				} else if(pos == 6) {
					eq.unequipSlot(ArmorSlot.Amulet);
					success = true;
				} else if(pos == 7) {
					eq.unequipSlot(ArmorSlot.Ring);
					success = true;
				} else System.out.println("NO SUCH POSITION AVAILABLE!!");
				
			}
			
			// escape from equipment mode
			if(key == KeyEvent.VK_ESCAPE) Game.instance.setGuiState(GuiState.None);
		
			// exit inventory mode automatically
			if(Game.AUTOMATICALLY_ESCAPE_FROM_INV_MODE_AFTER_SUCCESS && success) Game.instance.setGuiState(GuiState.None);
		}
		
		// toggle character panel
		if(key == KeyEvent.VK_C) {
			if(Handler.instance.isShowingStats()) Handler.instance.setShowStats(false);
			else Handler.instance.setShowStats(true);
		}
		
	}
	
	public void keyReleased(KeyEvent e) {}
}
