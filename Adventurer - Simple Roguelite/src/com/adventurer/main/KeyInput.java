package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.GameState;
import com.adventurer.enumerations.GuiState;
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
		
		// decide what actions to do when
		// in different GUI-states.
		if(guiState == GuiState.None) {
		
			// movement
			if(key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) player.Move(Direction.North);
			else if(key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) player.Move(Direction.South);
			else if(key == KeyEvent.VK_A || key == KeyEvent.VK_NUMPAD4) player.Move(Direction.West);
			else if(key == KeyEvent.VK_D || key == KeyEvent.VK_NUMPAD6) player.Move(Direction.East);
		
		} else if(guiState == GuiState.Inventory) {
			
			// move cursor in inventory
			if(key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) Handler.instance.moveInvCursorUp();
			else if(key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) Handler.instance.moveInvCursorDown();
			
		}
		
		// drop item
		if(key == KeyEvent.VK_R) player.dropItem();
		
		// Toggle inventory state
		if(key == KeyEvent.VK_I) {
			if(Game.instance.getGuiState() == GuiState.None) {
				
				// reset inventory cursor position
				Handler.instance.setInventoryCursorPos(0);
				Game.instance.setGuiState(GuiState.Inventory);
			}
			else Game.instance.setGuiState(GuiState.None);
		}
		
		// toggle character panel
		if(key == KeyEvent.VK_C) {
			if(Handler.instance.isShowingStats()) Handler.instance.setShowStats(false);
			else Handler.instance.setShowStats(true);
		}
		
		// bomb
		/*if(key == KeyEvent.VK_SPACE) {
			Tile t = World.instance.GetTileFromDirection(player.GetTilePosition(), player.GetLookDirection());
			player.UseBomb(t);
		}*/
		
		// shoot
		/*if(key == KeyEvent.VK_SHIFT) {
			player.Shoot(player.GetTilePosition(), player.GetLookDirection(), SpriteType.Arrow01);
		}*/
	}
	
	public void keyReleased(KeyEvent e) {}
}
