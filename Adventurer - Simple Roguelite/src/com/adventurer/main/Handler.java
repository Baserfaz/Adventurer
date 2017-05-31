package com.adventurer.main;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

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
	
	public void render(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			
			if(current == null) continue;
			
			current.render(g);
		}
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
