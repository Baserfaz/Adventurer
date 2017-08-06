package com.adventurer.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.data.Camera;
import com.adventurer.data.Coordinate;
import com.adventurer.data.Node;
import com.adventurer.data.World;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.*;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Tile extends GameObject {

	protected TileType type;
	protected boolean inView   = false;
	protected boolean selected = false;
	protected boolean walkable = false;
	
	protected List<Item> items = null;
	protected Actor actor      = null;
	protected Node node;
	
	// tile falling effect
	protected int targety        = 0;
	protected int fallingSpeed   = 1;
	protected int fallingYOffset = 10;
	
	public Tile(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type) {
		super(worldPos, tilePos, spritetype);
		this.type = type;
		this.node = new Node();
		items = new ArrayList<Item>();
		this.walkable = Util.isTileWalkable(this.type);	
	}
	
	public void tick() {
		
		// check if the tile is in the camera's view
		Rectangle camera = Camera.instance.getCameraBounds();
		
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
		if(camera != null) {
			if(camera.contains(x + Game.SPRITESIZE / 2, y + Game.SPRITESIZE / 2)) {
				inView = true;
				Show();
			} else {
				Hide();
				inView = false;
				return;
			}
		}
		
		if(Game.ANIMATE_TILE_DISCOVERY) UpdatePosition(x, y);
	}
	
	// move the tile
	protected void UpdatePosition(int x, int y) {
		if(y < targety + fallingSpeed && y < targety) this.SetWorldPosition(x, y + fallingSpeed);
	}
	
	public void render(Graphics g) {
		
		// tile is outside of our view
		if(inView == false) return;
		
		// Tile is selected
		// -> override fov-rendering.
		if(selected) {
			if(hidden == false) {
				Renderer.RenderSpriteWithBorder(sprite, this.GetWorldPosition(), g, Color.white);
			} else {
				// if the tile is hidden 
				// -> we must tint the sprite to default color first.
				Renderer.RenderSpriteWithBorder(Util.tint(sprite, true), this.GetWorldPosition(), g, Color.white);
			}
			return;
		}
		
		// fov-rendering
		if(hidden == false && discovered) {
			
			// render tile
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
			
		} else if(hidden == true && discovered) {
			
			Renderer.RenderSprite(Util.tint(sprite, true), this.GetWorldPosition(), g);
			
		} else {
			// We haven't discovered this tile and it's still hidden,
			// therefore we don't want to render it at this point.
		}
	}
	
	public void Show() {
		this.hidden = false;
		if(this.actor != null) this.actor.Show();
		
		if(this.items.isEmpty() == false) {
			for(Item item : this.items) item.Show();
		}
	}
	
	public void Hide() {
		this.hidden = true;
		if(this.actor != null) this.actor.Hide();
		
		if(this.items.isEmpty() == false) {
			for(Item item : this.items) item.Hide();
		}
	}
	
	public void Discover() {
		
		int x = this.GetWorldPosition().getX();
		int y = this.GetWorldPosition().getY();
		
		if(Game.ANIMATE_TILE_DISCOVERY) {
			this.targety = this.GetWorldPosition().getY();
			this.SetWorldPosition(x, y - fallingYOffset);
		}
			
		this.discovered = true;
		
		// discover actors and items.
		if(this.GetActor() != null) this.GetActor().Discover();
		if(this.items.isEmpty() == false) for(Item item : this.items) item.Discover();
	}
	
	public void Remove() {
		
		if(this.items.isEmpty() == false) {
			List<Item> temp = new ArrayList<Item>(this.items);
			for(Item item : temp) item.Remove();
		}
		
		if(this.GetActor() != null && this.GetActor() instanceof Player == false) this.GetActor().Remove();
		
		if(World.instance != null && World.instance.GetTiles() != null) World.instance.RemoveTiles(this);
		Handler.instance.RemoveObject(this);
		Hide();
	}
	
	public void toggleSelect() {
		if (this.selected) this.selected = false;
		else this.selected = true;
	}

	public String GetInfo() {
		String s = super.GetInfo();			// tile info
		String itemsInfo = getItemsInfo();	// items info
		
		s += ", tiletype: " + this.GetTileType() + ", Items: " + itemsInfo + ", Actor: " + this.GetActor();
		return s;
	}

	public String getItemsInfo() {
		String itemsInfo = "";
		for(Item item : this.items) itemsInfo += item.itemName  + ", ";
		return itemsInfo;
	}
	
	public void Select() { this.selected = true; }
	public void Deselect() { this.selected = false; }
	
	public boolean isSelected( ) { return this.selected; }
	public boolean isWalkable() { return this.walkable; }
	public boolean isInView() { return this.inView; }
	
	public TileType GetTileType() { return this.type; }
	public List<Item> GetItems() { return items; }
	public Actor GetActor() { return this.actor; }
	public Node getNode() { return this.node; }
	
	public void SetActor(Actor actor) { this.actor = actor; }
	public void SetTileType(TileType t) { this.type = t; }
	public void AddItem(Item item) { this.items.add(item); }
	public void RemoveItem(Item item) { this.items.remove(item); }
	
	public Rectangle GetBounds() {
		return new Rectangle(this.GetWorldPosition().getX(), this.GetWorldPosition().getY(), Game.SPRITESIZE, Game.SPRITESIZE);
	}
}
