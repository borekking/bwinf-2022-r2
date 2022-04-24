package de.flo.zzz.util;

import org.jlinalg.Matrix;
import org.jlinalg.RingElement;
import org.jlinalg.Vector;
import org.jlinalg.f2.F2;

import java.util.function.Function;

public class JLinAlgUtils {

    // F2 -> Integer
    public static final Function<F2, Integer> f2ToIntFunction = f2 -> f2.isOne() ? 1 : 0;

    // F2 -> Boolean
    public static final Function<F2, Boolean> f2ToBoolFunction = RingElement::isOne;

    // Integer -> F2
    public static final Function<Integer, F2> intToF2Function = i -> i % 2 == 0 ? F2.ZERO : F2.ONE;

    // Function to get from boolean to F2-value
    public static Function<Boolean, F2> boolToF2Function = b -> b ? F2.ONE : F2.ZERO;

    // Create zero-vector
    public static Vector<F2> getZeroVector(int rows) {
        F2[] bVecArray = new F2[rows];
        for (int i = 0; i < rows; i++) {
            bVecArray[i] = F2.ZERO;
        }
        return new Vector<>(bVecArray);
    }

    public static Matrix<F2> createMatrix(Vector<F2>[] vectors, int rows) {
        F2[][] matrixArr = new F2[rows][vectors.length];

        for (int row = 0; row < vectors.length; row++) {
            for (int col = 0; col < rows; col++) {
                matrixArr[col][row] = vectors[row].getEntry(col + 1);
            }
        }

        return new Matrix<>(matrixArr);
    }
}
