package de.flo.zzz.f2HomoSystemSolver;

import java.util.Arrays;
import java.util.StringJoiner;

public class F2Matrix {

    private final int[][] matrix;

    private final int rows, columns;

    public F2Matrix(int[][] matrix, int rows, int columns) {
        this.matrix = matrix;
        this.rows = rows;
        this.columns = columns;

        this.checkMatrix(matrix, rows, columns);
        this.checkF2(matrix);
    }

    /**
     * Swap the rows at indexes rowA, rowB
     * @param rowIndexA First row
     * @param rowIndexB Second row
     */
    public void swap(int rowIndexA, int rowIndexB) {
        // Check valid indexes
        this.checkRowIndex(rowIndexA);
        this.checkRowIndex(rowIndexB);

        // Trivial
        if (rowIndexA == rowIndexB) return;

        // Actually swap:
        // Save first row
        int[] rowA = this.matrix[rowIndexA];
        // Set first row to second row in matrix
        this.matrix[rowIndexA] = this.matrix[rowIndexB];
        // Set second row to saved first row
        this.matrix[rowIndexB] = rowA;
    }

    /**
     * Add rows at given indexes in F2/Z_2
     * @param rowIndexA First row's index
     * @param rowIndexB Second row's index
     * @return Combined rows (added element by element)
     */
    public int[] addRows(int rowIndexA, int rowIndexB) {
        // Check valid indexes
        this.checkRowIndex(rowIndexA);
        this.checkRowIndex(rowIndexB);

        // Trivial
        if (rowIndexA == rowIndexB) return new int[this.columns];

        int[] rowA = this.matrix[rowIndexA];
        int[] rowB = this.matrix[rowIndexB];

        int[] result = new int[this.columns];
        for (int i = 0; i < this.columns; i++) {
            // Note, a + b in F2 <=> (a + b) mod 2 <=> a ^ b,
            // since both result in 1 if one of a and b is 1.
            result[i] = rowA[i] ^ rowB[i];
        }

        return result;
    }

    /**
     * Set row at given index to given row
     * @param rowIndex Row's index
     * @param row New row
     */
    public void setRow(int rowIndex, int[] row) {
        // Check valid index
        this.checkRowIndex(rowIndex);

        // Check valid row (by columns)
        this.checkRow(row, this.columns);

        // Set the row at given index in matrix
        this.matrix[rowIndex] = row;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", " + System.lineSeparator());

        for (int i = 0; i < this.rows; i++) {
            joiner.add(Arrays.toString(this.matrix[i]));
        }

        return joiner.toString();
    }

    /**
     * Check an index being a valid index for a row, meaning 0 <= row < rows
     * @param row The row's index to check
     * @throws IllegalArgumentException If the row's index is invalid
     */
    private void checkRowIndex(int row) {
        if (row >= 0 && row < this.rows) return;

        throw new IllegalArgumentException("Row index " + row + " is invalid for " + rows + " rows.");
    }

    /**
     * Check an index being a valid index for a column, meaning 0 <= column < columns
     * @param column The column's index to check
     * @throws IllegalArgumentException If the column's index is invalid
     */
    private void checkColumnIndex(int column) {
        if (column >= 0 && column < this.columns) return;

        throw new IllegalArgumentException("Column index " + column + " is invalid for " + this.columns + " columns.");
    }

    /**
     * Check if a matrix is valid
     * @param matrix Given matrix
     * @param rows Expected amount of rows in matrix
     * @param columns Expected amount of columns for each row in matrix
     * @throws IllegalArgumentException If amount of rows or columns does not match matrix
     */
    private void checkMatrix(int[][] matrix, int rows, int columns) {
        if (matrix.length == rows) {
            for (int i = 0; i < rows; i++) {
                this.checkRow(matrix[i], columns);
            }
            return;
        }

        throw new IllegalArgumentException("Given rows amount does not match given matrix!");
    }

    /**
     * Check if a row is valid
     * @param row Row to check
     * @throws IllegalArgumentException If the row does not match the columns (by length)
     */
    private void checkRow(int[] row, int columns) {
        if (row.length == columns) return;

        throw new IllegalArgumentException("Row did not match column amount!");
    }

    /**
     * Methode to check if a matrix is in F2 meaning all entries
     * are either 0 or 1.
     * @param matrix The matrix to solve as 2D int array
     * @throws RuntimeException If matrix contains any entry not equal to 0 or 1
     */
    private void checkF2(int[][] matrix) {
        for (int[] row : matrix) {
            for (int entry : row) {
                if (entry == 0 || entry == 1) {
                    continue;
                }

                throw new IllegalArgumentException("Given matrix has entries not being either 1 or 0!");
            }
        }
    }

    public int getEntry(int row, int column) {
        this.checkRowIndex(row);
        this.checkColumnIndex(column);

        return this.matrix[row][column];
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}
