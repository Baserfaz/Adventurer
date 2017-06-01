package com.adventurer.gameobjects;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.main.*;

public class Tile extends GameObject {

	protected TileType type;
	protected boolean discovered = false;
	protected boolean inView = false;
	
	protected GameObject item = null;
	protected GameObject actor = null;
	protected List<GameObject> vanityItems = new ArrayList<GameObject>();
	
	protected BufferedImage discoveredSprite = null; // used for caching darker image.
	
	// tile falling effect
	protected int targety = 0;
	protected int fallingSpeed = 1;
	protected int fallingYOffset = 10;
	
	public Tile(Coordinate worldPos, SpriteType spritetype, TileType type, Coordinate tilePos) {
		super(worldPos, tilePos, spritetype);
		
		this.type = type;
	}
	
	public void tick() {
		
		// check if the tile is in the camera's view
		Rectangle camera = Game.instance.camera;
		
		int x = worldPosition.getX();
		int y = worldPosition.getY();
		
		if(camera != null) {
			if(camera.contains(x + World.tileSize / 2, y + World.tileSize / 2)) {
				inView = true;
				Show();
			} else {
				Hide();
				inView = false;
				return;
			}
		}
		
		// move the tile
		if(y < targety + fallingSpeed) {
			if(y < targety) worldPosition.addY(fallingSpeed);
		}
		
	}
	
	public void render(Graphics g) {
		
		int x = worldPosition.getX();
		int y = worldPosition.getY();
		
		if(hidden == false) {
			
			// render tile
			g.drawImage(sprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			
			// render vanity items
			for(GameObject vi : vanityItems) {
				g.drawImage(vi.sprite, vi.GetWorldPosition().getX(), vi.GetWorldPosition().getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
			}
			
		} else if(inView == false) {
		
			// don't render this tile at all!
			
		} else if(hidden == true && discovered) {
			
			// create tinted version of the sprite 
			if(discoveredSprite == null) {
				discoveredSprite = Util.tint(sprite);
			} else {
				g.drawImage(discoveredSprite, x, y, Game.SPRITESIZE, Game.SPRITESIZE, null);
			}
			
			// render vanity items
			for(GameObject go : vanityItems) {
				
				VanityItem vi = (VanityItem) go;
				
				if(vi.GetTintedSprite() == null) {
					vi.SetTintedSprite(Util.tint(vi.sprite));
				}
				
				g.drawImage(vi.GetTintedSprite(), vi.GetWorldPosition().getX(), vi.GetWorldPosition().getY(), Game.SPRITESIZE, Game.SPRITESIZE, null);
			}
			
		}
	}
	
	public void Show() {
		this.hidden = false;
		if(this.actor != null) this.actor.Show();
	}
	
	public void Hide() {
		this.hidden = true;
		if(this.actor != null) this.actor.Hide();
	}
	
	public void Discover() {
		
		// when first time discovering
		// this tile -> set the target positions.
		if(discovered == false) {
			this.targety = worldPosition.getY();
			worldPosition.decreaseY(fallingYOffset);
			this.discovered = true;
		} 
		
		// always discover the 
		// actors and items.
		if(this.GetActor() != null) {
			
			GameObject actor = this.GetActor();
			
			actor.Discover();
		}
	}
	
	@Override
	public void Remove() {
		World.instance.RemoveTiles(this);
		Game.instance.GetHandler().RemoveObject(this);
		Hide();
	}
	
	public boolean GetDiscovered() {
		return this.discovered;
	}
	
	public Rectangle GetBounds() {
		return new Rectangle(worldPosition.getX(), worldPosition.getY(), Game.SPRITESIZE, Game.SPRITESIZE);
	}
	
	public TileType GetTileType() {
		return this.type;
	}

	public void SetTileType(TileType t) {
		this.type = t;
	}
	
	public GameObject GetItem() {
		return item;
	}

	public void RemoveVanityItem(GameObject i) {
		this.vanityItems.remove(i);
	}
	
	public void AddVanityItem(GameObject i) {
		this.vanityItems.add(i);
	}
	
	public List<GameObject> GetVanityItems() {
		return this.vanityItems;
	}
	
	public void SetItem(GameObject item) {
		this.item = item;
	}

	public GameObject GetActor() {
		return this.actor;
	}

	public void SetActor(GameObject actor) {
		this.actor = actor;
	}
}
