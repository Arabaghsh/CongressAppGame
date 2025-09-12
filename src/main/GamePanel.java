package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Entity.Player;
import object.superObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	//SCREEN SETTINGS
	final int originalTileSize = 16; //16x16 tiles
	final int scaleValue = 3;
	
	public final int tileSize = originalTileSize * scaleValue; // (16x3) pixels per tile
	
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
	
	//WORLD SETTINGS
	
	public final int maxWorldCol = 37;
	public final int maxWorldRow = 30;
	
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	
		
	public int FPS = 60; //fps
	
	public TileManager tileM = new TileManager(this);
	
	public KeyHandler key = new KeyHandler(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public Sound sound = new Sound();
	public Sound music = new Sound();
	public UI ui = new UI(this);
	Thread gameThread;
	
	public Player player = new Player(this, key);
	public superObject obj[] = new superObject[16];
	
	//GAMESTATE
	public int gameState;
	public int splashState = -1;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int optionState = 3; 
	public final int gameOverState = 4;
	public final int winState = 5;
	
	//Timer
	public int timeLeft; 
	private long lastTimerCheck = System.nanoTime();
	private boolean timerActive = false;
	
	private double drawInterval;
	

	
	
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(key);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		aSetter.setObject();
		if (key.isHard) {
			timeLeft = 40;
		}else {
			timeLeft = 60;
		}
		lastTimerCheck = System.nanoTime();
		timerActive = true;

	}
	
	
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
		gameState = splashState;
		music.setFile(8);
	}
	
	public void run() {
		//Game Loop
		drawInterval = 1000000000/FPS; // 0.01666666 seconds
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (gameThread != null) {
			
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime)/drawInterval;
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}
	
	public void update() {
		
		if (gameState == playState) {
			player.update();
			
			if (timerActive) {
			    long currentTime = System.nanoTime();
			    if (currentTime - lastTimerCheck >= 1_000_000_000) { // 1 second
			        timeLeft--;
			        lastTimerCheck = currentTime;

			        if (player.trash == 12) {
			            timerActive = false;
			        	ui.commandNum=0;
			            gameState = winState;
			        }else if (timeLeft <= 0) {
			        	timerActive = false;
			            ui.commandNum=0;
			            gameState = gameOverState; 
			        				        	 
			        }
			    }
			}

		}
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//Title screen
		
		if (gameState == titleState) {
			ui.draw(g2);
		}else {
		
		tileM.draw(g2); // TILE
		
		
		for(int i = 0; i < obj.length;i++) {
			if (obj[i] != null) {
				obj[i].draw(g2,this); // OBJECT
			}
		}
		
		
		player.draw(g2);//PLAYER
		
		ui.draw(g2); //UI
		}
		
		g2.dispose();
		
	}
	
	public void playMusic(int i) {
		
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int i) {
		sound.setFile(i);
		sound.play();
	}

	public void lowFPSMode(boolean b) {
		if (b) {
			FPS = 18;
			player.speed = 12;
		}else {
			FPS = 60;
			player.speed = 4;
		}
		
		drawInterval = 1000000000/FPS;
	}

	public int getFPS() {
		return FPS;
	}
}










