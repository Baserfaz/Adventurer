package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.adventurer.data.World;
import com.adventurer.enumerations.Direction;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Tile;

public class KeyInput extends KeyAdapter {
	
	public KeyInput() {}
	
	public void keyPressed(KeyEvent e) {
		
		// get the pressed key 
		int key = e.getKeyCode();
		
		Player player = ActorManager.GetPlayerInstance();
		
		if(player.GetHealth().isDead()) return;
		
		// movement
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) {
			
			if(player.GetLookDirection() == Direction.North || Game.MOVEMENT_ROTATE_FIRST == false) player.Move(Direction.North);
			else player.SetLookDirection(Direction.North);
				
		} else if(key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) {
			
			if(player.GetLookDirection() == Direction.South || Game.MOVEMENT_ROTATE_FIRST == false) player.Move(Direction.South);
			else player.SetLookDirection(Direction.South);
			
		} else if(key == KeyEvent.VK_A || key == KeyEvent.VK_NUMPAD4) {
			
			if(player.GetLookDirection() == Direction.West || Game.MOVEMENT_ROTATE_FIRST == false) player.Move(Direction.West);
			else player.SetLookDirection(Direction.West);
			
		} else if(key == KeyEvent.VK_D || key == KeyEvent.VK_NUMPAD6) {
			
			if(player.GetLookDirection() == Direction.East || Game.MOVEMENT_ROTATE_FIRST == false) player.Move(Direction.East);
			else player.SetLookDirection(Direction.East);
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
