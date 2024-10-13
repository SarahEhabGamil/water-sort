package code;

import java.io.Console;
import java.util.*;

public class WaterSortSearch extends GenericSearch {
	static int numberOfBottles;
	static int bottleCapacity;

	public static Node prepareInitialState(String input) {

		String[] parts = input.split(";");

		numberOfBottles = Integer.parseInt(parts[0]);
		bottleCapacity = Integer.parseInt(parts[1]);

		String[][] state = new String[numberOfBottles][bottleCapacity];

		for (int i = 0; i < numberOfBottles; i++) {

			String[] colors = parts[i + 2].split(",");

			for (int j = 0; j < colors.length; j++) {
				state[i][j] = colors[j].toString();
			}

			for (int j = colors.length; j < bottleCapacity; j++) {
				state[i][j] = "e";
			}
		}

		Node initialNode = new Node(state, null, null, 0, 0);
		printNode(initialNode);

		return initialNode;
	}

	@Override
	public boolean isGoalState(String[][] state) {
		for (int i = 0; i < state.length; i++) {
			String color = state[i][0];
			for (int j = 0; j < state[i].length; j++) {
				if (!state[i][j].equals(color))
					return false;
			}
		}
		return true;
	}

	@Override
	public List<String> getOperations(Node node) {
		List<String> operations = new ArrayList<>();
		String[][] state = node.getState();
		
		for (int i = 0; i < state.length; i++) {
			String bottleTop = getTop(state[i]);
			
		
			
			for (int j = 0; j < state.length; j++) {
				if (i != j && emptySlots(state[j]) > 0) {
					
					if (getTop(state[j]).equals(bottleTop) || getTop(state[j]).equals("e")) {
						String action = formulateAction(i, j);
				
						
						operations.add(action);
					}
				}
			}
		}
		return operations;

	}

	

	@Override
	public int getStepCost(String state, String action) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static String solve(String initialString, String strategy, boolean visualize) {
		WaterSortSearch sarah = new WaterSortSearch();
		Node rootNode = prepareInitialState(initialString);


		String solution;
		switch (strategy) {
		case "BF":
			solution = sarah.breadthFirstSearch(rootNode);
			break;
		default:
			solution = "NOSOLUTION";
		}
		return solution;

	}

	public static String formulateAction(int i, int j) {
		return "pour_" + i + "_" + j;
	}

	////////////Pouring Methods
	@Override
	public String[][] getResult(Node node, String action) {
		int[] coordinates = extractAction(action);
		int i = coordinates[0];
		int j = coordinates[1];
		return pour(node, i, j);

	}
	
	public static int[] extractAction(String action) {

		String[] parts = action.split("_");

		int i = Integer.parseInt(parts[1]);
		int j = Integer.parseInt(parts[2]);

		return new int[] { i, j };
	}

	public static String[][] pour(Node node, int i, int j) {
		String[][] state = node.getState();
		String[][] newState = new String[numberOfBottles][bottleCapacity];

		String[] bottleToPourFrom = state[i];
		String topColorToPourFrom = getTop(bottleToPourFrom);

		String[] bottleToPourTo = state[j];
	

		for (int k = 0; k < emptySlots(bottleToPourTo); k++) {
			if(getTop(bottleToPourTo).equals(topColorToPourFrom)) {
				newState = actuallyPour(node, i , j);
			}
			
		}
		return newState;

	}
	
	
	public static String[][] actuallyPour( Node node, int i , int j) {
		String [][] state = node.getState();
		String [] fromBottle =state[i];
		String [] toBottle = state[j];
		int from = getTopIndex(fromBottle);
		int to = getTopIndex(toBottle)+1;
		toBottle[to]= fromBottle[from];
		fromBottle[from]= "e";
		state[i]=fromBottle;
		state[j] = toBottle;
		return state;
		
	}
	

	public static int emptySlots(String[] bottle) {
		int count = 0;

		for (int i = 0; i < bottle.length; i++) {
			if (bottle[i].equals("e")) {
				count++;
			}
		}

		return count;
	}


	
	
	

	public static void main(String[] args) {

		String init = "3;" + "4;" + "r,y,r,y;" + "y,r,y,r;" + "e,e,e,e;";
		prepareInitialState(init);

		solve(init, "BF", false);

	}
	public static String getTop (String[] bottle) {
		for(String color : bottle) {
			if(!color.equals("e")) {
				return color;
			}
		}
		return "e";
		
	}
	
	public static int getTopIndex(String [] bottle) {
		for(int i = 0 ; i< bottle.length ; i++) {
			String color = bottle[i];
			if(!color.equals("e")) {
				return i;
			}
		}
		return -1;
	}


	



}
