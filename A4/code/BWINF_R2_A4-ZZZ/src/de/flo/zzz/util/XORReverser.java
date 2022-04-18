package de.flo.zzz.util;

import java.util.HashMap;
import java.util.Map;

public class XORReverser {

    /*
     * static class for reversing XORs
     *
     */


    private static final boolean[][][]
            reversedXorTrue = new boolean[][][]{{{false}, {true}}, {{true}, {false}}},
            reversedXorFalse = new boolean[][][]{{{true}, {true}}, {{false}, {false}}}
    ;

    private static final Map<String, BitSequence[][]> memo = new HashMap<>();

    // Returns Array of pairs of BitSequences
    // O(n * 2^n) + O(2^n) = O(n * 2^n)
    public static BitSequence[][] reverseXOR(BitSequence xor) {
        String str = xor.toString();
        if (memo.containsKey(str)) return memo.get(str);

        boolean[][][] reversed = reverseXORBoolean(xor);
        int len = reversed.length;

        BitSequence[][] reversedBS = new BitSequence[len][2];

        for (int i = 0; i < len; i++) {
            BitSequence b1 = new BitSequence(reversed[i][0]);
            BitSequence b2 = new BitSequence(reversed[i][1]);

            reversedBS[i] = new BitSequence[] {b1, b2};
        }

        memo.put(str, reversedBS);
        return reversedBS;
    }

    // Create reverse XOR of given BitSequence
    // Returns boolean array sized 2^n * 2 * n, where n = xor.size
    // -> O(n * 2^n)
    public static boolean[][][] reverseXORBoolean(BitSequence xor) {
        if (xor == null || xor.getSize() == 0) {
            throw new RuntimeException("XOR might not be null or empty for reverse-XOR!");
        }

        int n = xor.getSize();

        int size = (int) Math.pow(2, n);
        boolean[][][] reversed = new boolean[size][2][n];

        for (int index = 0; index < n; index++) // n
            reverseXORImp(reversed, n, index, xor.getBit(index)); // O(2^n)

        return reversed;
    }

    // Creating reverse xor combinations (with repetition) by putting the result for each digit into results (TODO Text)
    // -> O(2^n)
    private static void reverseXORImp(boolean[][][] arr, int n, int index, boolean bit) {
        int steps = (int) Math.pow(2, n - (index + 1));
        int amount = (int) Math.pow(2, index + 1);

        boolean[][][] bitReversed = reverseXOR(bit);

        // Note: amount*steps = 2^{n-index-1} * 2^{index+1} = 2^{n-index-1+index+1} = 2^n
        int current = 0;
        for (int i = 0; i < amount; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < steps; j++) {
                    arr[current][0][index] = bitReversed[0][0][0];
                    arr[current][1][index] = bitReversed[0][1][0];

                    current++;
                }
            }  else {
                for (int j = 0; j < steps; j++) {
                    arr[current][0][index] = bitReversed[1][0][0];
                    arr[current][1][index] = bitReversed[1][1][0];

                    current++;
                }
            }
        }
    }

    // Reverse XOR for single bit.
    // Returns a 2*2*1 boolean-array
    private static boolean[][][] reverseXOR(boolean bit) {
        if (bit) return reversedXorTrue;

        return reversedXorFalse;
    }
}
