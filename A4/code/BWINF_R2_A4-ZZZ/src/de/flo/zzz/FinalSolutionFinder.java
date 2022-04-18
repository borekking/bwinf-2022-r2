package de.flo.zzz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final Vector<F2>[] vectors;

    /**
     * The valid solution as boolean array
     */
    private final boolean[] values;

    /**
     * Map from free variable's index to amount of 1's in its vector
     */
    private final Map<Integer, Integer> onesAmount;

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
        this.vectors = vectors;

        // Fill onesAmount
        this.onesAmount = new HashMap<>();
        this.fillOnesAmount();


        /*
         * TODO implement getting all combinations of frees and non frees creating amount chosen
         * Ex.:
         *  amount = 11
         * (- free = 11, non-free = 0) <- not possible (ig)
         * - free = 10, non-free = 1
         * - ...
         * - free = 1, non free = 10
         * (- free = 0, non free = 11) <- not possible!
         *
         *
         */
        this.values = null; // this.getEnabledImp(this.a);
    }

    /**
     * Methode to fill up oneAmount Map with amount of 1's for each vector
     */
    private void fillOnesAmount() {
        for (int i = 0; i < this.frees; i++) {
            Vector<F2> vector = this.vectors[i];
            int counter = 0;

            // Count amount of 1's in the vector // TODO do HashMap
            for (int j = 0; j < this.rows; j++) {
                F2 e = vector.getEntry(j);

                if (e == F2.ONE) counter++;
            }

            this.onesAmount.put(i, counter);
        }
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
