package de.flo.zzz;

import de.flo.zzz.bitSequence.BitSequence;
import de.flo.zzz.util.JavaUtils;

import java.io.File;
import java.io.FileNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Main {

    private static Function<ZZZProblem, int[]> solve =
            prop -> new ZZZSolver(prop).getResult();

    /*
     * Formula:
     *      n! / (k! * (n-k)! )
     *
     *   = (1 * ... * (n-k) * ... * n) / (k! * (1 * ... * (n-k)))
     *   = ((n-k+1) * ... * n) / k!
     */
    public static BigDecimal binomialCoefficient(int n, int k) {
        BigDecimal upper = multiplyRange(n - k + 1, n); // (n-k+1) * ... * n
        BigDecimal down = multiplyRange(1, k); // 1 * ... * k
        return upper.divide(down, 0, RoundingMode.DOWN);
    }

    // Calculate (a * ... * b), where a < b
    // Calling (1, n) is equal to n! = 1 * ... * n
    public static BigDecimal multiplyRange(int a, int b) {
        // !(a <= b) = a > b
        if (a > b) return new BigDecimal(-1);

        BigDecimal result = new BigDecimal(1);
        for (int i = a; i <= b; i++) {
            result = result.multiply(new BigDecimal(i));
        }

        return result;
    }

    /*
     * Note:
     *
     * Zara's card: k + 1
     * Added cards: m - k - 1
     *
     */

    // unsorted 0: [2, 3, 5, 9, 11]
    // unsorted 1: [1, 2, 4, 6, 7, 9, 11, 14, 15]
    // unsorted 5: [70, 77, 163, 167, 185]

    public static void main(String[] args) throws FileNotFoundException {
        new Main();
    }

    private Main() throws FileNotFoundException {
        // "stapel%d.txt";
//        this.runTask("inputs/stapel2.txt");
        this.runAllTasks(this::runTask, 6);
    }

    private void runAllTasks(Consumer<ZZZProblem> consumer, int amount) throws FileNotFoundException {
        String format = "inputs/stapel%d.txt";

        for (int i = 0; i < amount; i++) {
            String fileName = String.format(format, i);
            System.out.println(fileName + ":");

            ZZZProblem task = Main.readFile(fileName);

            consumer.accept(task);

            System.out.println("----------");
        }
    }

    private void runTask(ZZZProblem task) {
        List<BitSequence> bitSequences = task.getBitSequences();

        long millis = System.currentTimeMillis();
        int[] indexes = solve.apply(task);
        millis = System.currentTimeMillis() - millis;

        System.out.println("Bit Sequences:");
        if (indexes.length == 0) System.out.println("/");
        for (int index : indexes) {
            System.out.println(bitSequences.get(index));
        }

        System.out.println();
        System.out.println("millis: " + millis);
        System.out.println("indexes: " + Arrays.toString(indexes));
        System.out.println();
    }

    private void runTask(String fileName) throws FileNotFoundException {
        ZZZProblem task = Main.readFile(fileName);

        this.runTask(task);
    }

    public static ZZZProblem readFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        List<String> lines = JavaUtils.readFile(file);

        String firstLine = lines.get(0);

        String[] splitted = firstLine.split(" ");

        int n = Short.parseShort(splitted[0]);
        int k = Short.parseShort(splitted[1]);
        int m = Short.parseShort(splitted[2]);

        List<BitSequence> bits = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            bits.add(new BitSequence(lines.get(i)));
        }

        return new ZZZProblem(n, k, m, bits);
    }
}
