package de.flo.zzz;

import de.flo.zzz.bitSequence.BitSequence;

import de.flo.zzz.util.JavaUtils;
import java.io.File;
import java.io.FileNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Main {

    public static class ZZZTask {
        public final int n, k, m;
        public final List<BitSequence> bits;

        private ZZZTask(int n, int k, int m, List<BitSequence> bits) {
            this.n = n;
            this.k = k;
            this.m = m;
            this.bits = bits;
        }
    }

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

    public static Set<int[]> getAllCombinations(int n, int k) {
        Set<int[]> set = new HashSet<>();

        int current = -1;
        int index = 0;
        int[] array = new int[k];

        getBioArray(set, array, current, index, n, k);

        return set;
    }

    public static void getBioArray(Set<int[]> set, int[] array, int current, int index, int n, int k) {
        if (k == 0) {
            set.add(Arrays.copyOf(array, array.length));
            return;
        }

        int start = current + 1;
        int end = n - k;
        int nextK = k - 1;
        int nextIndex = index + 1;

        for (int i = start; i <= end; i++) {
            array[index] = i;
            getBioArray(set, array, i, nextIndex, n, nextK);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Main();
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

    Main() throws FileNotFoundException {
//        System.out.println("start");

//        String fileName = "stapel2.txt"; // "stapel%d.txt";
        this.runTask("stapel3.txt");
//        this.runAllTasks(this::runTask, 6);
    }

    private void runAllTasks(Consumer<ZZZTask> consumer, int amount) throws FileNotFoundException {
        String format = "stapel%d.txt";

        for (int i = 0; i < amount; i++) {
            String fileName = String.format(format, i);
            ZZZTask task = Main.readFile(fileName);

            consumer.accept(task);
        }
    }

    private void runTask(ZZZTask task) {
        AbstractZZZSolver solver = new ZZZSolver(task.n, task.k, task.m, task.bits);

        long millis = System.currentTimeMillis();
        List<Integer> indexes = solver.getResult();
        millis = System.currentTimeMillis() - millis;

        System.out.println();
        System.out.println("millis: " + millis);
        System.out.println("indexes: " + indexes);
    }

    private void runTask(String fileName) throws FileNotFoundException {
        ZZZTask task = Main.readFile(fileName);

        this.runTask(task);
    }

    public static ZZZTask readFile(String fileName) throws FileNotFoundException {
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

        return new ZZZTask(n, k, m, bits);
    }
}
