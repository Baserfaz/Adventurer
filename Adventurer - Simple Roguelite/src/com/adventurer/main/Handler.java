package com.adventurer.main;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Effect;
import com.adventurer.gameobjects.GameObject;
import com.adventurer.gameobjects.Item;
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
			current.tick();
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
		// TODO
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
