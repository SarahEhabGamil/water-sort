package code;

public class PourResult {
    private String[][] state;
    private int pours;

    public PourResult(String[][] state, int pours) {
        this.state = state;
        this.pours = pours;
    }

    public String[][] getState() {
        return state;
    }

    public int getPours() {
        return pours;
    }
}