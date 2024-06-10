package simulation2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ai.PathFinder;
import entity.Navigator;
import entity.Player;
import tile.TileManager;

public class MainPanel extends JPanel implements Runnable{		//Runnable and use Thread for run method
	
	// SCREEN SETTINGS
	public final byte tileSize = 16;							//define and instantiate tileSize as a byte for 16x16 pixel tile
	
	public final int maxScreenCol = 100; 						//define and instantiate maxScreenCol as an integer for displaying tiles in column
	public final int maxScreenRow = 50;							//define and instantiate maxScreenRow as an integer for displaying tiles in row
	
	public final int screenWidth = tileSize * maxScreenCol;		//define and instantiate screenWidth as an integer for 40*(16*3) = 1920 pixels
	public final int screenHeight = tileSize * maxScreenRow;	//define and instantiate screenHeight as an integer for 20*(16*3) = 960 pixels

	// WORLD SETTINGS
	public String mapName = "Save/map.txt";						//define and instantiate mapName as a String for file's name
	public int maxWorldCol = 1024; 								//define and instantiate maxWorldCol as an integer and initially set to 1024
	public int maxWorldRow = 1024; 								//define and instantiate maxWorldRow as an integer and initially set to 1024
	public boolean jobDone = setMap(mapName);
	
	
	public TileManager tileManager = new TileManager(this);		//create a TileManager instance for handling tiles in the world.
	
	public final int worldWidth = tileSize * maxWorldCol;		//Define and instantiate the total width of the game world in pixels.
	public final int worldHeight = tileSize * maxWorldRow;		//Define and instantiate the total height of the game world in pixels.
	
	// TIME FPS SETTING
	final int FPS = 60;											//Define a constant for frames per second (FPS) as 240 (fast), 120(Moderate), 60(Slow)
	
	// Initialize variables for measuring time.
	public float speedFactor = 0.5f;							// Define a speed factor with an initial value of 0.5 step
	

	KeyHandler keyH = new KeyHandler(this);									//define and instantiate keyHandler as KeyHandler object.
	Thread mainThread;													//define mainThread as Thread object to be used as the main thread.
	
	public MouseHandler mouseH = new MouseHandler();
	public Navigator navigator = new Navigator(this, keyH);	
	
	public PathFinder pathFinder = new PathFinder(this);				// Create a pathFinder instance for finding path
	
	public UI ui = new UI(this);										// Create a UI (User Interface) instance for managing the user interface of the simulation.
	public Player player = new Player(this);							// Create a Robot instance for automation in the simulation, 
																		// which can uses keyH (KeyHandler) for input for debugging purpose.

	public MainPanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));	//set panel dimension
		this.setBackground(Color.black);	//set Background color						
		this.setDoubleBuffered(true); 		//set to true, all the drawing from this component will be done in an off screen paint buffer.
											//to improve rendering performance. (JComponent feature)
		this.addKeyListener(keyH);			//add keyHandler (KeyListener) to this panel
		this.addMouseListener(mouseH);
		this.setFocusable(true);			//setFocusable, so this panel can be focused to receive key input 
	}
	
	public void startMainThread() {
		
		mainThread = new Thread(this);		//instantiate Thread
		mainThread.start();					//start Thread
	}

	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;	//Define and instantiate drawInterval as a drawInterval and set how many nanoseconds to complete 1 frame
		double delta = 0;						//Define and instantiate drawInterval as a delta and reset delta = 0
		long lastTime = System.nanoTime();		//Define and instantiate lastTime as a long and set to time from the machine, use nanoTime for better accuracy frame rate
		long currentTime;						//define current time
		
		while (mainThread != null) {	//as long as gameThread exists

			currentTime = System.nanoTime();						//Instantiate currentTime as a long and set to time from the machine, use nanoTime for better accuracy frame rate
			delta += (currentTime - lastTime) / drawInterval;		//set delta how much time has passed
			//timer += (currentTime - lastTime);					//(for debugging)
			lastTime = currentTime;									//update lastTime = currentTime
			
			
			if (delta >= 1) {									//when delta is big enough to complete 1 frame
				// 1 UPDATE Section: update information. Example: Position, State.
				update();
				
				// 2 DRAW Section: draw the screen with the updated information. 
				repaint();										//triggers 'paintComponent(Graphics g)' method
				
				//UI	
				//countTime();
				
				delta--;										//reset delta
				//drawCount++;									//for display FPS
			}
		}
	}
	 
	public void update() {
		
		player.update();							//run update method from player object
		navigator.update();
		
	}
	
	public void paintComponent(Graphics g) {

		
		super.paintComponent(g);				//superclass method (JPanel) so it can be called from JPanel
	
		Graphics2D g2 = (Graphics2D)g;			//convert Graphics to Graphics2D class extends Graphics class for more control over geometry
												//, coordinate transformations, color management, and text layout.
		//tile
		tileManager.draw(g2);					//call draw method to render tile graphic
		
		//player
		player.draw(g2);						//call draw method to render player graphic
		
		//navigator
		navigator.draw(g2);
		
		ui.draw(g2);							//call draw method to render ui graphic

		g2.dispose();							//dispose of this graphics context and release any system resources that it is using to save some memory.
	
	}
	
	public boolean setMap(String mapName) {
		DefaultListModel<String> loadData = new DefaultListModel<String>();
			String temp_string;
			
			try {																			//try
				FileReader inputFile = new FileReader(mapName);								//load file
				BufferedReader bufferInputFile = new BufferedReader(inputFile);
				
				
				while ( (temp_string = bufferInputFile.readLine()) != null) {				// when file is read
					loadData.addElement(temp_string);										// add data to loadData array (load data is array)
				}
				
				maxWorldCol = Integer.parseInt(loadData.get(0));							//get data from the 1st line
				maxWorldRow = Integer.parseInt(loadData.get(1));							//get data from the 2nd line
				
				bufferInputFile.close();													//close file
				inputFile.close();
				
			} catch (Exception e) {
				System.out.println("fail loading map in mainPanel");						//display error message
			}
			
			return false;																	//return false

		}
}
