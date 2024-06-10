package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import simulation2D.MainPanel;

public class Entity {
	
	MainPanel mainPanel;
	
	// Movement-related properties.
	public double speed;				// Overall speed of the entity. Using double for better precision.
	public double speedX, speedY;		// Speed components for x and y directions.
	public int nextX, nextY;			// Next position (in pixels).
	public int nowTileX, nowTileY;		// Current tile coordinates.
	public int nextTileX, nextTileY;	// Next tile coordinates.

	public BufferedImage entityImage;	// Image representing the entity.
	
	// Collision and behavior flags.
	public Rectangle solidArea = new Rectangle(2, 2, 12, 12);;				// Rectangle representing the entity's solid area.
	//public boolean collisionOn = false;		// Flag indicating if collision detection is active.

	public boolean foundCharger = false;	// Flag for finding the Chargers.
	public boolean lowPower = false;		// Flag for simulating lowPower state.
	public boolean finish = false;			// Flag for simulating finish state.
	
	public boolean onPath = false;			// Flag for simulating onPath state.
	
	public Entity(MainPanel mp) {
		this.mainPanel = mp;
	}
}

