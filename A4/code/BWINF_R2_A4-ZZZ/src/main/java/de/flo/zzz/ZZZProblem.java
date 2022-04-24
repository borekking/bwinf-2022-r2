package de.flo.zzz;

import de.flo.zzz.bitSequence.BitSequence;
import de.flo.zzz.util.JLinAlgUtils;
import de.flo.zzz.util.JavaUtils;

import org.jlinalg.Matrix;
import org.jlinalg.f2.F2;

import java.util.List;

public class ZZZProblem {

    /*
     * n = size of bitSequences
     * m = amount if bits per bitSequence
     * k = amount of added bitSequences
     *
     */
    public final int n, k, m, size;

    private final List<BitSequence> bitSequences;

    public ZZZProblem(int n, int k, int m, List<BitSequence> bitSequences) {
        this.n = n;
        this.k = k;
        this.m = m;
        this.size = this.k + 1;
        this.bitSequences = bitSequences;
    }

    public Matrix<F2> createLinearSystem() {
        // Define amount of rows and columns
        int rows = this.m;
        int columns = this.n;

        // Create matrix
        F2[][] matrixArray = new F2[rows][columns];

        // Iterate through rows and columns
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Get current BS' bit, transform to F2, set in matrix-array
                boolean val = this.bitSequences.get(col).getBit(row);
                F2 f2 = JLinAlgUtils.boolToF2Function.apply(val);

                matrixArray[row][col] = f2;
            }
        }

        return new Matrix<>(matrixArray);
    }

    public List<BitSequence> getBitSequences() {
        return JavaUtils.clone(this.bitSequences);
    }
}
