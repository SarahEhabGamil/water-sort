package code;

public class Node {

	private String [][] state;
	private Node parent;
	private String operator;
	private int depth;
	private int pathCost;
	
	
	 public Node(String[][] state, Node parent, String operator, int depth, int pathCost) {
	        this.state = state;
	        this.parent = parent;
	        this.operator = operator;
	        this.depth = depth;
	        this.pathCost = pathCost;
	    }
	
	
	  public String[][] getState() {
	        return state;
	    }

	    public Node getParent() {
	        return parent;
	    }

	    public String getAction() {
	        return operator;
	    }

	    public int getPathCost() {
	        return pathCost;
	    }
	    
	  
}
