package ai;

public class Node {
	
	Node parent;					//define parent as Node for back tracking
	public int col;					//define col as integer for tile position
	public int row;					//define row as integer for tile position
	int gCost;						//define gCost as integer for a*
	int hCost;						//define hCost as integer for a*
	int fCost;						//define fCost as integer for a*
	boolean start;					//define start as boolean
	boolean goal;					//define goal as boolean
	boolean solid;					//define solid as boolean
	boolean open;					//define open as boolean
	boolean checked;				//define checked as boolean
	
	public Node(int col, int row) {
		
		this.col = col;			//set col
		this.row = row;			//set row
	}
}
