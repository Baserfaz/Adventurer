package com.adventurer.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.adventurer.data.Camera;
import com.adventurer.data.Coordinate;
import com.adventurer.data.Experience;
import com.adventurer.data.Offense;
import com.adventurer.data.Resistances;
import com.adventurer.data.Stats;
import com.adventurer.data.World;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.GuiState;
import com.adventurer.enumerations.WorldType;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.Effect;
import com.adventurer.gameobjects.Enemy;
import com.adventurer.gameobjects.GameObject;
import com.adventurer.gameobjects.Gold;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Shrine;
import com.adventurer.gameobjects.Tile;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Handler {

	private List<GameObject> objects = new ArrayList<GameObject>();
	
	// TODO: mouse hover --- > this is bad.
	private Tile hoverTile = null;
	private Coordinate mousePosition = new Coordinate(0, 0);
	
	// GUI cursors
	private int inventoryCursorPos = 0;
	private int equipmentCursorPos = 0;
	
	// flags to show different GUI-elements.
	private boolean showStats = false;
	
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
	
	// TODO: move some of these to Renderer, mainly renderGUI() ?
	
	public void render(Graphics g) {
		renderTiles(g);
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
	    int charinfo_yPos = (int) cam.getMinY() + 20;
	    int charinfo_xPos = (int) cam.getMinX() + 50;
	    Coordinate chainfo_coord = new Coordinate(charinfo_xPos, charinfo_yPos);
	
	    // hover tile info
	    int tileinfo_yPos = mousePosition.getY() - 10; //(int) cam.getMaxY() - 200;
	    int tileinfo_xPos = mousePosition.getX() + 20; //(int) cam.getMaxX() - 200;
	    Coordinate tileinfo_coord = new Coordinate(tileinfo_xPos, tileinfo_yPos);
	    
	    // inventory position
	    int inventory_yPos = (int) cam.getMinY() + 25;
	    int inventory_xPos = (int) cam.getMaxX() - 100;
	    Coordinate inventory_coord = new Coordinate(inventory_xPos, inventory_yPos);
	    
	    // equipment position
	    int equipment_yPos = (int) cam.getMinY() + 25;
	    int equipment_xPos = (int) cam.getMaxX() - 210;
	    Coordinate equipment_coord = new Coordinate(equipment_xPos, equipment_yPos);
	    
	    // help text position
	    int help_yPos = (int) cam.getMaxY() - 15;
	    int help_xPos = (int) cam.getMinX() + 100;
	    Coordinate help_coord = new Coordinate(help_xPos, help_yPos);
	    
	    // end calc
	    
	    // -------------- LOCATION ---------------------
	    
	    if(showStats == false) {
	    
		    // render location tag.
		    Renderer.renderString("Location: ", dungeonInfo_coord, Color.white, 8, g2d);
		    
	        // render dungeon name and level
		    if(World.instance.getWorldType() == WorldType.Predefined) {
		    	
	            Renderer.renderString(
	                "Chilly lobby",
	                new Coordinate(dungeonInfo_coord.getX() + 50, dungeonInfo_coord.getY()), 
	                Color.gray, 8, g2d
	            );
	            
		    } else if(World.instance.getWorldType() == WorldType.Random) {
		        
	            Renderer.renderString(
	                "Dungeon (lvl. "+ Game.instance.getCurrentSession().getDungeonLevel() + ")",
	                new Coordinate(dungeonInfo_coord.getX() + 50, dungeonInfo_coord.getY()),
	                Color.gray, 8, g2d
	            );
	            
		    }
	    }
	    
	    // ---------------------- VITALS ------------------------
	    
	    if(showStats == false) {
		    // render vitals tag
		    Renderer.renderString("Vitals", stats_coord, Color.white, 8, g2d);
		    
			// render vitals (HP etc.)
			Renderer.renderString(
		        "\nHP: " + player.getHealth().GetCurrentHealth() + "/" + player.getHealth().GetMaxHP() + "\n" +
		        "MP: " + player.getMana().GetCurrentMana() + "/" + player.getMana().GetMaxMP() + "\n",
		        stats_coord,
		        Color.gray, 8, g2d
			);
	    }
	    
	    // ---------------------- STATS -------------------------
	   
	    if(showStats) {
			
			// render character info tag
			Renderer.renderString("CHARACTER", chainfo_coord, Color.white, 8, g2d);
			
			Resistances resistances = player.getResistances();
			Offense offense = player.getOffense();
			Experience exp = player.getPlayerExperience();
			Stats stats = player.getStats();
			
			// render character info
	        Renderer.renderString(
	            String.format(
	            		"\nName: %s\n"
	            		+ "Class: %s\n"
	            		+ "Level: %d\n"
	            		+ "Experience: %d / %d\n"
	            		+ "-------- VITALS --------\n"
	            		+ "Health: %d / %d\n"
	            		+ "Mana:   %d / %d\n"
	            		+ "-------- STATS ---------\n"
	            		+ "STR: %d (base: %d)\n"
	            		+ "VIT: %d (base: %d)\n"
	            		+ "INT: %d (base: %d)\n"
	            		+ "DEX: %d (base: %d)\n"
	            		+ "-------- DAMAGE --------\n"
	            		+ "Melee:  %d\n" 
	            		+ "> fire: %d, frost: %d, shock: %d\n" 
	            		+ "> holy: %d, physical: %d\n"
	            		+ "Magic:  %d\n"
	            		+ "> fire: %d, frost: %d, shock: %d\n"
	            		+ "> holy: %d, physical: %d\n"
	            		+ "Ranged: %d\n"
	            		+ "> fire: %d, frost: %d, shock: %d\n"
	            		+ "> holy: %d, physical: %d\n"
	            		+ "------ RESISTANCES -----\n"
	            		+ "Physical: %d\n"
	            		+ "Fire:     %d\n"
	            		+ "Frost:    %d\n"
	            		+ "Shock:    %d\n"
	            		+ "Holy:     %d\n",
	            	
	            		player.getName(),
	            		player.getPlayerClass().toString(),
	            		exp.getCurrentLevel(),
	            		exp.getCurrentExp(),
	            		exp.getNeededExp(exp.getCurrentLevel()),
	            		
	            		player.getHealth().GetCurrentHealth(),
	            		player.getHealth().GetMaxHP(),
	            		player.getMana().GetCurrentMana(),
	            		player.getMana().GetMaxMP(),
	            		
	            		stats.getSumStr(),
		            	stats.getBaseStrength(),
		            	stats.getSumVit(),
		            	stats.getBaseVitality(), 
		            	stats.getSumInt(),
		            	stats.getBaseIntelligence(), 
		            	stats.getSumDex(),
		            	stats.getBaseDexterity(),
		            	
		            	Util.calcMeleeDamage(),  // calculated from str
		            	
		            	offense.getMeleeDmgOfType(DamageType.Fire),
		            	offense.getMeleeDmgOfType(DamageType.Frost),
		            	offense.getMeleeDmgOfType(DamageType.Shock),
		            	offense.getMeleeDmgOfType(DamageType.Holy),
		            	offense.getMeleeDmgOfType(DamageType.Physical),
		            	
		            	Util.calcMagicDamage(),  // calculated from int
		            	
		            	offense.getMagicDmgOfType(DamageType.Fire),
		            	offense.getMagicDmgOfType(DamageType.Frost),
		            	offense.getMagicDmgOfType(DamageType.Shock),
		            	offense.getMagicDmgOfType(DamageType.Holy),
		            	offense.getMagicDmgOfType(DamageType.Physical),
		            	
		            	Util.calcRangedDamage(), // calculated from dex
		            	
		            	offense.getRangedDmgOfType(DamageType.Fire),
		            	offense.getRangedDmgOfType(DamageType.Frost),
		            	offense.getRangedDmgOfType(DamageType.Shock),
		            	offense.getRangedDmgOfType(DamageType.Holy),
		            	offense.getRangedDmgOfType(DamageType.Physical),
		            	
		            	resistances.getPhysicalResistance(),
		            	resistances.getFireResistance(),
		            	resistances.getFrostResistance(),
		            	resistances.getShockResistance(),
		            	resistances.getHolyResistance()
		            	
	            ), chainfo_coord, Color.gray, 8, g2d
	        );
	        
	    }
        
        // -------------------- INVENTORY ----------------------
        
        String invItems = "";
        int currentInvSpaces = player.getInventory().getInventoryItems().size();
        int maxinvSpaces = player.getInventory().getMaxSize();
        
        // populate inv items string
        if(player.getInventory().getInventoryItems().isEmpty()) {
        	
        	// add '-' to fill the empty spaces.
        	for(int i = 0; i < maxinvSpaces; i++) { invItems += "-\n"; }
        	
        } else {
        	
        	// first add the items into the list.
        	for(Item item : player.getInventory().getInventoryItems()) { 
        		
        		// handle different items here.
        		// --> show different info.
        		
        		if(item instanceof Gold) {
        			Gold gold = (Gold) item;
        			invItems += gold.getName() + " (" + gold.getAmount() + ")\n";
        		} else {
        			invItems += item.getName() + "\n"; 
        		}
        		
        	}
        	
        	// then add '-' to fill the empty spaces.
        	int count = maxinvSpaces - currentInvSpaces;
        	for(int i = 0; i < count; i++) { invItems += "-\n"; }
        }
        
        // when the inventory GUI-mode is selected...
        if(Game.instance.getGuiState() == GuiState.Inventory) {
        	
        	// render inventory cursor
        	Renderer.renderRect(new Coordinate(0 + inventory_coord.getX(), this.getInventoryCursorPos() * 10 + inventory_coord.getY() + 13),
        			new Coordinate(90, 10), Color.white, Color.white, true, g2d);
        	
        	// render inventory help
        	Renderer.renderString("Inventory mode: Move cursor up: W, down: S, Equip item: E, Drop item: R, Exit: ESC",
        			help_coord, Color.white, 8, g2d);
        	
        } else if(Game.instance.getGuiState() == GuiState.None) {
        	
        	// render general help
        	Renderer.renderString("Play mode: Move/attack WASD, Inventory: I, Equipment: E, Character sheet: C, Mouse hover: info",
        			help_coord, Color.gray, 8, g2d);
        	
        } else if(Game.instance.getGuiState() == GuiState.Equipment) {
        	
        	// render inventory cursor
        	Renderer.renderRect(new Coordinate(0 + equipment_coord.getX(), this.getEquipmentCursorPos() * 10 + equipment_coord.getY() + 13),
        			new Coordinate(90, 10), Color.white, Color.white, true, g2d);
        	
        	// render inventory help
        	Renderer.renderString("Equipment mode: Move cursor up: W, down: S, Unequip item: E, Exit: ESC",
        			help_coord, Color.white, 8, g2d);
        	
        }
        
        // render inventory tag
        Renderer.renderString(
        		"Inventory " + "(" + currentInvSpaces + "/" + player.getInventory().getMaxSize() + ")",
        		inventory_coord, Color.white, 8, g2d
        );
        
        // render inventory items.
        Renderer.renderString("\n" + invItems, inventory_coord, Color.gray, 8, g2d);
        
        // -------------------- EQUIPMENT ---------------------
        
        String equipmentInfo = "";
        for(Entry<String, Item> eq: player.getEquipment().getAllEquipment().entrySet()) {
        	
        	equipmentInfo += "- " + eq.getKey() + ": ";
        	
        	Item i = eq.getValue();
        	if(i != null) equipmentInfo += eq.getValue().getName() + "\n";
        	else equipmentInfo += "None\n";
        }
        
        // render equipment tag.
        Renderer.renderString("Equipment", equipment_coord, Color.white, 8, g2d);
        
        // render equipment info.
        Renderer.renderString("\n" + equipmentInfo, equipment_coord, Color.gray, 8, g2d);
        
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
        					cachedTile.GetTilePosition().toString(), tileinfo, actorinfo, iteminfo);
        	
        	Renderer.renderString(txt, tileinfo_coord, Color.white, 8, g2d);
        	
        } else Renderer.renderString("", tileinfo_coord, Color.white, 8, g2d);
	}
	
	public void AddObject(GameObject go) { this.getObjects().add(go); }	
	public void RemoveObject(GameObject go) { this.getObjects().remove(go); }
	public List<GameObject> getObjects() { return objects; }
	public void setObjects(List<GameObject> objects) { this.objects = objects; }

	public Tile getHoverTile() { return hoverTile; }
	public void setHoverTile(Tile hoverTile) { this.hoverTile = hoverTile; }

	public Coordinate getMousePosition() { return mousePosition; }
	public void setMousePosition(Coordinate mousePosition) { this.mousePosition = mousePosition; }

	public boolean isShowingStats() { return showStats; }
	public void setShowStats(boolean showStats) { this.showStats = showStats; }

	public int getInventoryCursorPos() { return inventoryCursorPos; }
	public void setInventoryCursorPos(int inventoryCursorPos) { this.inventoryCursorPos = inventoryCursorPos; }
	
	public int getEquipmentCursorPos() { return equipmentCursorPos; }
	public void setEquipmentCursorPos(int equipmentCursorPos) { this.equipmentCursorPos = equipmentCursorPos; }
	
	public void moveInvCursorDown() {
		
		int max = ActorManager.GetPlayerInstance().getInventory().getMaxSize();
		
		// set cursor to the last item -> loops
		if(this.inventoryCursorPos == max - 1) {
			this.setInventoryCursorPos(0);
		} else {
			this.setInventoryCursorPos(inventoryCursorPos + 1);
		}
	}
	
	public void moveInvCursorUp() {
		// set cursor to the last item -> loops
		if(this.inventoryCursorPos == 0) {
			int max = ActorManager.GetPlayerInstance().getInventory().getMaxSize();
			this.setInventoryCursorPos(max - 1);
		} else {
			this.setInventoryCursorPos(inventoryCursorPos - 1);
		}
	}

	public void moveEquipmentCursorDown() {
		
		int max = 8; // player has 8 slots.
		
		// set cursor to the last item -> loops
		if(this.equipmentCursorPos == max - 1) {
			this.setEquipmentCursorPos(0);
		} else {
			this.setEquipmentCursorPos(equipmentCursorPos + 1);
		}
	}
	
	public void moveEquipmentCursorUp() {
		
		int max = 8; // player has 8 slots.
		
		// set cursor to the last item -> loops
		if(this.equipmentCursorPos == 0) {
			this.setEquipmentCursorPos(max - 1);
		} else {
			this.setEquipmentCursorPos(equipmentCursorPos - 1);
		}
	}
	

}
