package code;

import java.util.*;

public abstract class GenericSearch {
	public int nodesExpanded;

	public abstract boolean isGoalState(String[][] currentState);

	public abstract List<String> getOperations(Node node);

	public abstract PourResult getResult(Node node, String action);

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

				PourResult childResult = getResult(node, action);
				String [][] childState =childResult.getState();
				int pours = childResult.getPours();
				String childStateString = convertStateToString(childState);

				if (!visited.contains(childStateString) && !isReverseAction(node.getAction(), action)) {

					int cost =node.getPathCost()+pours;
					
					Node childNode = new Node(childState, node, action, node.getDepth() + 1, cost);
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

	public String depthFirstSearch(Node initialNode) {
	    Stack<Node> stack = new Stack<>();  
	    Set<String> visited = new HashSet<>();

	    stack.push(initialNode);  
	    visited.add(convertStateToString(initialNode.getState()));

	    while (!stack.isEmpty()) {
	        System.out.println("--------------------------------------------------------------");
	        Node node = stack.pop();

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

	            PourResult childResult = getResult(node, action);
				String [][] childState =childResult.getState();
				int pours = childResult.getPours();
				String childStateString = convertStateToString(childState);
				
	            if (!visited.contains(childStateString) && !isReverseAction(node.getAction(), action)) {
	            	int cost =node.getPathCost()+pours;
	                Node childNode = new Node(childState, node, action, node.getDepth() + 1, cost);
	                System.out.println("Child Node:");
	                printNode(childNode);

	                visited.add(childStateString); 
	                stack.push(childNode);
	                nodesExpanded++;
	            }

	            System.out.println("--------------------------------------------------------------");
	        }
	    }

	    return "nosolution";
	}

	public String iterativeDeepeningSearch(Node initialNode) {
	    int depthLimit = 0;

	    while (true) {
	        System.out.println("--------------------------------------------------------------");
	        String result = depthLimitedSearch(initialNode, depthLimit);
	        if (!result.equals("cutoff") && !result.equals("nosolution")) {
	            System.out.println("Goal Path: " + result);
	            return result;
	        }

	        if (result.equals("nosolution")) {
	            return "nosolution";
	        }
	        
	        depthLimit++;
	    }
	}

	public String depthLimitedSearch(Node node, int limit) {
	    Stack<Node> stack = new Stack<>();
	    Set<String> visited = new HashSet<>();
	    
	    stack.push(node);
	    visited.add(convertStateToString(node.getState()));

	    while (!stack.isEmpty()) {
	        System.out.println("--------------------------------------------------------------");
	        Node currentNode = stack.pop();
	        
	        String[][] currentState = currentNode.getState();

	        if (isGoalState(currentState)) {
	            System.out.println("Goal state found.");
	            String plan = plan(currentNode);
	            return formulateOutput(plan, currentNode.getPathCost(), nodesExpanded);
	        }
	        
	        if (currentNode.getDepth() >= limit) {
	            return "cutoff";
	        }

	        for (String action : getOperations(currentNode)) {
	            PourResult childResult = getResult(currentNode, action);
	            String[][] childState = childResult.getState();
	            int pours = childResult.getPours();
	            String childStateString = convertStateToString(childState);

	            if (!visited.contains(childStateString) && !isReverseAction(currentNode.getAction(), action)) {
	                int cost = currentNode.getPathCost() + pours;
	                Node childNode = new Node(childState, currentNode, action, currentNode.getDepth() + 1, cost);
	                visited.add(childStateString);
	                stack.push(childNode);
	                nodesExpanded++;
	            }
	        }
	    }
	    
	    return "nosolution";
	}



