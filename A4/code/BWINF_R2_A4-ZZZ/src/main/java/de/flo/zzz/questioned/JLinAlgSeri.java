package de.flo.zzz.questioned;

import de.flo.zzz.util.JLinAlgUtils;
import org.jlinalg.AffineLinearSubspace;
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.f2.F2;

import java.util.StringJoiner;
import java.util.function.Function;

public class JLinAlgSeri {

    public static void printMatrix(Matrix<F2> matrix) {
        int rows = matrix.getRows();
        int columns = matrix.getCols();

        for (int row = 1; row <= rows; row++) {
            StringJoiner joiner = new StringJoiner(" ", "[", "]");

            for (int col = 1; col <= columns; col++) {
                int e = JLinAlgUtils.f2ToIntFunction.apply(matrix.get(row, col));
                joiner.add(String.valueOf(e));
            }

            System.out.println(joiner);
        }
    }

    public static void printSolutionSpace(AffineLinearSubspace<F2> solutionSpace, int rows) {
        Vector<F2> inhomogenousPart = solutionSpace.getInhomogenousPart();
        Vector<F2>[] set = solutionSpace.getGeneratingSystem();

        // Print vectors row wise
        for (int row = 1; row <= rows; row++) {
            if (inhomogenousPart != null) {
                System.out.print("[" + JLinAlgUtils.f2ToIntFunction.apply(inhomogenousPart.getEntry(row)) + "]  |  ");
            }

            for (Vector<F2> v : set) {
                F2 entry = v.getEntry(row);
                System.out.print("[" + JLinAlgUtils.f2ToIntFunction.apply(entry) + "]  ");
            }

            System.out.println();
        }


        System.out.println("Frees: " + set.length);
    }

    public static void printVector(Vector<F2> vector) {
        printMatrix(vector.toMatrix());
    }
}
