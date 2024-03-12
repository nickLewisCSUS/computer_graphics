package SlRenderer;

import java.util.Random;

public class slGoLBoard {
    private boolean[][] liveCellArray;
    private boolean[][] nextCellArray;
    private int numRows;
    private int numCols;

    public slGoLBoard(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        liveCellArray = new boolean[numRows][numCols];
        nextCellArray = new boolean[numRows][numCols];
        initializeBoard();
    }

    private void initializeBoard() {
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
                    // Any live cell with fewer than two live neighbors dies
                    // Any live cell with two or three live neighbors lives on to the next generation
                    // Any live cell with more than three live neighbors dies
                    nextCellArray[row][col] = degreeTwoNeighbors == 2 || degreeTwoNeighbors == 3;
                } else {
                    // Any dead cell with exactly three live neighbors becomes a live cell
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

    public boolean isCellAlive(int row, int col) {
        return liveCellArray[row][col];
    }
}
