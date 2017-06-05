package com.adventurer.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.adventurer.gameobjects.Player;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = -5226776943692411279L;

	public static Game instance;
	
	public static final int WIDTH = 1280, HEIGHT = 720; 			// viewport size
	public static final int SPRITESIZE = 16; 						// sprite size in pixels
	public static final int CAMERAZOOM = 1; 						// level of zoom
	public static final int CAMERAVIEWZOOM = 0; 					// shrinks the camera view so smaller amount of tiles are being rendered
	public static final double FRAME_CAP = 60.0;					// cap the framerate to this
	public static final String spritesheetname = "spritesheet.png";	// main spritesheet name
	
	//------------------------------
	// DEBUGGING TOOLS
	
	public static final boolean DRAW_CAMERA = true;
	public static final boolean PRINT_WORLD_CREATION_PERCENTAGE_DONE = true;
	public static final boolean PRINT_WORLD_CREATION_START_END = true;
	public static final boolean PRINT_DOOR_CREATED = false;
	public static final boolean CALCULATE_PLAYER_LOS = false;
	
	//------------------------------
	
	private Thread thread;
	private boolean isRunning = false;
	
	private Window window;
	
	// room count in world
	private final int worldHeight = 10;
	private final int worldWidth = 10;
	
	// tiles in one room
	private final int roomHeight = 10;
	private final int roomWidth = 10;
	
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
		
		// create world
		new World(worldWidth, worldHeight, roomWidth, roomHeight);
		
		// create player
		ActorManager.CreatePlayerInstance(300, 100);
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
			if(isRunning && render) {
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
		// - gameobjects that are rendered first are bottom
		// - gameobjects that are rendered last are top
		
		// TODO: draw order:
		// 0. background
		// 1. tiles 
		// 2. vanity items (blood etc.)
		// 3. items
		// 4. actors
		// 5. effects
		// 6. GUI
		
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
			
			// update 'camera' position
			Camera.instance.Update(new Coordinate(-targetx + CAMERAVIEWZOOM, -targety + CAMERAVIEWZOOM),
					(1273 - CAMERAVIEWZOOM * 5) / CAMERAZOOM,
					(690 - CAMERAVIEWZOOM * 5) / CAMERAZOOM);
		
			if(DRAW_CAMERA) {
				g.setColor(Color.red);
				g.drawRect((int) Camera.instance.getCameraBounds().getX(), 
						(int) Camera.instance.getCameraBounds().getY(),
						(int) Camera.instance.getCameraBounds().getWidth(),
						(int) Camera.instance.getCameraBounds().getHeight());
			}
		}
		
		// render objects
		Handler.instance.render(g);
		
		// render GUI
		// after everything else.
		// TODO: GUI
		
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
