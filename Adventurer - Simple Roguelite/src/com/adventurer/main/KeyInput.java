package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.adventurer.gameobjects.Bomb;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Tile;

public class KeyInput extends KeyAdapter {
	
	public KeyInput() {}
	
	public void keyPressed(KeyEvent e) {
		
		// get the pressed key 
		int key = e.getKeyCode();
		
		Player player = ActorManager.GetPlayerInstance();
		
		if(player.GetHealth().isDead()) return;
		
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
		
		if(key == KeyEvent.VK_SPACE) {
			
			Tile t = World.instance.GetTileFromDirection(player.GetTilePosition(), player.GetLookDirection());
			
			if(t.GetTileType() == TileType.Floor || t.GetTileType() == TileType.TrapTile && t.GetActor() == null) {
				
				Bomb bomb = new Bomb(t.GetWorldPosition(), t.GetTilePosition(), SpriteType.Bomb01, 1500, 300);
				t.SetActor(bomb);
			}
		}
		
		// shooting debugging
		/*else if(key == KeyEvent.VK_UP) {
			new Projectile(player.GetX(), player.GetY(),
					player.GetPosition()[0], player.GetPosition()[1],
					SpriteType.Projectile01, 100, Direction.North);
		} else if(key == KeyEvent.VK_DOWN) {
			new Projectile(player.GetX(), player.GetY(),
					player.GetPosition()[0], player.GetPosition()[1],
					SpriteType.Projectile01, 100, Direction.South);
		} else if(key == KeyEvent.VK_LEFT) {
			new Projectile(player.GetX(), player.GetY(),
					player.GetPosition()[0], player.GetPosition()[1],
					SpriteType.Projectile01, 100, Direction.West);
		} else if(key == KeyEvent.VK_RIGHT) {
			new Projectile(player.GetX(), player.GetY(),
					player.GetPosition()[0], player.GetPosition()[1],
					SpriteType.Projectile01, 100, Direction.East);
		}*/
	}
	
	public void keyReleased(KeyEvent e) {}
}
