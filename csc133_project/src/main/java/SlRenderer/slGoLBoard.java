package SlRenderer;
import java.util.Random;
/**
 * The slGoLBoard class represents the Game of Life board,
 * managing the state of cells and updating their states based on the game's rules.
 */
public class slGoLBoard {
    private static boolean[][] liveCellArray;
    private boolean[][] nextCellArray;
    private static int numRows;
    private static int numCols;
    public slGoLBoard(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        liveCellArray = new boolean[numRows][numCols];
        nextCellArray = new boolean[numRows][numCols];
        initializeBoard();
    }
    public void initializeBoard() {
        // Initialize the board here (randomly or as needed)
        Random random = new Random();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                liveCellArray[row][col] = random.nextBoolean();
            }
        }
    }
    public void updateBoard() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int degreeTwoNeighbors = countDegreeTwoNeighbors(row, col);
                if (liveCellArray[row][col]) {
                    nextCellArray[row][col] = degreeTwoNeighbors == 2 || degreeTwoNeighbors == 3;
                } else {
                    nextCellArray[row][col] = degreeTwoNeighbors == 3;
                }
            }
        }
        // Swap current and next generation arrays
        boolean[][] temp = liveCellArray;
        liveCellArray = nextCellArray;
        nextCellArray = temp;
    }
    private int countDegreeTwoNeighbors(int row, int col) {
        int count = 0;
        for (int i = row - 2; i <= row + 2; i++) {
            for (int j = col - 2; j <= col + 2; j++) {
                if (i >= 0 && i < numRows && j >= 0 && j < numCols && !(i == row && j == col)) {
                    if (liveCellArray[i][j]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    public static String getStatus() {
        StringBuilder status = new StringBuilder();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                status.append(isCellAlive(row, col) ? "1" : "0");
            }
            status.append("\n"); 
        }
        return status.toString();
    }
    public static boolean isCellAlive(int row, int col) {
        return liveCellArray[row][col];
    }
    public static void setCellState(int row, int col, boolean state) {
        // Set the state of the cell at the specified row and column
        liveCellArray[row][col] = state;
    }
}
