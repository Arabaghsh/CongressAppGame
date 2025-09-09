package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.UtilityTool;

public class superObject {

	public BufferedImage image;
	public String name;
	public boolean collision = true;
	public int worldX, worldY;
	public Rectangle solidArea = new Rectangle(0,0,48,48);
	public int solidAreaDefaultX = 0;
	public int solidAreaDefaultY = 0;
	UtilityTool uTool = new UtilityTool();
	
	
	public void draw(Graphics2D g2, GamePanel gp) {
	    int screenX = worldX - gp.player.worldX + gp.player.screenX;
	    int screenY = worldY - gp.player.worldY + gp.player.screenY;

	    // Camera lock correction (top-left)
	    if (gp.player.worldX < gp.player.screenX) {
	        screenX = worldX;
	    }
	    if (gp.player.worldY < gp.player.screenY) {
	        screenY = worldY;
	    }

	    // Camera lock correction (bottom-right)
	    int rightOffset = gp.screenWidth - gp.player.screenX;
	    if (rightOffset > gp.worldWidth - gp.player.worldX) {
	        screenX = gp.screenWidth - (gp.worldWidth - worldX);
	    }

	    int bottomOffset = gp.screenHeight - gp.player.screenY;
	    if (bottomOffset > gp.worldHeight - gp.player.worldY) {
	        screenY = gp.screenHeight - (gp.worldHeight - worldY);
	    }


	    boolean isVisible =
	            worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
	            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
	            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
	            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY;

	    if (isVisible || playerNearWorldEdge(gp)) {
	        g2.drawImage(image, screenX, screenY, gp.tileSize , gp.tileSize,null);
	    }
	}

	private boolean playerNearWorldEdge(GamePanel gp) {
	    return gp.player.worldX < gp.player.screenX ||
	           gp.player.worldY < gp.player.screenY ||
	           gp.screenWidth - gp.player.screenX > gp.worldWidth - gp.player.worldX ||
	           gp.screenHeight - gp.player.screenY > gp.worldHeight - gp.player.worldY;
	}

	
}
