package com.adventurer.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = -5226776943692411279L;

	public static Game instance;
	
	public static final int WIDTH = 1280, HEIGHT = 720;
	public static final int SPRITESIZE = 16;
	public static final int CAMERAZOOM = 4; 
	public static final String spritesheetname = "spritesheet.png";
	
	private Thread thread;
	private boolean isRunning = false;
	
	private Handler handler;
	private SpriteCreator spritecreator;
	private World world;
	private Player player;
	
	public Rectangle camera = new Rectangle();
	
	// tiles
	private final int worldHeight = 20;
	private final int worldWidth = 30;
	
	// pixels
	private int worldHeightPixels = 0;
	private int worldWidthPixels = 0;
	
	public Game() {
		
		if(instance != null) return;
		
		instance = this;
		
		// create object handler
		handler = new Handler();
		
		// create key listener for inputs.
		this.addKeyListener(new KeyInput(handler));
		
		// create mouse input object
		MouseInput mouseinput = new MouseInput();
		
		// create mouse listener
		this.addMouseMotionListener(mouseinput);
		this.addMouseListener(mouseinput);
		
		// create window 
		new Window(WIDTH, HEIGHT, "Adventurer", this);
		
		// create sprite creator
		spritecreator = new SpriteCreator(spritesheetname);
		
		// create world
		world = new World(worldWidth, worldHeight);
		
		// calculate world bounds
		worldWidthPixels = worldWidth * SPRITESIZE + worldWidth * World.tileGap;
		worldHeightPixels = worldHeight * SPRITESIZE + worldHeight * World.tileGap;
		
		// get position
		// return world positions 0, 1 
		// and tile positions 2, 3
		int[] pos = world.GetFreePosition();
		
		Coordinate playerWorldPos = new Coordinate(pos[0], pos[1]);
		Coordinate playerTilePos = new Coordinate(pos[2], pos[3]);
		
		// create player and add it to our handler.
		player = new Player(playerWorldPos, playerTilePos, SpriteType.Player, 300, 100);
		
		// create enemies
		for(int i = 0; i < 10; i++) {
			
			// get position
			int[] epos = world.GetFreePosition();
			
			// creates a list of enemy sprites
			EnemyType randomType = EnemyType.values()[Util.GetRandomInteger(0, EnemyType.values().length)];
			SpriteType spriteType = null;
			
			switch(randomType) {
			case Skeleton:
				spriteType = SpriteType.Skeleton01;
				break;
			case Zombie:
				spriteType = SpriteType.Zombie01;
				break;
			case Maggot:
				spriteType = SpriteType.Maggot01;
				break;
			default:
				System.out.println("SPRITETYPE NOT FOUND FOR ENEMYTYPE: " + randomType);
				break;
			}
			
			Coordinate enemyWorldPos = new Coordinate(epos[0], epos[1]);
			Coordinate enemyTilePos = new Coordinate(epos[2], epos[3]);
			
			// create enemy object
			new Enemy(enemyWorldPos, enemyTilePos, 300, randomType, spriteType, 100);
		}
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
	
	// Taken from Ryan van Zeben - Java Game Engine Development video series.
	// https://www.youtube.com/watch?v=VE7ezYCTPe4&list=PL8CAB66181A502179
	public void run() {
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(isRunning) {
			
			// calculate delta time between last time and the current time.
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			// tick the whole system.
			while(delta >= 1) {
				tick();
				delta--;
			}
			
			// render the scene
			if(isRunning) {
				render();
			}
			
			// increment frame
			frames++;
			
			// print frame count
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		Stop();
	}
	
	public SpriteCreator GetSpriteCreator() {
		return this.spritecreator;
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
		
		// set background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// zoom
		g2d.scale(CAMERAZOOM, CAMERAZOOM);
		
		// player is the origin of the graphics
		// --> camera follow effect
		if(player != null) {
			
			int targety = (-player.GetWorldPosition().getY() * CAMERAZOOM - (SPRITESIZE - HEIGHT / 2)) / CAMERAZOOM;
			int targetx = (-player.GetWorldPosition().getX() * CAMERAZOOM - (SPRITESIZE - WIDTH / 2)) / CAMERAZOOM;
			
			// translate
			g.translate(targetx, targety);
			
			// update 'camera' position
			camera.setBounds(-targetx - SPRITESIZE, -targety - SPRITESIZE, SPRITESIZE * 21, SPRITESIZE * 12);
		}
		
		// render objects on top of it.
		handler.render(g);
		
		// render GUI
		// after everything else.
		// TODO
		
		g.dispose();
		bs.show();
	}

	private void tick() {
		handler.tick();
	}

	public static void main(String args[]) {
		new Game();
	}
	
	public Handler GetHandler() {
		return this.handler;
	}
	
	public World GetWorld() {
		return this.world;
	}
}
