package com.adventurer.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

import com.adventurer.gameobjects.Enemy;
import com.adventurer.gameobjects.Player;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = -5226776943692411279L;

	public static Game instance;
	
	public static final int WIDTH = 1280, HEIGHT = 720;
	public static final int SPRITESIZE = 16;
	public static final int CAMERAZOOM = 4; 
	public static final double FRAME_CAP = 60.0;
	
	public static final String spritesheetname = "spritesheet.png";
	
	private Thread thread;
	private boolean isRunning = false;
	
	public Rectangle camera = new Rectangle();
	
	// tiles
	private final int worldHeight = 20;
	private final int worldWidth = 30;
	
	public Game() {
		
		if(instance != null) return;
		
		instance = this;
		
		// create object handler
		Handler handler = new Handler();
		
		// create key listener for inputs.
		this.addKeyListener(new KeyInput(handler));
		
		// create mouse input object
		MouseInput mouseInput = new MouseInput();
		
		// create mouse listener
		this.addMouseMotionListener(mouseInput);
		this.addMouseListener(mouseInput);
		
		// create window 
		new Window(WIDTH, HEIGHT, "Adventurer", this);
		
		// create sprite creator
		new SpriteCreator(spritesheetname);
		
		// create world
		new World(worldWidth, worldHeight);
		
		// create player
		ActorManager.CreatePlayerInstance(300, 100);
		
		// create enemies
		ActorManager.CreateEnemies(10);
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
	// and originally taken from Notch's work.
	// Also applied stuff from https://www.youtube.com/watch?v=rwjZDfcQ7Rc&list=PLEETnX-uPtBXP_B2yupUKlflXBznWIlL5&index=3
	public void run() {
		
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
			
			unprocessedTime += passedTime / (double)SECOND;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime) {
				
				render = true;
				unprocessedTime -= frameTime;
				
				tick();
				
				if(frameCounter >= SECOND) {
					System.out.println("FPS: " + frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			
			// render the scene
			if(isRunning && render) {
				render();
				frames++;
				
			} else {
				/*try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
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
		
		// set background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// zoom
		g2d.scale(CAMERAZOOM, CAMERAZOOM);
		
		// camera follow
		Player player = ActorManager.GetPlayerInstance();
		if(player != null) {
			
			int targety = (-player.GetWorldPosition().getY() * CAMERAZOOM - (SPRITESIZE - HEIGHT / 2)) / CAMERAZOOM;
			int targetx = (-player.GetWorldPosition().getX() * CAMERAZOOM - (SPRITESIZE - WIDTH / 2)) / CAMERAZOOM;
			
			// translate
			g.translate(targetx, targety);
			
			// update 'camera' position
			camera.setBounds(-targetx - SPRITESIZE, -targety - SPRITESIZE, SPRITESIZE * 21, SPRITESIZE * 12);
		}
		
		// render objects on top of it.
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
