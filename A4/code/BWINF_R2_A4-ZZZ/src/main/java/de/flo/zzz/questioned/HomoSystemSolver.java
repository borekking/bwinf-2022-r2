package de.flo.zzz.questioned;

import de.flo.zzz.util.JLinAlgSeri;
import de.flo.zzz.util.JLinAlgUtils;

import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.f2.F2;

public class HomoSystemSolver {

    public static boolean minSolRespectsColorPattern(int[] varColors, int colorAmount, Matrix<F2> matrix) {
        int rows = matrix.getRows(), cols = matrix.getCols();
        Vector<F2> zeroCol = JLinAlgUtils.getZeroVector(rows);
        Vector<F2> vars = JLinAlgUtils.getZeroVector(cols);
        Vector<F2> b = zeroCol.copy();
        boolean[] usedColors = new boolean[colorAmount];

        for (int col = 1; col <= cols; col++) {
            vars.set(col, F2.ONE);

            // Create copy of matrix and set col of variable to zero-vector
            Matrix<F2> newMatrix = matrix.copy();
            newMatrix.setCol(col, zeroCol);

            // Create Copy of b and add current row to it
            Vector<F2> newB = b.add(matrix.getCol(col));

            // Recursively call methode
            if (minSolRespectsColorPatternImp(newMatrix, vars, newB, zeroCol, cols, rows,
                    1, varColors, usedColors))
                return true;

            vars.set(col, F2.ZERO);
        }

        return false;
    }

    private static boolean minSolRespectsColorPatternImp(Matrix<F2> matrix, Vector<F2> vars, Vector<F2> b,
                                              Vector<F2> zeroCol, int cols, int rows, int selected,
                                                         int[] varColors, boolean[] usedColors) {
        if (selected > cols) {
            return false;
        }

        if (isZeroVec(b, rows)) {
            JLinAlgSeri.printVector(vars);
            System.out.println("---");
            return true;
        }

        if (selected == cols) return false;

        // Get first equation where b_i = 1 and iterate over
        // all variables which occur in equation i.
        for (int row = 1; row <= rows; row++) {
            // Check b_i != 1
            if (b.getEntry(row) != F2.ONE) continue;

            // b_i is 1:
            // Iterate over all variables, check for those
            // which are enabled in equation i.
            for (int col = 1; col <= cols; col++) {
                // Filter non-appearing variables
                if (matrix.get(row, col) != F2.ONE) continue;

                // Only Choose variable if it's color is not used set
                int color = varColors[col]; // Get current var's color
                if (usedColors[color]) continue; // Continue if current color is already used

                // Set current color to be used
                usedColors[color] = true;

                // Set current variable to be enabled
                vars.set(col, F2.ONE);

                // Create copy of matrix and set col of variable to zero-vector
                Matrix<F2> newMatrix = matrix.copy();
                newMatrix.setCol(col, zeroCol);

                // Create Copy of b and add current row to it
                Vector<F2> newB = b.add(matrix.getCol(col));

                // Recursively call methode
                if (minSolRespectsColorPatternImp(newMatrix, vars, newB, zeroCol, cols, rows,
                        selected + 1, varColors, usedColors))
                    return true;

                // Re-set current variable to be disabled
                vars.set(col, F2.ZERO);

                // Re-set current color to be unused
                usedColors[color] = false;
            }

            // Stop loop
            break;
        }

        return false;
    }

    public static int minSolSize(Matrix<F2> matrix) {
        int cols = matrix.getCols();

        for (int i = 1; i <= cols; i++) {
            if (linEqAtMostHomo(matrix, i)) return i;
        }

        return -1;
    }

    public static boolean linEqAtMostHomo(Matrix<F2> matrix, int t) {
        int rows = matrix.getRows(), cols = matrix.getCols();
        Vector<F2> zeroCol = JLinAlgUtils.getZeroVector(rows);
        Vector<F2> vars = JLinAlgUtils.getZeroVector(cols);
        Vector<F2> b = zeroCol.copy();

        for (int col = 1; col <= cols; col++) {
            vars.set(col, F2.ONE);

            // Create copy of matrix and set col of variable to zero-vector
            Matrix<F2> newMatrix = matrix.copy();
            newMatrix.setCol(col, zeroCol);

            // Create Copy of b and add current row to it
            Vector<F2> newB = b.add(matrix.getCol(col));

            // Recursively call methode
            if (linEqAtMostHomoImp(newMatrix, vars, newB, zeroCol, t, cols, rows, 1))
                return true;

            vars.set(col, F2.ZERO);
        }

        return false;
    }

    private static boolean linEqAtMostHomoImp(Matrix<F2> matrix, Vector<F2> vars, Vector<F2> b,
                                              Vector<F2> zeroCol, int t, int cols, int rows,
                                              int selected) {
        if (selected > t) {
            return false;
        }

        if (isZeroVec(b, rows)) {
            JLinAlgSeri.printVector(vars);
            System.out.println("---");
            return true;
        }

        if (selected == t) return false;

        // Get first equation where b_i = 1 and iterate over
        // all variables which occur in equation i.
        for (int row = 1; row <= rows; row++) {
            // Check b_i != 1
            if (b.getEntry(row) != F2.ONE) continue;

            // b_i is 1:
            // Iterate over all variables, check for those
            // which are enabled in equation i.
            for (int col = 1; col <= cols; col++) {
                // Filter non-appearing variables
                if (matrix.get(row, col) != F2.ONE) continue;

                // Set current variable to be enabled
                vars.set(col, F2.ONE);

                // Create copy of matrix and set col of variable to zero-vector
                Matrix<F2> newMatrix = matrix.copy();
                newMatrix.setCol(col, zeroCol);

                // Create Copy of b and add current row to it
                Vector<F2> newB = b.add(matrix.getCol(col));

                // Recursively call methode
                if (linEqAtMostHomoImp(newMatrix, vars, newB, zeroCol, t, cols, rows, selected + 1))
                    return true;

                // Re Set current variable to be disabled
                vars.set(col, F2.ZERO);
            }

            // Stop loop
            break;
        }

        return false;
    }

    private static boolean isZeroVec(Vector<F2> vec, int rows) {
        for (int i = 1; i <= rows; i++) {
            if (vec.getEntry(i) == F2.ONE) return false;
        }
        return true;
    }
}