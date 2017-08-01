package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.GameState;
import com.adventurer.gameobjects.Player;

public class KeyInput extends KeyAdapter {
	
	public KeyInput() {}
	
	public void keyPressed(KeyEvent e) {
	    
	    // when game is loading, don't allow key presses.
	    if(Game.instance.getGameState() == GameState.Loading) return;
	    
		// get the pressed key 
		int key = e.getKeyCode();
		
		// when loading is finished, wait for player input to start game.
		if(Game.instance.getGameState() == GameState.Ready && key != KeyEvent.CHAR_UNDEFINED) {
		    Game.instance.setGameState(GameState.InGame);
		    return;
		}
		
		Player player = ActorManager.GetPlayerInstance();
		
		if(player.getHealth().isDead()) return;
		
		// -------------- HANDLE INPUTS ------------------
		
		// movement
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) {
			
			player.Move(Direction.North);
				
		} else if(key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) {
			
			player.Move(Direction.South);
			
		} else if(key == KeyEvent.VK_A || key == KeyEvent.VK_NUMPAD4) {
			
			player.Move(Direction.West);
			
		} else if(key == KeyEvent.VK_D || key == KeyEvent.VK_NUMPAD6) {
			
			player.Move(Direction.East);

		}
		
		// drop item
		if(key == KeyEvent.VK_R) {
			player.dropItem();
		}
		
		// bomb
		/*if(key == KeyEvent.VK_SPACE) {
			Tile t = World.instance.GetTileFromDirection(player.GetTilePosition(), player.GetLookDirection());
			player.UseBomb(t);
		}*/
		
		// shoot
		/*
		if(key == KeyEvent.VK_SHIFT) {
			player.Shoot(player.GetTilePosition(), player.GetLookDirection(), SpriteType.Arrow01);
		}*/
	}
	
	public void keyReleased(KeyEvent e) {}
}
