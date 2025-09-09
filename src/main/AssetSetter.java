package main;

import java.util.Arrays;

import object.OBJ_Blue;
import object.OBJ_Green;
import object.OBJ_Yellow;
import object.OBJ_door;
import object.superObject;



public class AssetSetter {

	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
		public void setObject() {
			
			Arrays.fill(gp.obj, null);
		
		    
		    if (gp.tileM.map == "/maps/World_01.txt") {
		    	gp.obj[12] = new OBJ_door("door",gp, "Door");
		    	gp.obj[12].worldX = gp.tileSize * 8;
		    	gp.obj[12].worldY = gp.tileSize * 9;
		    
		    	gp.obj[13] = new OBJ_door("door",gp, "Door");
		    	gp.obj[13].worldX = gp.tileSize * 28;
		    	gp.obj[13].worldY = gp.tileSize * 16;
		    
		    	
		    }else if (gp.tileM.map == "/maps/World_02.txt") {
		    	gp.obj[12] = new OBJ_door("door",gp,"LeftDoor");
		    	gp.obj[12].worldX = gp.tileSize * 21;
		    	gp.obj[12].worldY = gp.tileSize * 7;
		    
		    	gp.obj[13] = new OBJ_door("door",gp,"RightDoor");
		    	gp.obj[13].worldX = gp.tileSize * 30;
		    	gp.obj[13].worldY = gp.tileSize * 7;
		    	
		    
		    }else if (gp.tileM.map == "/maps/World_03.txt") {
		    	gp.obj[12] = new OBJ_door("door",gp,"LeftDoor");
		    	gp.obj[12].worldX = gp.tileSize * 25;
		    	gp.obj[12].worldY = gp.tileSize * 23;
		    
		    	gp.obj[13] = new OBJ_door("door",gp,"RightDoor");
		    	gp.obj[13].worldX = gp.tileSize * 29;
		    	gp.obj[13].worldY = gp.tileSize * 23;
		    	
		    	gp.obj[14] = new OBJ_door("door",gp,"LeftDoor");
		    	gp.obj[14].worldX = gp.tileSize * 7;
		    	gp.obj[14].worldY = gp.tileSize * 5;
		    
		    	gp.obj[15] = new OBJ_door("door",gp,"LeftDoor");
		    	gp.obj[15].worldX = gp.tileSize * 7;
		    	gp.obj[15].worldY = gp.tileSize * 6;
		    
		    }
		    
		    placeTrash(new OBJ_Green("Banana peel", gp), 0);
		    placeTrash(new OBJ_Blue("Water Bottle", gp), 1);
		    placeTrash(new OBJ_Blue("Plastic Bag", gp), 2);
		    placeTrash(new OBJ_Yellow("Cola Can 1", gp), 3);
		    placeTrash(new OBJ_Yellow("Cola Can 2", gp), 4);
		    placeTrash(new OBJ_Green("Apple Core", gp), 5);
		    placeTrash(new OBJ_Yellow("Gear", gp), 6);
		    placeTrash(new OBJ_Blue("Straw Cup", gp), 7);
		    placeTrash(new OBJ_Green("Donut", gp), 8);
		    placeTrash(new OBJ_Green("Fishbone", gp), 9);
		    placeTrash(new OBJ_Yellow("Battery", gp), 10);
		    placeTrash(new OBJ_Blue("Toothbrush", gp), 11);
		}

		private void placeTrash(superObject obj, int index) {
			 boolean validPosition = false;
		        int col = 0;
		        int row = 0;
		        int tileNum;

		        while (!validPosition) {
		            col = (int) (Math.random() * (gp.maxWorldCol - 3)) + 2;
		            row = (int) (Math.random() * (gp.maxWorldRow - 3)) + 2;

		            tileNum = gp.tileM.mapTileNum[col][row];
		            validPosition = true;

		            // Check if tile has collision
		            if (gp.tileM.tile[tileNum].collision) {
		                validPosition = false;
		                continue;
		            }

		            int x = col * gp.tileSize;
		            int y = row * gp.tileSize;

		            // Check against all already-placed objects
		            for (int i = 0; i < index; i++) {
		                if (gp.obj[i] != null && gp.obj[i].worldX == x && gp.obj[i].worldY == y) {
		                    validPosition = false;
		                    break;
		                }
		            }
		        }

		        obj.worldX = col * gp.tileSize;
		        obj.worldY = row * gp.tileSize;
		        gp.obj[index] = obj;
		}

}
