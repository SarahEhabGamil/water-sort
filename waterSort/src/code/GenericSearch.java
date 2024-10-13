package code;

import java.util.*;

public abstract class GenericSearch {
	public int nodesExpanded;

	public abstract boolean isGoalState(String[][] currentState);

	public abstract List<String> getOperations(Node node);

	public abstract String[][] getResult(Node node, String action);

	public abstract int getStepCost(String state, String action);

	public String breadthFirstSearch(Node initialNode) {
		Queue<Node> queue = new LinkedList<>();
		Set<String> visited = new HashSet<>(); 

		
		queue.add(initialNode);
		visited.add(convertStateToString(initialNode.getState()));

		while (!queue.isEmpty()) {
			System.out.println("--------------------------------------------------------------");
			Node node = queue.poll();

			String[][] currentState = node.getState();

			if (isGoalState(currentState)) {
				System.out.println("--------------------------------------------------------------");
				System.out.println("Goal state");
				String plan = plan(node);
				String goalPath = formulateOutput(plan, node.getPathCost(), nodesExpanded);
				System.out.println("Goal Path: " + goalPath);
				System.out.println("--------------------------------------------------------------");
				return goalPath;
			}

			System.out.println("Operations for next Child: " + getOperations(node));

			for (String action : getOperations(node)) {
				System.out.println("Action in hand: " + action);

				String[][] childState = getResult(node, action);
				String childStateString = convertStateToString(childState);

				if (!visited.contains(childStateString) && !isReverseAction(node.getAction(), action)) {

					Node childNode = new Node(childState, node, action, node.getDepth() + 1, node.getPathCost());
					System.out.println("Child Node:");
					printNode(childNode);

					visited.add(childStateString);
					queue.add(childNode);
					nodesExpanded++;
				}

				System.out.println("--------------------------------------------------------------");
			}
		}

		return "nosolution";
	}

	public String depthFirstSearch(String[][] initialSatate) {
		return null;
	}

	public String iterativeDeepningSearch(String[][] initialState, int maxHeight) {
		return null;
	}

	public String uniformCostSearch(String[][] initialSatate) {
		return null;
	}

	public String plan(Node node) {
		List<String> actions = new ArrayList<>();

		while (node.getParent() != null) {
			String operator = node.getAction();
			actions.add(0, operator);
			node = node.getParent();
		}

		return String.join(",", actions);
	}

	public String formulateOutput(String plan, int pathCost, int nodesExpanded) {
		return plan + ";" + pathCost + ";" + nodesExpanded;
	}

	public static void printNode(Node node) {
		System.out.println("Parent State:");
		if (node.getParent() != null) {
			printState(node.getParent().getState());
			System.out.println("----------------");
		}
		printState(node.getState());
		System.out.println("Operator: " + node.getAction());
		System.out.println("Depth: " + node.getDepth());
		System.out.println("Path Cost: " + node.getPathCost());

	}

	public static void printState(String[][] state) {
		for (int i = 0; i < state.length; i++) {
			System.out.print("Bottle " + i + ": ");
			for (int j = 0; j < state[i].length; j++) {
				System.out.print(state[i][j] + " ");
			}

			System.out.println();
		}
	}

	public boolean isReverseAction(String previousAction, String currentAction) {

		if (previousAction == null || currentAction == null)
			return false;

		String[] prevParts = previousAction.split("_");
		String[] currParts = currentAction.split("_");

		if (prevParts.length < 3 || currParts.length < 3)
			return false;

		String prevSource = prevParts[1];
		String prevTarget = prevParts[2];
		String currSource = currParts[1];
		String currTarget = currParts[2];

		return prevSource.equals(currTarget) && prevTarget.equals(currSource);
	}

	public String convertStateToString(String[][] state) {
		StringBuilder sb = new StringBuilder();
		for (String[] bottle : state) {
			for (String color : bottle) {
				sb.append(color).append(",");
			}
			sb.append(";");
		}
		return sb.toString();
	}
}
