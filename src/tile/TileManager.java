package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	public String map;
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile [50]; 
		
		mapTileNum = new int [gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
//		loadMap(map);
	}
	
	public void getTileImage() {
		
		try {
			
			setup(0, "grass" , false);
			setup(1, "grass" , false);
			setup(2, "grass" , false);
			setup(3, "grass" , false);
			setup(4, "grass" , false);
			setup(5, "grass" , false);
			setup(6, "grass" , false);
			setup(7, "grass" , false);
			setup(8, "grass" , false);
			setup(9, "grass" , false);
			
			
			setup(10, "grass" , false);
			setup(11, "water8" , true);
			setup(12, "water" , true);
			setup(13, "tree" , true);
			setup(14, "wood" , false);
			setup(15, "wall" , true);
			setup(16,"carpet",false);
				
			
			
			setup(17, "water2" , true);
			setup(18, "water3" , true);
			setup(19, "water4" , true);
			setup(20, "water5" , true);
			setup(21, "water6" , true);
			setup(22, "water7" , true);
			setup(23, "water9" , true);
			setup(24, "water10" , true);
			setup(25, "water11" , true);
			setup(26, "water12" , true);
			setup(27, "water13" , true);
			setup(29, "water1" , true);
			
			
			
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setup(int i, String image, boolean col) {
		
		UtilityTool uTool = new UtilityTool();
		
		try {
			tile[i] = new Tile();
			tile[i].image = ImageIO.read(getClass().getResourceAsStream("/tile/" + image + ".png"));
			tile[i].image = uTool.scaleImage(tile[i].image , gp.tileSize, gp.tileSize);
			tile[i].collision = col;
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	
	public void loadMap(String map) {
		
		try{
			InputStream is = getClass().getResourceAsStream(map);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			while(col<gp.maxWorldCol && row < gp.maxWorldRow) {
				
				String line = br.readLine();
				while(col<gp.maxWorldCol) {
					
					String number[] = line.split(" ");
					
					int num = Integer.parseInt(number[col]);
					
					mapTileNum[col][row] = num;
					
					col++;
					
				}if (col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
				
			}
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
		
		public void draw(Graphics2D g2) {
			
			int worldCol = 0;
			int worldRow = 0;
			
			while(worldCol<gp.maxWorldCol && worldRow < gp.maxWorldRow) {
				
				int tileNum = mapTileNum[worldCol][worldRow];
				
				int worldX = worldCol * gp.tileSize;
				int worldY = worldRow * gp.tileSize;
				int screenX = worldX - gp.player.worldX + gp.player.screenX;
				int screenY = worldY - gp.player.worldY + gp.player.screenY;
				
				
				if (gp.player.screenX > gp.player.worldX) {
					
					screenX = worldX;
					
				}
				if (gp.player.screenY > gp.player.worldY) {
					
					screenY = worldY;
					
				}
				int rightOffset = gp.screenWidth - gp.player.screenX;
				if (rightOffset > gp.worldWidth - gp.player.worldX) {
					
					screenX = gp.screenWidth -( gp.worldWidth - worldX);
					
				}
				int bottomOffset = gp.screenHeight - gp.player.screenY;
				if (bottomOffset > gp.worldHeight - gp.player.worldY) {
					
					screenY = gp.screenHeight -( gp.worldHeight - worldY);
					
				}
				
				if (worldX + gp.tileSize> gp.player.worldX - gp.player.screenX && 
					worldX - gp.tileSize< gp.player.worldX + gp.player.screenX && 
					worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
					worldY - gp.tileSize< gp.player.worldY + gp.player.screenY ) {
					
					g2.drawImage(tile[tileNum].image, screenX,screenY, null);
					
				}else if (gp.player.screenX>gp.player.worldX || 
						  gp.player.screenY > gp.player.worldY ||
						  rightOffset > gp.worldWidth - gp.player.worldX || 
						  bottomOffset > gp.worldHeight - gp.player.worldY) {
					
					g2.drawImage(tile[tileNum].image, screenX,screenY,null);
					
				}
				
				worldCol++;
				
				if (worldCol == gp.maxWorldCol) {
					worldCol = 0;
					worldRow++;
				}
			}
			
		}
}
