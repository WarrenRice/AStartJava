package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;

import simulation2D.KeyHandler;
import simulation2D.MainPanel;

public class Player extends Entity{
	KeyHandler keyH;					// Define keyH as KeyHandler in the simulation.

	public int worldX, worldY;			// World coordinates.
	public int screenX, screenY;		// Screen coordinates.
	
	public int goalX, goalY;		// Target tile coordinates.
	
	public int mouseWorldX, mouseWorldY, mouseTileX, mouseTileY;

	public String status;						// Define status as a string for displaying player's status.
	public byte numberOfCharge = 0;				// Define and instantiate numberOfCharge as a byte

	public Player(MainPanel mainPanel) {

		super(mainPanel);
		
		setDefaultValues();									//Call setDefaultValues to reset data member
		getImage();
	}
	
	public void setDefaultValues() {

		nowTileX = 1;											// Set the current tile X coordinate.
		nowTileY = 1;											// Set the current tile Y coordinate.
		worldX = mainPanel.tileSize * (nowTileX);				// Calculate the world X coordinate based on the tile size.
		worldY = mainPanel.tileSize * (nowTileY);				// Calculate the world Y coordinate based on the tile size.
		
		this.speed = 16*mainPanel.speedFactor;					// set player's speed (16 * 0.5 Tile/step)
	}
	
	public void getImage(){

		try {																				
			entityImage = ImageIO.read(getClass().getResourceAsStream("/unit/robot.png"));	//load image file
		} catch (IOException e) {
			System.out.println("fail loading player's image");									//display error message
		}
	}
	
	public void update() {

		runRobot();
		if (mainPanel.mouseH.rightClicked ) {						//If mouse is click

			mouseWorldX = mainPanel.navigator.worldX + mainPanel.mouseH.mouseSceenX - mainPanel.screenWidth/2 + 8; 		
			mouseWorldY = mainPanel.navigator.worldY + mainPanel.mouseH.mouseSceenY - mainPanel.screenHeight/2 + 8;
			mouseTileX = mouseWorldX/16;
			mouseTileY = mouseWorldY/16;
			
			try {
				if (mainPanel.tileManager.mapTileNum[mouseTileX][mouseTileY] == 1) {
					this.goalX = mouseTileX;
					this.goalY = mouseTileY;

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
	
	public void moveToGoal(int startTileX, int startTileY,int goTileX, int goTileY) {
		int startX = startTileX*16;
		int startY = startTileY*16;
		searchPath(startX,startY,goTileX,goTileY);

		if (mainPanel.pathFinder.pathList.size() != 0 ) {

			// Get the next tile coordinates and world coordinates from the path list.
			nextTileX = mainPanel.pathFinder.pathList.get(0).col;
			nextTileY = mainPanel.pathFinder.pathList.get(0).row;
			nextX = nextTileX*16;
			nextY = nextTileY*16;
			
			// Calculate the horizontal and vertical distances between the next position and the current position.
			int speedBack = (int) (speed);
			int dX = nextX - worldX;
			int dY = nextY - worldY;
			
			// Calculate the absolute values of the horizontal and vertical distances.
			int absX = Math.abs(dX);
			int absY = Math.abs(dY);
				
			// Check if the movement at the increased speed is less than or equal to the absolute distances.
			if ( (speedBack <= absX) || (speedBack <= absY) ) {
				if (speedBack <= absX) {
					// Move horizontally based on the sign of the horizontal distance.
					if (dX >= 0) {
						this.worldX += speedBack;
					} else {
						this.worldX -= speedBack;
					}
				}
				else if (speedBack <= absY) {
					// Move vertically based on the sign of the vertical distance.
					if (dY >= 0) {
						this.worldY += speedBack;
					} else {
						this.worldY -= speedBack;
					}
				}
			} else {
				// Update current tile and world coordinates when reaching the next tile.
				nowTileX = nextTileX;
				nowTileY = nextTileY;
				this.worldX = nowTileX*16;
				this.worldY = nowTileY*16;

				// Remove the first node from the path list.
				mainPanel.pathFinder.pathList.remove(0);
			}
		} 
	}
	
	
	public void runRobot() {
		moveToGoal(this.nowTileX,this.nowTileY,this.goalX,this.goalY);
	}
	
	
	public void draw(Graphics2D g2) {

		int wx = this.worldX + this.mainPanel.navigator.centerX - this.mainPanel.navigator.worldX;
		int wy = this.worldY + this.mainPanel.navigator.centerY - this.mainPanel.navigator.worldY;
		
		BufferedImage image = entityImage;	
		g2.drawImage(image, wx, wy, mainPanel.tileSize, mainPanel.tileSize, null);	//Set position and size, and Draw image in mainPanel
	}
	
	
	public void searchPath(int startX, int startY, int goalCol, int goalRow) {

		int startCol = (startX + solidArea.x)/mainPanel.tileSize;
		int startRow = (startY + solidArea.y)/mainPanel.tileSize;

		mainPanel.pathFinder.setNodes(startCol, startRow, goalCol, goalRow);
		mainPanel.pathFinder.search();

	}
}
