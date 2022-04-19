package de.flo.zzz.f2HomoSystemSolver;

public class F2Solver {

    /*
     * TODO Unfinished
     *
     */

    /*
     * Linear System solving for F2 = Z_2
     *
     */

    public static void main(String[] args) {
        int[][] array = new int[][]{
                {0, 1, 1},
                {1, 1, 0},
                {0, 0, 1},
                {1, 0, 1}
        };
        int rows = 4;
        int columns = 3;

        F2Solver solver = new F2Solver();

        // Create matrix (validation is done in F2Matrix class)
        F2Matrix matrix = new F2Matrix(array, rows, columns);

        System.out.println(matrix);

        solver.solveHomo(matrix);
    }

    public F2Solver() {
    }

    /**
     * Methode for solving a homogenous system.
     * Find x where Ax = 0
     *
     * @param matrix Matrix A
     */
    public void solveHomo(F2Matrix matrix) {
        // Create Echelon Form
        this.getEchelonForm(matrix);

        // Create Reduced Echelon form


    }

    /**
     * Create echelon form in matrix.
     * @param matrix Matrix to create echelon form in
     */
    private void getEchelonForm(F2Matrix matrix) {
        for (int col = 0; col < matrix.getColumns(); col++) {
            this.moveZeroRowsDone(matrix, col, col);
        }

        System.out.println("-----------");
        System.out.println(matrix);

        for (int col = 0; col < matrix.getColumns(); col++) {
            this.eliminateUnder(matrix, col, col);
        }

        System.out.println("-----------");
        System.out.println(matrix);
    }

    /**
     * Eliminate all 1's in given column under (exclusive) a given row using the row at startRow.
     * Expecting that matrix[startRow][column] is 1. (checked)
     * Expecting column is sorted like 1, ..., 1, 0, ..., 0 (unchecked)
     * @param matrix The matrix to work in
     * @param column The column
     * @param startRow The row to start from (exclusive)
     * @throws IllegalArgumentException If matrix[startRow][column] != 1
     */
    private void eliminateUnder(F2Matrix matrix, int column, int startRow) {
        // Check matrix[startRow][column] is 1, s.t. the rows below
        // can be eliminated in column.
        System.out.println("--");
        System.out.println(matrix);

        if (matrix.getEntry(startRow, column) != 1) {
            throw new IllegalArgumentException("Matrix has to be 1 at startRow (" + startRow + ") , Column (" + column + ")");
        }

        // TODO Add managing of free variables ... (?)

        for (int row = startRow + 1; row < matrix.getRows(); row++) {
            // Stop loop if 0's are reached
            if (matrix.getEntry(row, column) == 0) {
                break;
            }

            // Eliminate by adding startRow and current row,
            // setting current row to result. (cause 1+1 = 0, in F2)
            int[] added = matrix.addRows(startRow, row);
            matrix.setRow(row, added);
        }
    }

    /**
     * Move all rows containing zero at row smaller, equal to startRow to the bottom of the matrix,
     * s.t. column will be of form 1, ... , 1, 0, ..., 0, from startRow on (inclusive)
     * @param matrix The matrix do perform the change in
     * @param column The column to swap in
     * @param startRow Row to start from
     */
    private void moveZeroRowsDone(F2Matrix matrix, int column, int startRow) {
        int latestOne = matrix.getRows();

        for (int row = startRow; row < matrix.getRows(); row++) {
            if (matrix.getEntry(row, column) == 0) { // Current is 0
                boolean done = true; // If the procedure is done

                // Search for next 1 from bottom up
                for (int i = latestOne - 1; i > row; i--) {
                    if (matrix.getEntry(i, column) == 1) {
                        // Swap row of found 1 (row i) with current row (where there is a 0)
                        matrix.swap(row, i);

                        done = false; // The column can not be done since there is a 1 under current 0
                        latestOne = i;
                        break;
                    }
                }

                if (done) {
                    break;
                }
            }
        }
    }
}
