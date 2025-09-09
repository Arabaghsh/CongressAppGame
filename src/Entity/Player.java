package Entity;


import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{

	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	
	public int colorIndicator = 1;
	public String color = "green";
	public int trash = 0;
	public boolean canWin = true;
	private int sfxNum=0;
	private int sfxSwitchNum = 3;
	
	private int knockbackFrames = 0;
	private int knockbackDX = 0;
	private int knockbackDY = 0;

			
	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 -(gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle((gp.tileSize/2)-12,(gp.tileSize/2)-7,25,15);
		
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		setDefaultVals();
		getPlayerImage();
	}
	
	public void setDefaultVals() {
		speed = 4;
		dir = "down";
		worldX = gp.tileSize*21;
		worldY = gp.tileSize*22;
		
	}
	
	public void getPlayerImage() {
		
		if (colorIndicator == 1) {

				color = "green";
		
				up1 = setup("Garbage_Goober_Back1");
				up2 = setup("Garbage_Goober_Back2");
			
				down1 = setup("Garbage_Goober_Front1");
				down2 = setup("Garbage_Goober_Front2");
			
				left1 = setup("Garbage_Goober_Left1");
				left2 = setup("Garbage_Goober_Left2");
			
				right1 = setup("Garbage_Goober_Right1");
				right2 = setup("Garbage_Goober_Right2");
			
				upStill = setup("Garbage_Goober_BackStill");
				downStill = setup("Garbage_Goober_FrontStill");
				leftStill = setup("Garbage_Goober_LeftSill");
				rightStill = setup("Garbage_Goober_RightStill");
			
	
			
		}else if(colorIndicator == 2) {
			
				color = "blue";
			
				
				up1 = setup("Garbage_Goober_Blue_Back1");
				up2 = setup("Garbage_Goober_Blue_Back2");
				
				down1 = setup("Garbage_Goober_Blue_Front1");
				down2 = setup("Garbage_Goober_Blue_Front2");
				
				left1 = setup("Garbage_Goober_Blue_Left1");
				left2 = setup("Garbage_Goober_Blue_Left2");
				
				right1 = setup("Garbage_Goober_Blue_Right1");
				right2 = setup("Garbage_Goober_Blue_Right2");
				
				upStill = setup("Garbage_Goober_Blue_BackStill");
				downStill = setup("Garbage_Goober_Blue_FrontStill");
				leftStill = setup("Garbage_Goober_Blue_LeftStill");
				rightStill = setup("Garbage_Goober_Blue_RightStill");
				
			
		}else if(colorIndicator == 3) {
			
				color = "yellow";
			
				
				up1 = setup("Garbage_Goober_Yellow_Back1");
				up2 = setup("Garbage_Goober_Yellow_Back2");
				
				down1 = setup("Garbage_Goober_Yellow_Front1");
				down2 = setup("Garbage_Goober_Yellow_Front2");
				
				left1 = setup("Garbage_Goober_Yellow_Left1");
				left2 = setup("Garbage_Goober_Yellow_Left2");
				
				right1 = setup("Garbage_Goober_Yellow_Right1");
				right2 = setup("Garbage_Goober_Yellow_Right2");
				
				upStill = setup("Garbage_Goober_Yellow_BackStill");
				downStill = setup("Garbage_Goober_Yellow_FrontStill");
				leftStill = setup("Garbage_Goober_Yellow_LeftStill");
				rightStill = setup("Garbage_Goober_Yellow_RightStill");
				
			
		}
	}
	
	public BufferedImage setup(String imageName) {
		
		UtilityTool uTool = new UtilityTool();
		BufferedImage scaledImage = null;
		
		try {
			scaledImage = ImageIO.read(getClass().getResourceAsStream("/player/"+ imageName +".png"));
			scaledImage = uTool.scaleImage(scaledImage, gp.tileSize, gp.tileSize);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return scaledImage;
	}
	
	public void update() {
    int dx = 0;
    int dy = 0;

    if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
        spriteCounter++;
        isMoving = true;

        if (keyH.upPressed) {
            dy -= 1;
            dir = "up";
        }
        if (keyH.downPressed) {
            dy += 1;
            dir = "down";
        }
        if (keyH.leftPressed) {
            dx -= 1;
            dir = "left";
        }
        if (keyH.rightPressed) {
            dx += 1;
            dir = "right";
        }

    } else {
        isMoving = false;
    }

    if (keyH.changePressed) {
        gp.playSE(sfxSwitchNum);
        sfxSwitchNum++;
        if (sfxSwitchNum > 5) sfxSwitchNum = 0;

        colorIndicator++;
        if (colorIndicator > 3) colorIndicator = 1;

        getPlayerImage();
        keyH.changePressed = false;
    }

    // Handle knockback first
    if (knockbackFrames > 0) {
        // Predict next position
        int nextX = (int)(worldX + knockbackDX * 2.5);
        int nextY = (int)(worldY + knockbackDY * 2.5);

        // Store original position
        int oldX = worldX;
        int oldY = worldY;

        // Temporarily move to test collision
        worldX = nextX;
        worldY = nextY;

        collisionOn = false;
        gp.cChecker.checkTile(this);

        // Revert position
        worldX = oldX;
        worldY = oldY;

        if (!collisionOn) {
            // Apply knockback
            worldX += (int)(knockbackDX * 2.5);
            worldY += (int)(knockbackDY * 2.5);
            knockbackFrames--;
        } else {
            // Cancel knockback if blocked
            knockbackFrames = 0;
        }

    } else {
        // Normal movement
        collisionOn = false;
        gp.cChecker.checkTile(this);
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        if (!collisionOn && isMoving) {
            double moveX = dx;
            double moveY = dy;

            if (dx != 0 && dy != 0) {
                moveX *= 0.7071;
                moveY *= 0.7071;
            }

            worldX += moveX * speed;
            worldY += moveY * speed;
        }
    }

    // Handle sprite animation
    if (spriteCounter > 12) {
        spriteNum = (spriteNum == 1) ? 2 : 1;
        spriteCounter = 0;
    }
}

	
	public void pickUpObject(int i) {

	    if (i != 999) {

	        String objName = gp.obj[i].name;
	        

	        if (objName.equals("door")) {
	            gp.playSE(6);
	            gp.obj[i] = null;
	            return; 
	        }

	        switch (color) {
	            case "green":
	                if (objName.equals("Banana peel") || objName.equals("Apple Core") || objName.equals("Donut") || objName.equals("Fishbone")) {
	                    gp.playSE(sfxNum);
	                  
	                    sfxNum++;
	                    if (sfxNum > 2) {
	                    	sfxNum=0;
	                    }
	                    
	                    gp.obj[i] = null;
	                    trash++; 
	                    gp.ui.addMessage(objName + " collected!");
	                } else {
	                    gp.playSE(7);
	                    gp.obj[i] = null;
	                    trash++;
	                    gp.timeLeft -= 10;
	                    canWin = false;
	                    gp.ui.addMessage("Oops, wrong bin");
	                    knockBack();
	                }
	                break;

	            case "blue":
	                if (objName.equals("Water Bottle") || objName.equals("Plastic Bag") || objName.equals("Straw Cup") || objName.equals("Toothbrush")) {
	                    gp.playSE(sfxNum);
	                    
	                    sfxNum++;
	                    if (sfxNum > 2) {
	                    	sfxNum=0;
	                    }
	                    	                    
	                    gp.obj[i] = null;
	                    trash++;
	                    gp.ui.addMessage(objName + " collected!");
	                } else {
	                    gp.playSE(7);
	                    gp.obj[i] = null;
	                    trash++;
	                    gp.timeLeft -= 10;
	                    canWin = false;
	                    gp.ui.addMessage("Oops, wrong bin");
	                    knockBack();
	                }
	                break;

	            case "yellow":
	                if (objName.equals("Cola Can 1") || objName.equals("Cola Can 2") || objName.equals("Gear") || objName.equals("Battery")) {
	                    gp.playSE(sfxNum);
	                    
	                    sfxNum++;
	                    if (sfxNum > 2) {
	                    	sfxNum=0;
	                    }
	                    
	                    gp.obj[i] = null;
	                    trash++;
	                    gp.ui.addMessage(objName + " collected!");
	                } else {
	                    gp.playSE(7);
	                    gp.obj[i] = null;
	                    trash++;
	                    gp.timeLeft -= 10;
	                    canWin = false;
	                    gp.ui.addMessage("Oops, wrong bin");
	                    knockBack();
	                    
	                }
	                break;
	        }
	    }
	}

	
	public void knockBack() {

		switch (dir) {
	    case "up":
	        knockbackDX = 0;
	        knockbackDY = 1;
	        break;
	    case "down":
	        knockbackDX = 0;
	        knockbackDY = -1;
	        break;
	    case "left":
	        knockbackDX = 1;
	        knockbackDY = 0;
	        break;
	    case "right":
	        knockbackDX = -1;
	        knockbackDY = 0;
	        break;
		}
		if (gp.key.isHard) {
			knockbackFrames = 30;
		}else {
			knockbackFrames = 15;
		}
	
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;

		switch(dir) {
		case "up":
			
			if (!isMoving) {
				image = upStill;
			}else {
			
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
		}
			break;
		case "down":
			
			if (!isMoving) {
				image = downStill;
			}else {
			
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
			}
			break;
		case "right":
			
			if (!isMoving) {
				image = rightStill;
			}else {
			
				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}
			}
			break;
		case "left":
			
			if (!isMoving) {
				image = leftStill;
			}else {
			
				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}
			}
			break;
		}
		
		
		int x = screenX;
		int y = screenY;
		
		if (screenX>worldX) {
			x = worldX;
		}
		if (screenY > worldY) {
			y = worldY;
		}
		int rightOffset = gp.screenWidth - screenX;
		if (rightOffset > gp.worldWidth - worldX) {
			
			x = gp.screenWidth -( gp.worldWidth - worldX);
			
		}
		int bottomOffset = gp.screenHeight -screenY;
		if (bottomOffset > gp.worldHeight -worldY) {
			
			y = gp.screenHeight -( gp.worldHeight - worldY);
			
		}
		
		g2.drawImage(image, x, y, null);
	}
}
