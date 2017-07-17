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
import com.adventurer.gameobjects.Player;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Window;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = -5226776943692411279L;

	public static Game instance;
	
	public static final int WIDTH              = 1280;			           // viewport width
	public static final int HEIGHT             = 720;                      // viewport height
	public static final int SPRITESIZE         = 16;                       // sprite size in pixels
	public static final int CAMERAZOOM         = 1;                        // level of zoom
	public static final double FRAME_CAP       = 60.0;                     // cap the framerate to this
	public static final String SPRITESHEETNAME = "spritesheet_simple.png"; // main spritesheet name
	
	//------------------------------
	// DEBUGGING TOOLS AND GAME SETTINGS
	
	// draw debugging stuff
	public static final boolean DRAW_CAMERA     = false;
	public static final boolean DRAW_ENEMY_FOV  = false;
	public static final boolean DRAW_ENEMY_PATH = false; 			// works correctly only with one enemy
	
	// do we create lobby or random dungeon first.
	public static final boolean START_GAME_WITH_RANDOM_ROOM = true;
	
	// use simpler spritesheet layout
	public static final boolean USE_SIMPLE_SPRITESHEET_LAYOUT = true;
	
	// LOS settings
	public static final boolean CALCULATE_PLAYER_LOS = false;
	public static final boolean PERMANENTLY_SHOW_TILES = true;
	
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
	public static final int WORLDHEIGHT = 30;
	public static final int WORLDWIDTH  = 30;
	
	// room count
	public static final int ROOM_COUNT          = 20;
	public static final int ROOM_DOOR_MAX_COUNT = 2;
	
	// room sizes
	public static final int ROOM_MAX_WIDTH  = 10;
	public static final int ROOM_MIN_WIDHT  = 2;
	public static final int ROOM_MAX_HEIGHT = 10;
	public static final int ROOM_MIN_HEIGHT = 2;
	
	// how many times the room generation 
	// algorithm tries to fit a room to the world.
	public static final int ROOM_TRY_GENERATION_COUNT = 10;
	
	//------------------------------
	
	private Thread thread;
	private boolean isRunning = false;
	private Window window;
	
	private SaveFile currentSaveFile;
	
	public Game() {
		
		if(instance != null) return;
		
		Game.instance = this;
		
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
		
		while(isRunning) {
			
			boolean render = false;
			
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			
			unprocessedTime += passedTime / (double) SECOND;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime) {
				
				render = true;
				unprocessedTime -= frameTime;
				
				tick();
				
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
		Graphics2D g2d = (Graphics2D) g;
		
		//-------------------------------------------
		// DRAW GRAPHICS HERE
		
		// set background
		Renderer.FillScreen(g, Color.black);
		
		// zoom
		g2d.scale(CAMERAZOOM, CAMERAZOOM);
		
		// camera follow
		// TODO: camera shouldn't be in render().
		Player player = ActorManager.GetPlayerInstance();
		if(player != null) {
			
			int targetx = ((-player.GetWorldPosition().getX() * CAMERAZOOM) - (SPRITESIZE - WIDTH / 2)) / CAMERAZOOM;
			int targety = ((-player.GetWorldPosition().getY() * CAMERAZOOM) - (SPRITESIZE - HEIGHT / 2)) / CAMERAZOOM;
			
			// translate
			g.translate(targetx, targety);
			
			// update camera's position
			Camera.instance.Update(new Coordinate(-targetx, -targety), 1273 / CAMERAZOOM, 690 / CAMERAZOOM);
		
			if(DRAW_CAMERA) {
				g.setColor(Color.red);
				g.drawRect((int) Camera.instance.getCameraBounds().getX(), 
						(int) Camera.instance.getCameraBounds().getY(),
						(int) Camera.instance.getCameraBounds().getWidth(),
						(int) Camera.instance.getCameraBounds().getHeight());
			}
			
		} else Renderer.FillScreen(g, Color.black);
		
		// render objects
		// uses simplistic rendering queue
		Handler.instance.render(g);
		
		//-------------------------------------------
		g.dispose();
		bs.show();
	}

	private void tick() { Handler.instance.tick(); }
	public static void main(String args[]) { new Game(); }
	public SaveFile getCurrentSaveFile() { return currentSaveFile; }
	public void setCurrentSaveFile(SaveFile currentSaveFile) { this.currentSaveFile = currentSaveFile; }
}
