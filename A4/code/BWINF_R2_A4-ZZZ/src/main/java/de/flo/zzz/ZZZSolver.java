package de.flo.zzz;

import de.flo.zzz.bitSequence.BitSequence;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jlinalg.AffineLinearSubspace;
import org.jlinalg.LinSysSolver;
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.f2.F2;

public class ZZZSolver extends AbstractZZZSolver {

    /*
     * Solve with lin systems
     *
     */

    // F2 -> Integer
    private final Function<F2, Integer> f2ToIntFunction = f2 -> f2.isOne() ? 1 : 0;

    // Integer -> F2
    private final Function<Integer, F2> intToF2Function = i -> i % 2 == 0 ? F2.ZERO : F2.ONE;

    public ZZZSolver(int n, int k, int m, List<BitSequence> bitSequences) {
        super(n, k, m, bitSequences);
    }

    @Override
    public List<Integer> getResult() {
        // Define amount of rows and columns
        int rows = this.m;
        int columns = this.n;

        // Function to get from boolean to F2-value
        Function<Boolean, F2> toF2Function = b -> b ? F2.ONE : F2.ZERO;

        // Create matrix
        F2[][] matrixArray = new F2[rows][columns];

        // Iterate through rows and columns
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Get current BS' bit, transform to F2, set in matrix-array
                boolean val = bitSequences.get(col).getBit(row);
                F2 f2 = toF2Function.apply(val);

                matrixArray[row][col] = f2;
            }
        }

        Matrix<F2> matrix = new Matrix<>(matrixArray);

        // Create zero-vector
        F2[] bVecArray = new F2[rows];
        for (int i = 0; i < rows; i++) {
            bVecArray[i] = F2.ZERO;
        }
        Vector<F2> bVec = new Vector<>(bVecArray);


        // Solve Ax = b for solution space of x
        AffineLinearSubspace<F2> solutionSpace = LinSysSolver.solutionSpace(matrix, bVec);

        System.out.println("Dimensions_1: " + solutionSpace.getDimension());

        // Get valid solution in solution set, where x_1 + ... + x_n = k+1 (in N)
//        // Methode 1. Brute Force
        Vector<F2> solution = this.bruteForceSmart(solutionSpace.getGeneratingSystem(), columns);

//        this.getValidSolution(solutionSpace.getGeneratingSystem(), columns);

