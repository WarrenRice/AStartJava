package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import simulation2D.MainPanel;

public class TileManager {
	MainPanel mainPanel;			// Define mainPanel as MainPanel in the simulation.
	public Tile[] tile;				// Define tile as Tile[] array
	public byte mapTileNum[][];		// Define mapTileNum[][] as integer array[][]
	public int dockX, dockY;		// Define dockX, dockY as integers for dock position

	boolean drawPath = true;		// Define and instantiate drawPath as boolean to display path
	
	
	public TileManager(MainPanel mainPanel) {
		
		this.mainPanel = mainPanel;							// Set this.mainPanel data member to mainPanel parameter
		
		tile = new Tile[8];									// Instantiate tile = new Tile[8]
		mapTileNum = new byte[mainPanel.maxWorldCol][mainPanel.maxWorldRow];		//Instantiate mapTileNum Array to map dimension
						
		getTileImage();										// load image and set some data sets
		loadMap(mainPanel.mapName);							// loadMap to tiles
	
	}
	
	public void getTileImage() {

		try {
			tile[0] = new Tile();																	//Instantiate tile[0] = new Tile()
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));		//load Image
			tile[0].type = "Wall";
			tile[0].boundary = true;																//set boundary = true
			
			
			tile[1] = new Tile();																	//Instantiate tile[1] = new Tile()
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/highgrass.png"));	//load Image
			tile[1].type = "Grass";
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
public void loadMap(String mapName) {

		mapName = "Save/map.txt";
				
		DefaultListModel<String> loadData = new DefaultListModel<String>();
		String temp_string;
		
		try {																	//try
			FileReader inputFile = new FileReader(mapName);							//load file
			BufferedReader bufferInputFile = new BufferedReader(inputFile);
			
			int col = 0, row = 0, num = 0;
			
			while ( (temp_string = bufferInputFile.readLine()) != null) {			// when file is read
				loadData.addElement(temp_string);									// add data to loadData array (load data is array)
			}
					
			if (loadData.size() >= 2) {
			    loadData.removeElementAt(0); // Remove the first element
			    loadData.removeElementAt(0); // Remove the second element
			}
			
			for (row = 0; row < loadData.getSize(); row++) {
				String numbers[] = loadData.get(row).split(" ");
				
				for (col = 0; col < numbers.length; col++) {
					
					num = Integer.parseInt(numbers[col]);			//Use col as an index for numbers[] array
					mapTileNum[col][row] = (byte) num;				//Set mapTileNum[col][row] = num
				}
			}
	
			bufferInputFile.close();								//close file
			inputFile.close();
			
		} catch (Exception e) {
			System.out.println("fail loading map in TileManager.");	//display error message
		}
	}

	public void saveMap(String mapName) {
		
		try {																			//try
			FileWriter outputFile = new FileWriter(mapName);	
			BufferedWriter bufferedOutputFile = new BufferedWriter(outputFile);
			
			int col = 0, row = 0;
			String data;
			
			bufferedOutputFile.write(Integer.toString(mainPanel.maxWorldCol));			//write first line, map column
			bufferedOutputFile.newLine();
			bufferedOutputFile.write(Integer.toString(mainPanel.maxWorldRow));			//write second line, map row
			bufferedOutputFile.newLine();
			
			for (row = 0; row < mainPanel.maxWorldRow; row++) {							//write map data
				for (col = 0; col < mainPanel.maxWorldCol; col++) {
					data = Integer.toString(mapTileNum[col][row]) + " ";
					bufferedOutputFile.write(data);
				}
				bufferedOutputFile.newLine();
			}

			bufferedOutputFile.close();													// close bufferedOutputFile
			outputFile.close();	
			
			JOptionPane.showMessageDialog(null, "Map Saved ");
			
		} catch (Exception e) {
			System.out.println("fail saving map");				//display error message
			System.out.println(e);
		}
	}
	
	public void draw(Graphics2D g2) {

		
		int worldCol = 0, worldRow = 0;							//Define, instantiate worldCol, worldRow as integers and set = 0
		
		int worldX, worldY, screenX, screenY;					//Define, instantiate worldX, worldY, screenX, screenY as integers for drawing position
		int tileNum;
		
		while (worldCol < mainPanel.maxWorldCol && worldRow < mainPanel.maxWorldRow) {
			
			// Extract the tile number at the current world column and row.
			tileNum = mapTileNum[worldCol][worldRow];		
			
			// Calculate world and screen coordinates for the current tile.
			worldX = worldCol*mainPanel.tileSize;			
			worldY = worldRow*mainPanel.tileSize;			
			screenX = worldX - mainPanel.navigator.worldX + mainPanel.navigator.centerX;
			screenY = worldY - mainPanel.navigator.worldY + mainPanel.navigator.centerY;	
			
			// Render only tiles that are within the screen boundaries.
			if(	worldX + mainPanel.tileSize > mainPanel.navigator.worldX - mainPanel.navigator.centerX &&	
				worldX - mainPanel.tileSize < mainPanel.navigator.worldX + mainPanel.navigator.centerX &&
				worldY + mainPanel.tileSize > mainPanel.navigator.worldY - mainPanel.navigator.centerY &&
				worldY - mainPanel.tileSize < mainPanel.navigator.worldY + mainPanel.navigator.centerY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, mainPanel.tileSize, mainPanel.tileSize, null);
			}
			worldCol++;			// Move to the next world column.
			
			// Move to the next world row when the last column is reached.
			if (worldCol == mainPanel.maxWorldCol) {			
				worldCol = 0;
				worldRow++;
			}
		}
		
		//draw path on map
		if(drawPath == true) {
			g2.setColor(new Color(0,0,255,70));													//set path color
			int i, pathX, pathY;
			for(i = 0; i < mainPanel.pathFinder.pathList.size(); i++) {							//go through pathList
				
				//System.out.println(mainPanel.pathFinder.pathList.get(i).col + "," + mainPanel.pathFinder.pathList.get(i).row);
				
				pathX = mainPanel.pathFinder.pathList.get(i).col * mainPanel.tileSize;			//get position
				pathY = mainPanel.pathFinder.pathList.get(i).row * mainPanel.tileSize;				
				screenX = pathX + this.mainPanel.navigator.centerX - this.mainPanel.navigator.worldX;
				screenY = pathY + this.mainPanel.navigator.centerY - this.mainPanel.navigator.worldY;
			
				
				g2.fillRect(screenX, screenY, mainPanel.tileSize, mainPanel.tileSize);			//fill color
			}
		}
	}
}
