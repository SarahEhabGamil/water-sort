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
		Set<Node> visited = new HashSet<>();
		
		queue.add(initialNode);

		while (!queue.isEmpty()) {
			Node node = queue.poll();

			String[][] currentState = node.getState();

			if (isGoalState(currentState)) {
				System.out.println("--------------------------------------------------------------");
				System.out.println("Goal state");
				String plan = plan(node);
				String goalPath = formulateOutput(plan, node.getPathCost(), nodesExpanded);
				System.out.println("Goal Path: "+ goalPath);
				System.out.println("--------------------------------------------------------------");
			}

			visited.add(node);

			System.out.println("Operations for next Child" + getOperations(node));

			for (String action : getOperations(node)) {

				String[][] childState = getResult(node, action);

				Node childNode = new Node(childState, node, action, node.getDepth() + 1, node.getPathCost() + 1);
				System.out.println("Child Node");
				printNode(childNode);
				nodesExpanded++;

				if (!visited.contains(childNode)) {
					queue.add(childNode);

				}
				System.out.println(("--------------------------------------"));
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
		printState(node.getState());
		System.out.println("Parent State:");
//		printState(node.getParent().getState());
		System.out.println("Operator: " + node.getAction());
		System.out.print("Depth: " + node.getDepth());
		System.out.println("Path Cost" + node.getPathCost());
	

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

}
