package code;

import java.io.Console;
import java.util.*;

public class WaterSortSearch extends GenericSearch {
	static int numberOfBottles;
	static int bottleCapacity;

	public static Node prepareInitialState(String input, boolean visualize) {

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
		if (visualize) {
			printNode(initialNode);
		}
		return initialNode;
	}

	@Override
	public boolean isGoalState(String[][] state) {
		for (int i = 0; i < state.length; i++) {
			String color = state[i][bottleCapacity - 1];
			for (int j = bottleCapacity - 1; j >= 0; j--) {
				if (!state[i][j].equals(color)&&(!state[i][j].equals("e")))
					return false;
			}

		}
		return true;
	}

	/////////// Getting Operations/////////
	@Override
	public List<String> getOperations(Node node) {
		List<String> operations = new ArrayList<>();
		String[][] state = node.getState();

		for (int i = 0; i < state.length; i++) {
			String bottleTop = getTop(state[i]);


			for (int j = 0; j < state.length; j++) {
				if (i != j && emptySlots(state[j]) > 0) {


					if ((getTop(state[j]).equals(bottleTop) || getTop(state[j]).equals("e"))) {
						if (!(isFullySorted(state[i]) && isEmptyBottle(state[j]))
								&& !(isFullySorted(state[j]) && isEmptyBottle(state[i]))) {

							String action = formulateAction(i, j);

							operations.add(action);
						}
					}
				}
			}
		}
		return operations;

	}

	public static String formulateAction(int i, int j) {
		return "pour_" + i + "_" + j;
	}

	public static String solve(String initialString, String strategy, boolean visualize) {
		WaterSortSearch wss = new WaterSortSearch();
		Node rootNode = prepareInitialState(initialString, visualize);

		String solution;
		switch (strategy) {
		case "BF":
			solution = wss.breadthFirstSearch(rootNode, visualize);
			break;
		case "DF":
			solution = wss.depthFirstSearch(rootNode, visualize);
			break;
		case "UC":
			solution = wss.uniformCostSearch(rootNode, visualize);
			break;
		case "ID":
			solution = wss.iterativeDeepeningSearch(rootNode, visualize);
			break;
		case "GR1":
			solution = wss.greedy(rootNode, 1, visualize);
			break;
		case "GR2":
			solution = wss.greedy(rootNode, 2, visualize);
			break;
		case "AS1":
			solution = wss.aStar(rootNode, 1, visualize);
			break;
		case "AS2":
			solution = wss.aStar(rootNode, 2, visualize);
			break;

		default:
			solution = "NOSOLUTION";
		}
		return solution;

	}

	//////////// Pouring Methods////////////
	@Override
	public PourResult getResult(Node node, String action) {
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

	public static PourResult pour(Node node, int i, int j) {
		String[][] state = node.getState();
		String[][] newState = copyState(state, new String[numberOfBottles][bottleCapacity]);

		String[] bottleToPourFrom = newState[i];
		String[] bottleToPourTo = newState[j];
		int pours = 0;

		int consecutive = checkConsecutive(bottleToPourFrom);

		int emptyToPourTo = emptySlots(bottleToPourTo);

		int topPourFromIndex = validPourFromIndex(getTopIndex(bottleToPourFrom));

		int topPourToIndex = validPourToIndex(getTopIndex(bottleToPourTo));

		while (emptyToPourTo >= consecutive && consecutive > 0) {
			pourOnce(topPourFromIndex, topPourToIndex, bottleToPourFrom, bottleToPourTo);
			topPourFromIndex++;
			topPourToIndex--;
			emptyToPourTo--;
			consecutive--;
			pours++;

		}

		return new PourResult(newState, pours);
	}

	public static void pourOnce(int fromIndex, int toIndex, String[] bottleToPourFrom, String[] bottleToPourTo) {

		bottleToPourTo[toIndex] = bottleToPourFrom[fromIndex];
		bottleToPourFrom[fromIndex] = "e";

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

	public static int checkConsecutive(String[] bottle) {
		int count = 0;
		String bottleTop = getTop(bottle);
		int bottleTopIndex = getTopIndex(bottle);
		if (bottleTopIndex == -1) {
			return count;
		}

		for (int i = bottleTopIndex; i < bottle.length; i++) {
			if (!bottle[i].equals("e") && bottle[i].equals(bottleTop)) {
				count++;
			} else {
				break;
			}
		}
		return count;

	}

	public static String[][] copyState(String[][] oldState, String[][] newState) {

		for (int i = 0; i < oldState.length; i++) {

			for (int j = 0; j < oldState[i].length; j++) {
				newState[i][j] = oldState[i][j];
			}
		}

		return newState;
	}

	public static int validPourToIndex(int index) {
		if (index == -1) {
			int validIndex = bottleCapacity - 1;
			return validIndex;
		} else {
			if (index != 0) {
				return --index;
			} else {
				return 0;
			}
		}
	}

	public static int validPourFromIndex(int index) {
		if (index == -1) {
			int validIndex = bottleCapacity - 1;
			return validIndex;
		} else {
			return index;
		}
	}


	public static String getTop(String[] bottle) {
		for (String color : bottle) {
			if (!color.equals("e")) {
				return color;
			}
		}
		return "e";

	}

	public static int getTopIndex(String[] bottle) {
		for (int i = 0; i < bottle.length; i++) {
			String color = bottle[i];
			if (!color.equals("e")) {
				return i;
			}
		}
		return -1;
	}

	private boolean isFullySorted(String[] bottle) {
		String color = null;
		for (String liquid : bottle) {
			if (!liquid.equals("e")) {
				if (color == null) {
					color = liquid;
				} else if (!liquid.equals(color)) {
					return false; 
				}
			}
		}
		return color != null; 
	}

	private boolean isEmptyBottle(String[] bottle) {
		for (String liquid : bottle) {
			if (!liquid.equals("e")) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		String init = "6;" +"4;" +"g,g,g,r;" +"g,y,r,o;" +"o,r,o,y;" + "y,o,y,b;" + "r,b,b,b;" +  "e,e,e,e;";
		prepareInitialState(init, false);
		solve(init, "UC", true);

	}

}