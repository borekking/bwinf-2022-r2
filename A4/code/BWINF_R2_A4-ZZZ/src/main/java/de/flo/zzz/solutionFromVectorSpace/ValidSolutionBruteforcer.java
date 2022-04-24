package de.flo.zzz.solutionFromVectorSpace;

import de.flo.zzz.util.HammingUtils;

import org.jlinalg.Vector;
import org.jlinalg.f2.F2;

public class ValidSolutionBruteforcer {

    private final Vector<F2>[] span;

    private final int freeVariables;

    private final int rows;

    private final int weight;

    private final Vector<F2> result;

    public ValidSolutionBruteforcer(Vector<F2>[] span, int rows, int weight) {
        this.span = span;
        this.freeVariables = this.span.length;
        this.rows = rows;
        this.weight = weight;

        this.result = this.bruteforce();
    }

    private Vector<F2> bruteforce() {
        for (int i = 1; i <= this.weight; i++) {

            boolean[] choosenVectors = new boolean[this.freeVariables];
            Vector<F2> solution = this.bruteforceImp(choosenVectors, 0, -1, i);

            if (solution != null)
                return solution;
        }

        return null;
    }

    private Vector<F2> bruteforceImp(boolean[] values, int indexInArray, int indexInSpan, int k) {
        if (k == 0) {
            Vector<F2> solution = this.getSolutionVector(values);
            if (HammingUtils.getHammingWeight(solution, this.rows) == this.weight)
                return solution;

            return null;
        }

        int start = indexInSpan + 1;
        int end = this.freeVariables - k;

        for (int i = start; i <= end; i++) {
            values[indexInArray] = true;

            Vector<F2> solution = this.bruteforceImp(values, indexInArray + 1, i, k - 1);
            if (solution != null) return solution;

            values[indexInArray] = false;
        }

        return null;
    }

    // Returns a solution to the system with chosen values of x_i for 1 <= i <= vectors.length
    private Vector<F2> getSolutionVector(boolean[] values) {
        F2[] array = new F2[this.rows];

        for (int row = 0; row < this.rows; row++) {
            F2 val = F2.ZERO;

            for (int i = 0; i < this.freeVariables; i++) {
                if (!values[i]) continue;

                Vector<F2> vec = this.span[i];

                F2 cu = vec.getEntry(row + 1);
                val = val.add(cu);
            }

            array[row] = val;
        }

        return new Vector<>(array);
    }

    public Vector<F2> getResult() {
        return result;
    }
}