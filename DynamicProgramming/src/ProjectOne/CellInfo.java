package ProjectOne;

/**
 * The {@code CellInfo} class represents information about a specific cell in a matrix, typically used
 * in dynamic programming algorithms to store costs and paths for optimal decision-making processes.
 */
public class CellInfo {
    private int optimalCost; // Minimum or optimal cost to reach this cell
    private String optimalPath; // Description or identifier of the optimal path leading to this cell
    private CellInfo[] cellInfos; // Array of CellInfo objects, potentially used for tracking additional path details or branching decisions

    /**
     * Constructs a new CellInfo object with specified cost and path.
     *
     * @param optimalCost  the optimal or minimum cost associated with reaching this cell
     * @param optimalPath  the optimal or best path description leading to this cell
     */
    public CellInfo(int optimalCost, String optimalPath) {
        this.optimalCost = optimalCost;
        this.optimalPath = optimalPath;
    }

    /**
     * Retrieves the optimal cost of reaching this cell.
     *
     * @return the optimal cost
     */
    public int getOptimalCost() {
        return optimalCost;
    }

    /**
     * Sets the optimal cost of reaching this cell.
     *
     * @param optimalCost the new optimal cost to be set
     */
    public void setOptimalCost(int optimalCost) {
        this.optimalCost = optimalCost;
    }

    /**
     * Retrieves the optimal path description leading to this cell.
     *
     * @return the optimal path as a String
     */
    public String getOptimalPath() {
        return optimalPath;
    }

    /**
     * Sets the optimal path description for this cell.
     *
     * @param optimalPath the new optimal path description to be set
     */
    public void setOptimalPath(String optimalPath) {
        this.optimalPath = optimalPath;
    }

    /**
     * Retrieves an array of {@code CellInfo} objects associated with this cell.
     * This can be used to store additional information about paths or decisions related to this cell.
     *
     * @return an array of {@code CellInfo} objects
     */
    public CellInfo[] getCellInfos() {
        return cellInfos;
    }

    /**
     * Sets the array of {@code CellInfo} objects associated with this cell.
     *
     * @param cellInfos the new array of {@code CellInfo} objects to be linked with this cell
     */
    public void setCellInfos(CellInfo[] cellInfos) {
        this.cellInfos = cellInfos;
    }
}
