package com.adventurer.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import com.adventurer.gameobjects.Player;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = -5226776943692411279L;

	public static Game instance;
	
	public static final int WIDTH = 1280, HEIGHT = 720; 			// viewport size
	public static final int SPRITESIZE = 16; 						// sprite size in pixels
	public static final int CAMERAZOOM = 2; 						// level of zoom
	public static final double FRAME_CAP = 60.0;					// cap the framerate to this
	public static final String spritesheetname = "spritesheet.png";	// main spritesheet name
	
	//------------------------------
	// DEBUGGING TOOLS AND GAME SETTINGS
	
	public static final boolean DRAW_CAMERA = false;
	public static final boolean PRINT_WORLD_CREATION_PERCENTAGE_DONE = false;
	public static final boolean PRINT_WORLD_CREATION_START_END = false;
	public static final boolean PRINT_DOOR_CREATED = false;
	
	public static final boolean CALCULATE_PLAYER_LOS = true;
	public static final boolean RENDER_ACTORS_DIRECTION_ARROW = false;
	public static final boolean RENDER_PLAYER_DIRECTION_ARROW = true;
	
	public static final boolean CREATE_WALLS_INSIDE_ROOMS = false;
	public static final boolean CREATE_DOORS_INSIDE_ROOMS = false;
	public static final boolean CREATE_TRAPS_INSIDE_ROOMS = false;
	public static final boolean CREATE_TURRETS_INSIDE_ROOMS = false;
	public static final boolean CREATE_DESTRUCTIBLE_ITEMS_INSIDE_ROOMS = false;
	public static final boolean CREATE_DESTRUCTIBLE_WALLS_INSIDE_ROOMS = false;
	public static final boolean CREATE_VANITY_ITEMS_INSIDE_ROOMS = false;
	
	//------------------------------
	
	private Thread thread;
	private boolean isRunning = false;
	
	private Window window;
	
	// room count in world
	public static final int WORLDHEIGHT = 1;
	public static final int WORLDWIDTH = 1;
	
	// tiles in one room
	public static final int ROOMHEIGHT = 10;
	public static final int ROOMWIDTH = 10;
	
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
		new SpriteCreator(spritesheetname);
		
		// create camera
		new Camera();
		
		// create lobby
		new World(PredefinedMaps.GetLobby());
		
		// create world
		//new World(WORLDWIDTH, WORLDHEIGHT, ROOMWIDTH, ROOMHEIGHT);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Originally taken from Notch's work.
	// Also applied stuff from https://www.youtube.com/watch?v=rwjZDfcQ7Rc&list=PLEETnX-uPtBXP_B2yupUKlflXBznWIlL5&index=3
	public void run() {
		GameLoop();
	}
	
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
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// zoom
		g2d.scale(CAMERAZOOM, CAMERAZOOM);
		
		// camera follow
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
		}
		
		// render objects
		// uses simplistic rendering queue
		Handler.instance.render(g);
		
		//-------------------------------------------
		g.dispose();
		bs.show();
	}

	private void tick() {
		Handler.instance.tick();
	}

	public static void main(String args[]) {
		new Game();
	}
}
