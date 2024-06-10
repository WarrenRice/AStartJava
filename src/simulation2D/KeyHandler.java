package simulation2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

public class KeyHandler implements KeyListener{
	MainPanel mainPanel;
	
	//Define upPressed, downPressed, leftPressed, rightPressed as boolean
	public boolean upPressed, downPressed, leftPressed, rightPressed;
	public byte tileSelection;
	
	public KeyHandler(MainPanel mainPanel) {
		this.mainPanel = mainPanel;					//Set this.mainPanel data member to mainPanel parameter
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int code = e.getKeyCode();	 //getKeyCode returns the integer keyCode
		
		//if w,a,s,d, F4, F5 are pressed
		if (code == KeyEvent.VK_W) {
			upPressed = true;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		int code = e.getKeyCode();	//getKeyCode returns the integer keyCode
		
		//if w,a,s,d,0-7, F4, F5 are released
		if (code == KeyEvent.VK_W) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		
		if (code == KeyEvent.VK_0) {
			//tileSelection = 0;
		}
		if (code == KeyEvent.VK_1) {
			tileSelection = 0;
		}
		if (code == KeyEvent.VK_2) {
			tileSelection = 1;
		}

		if (code == KeyEvent.VK_F4) {
			mainPanel.tileManager.saveMap(mainPanel.mapName);
		}
		
		if (code == KeyEvent.VK_F5) {
			mainPanel.tileManager.loadMap(mainPanel.mapName);
		}
	}

}
