package de.flo.zzz.util;

import org.jlinalg.Vector;
import org.jlinalg.f2.F2;

public class HammingUtils {

    public static int getHammingWeight(Vector<F2> vector, int rows) {
        int counter = 0;
        for (int i = 1; i <= rows; i++)
            if (vector.getEntry(i) == F2.ONE)
                counter++;
        return counter;
    }

    public static int getHammingWeight(boolean[] arr) {
        int counter = 0;
        for (boolean b : arr)
            if (b)
                counter++;
        return counter;
    }
}
