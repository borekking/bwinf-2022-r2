package de.flo.zzz.bitSequence;

import de.flo.zzz.util.JavaUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BitSequenceUtils {

    // XOR on list of BitSequences
    // -> (amount * bitAmount)
    public static BitSequence xor(List<BitSequence> sequences) {
        int amount = sequences.size();

        // Special cases: 0, 1
        if (amount == 0) return null;
        if (amount == 1) return sequences.get(0);

        // Bit Amount of first list
        int bitAmount = sequences.get(0).getSize();

        // Make sure all BitSequences have the same size
        // -> O(amount-1)
        for (int i = 1; i < amount; i++) {
            int currentBitAmount = sequences.get(i).getSize();

            if (currentBitAmount != bitAmount) {
                throw new RuntimeException("BitSequence do not have the same length!");
            }
        }

        // O((amount-1) * bitAmount)
        BitSequence xor = sequences.get(0).xor(sequences.get(1));

        for (int i = 2; i < amount; i++) {
            BitSequence current = sequences.get(i);
            xor = xor.xor(current); // <- O(bitAmount);
        }

        return xor;
    }

    public static void sortBitSequences(List<BitSequence> list) {
        list.sort(BitSequence::compareTo);
    }

    public static boolean isBinary(String str) {
        return str.matches("[0|1]*");
    }

    // Binary-string to boolean-array
    public static boolean[] getBooleanArray(String binaryNumber) {
        if(!isBinary(binaryNumber)) {
            throw new RuntimeException("\"" + binaryNumber + "\" is not a binary number!");
        }

        List<Boolean> bitList = Arrays.stream(binaryNumber.split("")).map(s -> s.equals("1")).collect(Collectors.toList());
        return JavaUtils.toPrimitiveArray(bitList);
    }

    // Boolean-array to binary-string
    public static String getBinaryString(boolean[] bits) {
        char[] chars = new char[bits.length];

        for (int i = 0; i < bits.length; i++) {
            chars[i] = bits[i] ? '1' : '0';
        }

        return new String(chars);
    }

    // Returns array of BS containing all possible BS with given size.
    // Runtime: O(2^size * size)
    public static BitSequence[] getAllCombinations(int size) {
        int arraySize = (int) Math.pow(2, size);
        boolean[][] BSBits = new boolean[arraySize][size];

        for (int index = 0; index < size; index++) // size
            reverseXORImp(BSBits, size, index); // O(2^n)

        BitSequence[] result = new BitSequence[arraySize];
        for (int i = 0; i < arraySize; i++)
            result[i] = new BitSequence(BSBits[i]);

        return result;
    }

    // Creating reverse xor combinations (with repetition) by putting the result for each digit into results (TODO Text)
    // -> O(2^n)
    private static void reverseXORImp(boolean[][] arr, int n, int index) {
        int steps = (int) Math.pow(2, n - (index + 1));
        int amount = (int) Math.pow(2, index + 1);

        // Note: amount*steps = 2^{n-index-1} * 2^{index+1} = 2^{n-index-1+index+1} = 2^n
        int current = 0;
        for (int i = 0; i < amount; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < steps; j++) {
                    arr[current][index] = true;

                    current++;
                }
            } else {
                for (int j = 0; j < steps; j++) {
                    arr[current][index] = false;

                    current++;
                }
            }
        }
    }
}
