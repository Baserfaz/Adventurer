package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.adventurer.gameobjects.Bomb;
import com.adventurer.gameobjects.GameObject;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Tile;

public class KeyInput extends KeyAdapter {

	private Handler handler;
	
	public KeyInput(Handler handler) {
		this.handler = handler;
	}
	
	public void keyPressed(KeyEvent e) {
			
		// get the pressed key 
		int key = e.getKeyCode();
		
		// do stuff with the key
		for(int i = 0; i < handler.getObjects().size(); i++) {
			GameObject current = handler.getObjects().get(i);
			
			// get the player object
			if(current instanceof Player) {
				
				// get player from current gameobject
				// -> we know that this is the player object.
				Player player = (Player) current;
				
				// cant control the player when dead.
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
				
				// actions
				// bomb
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
		}
	}
	
	public void keyReleased(KeyEvent e) {}
}
