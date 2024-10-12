package code;
import java.util.*;

public abstract class GenericSearch {
	
	 public abstract boolean isGoalState(String state);
	 public abstract List<String> getOperations(String state);
	 public abstract String getResult(String state, String action);
	 public abstract int getStepCost(String state, String action);
	 
	 
	 public List<String> breadthFirstSearch(String[][] initialState) {
	       return null;
	 }
	 
	 public List<String> depthFirstSearch (String[][] initialSatate){
		 return null;
	 }
	 
	 public List<String> iterativeDeepningSearch(String[][] initialState , int maxHeight){
		 return null;
	 }
	 
	 public List<String> uniformCostSearch (String[][] initialSatate){
		 return null;
	 }
	 
	 private List<String> constructSolution(Node node) {
	       return null;
	    }

}
