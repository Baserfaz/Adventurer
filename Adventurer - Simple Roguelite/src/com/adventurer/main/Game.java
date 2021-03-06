package com.adventurer.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;

import com.adventurer.data.Camera;
import com.adventurer.data.PredefinedMaps;
import com.adventurer.data.SaveFile;
import com.adventurer.data.Session;
import com.adventurer.data.World;
import com.adventurer.enumerations.GameState;
import com.adventurer.enumerations.GuiState;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;
import com.adventurer.utilities.Window;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -5226776943692411279L;

	public static Game instance;

	public static final int WIDTH                  = 1280;			           // viewport width
	public static final int HEIGHT                 = 720;                      // viewport height

	public static final int CAMERA_WIDTH           = 1273 / Game.CAMERAZOOM;
	public static final int CAMERA_HEIGHT          = 690 / Game.CAMERAZOOM;
	
	public static final int SPRITESIZE             = 16;                       // sprite size in pixels
	public static final int CAMERAZOOM             = 2;                        // level of zoom
	public static final double FRAME_CAP           = 60.0;                     // cap the framerate to this

	public static final String SPRITESHEETNAME     = "images/spritesheet_simple.png";  // name of the spritesheet
	public static final String BACKGROUNDNAME      = "images/background.jpg";		   // name of the main menu background
	public static final String FRAMICONPATH        = "resources/images/icon.png";      // path to frame icon.

	public static final String CUSTOMFONTNAME      = "coders_crux";		       // name of the custom font
	public static final String CUSTOMFONTEXTENSION = ".ttf";			       // file extension name
	public static final String CUSTOMFONTFOLDER    = "coders_crux";		       // folder name within 'resources/fonts/'

	public static final int BASEFONTSIZE 		   = 8;						   // base font size used when rendering strings.
	public static final int LINEHEIGHT			   = 2;						   // usage: renderer.renderString(): fontMetrics.getHeight() + lineheight

	//------------------------------
	// DEBUGGING TOOLS AND GAME SETTINGS

	// draw debugging stuff
	public static final boolean DRAW_CAMERA     = false;
	public static final boolean DRAW_ENEMY_FOV  = false;
	public static final boolean DRAW_ENEMY_PATH = false;

	// do we create lobby or random dungeon first.
	public static final boolean START_GAME_WITH_RANDOM_ROOM = false;

	// GUI SETTINGS
	public static final boolean AUTOMATICALLY_ESCAPE_FROM_INV_MODE_AFTER_SUCCESS = false;

	// LOS settings
	public static final boolean CALCULATE_PLAYER_LOS   = true;
	public static final boolean PERMANENTLY_SHOW_TILES = false;

	// Color data
	// rarity colors
	public static final Color RARITYCOLOR_JUNK      = new Color(159, 159, 146);
	public static final Color RARITYCOLOR_GENERIC   = new Color(255, 255, 255);
	public static final Color RARITYCOLOR_SUPERIOR  = new Color(37, 137, 189);
	public static final Color RARITYCOLOR_EPIC      = new Color(137, 99, 186);
	public static final Color RARITYCOLOR_LEGENDARY = new Color(249, 160, 63);

	// GUI colors
	public static final Color GUI_HEALTH         = new Color(153, 65, 65);
	public static final Color GUI_MANA           = new Color(57, 67, 183);
	public static final Color GUI_DMG_FIRE       = new Color(186, 33, 13);
	public static final Color GUI_DMG_FROST      = new Color(57, 67, 183);
	public static final Color GUI_DMG_SHOCK      = new Color(252, 191, 73);
	public static final Color GUI_DMG_HOLY       = new Color(253, 255, 252);
	public static final Color GUI_DMG_PHYSICAL   = new Color(100, 100, 100);

	// loot spawn chances
	public static final int CHEST_GOLD_CHANCE = 25;

	// tile settings
	public static final int TILEGAP = 2;

	// player defaults
	public static final int PLAYER_START_BASE_HEALTH	= 50;
	public static final int PLAYER_START_BASE_MANA		= 10;

	// player stat defaults
	public static final int DEFAULT_VITALITY_PLAYER 	= 1;
	public static final int DEFAULT_STRENGTH_PLAYER 	= 1;
	public static final int DEFAULT_DEXTERITY_PLAYER 	= 1;
	public static final int DEFAULT_INTELLIGENCE_PLAYER = 1;

	// stats multipliers
	public static final int VITALITY_TO_HEALTH_MULTIPLIER 			= 2;
	public static final int INTELLIGENCE_TO_MANA_MULTIPLIER 		= 2;

	public static final int STRENGTH_TO_MELEE_DAMAGE_MULTIPLIER 	= 2;
	public static final int DEXTERITY_TO_RANGED_DAMAGE_MULTIPLIER 	= 2;
	public static final int INTELLIGENCE_TO_MAGIC_DAMAGE_MULTIPLIER = 2;

	// player inventory settings
	public static final int DEFAULT_INVENTORY_MAX_SIZE  = 12; 
	public static final int START_KEY_COUNT         	= 1;
	public static final int START_DIAMOND_KEY_COUNT	 	= 0;
	public static final int START_BOMB_COUNT        	= 1;
	public static final int START_PROJECTILE_COUNT  	= 1;

	// world size
	public static final int WORLDHEIGHT = 30; 
	public static final int WORLDWIDTH  = 30;

	// room count
	public static final int ROOM_COUNT                = 20;
	public static final int ROOM_DOOR_MAX_COUNT       = 4;
	public static final int ROOM_TRY_GENERATION_COUNT = 500;

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
	private Session currentSession;
	private GameState gameState;
	private GuiState guiState;

	private String mainmenuSubtitle = "";

	private Font customFont = null;
	private Image backgroundImage = null;

	public Game() {

		if(instance != null) return;

		Game.instance = this;

		gameState = GameState.MainMenu;
		guiState = GuiState.None;

		// create object handler
		new Handler();

		// create new keyInput.
		KeyInput keyInput = new KeyInput();

		// create key listener for inputs.
		this.addKeyListener(keyInput);

		// create mouse input object
		MouseInput mouseInput = new MouseInput();

		// create mouse listener
		this.addMouseMotionListener(mouseInput);
		this.addMouseListener(mouseInput);

		// load custom font
		Util.loadCustomFont();

		// create window 
		window = new Window(WIDTH, HEIGHT, "Adventurer", this);

		// create sprite creator
		new SpriteCreator(SPRITESHEETNAME);

		// create camera
		new Camera();

		// read and cache save file.
		setCurrentSaveFile(new SaveFile());

		// create a funny subtitle :)
		this.setMainmenuSubtitle(Util.getRandomSubtitle());
	}

	public void startGame() {
		if(START_GAME_WITH_RANDOM_ROOM) new World(ROOM_COUNT);
		else createLobby();
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

		//-------------------------------------------
		// DRAW GRAPHICS HERE

		if(gameState == GameState.InGame) Renderer.renderInGame(g);
		else if(gameState == GameState.Loading || gameState == GameState.Ready) Renderer.renderLoading(g);
		else if(gameState == GameState.MainMenu) Renderer.renderMainMenu(g);

		// END DRAW
		//-------------------------------------------

		g.dispose();
		bs.show();
	}

	private void createLobby() { new World(PredefinedMaps.GetLobby()); }

	private void tick() { Handler.instance.tick(); }
	public static void main(String args[]) { new Game(); }

	public Window getWindow() { return this.window; }

	public GameState getGameState() { return this.gameState; }
	public void setGameState(GameState state) { this.gameState = state; }

	public SaveFile getCurrentSaveFile() { return currentSaveFile; }
	public void setCurrentSaveFile(SaveFile currentSaveFile) { this.currentSaveFile = currentSaveFile; }

	public Session getCurrentSession() { return currentSession; }
	public void setCurrentSession(Session currentSession) { this.currentSession = currentSession; }

	public Image getBackgroundImage() { return backgroundImage; }
	public void setBackgroundImage(Image backgroundImage) { this.backgroundImage = backgroundImage; }

	public GuiState getGuiState() { return guiState; }
	public void setGuiState(GuiState guiState) { this.guiState = guiState; }

	public Font getCustomFont() { return customFont; }
	public void setCustomFont(Font customFont) { this.customFont = customFont; }

	public String getMainmenuSubtitle() { return mainmenuSubtitle; }
	public void setMainmenuSubtitle(String mainmenuSubtitle) { this.mainmenuSubtitle = mainmenuSubtitle; }
}
