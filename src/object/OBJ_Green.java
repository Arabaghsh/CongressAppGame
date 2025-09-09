package object;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Green extends superObject{
	
	GamePanel gp;

		public OBJ_Green(String n, GamePanel gp) {
			collision = true;
			name = n;
			
			try {
				
				image = ImageIO.read(getClass().getResourceAsStream("/objects/" + n + ".png"));
				uTool.scaleImage(image, gp.tileSize, gp.tileSize);
				
			}catch(IOException e) {
				e.printStackTrace();
			}
			
			
	}
	
}