//        return solution.toMatrix().get;
        return IntStream.range(1, columns+1).boxed().filter(i -> solution.getEntry(i) == F2.ONE).map(i -> i-1).collect(Collectors.toList());
    }

    private Vector<F2> getValidSolution(Vector<F2>[] vectors, int rows) {
        // Create vector containing sum (in F2) of every vector in vectors
        F2[] mArray = new F2[vectors.length];
        int counter = 0;

        for (int col = 0; col < vectors.length; col++) {
            Vector<F2> vec = vectors[col];
            F2 val = F2.ZERO;

            for (int i = 1; i <= rows; i++) {
                val = val.add(vec.getEntry(i));
            }

            mArray[col] = val;

            if (val == F2.ONE) counter++;
        }
        // --

        // Printing
//        System.out.println("m-Vector:");
//        printVector(mVector);
        System.out.println("Total: " + vectors.length);
        System.out.println("Counter (1's): " + counter);
//        System.out.println("-");

        // Load or tools stuff
//        Loader.loadNativeLibraries();
//
//        this.foo(mVector, vectors.length, this.size % 2);

        return null;
    }

    private Vector<F2> bruteForceValidSolution(Vector<F2>[] vectors, int rows) {
        // Choose x_i = 0, or x_i = 1 for each vector, then test
        return this.bruteForceValidSolutionImp(vectors, rows, 0, new boolean[vectors.length]);
    }

    private Vector<F2> bruteForceValidSolutionImp(Vector<F2>[] vectors, int rows, int i, boolean[] values) {
        if (i == values.length) {
            Vector<F2> solution = this.getSolutionVector(vectors, rows, values);

            if (this.testSolutionVector(solution, rows)) {
//                System.out.println(Arrays.toString(values));
                return solution;
            }

            return null;
        }

        // 1. First path x_i = 0
        values[i] = false;
        Vector<F2> solution1 = bruteForceValidSolutionImp(vectors, rows, i + 1, values);
        if (solution1 != null)
            return solution1;


        // 2. First path x_i = 1
        values[i] = true;
        Vector<F2> solution2 = bruteForceValidSolutionImp(vectors, rows, i + 1, values);
        if (solution2 != null)
            return solution2;

        return null;
    }

    private Vector<F2> bruteForceSmart(Vector<F2>[] vectors, int rows) {
        for (int i = 1; i <= this.size; i++) {
            counter = 0;
            System.out.println("i: " + i);
            Vector<F2> solution = this.bruteForceSmartImp(vectors, rows, i, -1, 0, new boolean[vectors.length]);

            if (solution != null)
                return solution;

            System.out.println(i + " out of " + vectors.length);
            System.out.println("Expected: " + Main.binomialCoefficient(vectors.length, i));
            System.out.println("Actual:   " + counter);
        }

        return null;
    }

    private long counter = 0;

    private Vector<F2> bruteForceSmartImp(Vector<F2>[] vectors, int rows, int free, int current, int index, boolean[] values) {
        if (index == free) {
            counter++;
            Vector<F2> solution = this.getSolutionVector(vectors, rows, values);

            if (this.testSolutionVector(solution, rows)) {
//                System.out.println(Arrays.toString(values));
                return solution;
            }

            return null;
        }

        int end = vectors.length - index;

        for (int i = current + 1; i <= end; i++) {
            values[index] = true;

            Vector<F2> solution = this.bruteForceSmartImp(vectors, rows, free, i, index + 1, values);
            if (solution != null) return solution;

            values[index] = false;
        }

        return null;
    }

    // Returns a solution to the system with chosen values of x_i for 1 <= i <= vectors.length
    private Vector<F2> getSolutionVector(Vector<F2>[] vectors, int rows, boolean[] values) {
        F2[] array = new F2[rows];

        for (int row = 0; row < rows; row++) {
            F2 val = F2.ZERO;

            for (int i = 0; i < vectors.length; i++) {
                if (!values[i]) continue;

                Vector<F2> vec = vectors[i];

                F2 cu = vec.getEntry(row + 1);
                val = val.add(cu);
            }

            array[row] = val;
        }

        return new Vector<>(array);
    }

    // Return if a solution is valid, meaning amount of 1's is equal to k+1
    private boolean testSolutionVector(Vector<F2> solution, int rows) {
        int counter = 0;

        for (int i = 1; i <= rows; i++) {
            if (solution.getEntry(i) == F2.ONE) {
                counter++;
            }
        }

        return counter == this.size;
    }

    private void printMatrix(Matrix<F2> matrix) {
        int rows = matrix.getRows();
        int columns = matrix.getCols();

        Function<F2, Integer> foo = e -> e.isOne() ? 1 : 0;

        for (int row = 1; row <= rows; row++) {
            StringJoiner joiner = new StringJoiner(" ", "[", "]");
            for (int col = 1; col <= columns; col++) {
                int e = foo.apply(matrix.get(row, col));
                joiner.add(String.valueOf(e));
            }
            System.out.println(joiner);
        }
    }

    private void printSolutionSpace(AffineLinearSubspace<F2> solutionSpace, int rows) {
        Vector<F2> inhomogenousPart = solutionSpace.getInhomogenousPart();
        Vector<F2>[] set = solutionSpace.getGeneratingSystem();

        // Print vectors row wise
        for (int row = 1; row <= rows; row++) {
            System.out.print("[" + this.f2ToIntFunction.apply(inhomogenousPart.getEntry(row)) + "]  |  ");

            for (Vector<F2> v : set) {
                F2 entry = v.getEntry(row);
                System.out.print("[" + this.f2ToIntFunction.apply(entry) + "]  ");
            }
            System.out.println();
        }


        System.out.println("Frees: " + set.length);
    }

    private void printVector(Vector<F2> vector) {
        printMatrix(vector.toMatrix());
    }
}
