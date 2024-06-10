package simulation2D;

import java.awt.Color;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		

		JFrame window = new JFrame();							//Define and instantiate JFrame object as window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//set close window when click on close button
		window.setResizable(false);								//disable resize window
		window.setTitle("A* Simulation2D");						//set title to "Simulation2D"

		MainPanel mainPanel = new MainPanel();					//Define and instantiate MainPanel object as window
		window.add(mainPanel);									//Add MainPanel to window
		
		window.pack();											//Pack every components
		
		window.setLocationRelativeTo(null);						//Set window to relative position
		window.setVisible(true);								//Display window
		
		mainPanel.startMainThread();							//Start thread startMainThread()
	}

}