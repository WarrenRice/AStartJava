package simulation2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener{

	public int mouseSceenX, mouseSceenY;
	public boolean leftClicked, rightClicked;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//Get mouse's position
		mouseSceenX = e.getX();
		mouseSceenY = e.getY();
        // Check which button is pressed
        if (e.getButton() == MouseEvent.BUTTON1) { // Left button
            leftClicked = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) { // Right button
            rightClicked = true;
        }
	}
	

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
        leftClicked = false;
        rightClicked = false;
    }

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
