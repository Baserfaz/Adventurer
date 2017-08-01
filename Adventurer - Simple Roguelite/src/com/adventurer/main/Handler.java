package com.adventurer.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.data.Camera;
import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.WorldType;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.Effect;
import com.adventurer.gameobjects.Enemy;
import com.adventurer.gameobjects.GameObject;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Shrine;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.VanityItem;
import com.adventurer.utilities.Renderer;

public class Handler {

	private List<GameObject> objects = new ArrayList<GameObject>();
	
	// TODO: mouse hover --- > this is bad.
	private Tile hoverTile = null;
	private Coordinate mousePosition = new Coordinate(0, 0);
	
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
	
	// Draw order/queue:
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
			if(current instanceof Tile) current.render(g);
		}
	}
	
	private void renderVanityItems(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			if(current == null) continue;
			if(current instanceof VanityItem) current.render(g);
		}
	}
	
	private void renderItems(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			if(current == null) continue;
			if(current instanceof Item) current.render(g);
		}
	}
	
	private void renderActors(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			if(current == null) continue;
			if(current instanceof Actor) current.render(g);
		}
	}
	
	private void renderEffects(Graphics g) {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject current = getObjects().get(i);
			if(current == null) continue;
			if(current instanceof Effect) current.render(g);
		}
	}	

	private void renderGUI(Graphics g) {
		
		// ---------------------- RENDERS INGAME GUI! ---------------------------
		
		// get player
		Player player = ActorManager.GetPlayerInstance();
		
		if(Camera.instance == null || player == null) return;
		
		// get camera
		Rectangle cam = Camera.instance.getCameraBounds();
		Graphics2D g2d = (Graphics2D) g;
		
		// ------------ DRAW GUI --------------
		
		// calculate all positions
		// stats pos
		int stats_yPos = (int) cam.getMaxY() - 75;
		int stats_xPos = (int) cam.getMinX() + 50;
		Coordinate stats_coord = new Coordinate(stats_xPos, stats_yPos);
		
		// dungeon info i.e. name 
		int dungeoninfo_yPos = (int) cam.getMinY() + 25;
		int dungeoninfo_xPos = (int) cam.getMinX() + 50;
		Coordinate dungeonInfo_coord = new Coordinate(dungeoninfo_xPos, dungeoninfo_yPos);
		
		// player info
	    int charinfo_yPos = (int) cam.getMaxY() - 75;
	    int charinfo_xPos = (int) cam.getMinX() + 200;
	    Coordinate chainfo_coord = new Coordinate(charinfo_xPos, charinfo_yPos);
	
	    // hover tile info
	    int tileinfo_yPos = mousePosition.getY() - 10; //(int) cam.getMaxY() - 200;
	    int tileinfo_xPos = mousePosition.getX() + 20; //(int) cam.getMaxX() - 200;
	    Coordinate tileinfo_coord = new Coordinate(tileinfo_xPos, tileinfo_yPos);
	    
	    // inventory position
	    int inventory_yPos = (int) cam.getMinY() + 25;
	    int inventory_xPos = (int) cam.getMaxX() - 100;
	    Coordinate inventory_coord = new Coordinate(inventory_xPos, inventory_yPos);
	    // end calc
	    
        // render dungeon name and level
	    if(World.instance.getWorldType() == WorldType.Predefined) {
	        
	    	// TODO: all predefined maps are now rendered as chilly lobbies.
	    	// ---> somehow decide which map we are currently at.
	    	
            Renderer.renderString(
                "Location: Chilly lobby",
                dungeonInfo_coord, new Color(150, 150, 150), 10, g2d
            );
            
	    } else if(World.instance.getWorldType() == WorldType.Random) {
	        
            Renderer.renderString(
                "Location: Dungeon (lvl. "+ Game.instance.getCurrentSession().getDungeonLevel() + ")",
                dungeonInfo_coord, new Color(150, 150, 150), 10, g2d
            );
            
	    }
	    
		// render stats (HP etc.)
		Renderer.renderString(
	        "HP: " + player.GetHealth().GetCurrentHealth() + "\n" +
	        "Keys: " + player.getInventory().getKeyCount(), 
	        stats_coord, new Color(150, 150, 150), 12, g2d
		);
		
		// render character info
        Renderer.renderString(
            String.format("str: %d int: %d dex: %d", 
            player.getStats().getStrength(), player.getStats().getIntelligence(), player.getStats().getDexterity()),
            chainfo_coord, new Color(150, 150, 150), 12, g2d
        );
        
        // -------------------- INVENTORY START----------------------
        
        String invItems = "";
        int currentInvSpaces = player.getInventory().getInventoryItems().size();
        int maxinvSpaces = player.getInventory().getMaxSize();
        
        // populate inv items string
        if(player.getInventory().getInventoryItems().isEmpty()) {
        	
        	// add '-' to fill the empty spaces.
        	for(int i = 0; i < maxinvSpaces; i++) { invItems += "-\n"; }
        	
        } else {
        	
        	// first add the items into the list.
        	for(Item item : player.getInventory().getInventoryItems()) { invItems += item.getName() + "\n"; }
        	
        	// then add '-' to fill the empty spaces.
        	int count = maxinvSpaces - currentInvSpaces;
        	for(int i = 0; i < count; i++) { invItems += "-\n"; }
        }
        
        // render inventory tag
        Renderer.renderString(
        		"Inventory " + "(" + currentInvSpaces + "/" + player.getInventory().getMaxSize() + ")",
        		inventory_coord, Color.white, 8, g2d
        );
        
        // render inventory items.
        Renderer.renderString("\n" + invItems, inventory_coord, Color.gray, 8, g2d);
        
        // -------------------- INVENTORY END -----------------------
        
        // -------------------- EQUIPMENT START ---------------------
        
        
        // TODO: render equipment somewhere.
        
        
        // -------------------- EQUIPMENT END -----------------------
        
        // -------------------- MOUSE HOVER -------------------------
        if(hoverTile != null) {
        	
        	// cache tile
        	Tile cachedTile = hoverTile;
        	
        	String actorinfo = "-";
        	String iteminfo = "-";
        	String tileinfo = cachedTile.GetTileType().toString();
        	
        	// get actor info
        	if(cachedTile.GetActor() != null) {
        		Actor actor = cachedTile.GetActor();
        		if(actor instanceof Enemy) actorinfo = ((Enemy)actor).toString();
        		else if(actor instanceof Player) actorinfo = ((Player)actor).toString();
        	}
        	
        	// get items info
        	if(cachedTile.GetItems().isEmpty() == false) iteminfo = cachedTile.getItemsInfo();
        	
        	// get tile info
        	if(cachedTile instanceof Shrine) tileinfo = ((Shrine)cachedTile).toString();
        	else if(cachedTile instanceof Door) tileinfo = ((Door)cachedTile).toString();
        	
        	// format our complete string
        	String txt = String.format("Pos: %s\nTile: %s\nActor: %s\nItem: %s", 
        					cachedTile.GetTilePosition().toString(), 
        					tileinfo, 
							actorinfo,
							iteminfo);
        	
        	Renderer.renderString(txt, tileinfo_coord, new Color(150, 150, 150), 10, g2d);
        	
        } else Renderer.renderString("", tileinfo_coord, new Color(150, 150, 150), 10, g2d);
        
        // ------------------ MOUSE HOVER END ---------------------
	}
	
	public void AddObject(GameObject go) { this.getObjects().add(go); }	
	public void RemoveObject(GameObject go) { this.getObjects().remove(go); }
	public List<GameObject> getObjects() { return objects; }
	public void setObjects(List<GameObject> objects) { this.objects = objects; }

	public Tile getHoverTile() { return hoverTile; }
	public void setHoverTile(Tile hoverTile) { this.hoverTile = hoverTile; }

	public Coordinate getMousePosition() { return mousePosition; }
	public void setMousePosition(Coordinate mousePosition) { this.mousePosition = mousePosition; }
}
