package de.flo.zzz.questioned;

import java.util.*;

import de.flo.zzz.Main;
import de.flo.zzz.util.HammingUtils;
import de.flo.zzz.util.JLinAlgSeri;
import de.flo.zzz.util.JLinAlgUtils;

import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.f2.F2;

/**
 * Class to find the valid solution from the solution set
 */
public class FinalSolutionFinder {

    public static void main(String[] args) {
        new FinalSolutionFinder(0, 0, 0, new Vector[0]);
    }

    /**
     * Amount of rows for every vector
     */
    private final int rows;

    /**
     * Amount of free variables to enable
     */
    private final int amount;

    /**
     * Amount of total free variables
     */
    private final int frees;

    /**
     * Solution set (homogenous)
     */
    private final Vector<F2>[] space;

    /**
     * The valid solution as boolean array
     */
    private final boolean[] values;

    /**
     * Constructor
     *
     * @param rows    Amount of rows for all vectors
     * @param amount  Amount of free variables to enable
     * @param frees   Amount of total free variables
     * @param vectors Solution set (homogenous)
     */
    public FinalSolutionFinder(int rows, int amount, int frees, Vector<F2>[] vectors) {
        this.rows = rows;
        this.amount = amount;
        this.frees = frees;
        this.space = vectors;

        /*
         * Amount of rows - amount of vectors/free variables
         * -> Amount to not ignore
         *
         * amount of vectors/free variables
         * -> Amount to ignore
         *
         * Summary:
         *   By ignoring the free variables, you can reduce the rows to look for.
         *   (3=128, 4=128, 5=64 <- Not random!).
         *      -> Looks like binary op. -> log_2 ??? -> Divide and Conquer?
         *
         * Now the task is:
         *   Choose l vectors, s.t. the non-"free" rows (free variables' rows)
         *   create hamming weight of k+1-l.
         *   -> Total Hamming Weight: k+1-l+l = k+1.
         *
         * Task:
         *   Let L be a vector space w/o the "free" rows (any vector space).
         *   In L find a vector v created by m vectors where
         *   d(v) = k+1-m, 0 < m <= k+1
         *
         * Binary - Divide and Conquer?
         *    (Expecting 1 solution)
         *    for m in {0, ... , k+1}
         *       Let q = k+1-m
         *       Try to find result vector v, where d(v) = q, created by m vectors
         *       On (non-"free") rows:
         *          Divide in half trying all combinations off q ( 1+(q-1), 2+(q-2), ... , (q-1)+1)
         *          (full range because of both side, trying all)
         *          Check solutions: Doubled Indexes, ?
         *          ->-> Finding all combinations creating v | d(v) = q in given range of rows
         */


        System.out.println("total: " + rows);
        System.out.println("Ignore: " + frees);
        System.out.println("Not ignore: " + (rows - frees));

        // Space w/o rows of free variables
        Vector<F2>[] newSpace = this.removeFreeRows(this.space, this.rows);
        Matrix<F2> newMatrix = JLinAlgUtils.createMatrix(newSpace, this.rows - this.frees);
        JLinAlgSeri.printMatrix(newMatrix.gaussjord());

        int newRows = this.rows - this.frees;
        System.out.println(newRows);

        this.values = null; // this.getEnabledImp(this.a);
    }

    private Vector<F2>[] removeFreeRows(Vector<F2>[] space, int rows) {
        Vector<F2>[] array = new Vector[space.length];

        // Get free rows
        Set<Integer> freeRows = new HashSet<>();
        for (int i = 1; i <= rows; i++) {
            boolean[] row = this.getRow(space, i, space.length);
            if (HammingUtils.getHammingWeight(row) != 1) continue;
            freeRows.add(i);
        }

        for (int i = 0; i < space.length; i++) {
            Vector<F2> vec = space[i];
            F2[] arr = new F2[this.rows - this.frees];

            int cu = 0;
            for (int j = 1; j <= rows; j++) {
                if (freeRows.contains(j)) continue;
                arr[cu++] = vec.getEntry(j);
            }

            array[i] = new Vector<>(arr);
        }

        return array;
    }

    /*
     * Recursively get indexes creating v | d(v) = q, created by n vectors
     * Options:
     *    1. Without n
     *       Always use k+1
     *    2. With n
     *       Spilt n too (?)
     */
    private List<boolean[]> foo(Vector<F2>[] space, int q, int n, int rowStart, int rowEnd) {
        // Base case 1 row left:
        if (rowEnd - rowStart == 1) {
            /*
             * let m = len(space)
             * Task:
             *    Given: E = {e_1, ... , e_m} where e_i (1 <= i <) m) \in Z_2
             *    Needed: U \subset E, where |U| = n and \sum_U e = q (in Z_2!)
             *            (all possibilities? <- think so <- DP? <- No, Same row never twice)
             *    -> Only possible when q \in Z_2
             */

            if (q != 0 && q != 1) {
                return new ArrayList<>();
            }

            boolean[] row = this.getRow(space, rowStart + 1, this.frees);
            int c = 0;
            for (int i = 0; i < this.frees; i++) {
                if (row[i]) c++;
            }
            System.out.println(c + " -> " + Main.binomialCoefficient(c, n));
            return null;
        }

        /*
         * given a=rowStart, b=rowEnd
         * Splitting:
         *    dist = b - a
         *    half = dist / 2
         *    1. half: a to a+half
         *    2. half: b-half to b
         *
         * e.g.:
         * 16 - 32
         * dist = 32 - 16 = 16
         * half = 8
         * 1. half to 16+8 = 24
         * 2. half 32-8 = 24 to 32
         *
         */

        // Dividing
        int dist = rowEnd - rowStart;
        int half = dist / 2; // Should be clean div.
        int splitA1 = rowStart, splitA2 = rowStart + half;
        int splitB1 = rowEnd - half, splitB2 = rowEnd;

        // Call recursively:
        // Trying all combinations off q ( 1+(q-1), 2+(q-2), ... , (q-1)+1)
        for (int i = 0; i <= q; i++) {
            List<boolean[]> top = this.foo(space, i, n, splitA1, splitA2);
            List<boolean[]> bottom = this.foo(space, q - i, n, splitB1, splitB2);
        }

        return null;

    }

    private boolean[] getRow(Vector<F2>[] space, int row, int columns) {
        boolean[] array = new boolean[columns];
        for (int i = 0; i < columns; i++) {
            array[i] = JLinAlgUtils.f2ToBoolFunction.apply(space[i].getEntry(row));
        }
        return array;
    }

    /**
     * Methode to get the valid solution as boolean array
     *
     * @return The valid solution as boolean array
     */
    public boolean[] getEnabled() {
        return this.values;
    }
}
