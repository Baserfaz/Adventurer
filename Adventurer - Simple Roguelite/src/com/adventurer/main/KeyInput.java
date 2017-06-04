package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.adventurer.gameobjects.Bomb;
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
			
			if((t.GetTileType() == TileType.Floor || t.GetTileType() == TileType.TrapTile) && t.GetActor() == null && t.GetItem() == null) {
				
				new Bomb(t.GetWorldPosition(), t.GetTilePosition(), SpriteType.Bomb01, 1500, 300);
			}
		}
		
		// shooting debugging
		if(key == KeyEvent.VK_UP) {
			player.Shoot(player.GetTilePosition(), Direction.North);
		} else if(key == KeyEvent.VK_DOWN) {
			player.Shoot(player.GetTilePosition(), Direction.South);
		} else if(key == KeyEvent.VK_LEFT) {
			player.Shoot(player.GetTilePosition(), Direction.West);
		} else if(key == KeyEvent.VK_RIGHT) {
			player.Shoot(player.GetTilePosition(), Direction.East);
		}
	}
	
	public void keyReleased(KeyEvent e) {}
}
