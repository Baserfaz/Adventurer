package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.adventurer.gameobjects.Bomb;
import com.adventurer.gameobjects.Gib;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Projectile;
import com.adventurer.gameobjects.Tile;

public class KeyInput extends KeyAdapter {
	
	public KeyInput() {}
	
	public void keyPressed(KeyEvent e) {
		
		// get the pressed key 
		int key = e.getKeyCode();
		
		Player player = ActorManager.GetPlayerInstance();
		
		if(player.GetHealth().isDead()) return;
		
		// movement
		if(key == KeyEvent.VK_W) {
			
			if(player.GetLookDirection() == Direction.North)
				player.Move(Direction.North);
			else
				player.SetLookDirection(Direction.North);
				
		} else if(key == KeyEvent.VK_S) {
			
			if(player.GetLookDirection() == Direction.South)
				player.Move(Direction.South);
			else 
				player.SetLookDirection(Direction.South);
			
		} else if(key == KeyEvent.VK_A) {
			
			if(player.GetLookDirection() == Direction.West)
				player.Move(Direction.West);
			else 
				player.SetLookDirection(Direction.West);
			
		} else if(key == KeyEvent.VK_D) {
			
			if(player.GetLookDirection() == Direction.East)
				player.Move(Direction.East);
			else
				player.SetLookDirection(Direction.East);
		} 
		
		// bomb
		if(key == KeyEvent.VK_SPACE) {
			Tile t = World.instance.GetTileFromDirection(player.GetTilePosition(), player.GetLookDirection());
			player.UseBomb(t);
		}
		
		// shoot
		if(key == KeyEvent.VK_SHIFT) {
			player.Shoot(player.GetTilePosition(), player.GetLookDirection(), SpriteType.Arrow01);
		}
			
		// shooting debugging
		/*if(key == KeyEvent.VK_UP) {
			player.Shoot(player.GetTilePosition(), Direction.North, SpriteType.Arrow01);
		} else if(key == KeyEvent.VK_DOWN) {
			player.Shoot(player.GetTilePosition(), Direction.South, SpriteType.Arrow01);
		} else if(key == KeyEvent.VK_LEFT) {
			player.Shoot(player.GetTilePosition(), Direction.West, SpriteType.Arrow01);
		} else if(key == KeyEvent.VK_RIGHT) {
			player.Shoot(player.GetTilePosition(), Direction.East, SpriteType.Arrow01);
		}*/
	}
	
	public void keyReleased(KeyEvent e) {}
}
