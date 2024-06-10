package entity;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;

import simulation2D.MainPanel;
import simulation2D.KeyHandler;

public class Navigator extends Entity{
	//MainPanel mainPanel;				// Define mainPanel as MainPanel in the simulation.
	KeyHandler keyH;					// Define keyH as KeyHandler in the simulation.
	
	//public final int screenX;			// Define and instantiate screenX as an integer for the X-coordinate of the screen position.
	//public final int screenY;			// Define and instantiate screenY as an integer for the Y-coordinate of the screen position.
	public int nevspeed;
	
	// Position in the world and on the screen.
	public int worldX, worldY;			// World coordinates.
	public int centerX, centerY;		// Screen coordinates.
	
	public int mouseWorldX, mouseWorldY, mouseTileX, mouseTileY;
	public byte chosenTile;
	
	public Navigator(MainPanel mainPanel, KeyHandler keyH) {
		
		super(mainPanel);
		
		//this.mainPanel = mainPanel;							//Set this.mainPanel data member to mainPanel parameter
		this.keyH = keyH;									//Set this.keyH data member to keyH parameter (for debugging)
		
		centerX = mainPanel.screenWidth/2 - (mainPanel.tileSize/2);		//Set player's position center to screen
		centerY = mainPanel.screenHeight/2 - (mainPanel.tileSize/2);	//Set player's position center to screen

		
		setDefaultValues();									//Call setDefaultValues to reset data member
		getImage();									//Call getRobotImage to load image file
	}
	
	public void setDefaultValues() {
		
		//position to center of the map
		worldX = mainPanel.tileSize*mainPanel.maxWorldCol/2-8;				
		worldY = mainPanel.tileSize*mainPanel.maxWorldRow/2-8;
		
		//worldX = 0;
		//worldY = 0;
		
		nevspeed = 8;			//set control speed
		chosenTile = 0;		//set tile type

	}
	
	public void getImage(){
		
		try {																				
			entityImage = ImageIO.read(getClass().getResourceAsStream("/unit/center.png"));	//load image file
		} catch (IOException e) {
			System.out.println("fail loading player's image");					//display error message
		}
	}
	
	public void update() {
		
		if (keyH.upPressed) {									//Move screen up, when press W
			worldY = worldY - nevspeed;
			//System.out.println(worldY);
		}
		if (keyH.downPressed) {									//Move screen down, when press S
			worldY = worldY + nevspeed;
			//System.out.println(worldY);
		}
		if (keyH.rightPressed) {								//Move screen right, when press D
			worldX = worldX + nevspeed;
			//System.out.println(worldX);
		}
		if (keyH.leftPressed) {									//Move screen left, when press A
			worldX = worldX - nevspeed;
			//System.out.println(worldX);
		}
		
		chosenTile = keyH.tileSelection;
		
		if (mainPanel.mouseH.leftClicked ) {						//If mouse is click
			
			//get mouse position on tiles
			mouseWorldX = worldX + mainPanel.mouseH.mouseSceenX - mainPanel.screenWidth/2 + 8; 		
			mouseWorldY = worldY + mainPanel.mouseH.mouseSceenY - mainPanel.screenHeight/2 + 8;
			mouseTileX = mouseWorldX/16;
			mouseTileY = mouseWorldY/16;
			
			//check if mouse position in the map
			if ( (mouseTileX >= 0) && (mouseTileY >= 0) && (mouseTileX < mainPanel.maxWorldCol) && (mouseTileY < mainPanel.maxWorldRow)) {
				
				mainPanel.tileManager.mapTileNum[mouseTileX][mouseTileY] = chosenTile;
			}
		}
	}
	
	public void draw(Graphics2D g2) {

		
		BufferedImage image = entityImage;									//Set image = player's Image
		g2.drawImage(image, centerX, centerY, mainPanel.tileSize, mainPanel.tileSize, null);	//Set position and size, and Draw image in mainPanel
	}
	
	
}
