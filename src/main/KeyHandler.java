package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, changePressed, enterPressed;
    public GamePanel gp;
    private String currentMap;
    public boolean isHard;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

       
        handleMovementKeys(code);
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = false;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;

        if (gp.gameState == gp.playState && code == KeyEvent.VK_E) {
            changePressed = true;
        }
        if (gp.gameState == gp.titleState) {
            handleTitleState(code);
        }

        handlePauseAndOptionStates(code);
        handleGameOverOrWinState(code);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    // =====================
    // STATE HANDLERS
    // =====================

    private void handleTitleState(int code) {
        if (gp.ui.titleScreenState == 0) {
            handleMainMenu(code);
        } else if (gp.ui.titleScreenState == 1) {
            handleMapSelectionMenu(code);
        } else if (gp.ui.titleScreenState == 2) {
            if (code == KeyEvent.VK_ENTER) {
            	gp.playSE(10);
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum = 0;
            }
        } else if (gp.ui.titleScreenState == 3) {
        	handleDifficultyMenu(code);
        }else if (gp.ui.titleScreenState == 4) {
        	
        	
        	if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            	gp.playSE(9);
            	gp.ui.commandNum--;
    			if(gp.ui.commandNum<0) {
    				gp.ui.commandNum=1;
    			}
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            	gp.playSE(9);
            	gp.ui.commandNum++;
    			if(gp.ui.commandNum>1) {
    				gp.ui.commandNum=0;
    			}
            }
        	
        	
        	 if (code == KeyEvent.VK_ENTER) {
        		 gp.playSE(10);
        		 
        		 if (gp.ui.commandNum == 0) {
        			 switch(gp.ui.subState) {
        			 case 0: gp.ui.titleScreenState = 0;gp.ui.commandNum = 0; break; 
        			 case 1: gp.ui.subState--; break; 
        			 case 2: gp.ui.subState--; break; 
        			 }
        		 }
        		 
        		 if (gp.ui.commandNum == 1) {
        			 switch(gp.ui.subState) {
        			 case 0: gp.ui.subState++; break; 
        			 case 1: gp.ui.subState++; break; 
        			 case 2: gp.ui.titleScreenState = 0;gp.ui.commandNum = 0; break; 
        			 }
        		 }
             }
        }
    }

    

	private void handleMainMenu(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
        	gp.playSE(9);
        	gp.ui.commandNum--;
			if(gp.ui.commandNum<0) {
				gp.ui.commandNum=3;
			}
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
        	gp.playSE(9);
        	gp.ui.commandNum++;
			if(gp.ui.commandNum>3) {
				gp.ui.commandNum=0;
			}
        }
        if (code == KeyEvent.VK_ENTER) {
        	gp.playSE(10);
            switch (gp.ui.commandNum) {
                case 0: gp.ui.titleScreenState = 1; break;
                case 1: gp.ui.titleScreenState = 2; break;
                case 2: gp.ui.titleScreenState = 4; gp.ui.subState = 0; gp.ui.commandNum = 1;break;
                case 3: System.exit(0); break;
            }
        }
    }

    private void handleMapSelectionMenu(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
        	gp.playSE(9);
        	gp.ui.commandNum--;
			if(gp.ui.commandNum<0) {
				gp.ui.commandNum=3;
			}
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
        	gp.playSE(9);
        	gp.ui.commandNum++;
			if(gp.ui.commandNum>3) {
				gp.ui.commandNum=0;
			}
        }
        if (code == KeyEvent.VK_ENTER) {
        	gp.playSE(10);
            switch (gp.ui.commandNum) {
                case 0: currentMap = "/maps/World_01.txt"; gp.ui.titleScreenState = 3; gp.ui.commandNum = 0; break;
                case 1: currentMap = "/maps/World_02.txt"; gp.ui.titleScreenState = 3; gp.ui.commandNum = 0;break;
                case 2: currentMap = "/maps/World_03.txt"; gp.ui.titleScreenState = 3; gp.ui.commandNum = 0;break;
                case 3: {
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0;
                    break;
                }
            }
        }
   }
    
    private void handleDifficultyMenu(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
        	gp.playSE(9);
        	gp.ui.commandNum--;
			if(gp.ui.commandNum<0) {
				gp.ui.commandNum=2;
			}
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
        	gp.playSE(9);
        	gp.ui.commandNum++;
			if(gp.ui.commandNum>2) {
				gp.ui.commandNum=0;
			}
        }
        if (code == KeyEvent.VK_ENTER) {
        	gp.playSE(10);
            switch (gp.ui.commandNum) {
                case 0: isHard = false; setupGame(currentMap);break;
                case 1: isHard = true; setupGame(currentMap); break;
                case 2: {
                    gp.ui.titleScreenState = 1;
                    gp.ui.commandNum = 0;
                    break;
                }
            }
        }
   }

    private void handleMovementKeys(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = true;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;
    }

    private void handlePauseAndOptionStates(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.gameState == gp.playState) {
            	gp.ui.subState =0;
                gp.gameState = gp.pauseState;
            } else if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
            }
        }

        if (code == KeyEvent.VK_O) {
            if (gp.gameState == gp.pauseState) {
                gp.ui.commandNum = 0;
                gp.gameState = gp.optionState;
            } else if (gp.gameState == gp.optionState && gp.ui.subState == 0) {
                gp.gameState = gp.pauseState;
            }
        }

        if (gp.gameState == gp.optionState) {
            handleOptionsMenu(code);
        }
    }

    private void handleOptionsMenu(int code) {
        boolean inVolumeMenu = gp.ui.subState == 0;
        boolean inControlsMenu = gp.ui.subState == 2;

        if ((inVolumeMenu || inControlsMenu) && (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)) {
        	gp.playSE(9);
        	gp.ui.commandNum--;
			if(gp.ui.commandNum<0) {
				if (gp.ui.subState == 0) {
				gp.ui.commandNum=4;
				}else {
					gp.ui.commandNum=1;
				}	
			}
        }

        if ((inVolumeMenu || inControlsMenu) && (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)) {
        	gp.playSE(9);
            gp.ui.commandNum++;
			if((gp.ui.commandNum>4 && inVolumeMenu) || (gp.ui.commandNum>1 && inControlsMenu)) {
				gp.ui.commandNum=0;
			}
        }

        if (code == KeyEvent.VK_ENTER) {
        	gp.playSE(10);
            enterPressed = true;
        }

        if (inVolumeMenu) {
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                if (gp.ui.commandNum == 0 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                }
                if (gp.ui.commandNum == 1 && gp.sound.volumeScale > 0) {
                    gp.sound.volumeScale--;
                }
            }

            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                if (gp.ui.commandNum == 0 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                }
                if (gp.ui.commandNum == 1 && gp.sound.volumeScale < 5) {
                    gp.sound.volumeScale++;
                }
            }
        }
    }

    private void handleGameOverOrWinState(int code) {
    	
        if (gp.gameState == gp.gameOverState || gp.gameState == gp.winState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            	gp.playSE(9);
            	gp.ui.commandNum--;
				if(gp.ui.commandNum<0) {
					gp.ui.commandNum=1;
				}
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            	gp.playSE(9);
            	gp.ui.commandNum++;
				if(gp.ui.commandNum>1) {
					gp.ui.commandNum=0;
				}
            }
            if (code == KeyEvent.VK_ENTER) {
            	gp.playSE(10);
                if (gp.ui.commandNum == 0) {
                    setupGame(currentMap);
                } else if (gp.ui.commandNum == 1) {
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0;
                    gp.gameState = gp.titleState;
                }
            }
        }
    }

    // =====================
    // SETUP METHOD
    // =====================

    public void setupGame(String map) {
        gp.tileM.map = map;
        currentMap = map;

        gp.player.canWin=true;
        
        gp.tileM.loadMap(map);
        gp.playMusic(8);
        gp.setupGame();

        gp.player.colorIndicator = 1;
        gp.player.dir = "down";
        gp.player.spriteNum = 1;
        gp.player.getPlayerImage();
        gp.player.worldX = gp.tileSize * 22;
        gp.player.worldY = gp.tileSize * 22;
        gp.player.trash = 0;

        gp.gameState = gp.playState;
        gp.ui.commandNum = 0;
    }
}
