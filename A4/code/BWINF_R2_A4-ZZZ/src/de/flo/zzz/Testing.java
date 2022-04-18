package de.flo.zzz;

import de.flo.zzz.util.BitSequence;
import de.flo.zzz.util.BitSequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Testing {

    /*
     * 1. Kommutativ Gesetzt
     * 2. Assoiziativ Gesetzt
     *
     * 3. Bildet man das XOR von einer Zahl s mit ihr selbst, erhält man 0...0
     *  -> Die gesuchten k+1 (11) Ziffern ergeben 0...0, verbindet man sie mit dem xor.
     *     -> Da Man aus den k Zahlen das Xor bekommt, und, zusätzlich das xor hat -> xor ^ xor = 0...0
     *
     * 4. Zwei Teillisten der k+1 Zahlen haben immer das gleiche Ergebniss
     * 5. Mehrere XORs mehrerer Teilmengen ergeben zusammen stets 0...0
     *
     * Task:
     * ->-> Teilmenge mit k+1 Elementen finden, die auf 0..0 kommt.
     *   -> Divide and Conquer (?):
     * 11 = 5 + 6
     * 5 = 3 + 2, 6 = 3 + 3
     * 3 = 2 + 1, ...
     *   -> Erst ein paar bits?
     *
     *
     * Imagine searching 4: (3+1)
     * 4 = 2+2
     *
     *
     */

    private static BitSequence[] bitSequences = new BitSequence[] {
            // List
            new BitSequence("1010"),
            new BitSequence("1001"),
            //                          =0011
            new BitSequence("1111"),
            //                          =1100
            new BitSequence("1001"),
            //                          =0101
            new BitSequence("0100"),
            //                          =0001
            // Xor
            new BitSequence("0001")
    };

    public static void main(String[] args) {
        int a = 23;
        int b = 10;
        int c = a^0;
        System.out.println(c);

////        List<BitSequence> list = JavaUtils.getAsList(bitSequences);
//        List<BitSequence> list = randomBinaries();
//
//        list.forEach(bs -> System.out.println("- " + bs.toString()));
//        printXor(list);
//
//        BitSequence xor = BitSequenceUtils.xor(list);
//
//        System.out.println();
//
//        list.add(xor);
//
//        System.out.println("With xor:");
//        printXor(list);
//
//        System.out.println();
//
//        System.out.println("Sublist 1: ");
//        printXor(list.subList(0, 4));
//
//        System.out.println();
//
//        System.out.println("Sublist 2: ");
//        printXor(list.subList(4, 8));
//
//        System.out.println();
//
//        System.out.println("Sublist 3: ");
//        printXor(list.subList(8, 12));
//
//        System.out.println(list.size());
    }

    private static void printXor(List<BitSequence> l) {
        BitSequence xor = BitSequenceUtils.xor(l);
        System.out.println("xor=" + xor);
    }

    private static List<BitSequence> randomBinaries() {
        int amount = 11, size = 4;
        Random random = new Random();
        List<BitSequence> l = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            int[] boos = new int[size];

            for (int j = 0; j < size; j++) {
                int r = random.nextInt(2);

                boos[j] = r;
            }

            String binary = Arrays.toString(boos).replaceAll(", ", "").replaceAll("\\[", "").replaceAll("]", "");
            l.add(new BitSequence(binary));
        }

        return l;
    }
}
