package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool {

	public BufferedImage scaleImage(BufferedImage og, int w, int h) {
		
		BufferedImage scaledImage = new BufferedImage(w,h,og.getType());
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(og, 0, 0, w, h, null);
		g2.dispose();
		
		return scaledImage;
		
	}
	
	
}
