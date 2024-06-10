package ai;

import java.util.ArrayList;

import simulation2D.MainPanel;

public class PathFinder {
	
	MainPanel mainPanel;									// Define mainPanel as MainPanel in the simulation.
	Node[][] nodes;											// Define nodes as Node[][], 2D array to represent nodes on the map.
	ArrayList<Node> openList = new ArrayList<>();			// Define openList as ArrayList<Node> for List of open nodes for pathfinding.
	public ArrayList<Node> pathList = new ArrayList<>();	// Define pathList as ArrayList<Node> for List to store the final path.
	Node startNode, goalNode, currentNode;					// Define startNode, goalNode, currentNode as Nodes object for path finding
	boolean goalReached = false;							// Define goalReached as boolean. Flag indicating whether the goal is reached.
	int step = 0;											// Define step as integer for counter for the number of steps taken in pathfinding.
	
	public PathFinder(MainPanel mainPanel) {
		
		this.mainPanel = mainPanel;											// Assign the MainPanel reference.
		instantiateNodes();													//instantiation
	}
	
	public void instantiateNodes() {
		
		//create Node array
		nodes = new Node[mainPanel.maxWorldCol][mainPanel.maxWorldRow];
		
		//instantiate and set nodes elements
		int col = 0;									// Initialize column index to 0.
		int row = 0;									// Initialize row index to 0.
		
		// Loop to iterate through each column and row in the nodes array.
		for(row = 0; row < mainPanel.maxWorldRow; row++) {
			for(col = 0; col < mainPanel.maxWorldCol; col++) {
				nodes[col][row] = new Node(col,row);	// Instantiate and set nodes elements
			}
		}
	}
	
	public void resetNodes() {
		
		int col = 0;												// Initialize column index to 0.
		int row = 0;												// Initialize row index to 0.
		// Loop through each row and column of the nodes array.
		for(row = 0; row < mainPanel.maxWorldRow; row++) {
			for(col = 0; col < mainPanel.maxWorldCol; col++) {
				//Reset open, check and solid state
				nodes[col][row].open = false;
				nodes[col][row].checked = false;
				nodes[col][row].solid = false;
			}
		}
		
		//Reset other setting
		openList.clear();		// Clear the openList, removing all elements.
		pathList.clear();		// Clear the pathList, removing all elements.
		goalReached = false;	// Reset the goalReached flag.
		step = 0;				// Reset the step counter.
	}
	
	public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
		
		resetNodes();								// Reset the state of nodes and pathfinding variables.
		
		//Set Start and Goal Node
		startNode = nodes[startCol][startRow];		// Set the starting node based on given coordinates.
		currentNode = startNode;					// Set the current node to the starting node.
		goalNode = nodes[goalCol][goalRow];			// Set the goal node based on given coordinates.
		openList.add(currentNode);					// Add the starting node to the open list.
		
		
		int col = 0;								// Initialize column index to 0.
		int row = 0;								// Initialize row index to 0.
		int tileNum;								// Variable to store the tile number.
		
		// Loop through each row and column of the nodes array.
		for(row = 0; row < mainPanel.maxWorldRow; row++) {
			for(col = 0; col < mainPanel.maxWorldCol; col++) {
				//Set Solid Node
				//Check Tiles
				tileNum = mainPanel.tileManager.mapTileNum[col][row];		// Get the tile number from the map.
				// If the tile is a boundary, mark the corresponding node as solid.
				if(mainPanel.tileManager.tile[tileNum].boundary == true) {
					nodes[col][row].solid = true;
				}
				
				// Set cost for each node.
				getCost(nodes[row][col]);
			}
		}
	}
	
	public void getCost(Node node) {

		//G cost
		int xDistance = Math.abs(node.col - startNode.col);		// Calculate the absolute horizontal distance.
		int yDistance = Math.abs(node.row - startNode.row);		// Calculate the absolute vertical distance.
		node.gCost = xDistance + yDistance;						// Set the G cost for the node.

		//H cost
		xDistance = Math.abs(node.col - goalNode.col);			// Calculate the absolute horizontal distance to the goal.
		yDistance = Math.abs(node.row - goalNode.row);			// Calculate the absolute vertical distance to the goal.
		node.hCost = xDistance + yDistance;						// Set the H cost for the node.
		
		//F cost
		node.fCost = node.gCost + node.hCost;					// Calculate and set the total F cost for the node.
	}
	
	public void search() {
		
		while(goalReached == false && step < 50000) { // step limit to 50000 in case is trapped
			int col = currentNode.col;			// Get the column index of the current node.
			int row = currentNode.row;			// Get the row index of the current node.
			
			//Check the current node
			currentNode.checked = true;			// Mark the current node as checked.
			openList.remove(currentNode);		// Remove the current node from the open list.
			
			//Open the Up node
			if(row - 1 >= 0) {
				openNode(nodes[col][row-1]);
			}
			
			//Open the Left node
			if(col - 1 >= 0) {
				openNode(nodes[col-1][row]);
			}
			
			//Open the Down node
			if(row + 1 < mainPanel.maxWorldRow) {
				openNode(nodes[col][row+1]);
			}
			
			//Open the Right node
			if(col + 1 < mainPanel.maxWorldCol) {
				openNode(nodes[col+1][row]);
			}
			
			//Find the best node
			int bestNodeIndex = 0;
			int bestNodefCost = 50000;
			
			for(int i = 0; i < openList.size(); i++) {
				
				//Current if this node's F cost is better
				if(openList.get(i).fCost < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = openList.get(i).fCost;
				}
				//If F cost is equal, check the G cost
				else if(openList.get(i).fCost == bestNodefCost) {
					if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i;
					}
				}
			}
			
			//If there is no node in the openList, end the loop
			if(openList.size() == 0) {
				break;
			}
			
			//After the loop, openList[bestNodeIndex] is the next step (= currentNode)
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
			step++;
		}

	}
	
	public void openNode(Node node) {
		
		// If the node is not open, not checked, and not solid
		if(node.open == false && node.checked == false && node.solid == false) {
			
			node.open = true;			// Mark the node as open.
			node.parent = currentNode;	// Set the parent of the node to the current node.
			openList.add(node);			// Add the node to the open list for exploration.
		}
	}
	
	public void trackThePath() {
		
		Node current = goalNode;			// Start from the goal node.
		
		while(current != startNode) {
			pathList.add(0,current);		// Add the current node to the beginning of the pathList.
			current = current.parent;		// Move to the parent node for the next iteration.
		}
	}
}
