package com.adventurer.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
	protected boolean inView = false;
	protected boolean selected = false;
	protected boolean lit = false;
	
	protected Item item = null;
	protected Actor actor = null;
	protected List<VanityItem> vanityItems = new ArrayList<VanityItem>();
	
	protected Node node;
	
	// tile falling effect
	protected int targety = 0;
	protected int fallingSpeed = 1;
	protected int fallingYOffset = 10;
	
	protected boolean walkable = false;
	
	public Tile(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type) {
		super(worldPos, tilePos, spritetype);
		
		this.type = type;
		this.node = new Node();
		
		if(this.type == TileType.Floor || this.type == TileType.Trap) walkable = true;
		
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
	
	protected void UpdatePosition(int x, int y) {
		// move the tile
		if(y < targety + fallingSpeed) {
			if(y < targety) this.SetWorldPosition(x, y + fallingSpeed);
		}
	}
	
	public void render(Graphics g) {
		
		// tile is outside of our view
		if(inView == false) return;
		
		// Tile is selected
		// -> override fov-rendering.
		if(selected) {
			
			if(hidden == false) {
				
				Renderer.RenderSpriteWithBorder(sprite, this.GetWorldPosition(), g, Color.red);
				
				for(GameObject vi : vanityItems) {
					Renderer.RenderSprite(vi.GetSprite(), vi.GetWorldPosition(), g);
				}
				
			} else {
				
				Renderer.RenderSpriteWithBorder(Util.tint(sprite, true), this.GetWorldPosition(), g, Color.red);
				
				for(GameObject vi : vanityItems) {
					Renderer.RenderSprite(Util.tint(vi.GetSprite(), true), vi.GetWorldPosition(), g);
				}
				
			}
			
			return;
		} 
		
		// fov-rendering
		if(hidden == false && discovered) {
			
			// brighten 
			if(lit) {
				
				Renderer.RenderSprite(Util.tint(sprite, false), this.GetWorldPosition(), g);
				
				for(GameObject vi : vanityItems) {
					Renderer.RenderSprite(Util.tint(vi.GetSprite(), false), vi.GetWorldPosition(), g);
				}
				
			// normal
			} else {
				
				// render tile
				Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
				
				// render vanity items
				try {
					for(GameObject vi : vanityItems) {
						Renderer.RenderSprite(vi.GetSprite(), vi.GetWorldPosition(), g);
					}
				} catch (ConcurrentModificationException e) {
					e.printStackTrace();
				}
				
			}
			
		} else if(hidden == true && discovered) {
			
			// create tinted version of the sprite and cache it.
			if(tintedSprite == null) {
				tintedSprite = Util.tint(sprite, true);
			} else {
				Renderer.RenderSprite(tintedSprite, this.GetWorldPosition(), g);
			}
			
			// render vanity items
			for(GameObject go : vanityItems) {
				
				VanityItem vi = (VanityItem) go;
				
				if(vi.GetTintedSprite() == null) {
					vi.SetTintedSprite(Util.tint(vi.GetSprite(), true));
				}
				
				Renderer.RenderSprite(vi.GetTintedSprite(), vi.GetWorldPosition(), g);
			}
		} else {
			// We haven't discovered this tile and it's still hidden,
			// therefore we don't want to render it at this point.
		}
	}
	
	public void Show() {
		this.hidden = false;
		
		if(this.actor != null) this.actor.Show();
		if(this.item != null) this.item.Show();
		
		if(this.vanityItems == null) return;
		
		if(this.vanityItems.size() > 0) {
			for(VanityItem vi : this.vanityItems) {
				vi.Show();
			}
		}
		
	}
	
	public void Hide() {
		this.hidden = true;
		
		if(this.actor != null) this.actor.Hide();
		if(this.item != null) this.item.Hide();
		
		if(this.vanityItems == null) return;
		
		if(this.vanityItems.size() > 0) {
			for(VanityItem vi : this.vanityItems) {
				vi.Hide();
			}
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
		if(this.GetActor() != null) {
			this.GetActor().Discover();
		}
		
		if(this.GetItem() != null) {
			this.GetItem().Discover();
		}
		
		if(this.vanityItems.size() > 0) {
			for(VanityItem vi : this.vanityItems) {
				vi.Discover();
			}
		}
	}
	
	public void Remove() {
		
		if(this.GetItem() != null) {
			this.GetItem().Remove();
		}
		
		if(this.GetActor() != null && this.GetActor() instanceof Player == false) {
			this.GetActor().Remove();
		}
		
		if(this.GetVanityItems().size() > 0) {
			List<VanityItem> temp = new ArrayList<VanityItem>(vanityItems);
			for(VanityItem vi : temp) vi.Remove();
		}
		
		if(World.instance != null && World.instance.GetTiles() != null) World.instance.RemoveTiles(this);
		Handler.instance.RemoveObject(this);
		Hide();
	}
	
	public boolean isLit() {
		return this.lit;
	}
	
	public void toggleLit() {
		if(this.lit) this.lit = false;
		else this.lit = true;
	}
	
	public void toggleSelect() {
		if (this.selected) this.selected = false;
		else this.selected = true;
	}
	
	public void Select() {
		this.selected = true;
	}
	
	public void Deselect() {
		this.selected = false;
	}
	
	public boolean isWalkable() {
		return this.walkable;
	}
	
	public String GetInfo() {
		String s = super.GetInfo();
		s += ", tiletype: " + this.GetTileType() + ", Item: " + this.GetItem() + ", Actor: " + this.GetActor();
		return s;
	}
	
	public boolean isInView() {
		return this.inView;
	}
	
	public Rectangle GetBounds() {
		return new Rectangle(this.GetWorldPosition().getX(), this.GetWorldPosition().getY(), Game.SPRITESIZE, Game.SPRITESIZE);
	}
	
	public TileType GetTileType() {
		return this.type;
	}

	public void SetTileType(TileType t) {
		this.type = t;
	}
	
	public Item GetItem() {
		return item;
	}

	public void RemoveVanityItem(GameObject i) {
		this.vanityItems.remove(i);
	}
	
	public void AddVanityItem(VanityItem i) {
		this.vanityItems.add(i);
	}
	
	public List<VanityItem> GetVanityItems() {
		return this.vanityItems;
	}
	
	public void SetItem(Item item) {
		this.item = item;
	}

	public Actor GetActor() {
		return this.actor;
	}

	public void SetActor(Actor actor) {
		this.actor = actor;
	}
	
	public Node getNode() {
		return this.node;
	}
}