		public String uniformCostSearch(Node initialNode) {
		    PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getPathCost));  
		    Set<String> visited = new HashSet<>();

		    priorityQueue.add(initialNode);  
		    visited.add(convertStateToString(initialNode.getState()));

		    while (!priorityQueue.isEmpty()) {
		        System.out.println("--------------------------------------------------------------");
		        Node node = priorityQueue.poll();

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

		            PourResult childResult = getResult(node, action);
		            String[][] childState = childResult.getState();
		            int pours = childResult.getPours();
		            String childStateString = convertStateToString(childState);

		            if (!visited.contains(childStateString) && !isReverseAction(node.getAction(), action)) {
		                int cost = node.getPathCost() + pours;
		                Node childNode = new Node(childState, node, action, node.getDepth() + 1, cost);
		                System.out.println("Child Node:");
		                printNode(childNode);

		                visited.add(childStateString); 
		                priorityQueue.add(childNode);
		                nodesExpanded++;
		            }

		            System.out.println("--------------------------------------------------------------");
		        }
		    }

		    return "nosolution";
		}
		
		public String aStar(Node initialNode, int heuristicNumber) {
		    PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.getPathCost() + evaluateHeuristic(node, heuristicNumber)));
		    Set<String> visited = new HashSet<>();

		    priorityQueue.add(initialNode);
		    visited.add(convertStateToString(initialNode.getState()));

		    while (!priorityQueue.isEmpty()) {
		        Node node = priorityQueue.poll();
		        String[][] currentState = node.getState();

		        if (isGoalState(currentState)) {
		            String plan = plan(node);
		            return formulateOutput(plan, node.getPathCost(), nodesExpanded);
		        }

		        for (String action : getOperations(node)) {
		            PourResult childResult = getResult(node, action);
		            String[][] childState = childResult.getState();
		            int pours = childResult.getPours();
		            String childStateString = convertStateToString(childState);

		            if (!visited.contains(childStateString) && !isReverseAction(node.getAction(), action)) {
		                int cost = node.getPathCost() + pours;
		                Node childNode = new Node(childState, node, action, node.getDepth() + 1, cost);
		                visited.add(childStateString);
		                priorityQueue.add(childNode);  // Add the child node to the queue
		                nodesExpanded++;
		            }
		        }
		    }

		    return "nosolution";
		}

		public String greedy(Node initialNode, int heuristicNumber) {
		    PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> evaluateHeuristic(node, heuristicNumber)));
		    Set<String> visited = new HashSet<>();

		    priorityQueue.add(initialNode);
		    visited.add(convertStateToString(initialNode.getState()));

		    while (!priorityQueue.isEmpty()) {
		        Node node = priorityQueue.poll();
		        String[][] currentState = node.getState();

		        if (isGoalState(currentState)) {
		            String plan = plan(node);
		            return formulateOutput(plan, node.getPathCost(), nodesExpanded);
		        }

		        for (String action : getOperations(node)) {
		            PourResult childResult = getResult(node, action);
		            String[][] childState = childResult.getState();
		            int pours = childResult.getPours();
		            String childStateString = convertStateToString(childState);

		            if (!visited.contains(childStateString) && !isReverseAction(node.getAction(), action)) {
		                int cost = node.getPathCost() + pours;
		                Node childNode = new Node(childState, node, action, node.getDepth() + 1, cost);
		                visited.add(childStateString);
		                priorityQueue.add(childNode);  // Add to the queue based on heuristic value
		                nodesExpanded++;
		            }
		        }
		    }

		    return "nosolution";
		}

		// Heuristic evaluation based on the heuristic number passed to the function
		private int evaluateHeuristic(Node node, int heuristicNumber) {
		    if (heuristicNumber == 1) {
		        return misplacedColorsHeuristic(node);
		    } else if (heuristicNumber == 2) {
		        return incompleteBottlesHeuristic(node);
		    } else {
		        throw new IllegalArgumentException("Invalid heuristic number.");
		    }
		}

		// Heuristic 1: Counts the number of misplaced colors in a bottle
		private int misplacedColorsHeuristic(Node node) {
		    String[][] state = node.getState();
		    int misplacedColors = 0;

		    for (String[] bottle : state) {
		        if (bottle[0] == null) continue;  // Skip empty bottles
		        String topColor = bottle[getTopIndex(bottle)];
		        for (String liquid : bottle) {
		            if (liquid != null && !liquid.equals(topColor)) {
		                misplacedColors++;
		            }
		        }
		    }

		    return misplacedColors;
		}
		private int getTopIndex(String[] bottle) {
		    for (int i = 0; i < bottle.length; i++) {
		        if (bottle[i] != null) {
		            return i;
		        }
		    }
		    return bottle.length; // If the bottle is empty, return length as the top index (no liquid)
		}

		// Heuristic 2: Counts the number of incomplete bottles (bottles with more than one unique color)
		private int incompleteBottlesHeuristic(Node node) {
		    String[][] state = node.getState();
		    int incompleteBottles = 0;

		    for (String[] bottle : state) {
		        Set<String> colors = new HashSet<>();
		        for (String liquid : bottle) {
		            if (liquid != null) {
		                colors.add(liquid);
		            }
		        }
		        if (colors.size() > 1) {
		            incompleteBottles++;
		        }
		    }

		    return incompleteBottles;
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
