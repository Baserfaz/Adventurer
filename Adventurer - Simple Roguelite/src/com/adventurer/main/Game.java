package com.adventurer.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.adventurer.data.Camera;
import com.adventurer.data.Coordinate;
import com.adventurer.data.PredefinedMaps;
import com.adventurer.data.SaveFile;
import com.adventurer.data.World;
import com.adventurer.enumerations.DungeonGenerationState;
import com.adventurer.enumerations.GameState;
import com.adventurer.gameobjects.Player;
import com.adventurer.utilities.DungeonGeneration;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Window;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = -5226776943692411279L;

	public static Game instance;
	
	public static final int WIDTH              = 1280;			           // viewport width
	public static final int HEIGHT             = 720;                      // viewport height
	public static final int SPRITESIZE         = 16;                       // sprite size in pixels
	public static final int CAMERAZOOM         = 2;                        // level of zoom
	public static final double FRAME_CAP       = 60.0;                     // cap the framerate to this
	public static final String SPRITESHEETNAME = "spritesheet_simple.png"; // name of the spritesheet 
	
	//------------------------------
	// DEBUGGING TOOLS AND GAME SETTINGS
	
	// draw debugging stuff
	public static final boolean DRAW_CAMERA     = false;
	public static final boolean DRAW_ENEMY_FOV  = false;
	public static final boolean DRAW_ENEMY_PATH = false; 			// works correctly only with one enemy
	
	// do we create lobby or random dungeon first.
	public static final boolean START_GAME_WITH_RANDOM_ROOM = false;
	
	// use simpler spritesheet layout
	public static final boolean USE_SIMPLE_SPRITESHEET_LAYOUT = true;
	
	// LOS settings
	public static final boolean CALCULATE_PLAYER_LOS   = true;
	public static final boolean PERMANENTLY_SHOW_TILES = false;
	
	// render settings
	public static final boolean RENDER_ACTORS_DIRECTION_ARROW = false;
	public static final boolean RENDER_PLAYER_DIRECTION_ARROW = false;
	
	// movement 
	public static final boolean MOVEMENT_ROTATE_FIRST = false;

	// tile settings
	public static final boolean ANIMATE_TILE_DISCOVERY = false;
	public static final int TILEGAP = 2;
	
	// player defaults
	public static final int START_KEY_COUNT         = 0;
	public static final int START_DIAMOND_KEY_COUNT = 0;
	public static final int START_BOMB_COUNT        = 0;
	public static final int START_PROJECTILE_COUNT  = 0;
	
	// enemy settings
	public static final boolean MAGGOTS_SPAWN_EGGS_ON_DEATH = false;
	
	// world size
	public static final int WORLDHEIGHT = 30; // ~40 max 
	public static final int WORLDWIDTH  = 30; // ~40 max
	
	// room count
	public static final int ROOM_COUNT                = 20;
	public static final int ROOM_DOOR_MAX_COUNT       = 4;
    public static final int ROOM_TRY_GENERATION_COUNT = 100;
    
	// room sizes
	public static final int ROOM_MAX_WIDTH  = 10;
	public static final int ROOM_MIN_WIDHT  = 2;
	public static final int ROOM_MAX_HEIGHT = 10;
	public static final int ROOM_MIN_HEIGHT = 2;
	
	//------------------------------
	
	private Thread thread;
	private boolean isRunning = false;
	private Window window;
	
	private SaveFile currentSaveFile;
	private GameState gameState;
	
	public Game() {
		
		if(instance != null) return;
		
		Game.instance = this;
		
		gameState = GameState.MainMenu;
		
		// create object handler
		new Handler();
		
		KeyInput keyInput = new KeyInput();
		
		// create key listener for inputs.
		this.addKeyListener(keyInput);
		
		// create mouse input object
		MouseInput mouseInput = new MouseInput();
		
		// create mouse listener
		this.addMouseMotionListener(mouseInput);
		this.addMouseListener(mouseInput);
		
		// create window 
		window = new Window(WIDTH, HEIGHT, "Adventurer", this);
		
		// create sprite creator
		new SpriteCreator(SPRITESHEETNAME);
		
		// create camera
		new Camera();
		
		// create save file:
		// this creates a save file object that
		// reads the actual permanent save file and
		// which has the data.
		setCurrentSaveFile(new SaveFile());
		
		// create lobby
		if(START_GAME_WITH_RANDOM_ROOM) new World(ROOM_COUNT);
		else new World(PredefinedMaps.GetLobby());
	}
	
	public synchronized void Start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}
	
	public synchronized void Stop() {
		try {
			thread.join();
			isRunning = false;
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void run() { GameLoop(); }
	
	// Originally taken from Notch's work.
	// Also applied stuff from https://www.youtube.com/watch?v=rwjZDfcQ7Rc&list=PLEETnX-uPtBXP_B2yupUKlflXBznWIlL5&index=3
	private void GameLoop() {
	    
		long lastTime = System.nanoTime();
		double unprocessedTime = 0;
		
		int frames = 0;
		long frameCounter = 0;
		
		final double frameTime = 1 / FRAME_CAP;
		final long SECOND = 1000000000L;
		
		boolean render = false;
		long now = 0l, passedTime = 0l;
		
		while(isRunning) {
		    
			render = false;
			
			now = System.nanoTime();
			passedTime = now - lastTime;
			lastTime = now;
			
			unprocessedTime += passedTime / (double) SECOND;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime) {
				
				render = true;
				unprocessedTime -= frameTime;
				
				// update gameobjects only when we are in game.
				if(gameState == GameState.InGame) tick();
				
				if(frameCounter >= SECOND) {
					window.SetCustomTitle("FPS: " + frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			
			// render the scene
			if(isRunning  && render) {
				render();
				frames++;
			}
		}
		Stop();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		// graphics and graphics 2d
		Graphics g = bs.getDrawGraphics();
		
		//-------------------------------------------
		// DRAW GRAPHICS HERE
		
		if(gameState == GameState.InGame) renderInGame(g);
		else if(gameState == GameState.Loading) renderLoading(g);
		
		// END DRAW
		//-------------------------------------------
		
		g.dispose();
		bs.show();
	}

	private void renderLoading(Graphics g) {
	    
	    Graphics2D g2d = (Graphics2D) g;
        
        // set background
        Renderer.FillScreen(g, Color.white);
	    
        // title
        Renderer.renderString("Adventurer - Roguelike",
                new Coordinate(Game.WIDTH / 3, 100), Color.black, 36, g2d);
        
        // creator info
        Renderer.renderString("by Heikki Heiskanen",
                new Coordinate(Game.WIDTH / 3, 150), Color.gray, 21, g2d);
        
        // generation phase
        Renderer.renderString(">> Generating " + DungeonGeneration.state, 
                new Coordinate(Game.WIDTH / 3, 300), Color.black, 32, g2d);
        
        // dungeon settings
        Renderer.renderString("Dungeon size: " + Game.WORLDWIDTH + "x" + Game.WORLDHEIGHT, 
                new Coordinate(Game.WIDTH / 3, 350), Color.black, 18, g2d);
        
        // print finished 
        if(DungeonGeneration.state == DungeonGenerationState.Finished) {
            Renderer.renderString(">> Finished <<", 
                    new Coordinate(Game.WIDTH / 3, 400), Color.black, 18, g2d);
        }
        
	}
	
	private void renderInGame(Graphics g) {
	    
	    Graphics2D g2d = (Graphics2D) g;
	    
        // set background
        Renderer.FillScreen(g, Color.black);
        
        // zoom
        g2d.scale(CAMERAZOOM, CAMERAZOOM);
        
        // camera follow
        Player player = ActorManager.GetPlayerInstance();
        if(player != null) {
    
            Coordinate pos = calculateCameraPos(player);
            int x = pos.getX(); 
            int y = pos.getY();
            
            // translate
            g.translate(x, y);
            
            // update camera's position
            Camera.instance.Update(new Coordinate(-x, -y), 1273 / CAMERAZOOM, 690 / CAMERAZOOM);
        
            if(DRAW_CAMERA) {
                g.setColor(Color.red);
                g.drawRect((int) Camera.instance.getCameraBounds().getX(), 
                        (int) Camera.instance.getCameraBounds().getY(),
                        (int) Camera.instance.getCameraBounds().getWidth(),
                        (int) Camera.instance.getCameraBounds().getHeight());
            }
            
        } else Renderer.FillScreen(g, Color.black);
        
        // render objects
        Handler.instance.render(g);   
	}
	
	private Coordinate calculateCameraPos(Player player) {
        int x = ((-player.GetWorldPosition().getX() * CAMERAZOOM) - (SPRITESIZE - WIDTH / 2)) / CAMERAZOOM;
        int y = ((-player.GetWorldPosition().getY() * CAMERAZOOM) - (SPRITESIZE - HEIGHT / 2)) / CAMERAZOOM;
	    return new Coordinate(x, y);
	}
	
	private void tick() { Handler.instance.tick(); }

	public static void main(String args[]) { new Game(); }
	
	public GameState getGameState() { return this.gameState; }
	public void setGameState(GameState state) { this.gameState = state; }
	
	public SaveFile getCurrentSaveFile() { return currentSaveFile; }
	public void setCurrentSaveFile(SaveFile currentSaveFile) { this.currentSaveFile = currentSaveFile; }
}
