package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class UI {

	GamePanel gp;
	Font font;
	Graphics2D g2;
	public int commandNum = 0;
	public int titleScreenState = 0; // 0: the first screen 1: second
	int subState = 0;
	public BufferedImage image;
	
	ArrayList<String> message  = new ArrayList<>();
	ArrayList<Integer> messageCounter  = new ArrayList<>();
	
	
	public int timerBarMaxWidth;
	public int timerBarHeight = 30;
	
	
	private float cursorAnimTimer = 0f;
	private static final float CURSOR_ANIM_SPEED   = 0.1f;  // how fast it oscillates
	private static final float CURSOR_ANIM_AMPLITUDE = 5f;  // max pixels offset
	
	private float splashAlpha = 0f;
	private boolean splashDone = false;
	private int splashTimer = 0;
	private final int splashDuration = 180; // 3 seconds at 60 FPS


	float titleAlpha = 0f;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		font = new Font("Press Start 2P", Font.PLAIN, 15);	
	}
	public void draw (Graphics2D g2) {
		this.g2 = g2;
		
		if (gp.gameState == gp.splashState) {
		    drawSplashScreen();
		}
		
		
		cursorAnimTimer += CURSOR_ANIM_SPEED;
	    // Keep it from growing unbounded
	    if (cursorAnimTimer > 2 * Math.PI) {
	        cursorAnimTimer -= 2 * Math.PI;
	    }
	  	
	    
		if (gp.gameState == gp.titleState) {
			drawTitleScreen();
		}
		
		if (gp.gameState == gp.playState) {
			drawPlayState();
			drawMessage();
		}
		if (gp.gameState == gp.pauseState) {
			drawPauseScreen();
		}
		if(gp.gameState == gp.optionState) {
			drawOptionScreen();
		}
		if(gp.gameState == gp.gameOverState) {
			drawGameOverScreen();
		}
		if(gp.gameState == gp.winState) {
			drawWinScreen();
		}
		
	}
	
	public void addMessage(String text) {

		message.add(text);
		messageCounter.add(0);
	}
	
	public void drawMessage() {
		
		int messageX = (int)(gp.tileSize*0.5);
		int messageY = gp.tileSize*4;
		
		g2.setFont(font = new Font("Press Start 2P", Font.PLAIN, 12));
		
		for (int i =0; i<message.size(); i++){
	
			if (message.get(i) != null) {
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX+2, messageY+2);
				
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);
				
				int counter = messageCounter.get(i) + 1;
				messageCounter.set(i,counter);
				messageY += 30;
				
				if(messageCounter.get(i)> 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}

		}
	}
	
	
	public void drawTitleScreen() {
	    Color topColor = new Color(34, 177, 76);
	    Color bottomColor = new Color(0, 128, 0);
	    GradientPaint gradient = new GradientPaint(0, 0, topColor, 0, gp.screenHeight, bottomColor);
	    

	    g2.setPaint(gradient);
	    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

	    switch (titleScreenState) {
	        case 0: drawMainTitleScreen(); break;
	        case 1: drawMapSelectionScreen(); break;
	        case 2: drawRulesScreen(); break;
	        case 3: drawDifficultyScreen(); break;
	        case 4: drawEducationalStuff();
	    }
   }
  
  private void drawMainTitleScreen() {
	  
	  
      g2.setFont(new Font("Press Start 2P", Font.BOLD, 50));
      String text = "Recycle Rescue";
      int x = getXCordForMid(text);
      int y = gp.tileSize*3; 
      
      g2.setColor(new Color(54, 54, 54,100));
      g2.drawString(text, x + 5, y + 5);

      g2.setColor(Color.white);
      g2.drawString(text, x, y);

      x = gp.screenWidth / 2 - gp.tileSize;
      y += gp.tileSize * 1.2;
      
      g2.setColor(new Color(54, 54, 54,100));
      g2.fillOval(x-1,y + 85,  gp.tileSize * 2, 14);
      
      BufferedImage image = gp.player.setup("Garbage_Goober_FrontStill");
      g2.drawImage(image, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
      

      g2.setFont(new Font("Press Start 2P", Font.PLAIN, 20));

      drawMenuItem("Start Game", 0, y += gp.tileSize * 3);
      drawMenuItem("Rules", 1, y += gp.tileSize);
      drawMenuItem("Learn More About Recycling" , 2,y += gp.tileSize);
      drawMenuItem("Quit", 3, y += gp.tileSize);

      g2.setFont(new Font("Press Start 2P", Font.PLAIN, 14));
      g2.setColor(new Color(186, 186, 186, 150));
      text = "Use WASD to navigate the menus, Enter to select";
      x = getXCordForMid(text);
      g2.drawString(text, x, gp.screenHeight - gp.tileSize);
      
  }
  
  private void drawDifficultyScreen() {
	  g2.setFont(new Font("Press Start 2P", Font.BOLD, 30));
      g2.setColor(Color.white);

      String text = "Choose Difficulty!";
      int x = getXCordForMid(text);
      int y = gp.tileSize * 3;
      g2.drawString(text, x, y);
      
      
      try {
	    	 image = ImageIO.read(getClass().getResourceAsStream("/objects/Easy icon.png"));
	    } catch (IOException e) {
	    	 e.printStackTrace();
	     }
      

      g2.setFont(new Font("Press Start 2P", Font.PLAIN, 25));
      
      text = "Easy";
	     x = gp.tileSize*4;
	     y += (int)(gp.tileSize*5); 
	     g2.drawString(text, x, y);
	     if (commandNum == 0) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }	     
	     g2.setColor(Color.white);
	     text = "Hard";
	     x += gp.tileSize*5;
	     g2.drawString(text, x, y);
	     if (commandNum == 1) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }	     
      
	     
	     g2.setColor(Color.white);
	     text = "Back";
	     x = gp.screenWidth-gp.tileSize*4;
	     y+= gp.tileSize*2;
	     g2.drawString(text, x, y);
	     if (commandNum == 2) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }	 
	     
	     
	     x = (int)(gp.tileSize*3.5);
	     y -= (int)(gp.tileSize*6);  
	     g2.drawImage(image,x, y, gp.tileSize * 3, gp.tileSize * 3,null);
	     
	     
	     try {
	    	 image = ImageIO.read(getClass().getResourceAsStream("/objects/Hard icon.png"));
	    } catch (IOException e) {
	    	 e.printStackTrace();
	     }
	     
	     x += (int)(gp.tileSize*5); 
	     g2.drawImage(image,x, y, gp.tileSize * 3, gp.tileSize * 3,null);
	     
	    
	     
	  
  }

  private void drawMenuItem(String text, int index, int y) {
      int x = getXCordForMid(text);
      g2.setColor(Color.white);
      g2.drawString(text, x, y);
      if (commandNum == index) {
    	  g2.setColor(Color.yellow);
    	  int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
          g2.drawString(">", x - gp.tileSize + dx, y);
          g2.setColor(Color.white);
      }
  }

  private void drawMapSelectionScreen() {
      g2.setFont(new Font("Press Start 2P", Font.BOLD, 30));
      g2.setColor(Color.white);

      String text = "Choose Map!";
      int x = getXCordForMid(text);
      int y = gp.tileSize * 3;
      g2.drawString(text, x, y);
      
      
      try {
	    	 image = ImageIO.read(getClass().getResourceAsStream("/objects/Map 1.png"));
	    } catch (IOException e) {
	    	 e.printStackTrace();
	     }
      

      g2.setFont(new Font("Press Start 2P", Font.PLAIN, 25));
      
      text = "Map 1";
	     x = gp.tileSize*2;
	     y += (int)(gp.tileSize*5); 
	     g2.drawString(text, x, y);
	     if (commandNum == 0) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }	     
	     g2.setColor(Color.white);
	     text = "Map 2";
	     x = getXCordForMid(text);
	     g2.drawString(text, x, y);
	     if (commandNum == 1) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }	     
      
	     g2.setColor(Color.white);
	     text = "Map 3";
	     x += gp.tileSize*5;
	     g2.drawString(text, x, y);
	     if (commandNum == 2) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }	 
	     
	     g2.setColor(Color.white);
	     text = "Back";
	     x = gp.screenWidth-gp.tileSize*4;
	     y+= gp.tileSize*2;
	     g2.drawString(text, x, y);
	     if (commandNum == 3) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }	 
	     
	     
	     x = (int)(gp.tileSize*1.75);
	     y -= (int)(gp.tileSize*6);  
	     g2.drawImage(image,x, y, gp.tileSize * 3, gp.tileSize * 3,null);
	     
	     
	     try {
	    	 image = ImageIO.read(getClass().getResourceAsStream("/objects/Map 2.png"));
	    } catch (IOException e) {
	    	 e.printStackTrace();
	     }
	     
	     x += (int)(gp.tileSize*4.75); 
	     g2.drawImage(image,x, y, gp.tileSize * 3, gp.tileSize * 3,null);
	     
	     
	     try {
	    	 image = ImageIO.read(getClass().getResourceAsStream("/objects/Map 3.png"));
	    } catch (IOException e) {
	    	 e.printStackTrace();
	     }
	     
	     x = gp.screenWidth-(int)(gp.tileSize*4.5);
	     g2.drawImage(image,x, y, gp.tileSize * 3, gp.tileSize * 3,null);
	     
  }

  	private void drawRulesScreen() {
      g2.setColor(Color.white);
      g2.setFont(new Font("Press Start 2P", Font.BOLD, 30));

      String text = "Rules";
      int x = getXCordForMid(text);
      int y = gp.tileSize * 3;
      g2.drawString(text, x, y);

      g2.setFont(new Font("Press Start 2P", Font.PLAIN, 15));
      String[] rules = {
          "1. Collect Trash Before Time Runs Out!",
          "2. Match the Bin Color to the Trash Type!",
          "3. Wrong matches result in penalties!",
          "• Green Bin = Organic Waste",
          "• Blue Bin = Recyclables",
          "• Yellow Bin = Metals",
          "Back"
      };

      for (String rule : rules) {
          y += gp.tileSize;
          x = getXCordForMid(rule);
          g2.drawString(rule, x, y);
      }
      g2.setColor(Color.yellow);
      int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
      g2.drawString(">", x - gp.tileSize + dx, y);
  	}

  	private void drawEducationalStuff() {
  		

			if (subState == 0) {
			 g2.setFont(new Font("Press Start 2P", Font.BOLD, 25));
			 g2.setColor(Color.white);
		     String text = "Why Recycling Is Important";
		     int x = getXCordForMid(text);
		     int y = gp.tileSize*2; 
		      
		     g2.drawString(text, x, y);
		     
		     x = gp.screenWidth / 2 - (int)(gp.tileSize*1.5);
		     y += gp.tileSize ;
		     
		   
		     try {
		    	 image = ImageIO.read(getClass().getResourceAsStream("/objects/Earth 1.png"));
		     } catch (IOException e) {
		    	 e.printStackTrace();
		     }
		   
		   
		     g2.drawImage(image, x, y, gp.tileSize * 3, gp.tileSize * 3, null);
		     g2.setColor(Color.white);
		     
		     g2.setFont(new Font("Press Start 2P", Font.PLAIN, 16));
		     text = "Trash is hurting Earth and its inhabitants.";
		     x = getXCordForMid(text);
		     y += gp.tileSize*4; 
		     g2.drawString(text, x, y);
		     
		     
		     text = "When we recycle, we help maintain our planet.";
		     x = getXCordForMid(text);
		     y += gp.tileSize; 
		     g2.drawString(text, x, y);
	     
	     text = "It’s an easy way to make a big difference!";
	     x = getXCordForMid(text);
	     y += gp.tileSize; 
	     g2.drawString(text, x, y);
	     
	     text = "Back";
	     x = gp.tileSize*2;
	     y += (int)(gp.tileSize*1.5); 
	     g2.drawString(text, x, y);
	     if (commandNum == 0) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }	     
	     
	     g2.setColor(Color.white);
	     text = "Next";
	     x = gp.screenWidth-gp.tileSize*3;
	     g2.drawString(text, x, y);
	     if (commandNum == 1) {
	    	 g2.setColor(Color.yellow);
	    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	    	 g2.drawString(">", x - gp.tileSize + dx, y);
	     }
	     
			}
			if (subState == 1) {
	  			 g2.setFont(new Font("Press Start 2P", Font.BOLD, 25));
	  			 g2.setColor(Color.white);
	  		     String text = "How Recycling Helps";
	  		     int x = getXCordForMid(text);
	  		     int y = gp.tileSize*2; 
	  		      
	  		     g2.drawString(text, x, y);
	  		     
	  		     x = gp.screenWidth / 2 - (int)(gp.tileSize*1.5);
	  		     y += gp.tileSize ;
	  		     
	  		   
	  		     try {
	  		    	 image = ImageIO.read(getClass().getResourceAsStream("/objects/Earth 2.png"));
	  		     } catch (IOException e) {
	  		    	 e.printStackTrace();
	  		     }
	  		   
	  		   
	  		     g2.drawImage(image, x, y, gp.tileSize * 3, gp.tileSize * 3, null);
	  		     g2.setColor(Color.white);
	  		     
	  		     g2.setFont(new Font("Press Start 2P", Font.PLAIN, 16));
	  		     text = "When you sort trash the right way";
	  		     x = getXCordForMid(text);
	  		     y += gp.tileSize*4; 
	  		     g2.drawString(text, x, y);
	  		     
	  		 
	  		     text = "it is then reused to make new things.";
			     x = getXCordForMid(text);
			     y += gp.tileSize; 
			     g2.drawString(text, x, y);
			     
			     text = "This saves energy and helps Mother Nature.";
			     x = getXCordForMid(text);
			     y += gp.tileSize; 
			     g2.drawString(text, x, y);
			     
			   text = "Back";
			   x = gp.tileSize*2;
			   y += (int)(gp.tileSize*1.5); 
			   g2.drawString(text, x, y);
			   if (commandNum == 0) {
		    	 g2.setColor(Color.yellow);
		    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
		    	 g2.drawString(">", x - gp.tileSize + dx, y);
			   }		     
		     
			   g2.setColor(Color.white);
			   text = "Next";
			   x = gp.screenWidth-gp.tileSize*3;
			   g2.drawString(text, x, y);
			   if (commandNum == 1) {
		    	 g2.setColor(Color.yellow);
		    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
		    	 g2.drawString(">", x - gp.tileSize + dx, y);
		     	
	  			}
			}
			if (subState == 2) {
	  			 g2.setFont(new Font("Press Start 2P", Font.BOLD, 25));
	  			 g2.setColor(Color.white);
	  		     String text = "Time To Learn!";
	  		     int x = getXCordForMid(text);
	  		     int y = gp.tileSize*2; 
	  		      
	  		     g2.drawString(text, x, y);
	  		     
	  		     x = gp.screenWidth / 2 - (int)(gp.tileSize*1.5);
	  		     y += gp.tileSize ;
	  		     
	  		   
	  		     try {
	  		    	 image = ImageIO.read(getClass().getResourceAsStream("/objects/Earth 3.png"));
	  		     } catch (IOException e) {
	  		    	 e.printStackTrace();
	  		     }
	  		   
	  		   
	  		     g2.drawImage(image, x, y, gp.tileSize * 3, gp.tileSize * 3, null);
	  		     g2.setColor(Color.white);
	  		     
	  		     g2.setFont(new Font("Press Start 2P", Font.PLAIN, 16));
	  		     text = "This game will teach you proper recycling.";
	  		     x = getXCordForMid(text);
	  		     y += gp.tileSize*4; 
	  		     g2.drawString(text, x, y);
	  		    
	  		     text = "Your actions matter — even the small ones. :)";
			     x = getXCordForMid(text);
			     y += gp.tileSize; 
			     g2.drawString(text, x, y);
			     
			     text = "Now Go Learn!";
			     x = getXCordForMid(text);
			     y += gp.tileSize; 
			     g2.drawString(text, x, y);
			     
			    text = "Back";
			     x = gp.tileSize*2;
			     y += (int)(gp.tileSize*1.5); 
			     g2.drawString(text, x, y);
			     if (commandNum == 0) {
			    	 g2.setColor(Color.yellow);
			    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			    	 g2.drawString(">", x - gp.tileSize + dx, y);
			     }	     
			     
			     g2.setColor(Color.white);
			     text = "Main Menu";
			     x = gp.screenWidth-(int)(gp.tileSize*4.5);
			     g2.drawString(text, x, y);
			     if (commandNum == 1) {
			    	 g2.setColor(Color.yellow);
			    	 int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			    	 g2.drawString(">", x - gp.tileSize + dx, y);
			     }
			}
  		
  	}
	
 
  
	public void drawPauseScreen() {
		g2.setColor(Color.white);
		g2.setFont(new Font("Press Start 2P", Font.PLAIN,16));
		String text = "PAUSED";
		String text2 = "PRESS ESC TO RESUME";
		String text3 = "PRESS O TO GO TO OPTIONS MENU";
		
		int x1 = getXCordForMid(text);
		int x2 = getXCordForMid(text2);
		int x3 = getXCordForMid(text3);
		int y = gp.screenHeight/2;
		
		int frameX = (int)(gp.tileSize*2.5); 
		int frameY = (int)(gp.tileSize*3.5);
		int frameWidth = gp.tileSize*11;
		int frameHeight = gp.tileSize*5;
		
		drawSubWindow(frameX,frameY,frameWidth,frameHeight);
		
		g2.drawString(text, x1, y-50);
		g2.drawString(text2, x2, y);
		g2.drawString(text3, x3, y+50);
	}
	
	public void drawOptionScreen() {
		
		g2.setColor(Color.white);
		g2.setFont(new Font("Press Start 2P", Font.PLAIN, 15));
		
		
		// window
		int frameX = gp.tileSize*4; 
		int frameY = gp.tileSize;
		int frameWidth = gp.tileSize*8;
		int frameHeight = gp.tileSize*10;
		
		drawSubWindow(frameX,frameY,frameWidth,frameHeight);
		
		switch(subState) {
		case 0:  optionsTop(frameX, frameY); break;
		case 1:  optionsControls(frameX, frameY);break;
		case 2:  optionsEndgameCon(frameX, frameY); break;
		
		}
		gp.key.enterPressed =false;
	}
	
	
	public void optionsTop(int x, int y) {
		
		int textX;
		int textY;
		
		//title
		String text = "Options";
		textX = getXCordForMid(text);
		textY = y + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		//music
		textX =  x + gp.tileSize;
		textY +=gp.tileSize *2;
		g2.drawString("Music", textX, textY);
		if (commandNum == 0) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	        g2.drawString(">", textX-25 + dx, textY);
		}
		//se
		textY +=gp.tileSize;
		g2.drawString("SFX", textX, textY);
		if (commandNum == 1) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			g2.drawString(">", textX-25 + dx, textY);
		}
		//control
		textY +=gp.tileSize;
		g2.drawString("Controls", textX, textY);
		if (commandNum == 2) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			g2.drawString(">", textX-25 + dx, textY);
			if (gp.key.enterPressed) {
				subState = 1;
				commandNum = 0;
			}
		}
		//quit
		textY +=gp.tileSize;
		g2.drawString("End Game", textX, textY);
		if (commandNum == 3) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			g2.drawString(">", textX-25 + dx, textY);
			if (gp.key.enterPressed) {
				subState = 2;
				commandNum = 1;
			}
		}
		
		//back
		textY +=gp.tileSize*2;
		g2.drawString("Back", textX, textY);
		if (commandNum == 4) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			g2.drawString(">", textX-25 + dx, textY);
			if (gp.key.enterPressed) {
				gp.gameState = gp.pauseState;
			}
		}
		
		
		//Music
		
		textX = x + (int)(gp.tileSize*5.5);
		textY = y + gp.tileSize*2 + 24;
		g2.drawRect(textX, textY, 85 ,25);
		
		int volumeWidth = 17 *gp.music.volumeScale;
		g2.fillRect(textX,textY, volumeWidth, 24);
		
		//SFX
		
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 85 ,25);
		volumeWidth = 17 *gp.sound.volumeScale;
		g2.fillRect(textX,textY, volumeWidth, 24);
		
		
	}
	
	public void optionsControls(int x, int y) {
		
		int textX;
		int textY;
		
		String text = "Controls";
		textX = getXCordForMid(text);
		textY = y + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		textX = x + gp.tileSize;
		textY += gp.tileSize; 
		g2.drawString("Move", textX, textY);textY += gp.tileSize;
		g2.drawString("Confirm", textX, textY);textY += gp.tileSize; 
		g2.drawString("Switch Bins", textX, textY);textY += gp.tileSize; 
		g2.drawString("Pause/Resume", textX, textY);textY += gp.tileSize; 
		g2.drawString("Options", textX, textY);textY += gp.tileSize; 
		
		textX = x + (int)(gp.tileSize*5.5);
		textY = y + gp.tileSize*2;
		g2.drawString("WASD", textX, textY);textY += gp.tileSize;
		g2.drawString("ENTER", textX, textY);textY += gp.tileSize;
		g2.drawString("E", textX, textY);textY += gp.tileSize;
		g2.drawString("ESC", textX, textY);textY += gp.tileSize;
		g2.drawString("O", textX, textY);textY += 33;
		textX-= 80;
		g2.drawString("(WHEN PAUSED)", textX, textY);textY += gp.tileSize;
		
		textX = x + gp.tileSize;
		textY = y + gp.tileSize*9; 
		g2.drawString("Back", textX, textY);
		if (commandNum == 0){
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			g2.drawString(">", textX-25 + dx, textY);
			if (gp.key.enterPressed) {
				subState = 0;
				commandNum = 2;
			}
		}
		
	}
	
	public void optionsEndgameCon(int x, int y) {
		int textX = x+gp.tileSize;
		int textY = y + gp.tileSize*3;
		
		
		g2.drawString("Quit the game?", textX, textY);
		
		//y
		
		String text = "Yes";
		textX = getXCordForMid(text);
		textY += gp.tileSize*3;
		g2.drawString(text, textX, textY);
		if (commandNum == 0) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			g2.drawString(">", textX-25 + dx, textY);
			if (gp.key.enterPressed) {
				subState=0;
				
				gp.stopMusic();
				gp.player.colorIndicator = 1;
				gp.player.getPlayerImage();
				gp.ui.commandNum=0;
				
				titleScreenState = 0;
				gp.gameState = gp.titleState;
			}
		}
		
		//n
		text = "No";
		textX = getXCordForMid(text);
		textY += gp.tileSize;
		g2.drawString(text, textX, textY);
		if (commandNum == 1) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
			g2.drawString(">", textX-25 + dx, textY);
			if (gp.key.enterPressed) {
				subState=0;
				commandNum = 3;
			}
		}
		
	}
	
	public void drawPlayState() {
		
		g2.setFont(font);
		g2.setColor(Color.white);
		g2.setFont(new Font("Press Start 2P", Font.BOLD, 20));
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/Trash icon.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		g2.drawImage(image, 40, 30, gp.tileSize,gp.tileSize, null);
		g2.drawString(gp.player.trash + "/" + (gp.obj.length-4), 100, 70);
		
		  int x ;
		  int y = 30;
		  
		  float actualTime;
		  
		  if (gp.key.isHard) {
			  x = 616;
			  timerBarMaxWidth =134;
			  actualTime = 40f;
		  }else{
			  x=550;
			  timerBarMaxWidth =200;
			  actualTime = 60f;
		  }

		    // Background bar
		    g2.setColor(new Color(0,0,0,220));
		    g2.fillRect(x, y, timerBarMaxWidth, timerBarHeight);

		    // Foreground bar (fill based on time left)
		    float timePercent = (float) gp.timeLeft / actualTime;
		    int barWidth = (int) (timerBarMaxWidth * timePercent);
		    
		    g2.setColor(Color.green);
		    if (timePercent < 0.3f) {
		    	g2.setColor(Color.red);       
		    }else if (timePercent < 0.6f) {
		    	g2.setColor(Color.orange);
		    }

		    g2.fillRect(x, y, barWidth, timerBarHeight);

		    // Optional: draw border
		    g2.setColor(Color.black);
		    g2.setStroke(new BasicStroke(3));
		    g2.drawRect(x, y, timerBarMaxWidth, timerBarHeight);
		
		
	}
	
	public void drawGameOverScreen() {

		gp.stopMusic();
		
		g2.setColor(new Color (0,0,0,150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		g2.setColor(Color.black);
		g2.setFont(new Font("Press Start 2P", Font.BOLD, 40));
		
		
		String text = "Game Over";
		int x = getXCordForMid(text);
		int y = gp.tileSize*4;
		
		g2.drawString(text, x,y);
		g2.setColor(Color.white);
		g2.drawString(text, x-4,y-4);
		
		
		//try again
		g2.setFont(new Font("Press Start 2P", Font.PLAIN, 30));
		text = "Try Again";
		x = getXCordForMid(text);
		y += gp.tileSize*4;
		g2.drawString(text, x,y);
		
		if (commandNum == 0) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	        g2.drawString(">", x - gp.tileSize + dx, y);
		}
		
		//back
		text = "Leave";
		x = getXCordForMid(text);
		y += 55;
		g2.drawString(text, x,y);
		if (commandNum == 1) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	        g2.drawString(">", x - gp.tileSize + dx, y);
		}
	}
	
	public void drawWinScreen() {
		
		gp.stopMusic();
		
		g2.setColor(new Color (0,0,0,150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		g2.setColor(Color.black);
		g2.setFont(new Font("Press Start 2P", Font.BOLD, 40));
		
		int x,y;
		String text;
		
		if (gp.player.canWin) {
			text = "You Win";
			x = getXCordForMid(text);
			y = gp.tileSize*4;
		
			g2.drawString(text, x,y);
			g2.setColor(Color.white);
			g2.drawString(text, x-4,y-4);
		
		}else {
			g2.setFont(new Font("Press Start 2P", Font.BOLD, 27));
			
			text = "You Collected The Garbage";
			x = getXCordForMid(text);
			y = gp.tileSize*4;
			
			g2.drawString(text, x,y);
			g2.setColor(Color.white);
			g2.drawString(text, x-4,y-4);
			
			text = "But Didn't Sort It Properly";
			x = getXCordForMid(text);
			y += gp.tileSize;
			g2.setColor(Color.black);
			g2.drawString(text, x,y);
			g2.setColor(Color.white);
			g2.drawString(text, x-4,y-4);
		}
		
		//try again
		g2.setFont(new Font("Press Start 2P", Font.PLAIN, 25));
		text = "Try Again";
		x = getXCordForMid(text);
		y += gp.tileSize*4;
		g2.drawString(text, x,y);
		
		if (commandNum == 0) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	        g2.drawString(">", x - gp.tileSize + dx, y);
		}
		
		//back
		text = "Leave";
		x = getXCordForMid(text);
		y += 55;
		g2.drawString(text, x,y);
		if (commandNum == 1) {
			int dx = (int)(Math.sin(cursorAnimTimer) * CURSOR_ANIM_AMPLITUDE);
	        g2.drawString(">", x - gp.tileSize + dx, y);
		}
	}
	
	public void drawSubWindow(int x ,int y, int w, int h) {
		
		Color c = new Color(0,0,0,220);
		g2.setColor(c);
		g2.fillRoundRect(x, y, w, h, 35,35);
		
		c = new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y +5 , w -10, h - 10, 25, 25);
	}
	
	public int getXCordForMid(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return gp.screenWidth/2 - length/2;
	}
	
	public void drawSplashScreen() {
	    splashTimer++;

	    // Fade in/out logic
	    if (splashTimer < splashDuration / 2) {
	        splashAlpha += 0.02f;
	    } else {
	        splashAlpha -= 0.02f;
	    }

	    // Clamp alpha
	    if (splashAlpha > 1.0f) splashAlpha = 1.0f;
	    if (splashAlpha < 0.0f) splashAlpha = 0.0f;

	    // Switch to title state once splash is done
	    if (splashTimer >= splashDuration) {
	        if (!splashDone) {
	        	gp.sound.setFile(0);
	        	gp.playSE(0);
	            gp.gameState = gp.titleState;
	            splashDone = true;
	        }
	        return;
	    }

	    Color topColor = new Color(34, 177, 76);
	    Color bottomColor = new Color(0, 128, 0);
	    GradientPaint gradient = new GradientPaint(0, 0, topColor, 0, gp.screenHeight, bottomColor);
	    

	    g2.setPaint(gradient);
	    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

	    // Draw text with fade
	    Composite original = g2.getComposite();
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, splashAlpha));

	    g2.setColor(Color.WHITE);
	    g2.setFont(new Font("Press Start 2P", Font.PLAIN, 15));
	    String text = "Ara Baghshetsyan presents";
	    int x = getXCordForMid(text);
	    int y = gp.screenHeight / 2;
	    g2.drawString(text, x, y);

	    g2.setComposite(original);
	    
	}

	
}
