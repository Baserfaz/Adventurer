package com.adventurer.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.data.Camera;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Effect;
import com.adventurer.gameobjects.GameObject;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.VanityItem;

public class Handler {

	private List<GameObject> objects = new ArrayList<GameObject>();
	
	public static Handler instance;
	
	public Handler() {
		if(instance != null) return;
		instance = this;
	}
	
	public void tick() {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			if(current != null) current.tick();
		}
	}
	
	// draw order:
	// 0. background (done in game.mainloop)
	// 1. tiles 
	// 2. vanity items (blood etc.)
	// 3. items
	// 4. actors
	// 5. effects
	// 6. GUI
	public void render(Graphics g) {
		renderTiles(g);
		renderVanityItems(g);
		renderItems(g);
		renderActors(g);
		renderEffects(g);
		renderGUI(g);
	}
	
	private void renderTiles(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			
			if(current == null) continue;
			
			if(current instanceof Tile) {
				current.render(g);
			}
		}
	}
	
	private void renderVanityItems(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			
			if(current == null) continue;
			
			if(current instanceof VanityItem) {
				current.render(g);
			}
		}
	}
	
	private void renderItems(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			
			if(current == null) continue;
			
			if(current instanceof Item) {
				current.render(g);
			}
		}
	}
	
	private void renderActors(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			
			if(current == null) continue;
			
			if(current instanceof Actor) {
				current.render(g);
			}
		}
	}
	
	private void renderEffects(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			
			if(current == null) continue;
			
			if(current instanceof Effect) {
				current.render(g);
			}
		}
	}	

	private void renderGUI(Graphics g) {
	    
		Player player = ActorManager.GetPlayerInstance();
		
		if(Camera.instance == null || player == null) return;
		
		Rectangle cam = Camera.instance.getCameraBounds();
		
		Graphics2D g2d = (Graphics2D) g;
		
		// change color
		Color fontCol = new Color(128, 0, 0);
		g2d.setColor(fontCol);
		
		// font settings
		Font font = new Font("Consolas", Font.PLAIN, 8);
		g2d.setFont(font);
		
		// draw health
		g2d.drawString("HP: " + player.GetHealth().GetCurrentHealth(),
				(int) cam.getMinX() + 5,
				(int) cam.getMaxY() - 5);
		
		// draw key count
		g2d.drawString("Keys: " + player.getInventory().getKeyCount(),
				(int) cam.getMinX() + 50, 
				(int) cam.getMaxY() - 5
				);
		
		// draw diamond key count
		g2d.drawString("DiamondKeys: " + player.getInventory().getDiamondKeyCount(), 
				(int) cam.getMinX() + 100, 
				(int) cam.getMaxY() - 5);
		
		// draw bomb count
		/*g2d.drawString("Bombs: " + player.getInventory().getBombCount(),
				(int) cam.getMinX() + 180,
				(int) cam.getMaxY() - 5
				);*/
		
		// draw projectile count
		/*g2d.drawString("Projectiles: " + player.getInventory().getProjectileCount(),
				(int) cam.getMinX() + 230,
				(int) cam.getMaxY() - 5
				);*/
		
	}	
	
	
	public void AddObject(GameObject go) {
		this.getObjects().add(go);
	}
	
	public void RemoveObject(GameObject go) {
		this.getObjects().remove(go);
	}

	public List<GameObject> getObjects() {
		return objects;
	}

	public void setObjects(List<GameObject> objects) {
		this.objects = objects;
	}
	
}
