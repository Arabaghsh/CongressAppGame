package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_door extends superObject {
	
	GamePanel gp;
		
	public OBJ_door(String n, GamePanel gp, String imageName) {
		collision = true;
		
		name = n;
		
		try {
				
			image = ImageIO.read(getClass().getResourceAsStream("/objects/"+ imageName + ".png"));
			uTool.scaleImage(image, gp.tileSize, gp.tileSize);
				
		}catch(IOException e) {
			e.printStackTrace();
		}
			
	}

}
