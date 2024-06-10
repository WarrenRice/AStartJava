package simulation2D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UI {
	MainPanel mainPanel;									// Define mainPanel as MainPanel in the simulation.
	Font arial_25;											// Font for UI elements.
	
	public UI(MainPanel mainPanel) {
		

		this.mainPanel = mainPanel;							//Set this.mainPanel data member to mainPanel parameter
		
		arial_25 = new Font("Arial", Font.PLAIN, 25);		//Instantiate Font
		
	}
	
	public void draw(Graphics2D g2) {

		
		g2.setFont(arial_25);						//Set Font 
		g2.setColor(Color.white);					//Set Font's color
		g2.drawImage(mainPanel.tileManager.tile[mainPanel.navigator.chosenTile].image, 16, 24, mainPanel.tileSize*2, mainPanel.tileSize*2, null);
		//Display font
		g2.drawString("Tile x: " + mainPanel.navigator.mouseTileX + " y: " + mainPanel.navigator.mouseTileY, 64, 50);
		g2.drawString("Note: Press '0-7' to select tile type, 'F4' to save, 'F5' to load, and 'w,a,s,d' to move", 520, 50);
		g2.drawString("Tile Type: " + mainPanel.tileManager.tile[mainPanel.navigator.chosenTile].type , 64, 90);
		g2.drawString("Left Click to edit", 64, 130);
		g2.drawString("Right Click to move", 64, 170);
	}
}
