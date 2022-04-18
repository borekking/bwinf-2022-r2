package de.flo.zzz.test;

import de.flo.zzz.AbstractZZZSolver;
import de.flo.zzz.JavaUtils;
import de.flo.zzz.ZZZSolver11;
import de.flo.zzz.ZZZSolver17;
import de.flo.zzz.ZZZSolver3_3;
import de.flo.zzz.ZZZSolver6;
import de.flo.zzz.util.BitSequence;
import de.flo.zzz.util.BitSequenceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

public class ZZZTest {

    private final static String path = "inputs/";

    private final static String fileFormat = path + "stapel%d.txt";

    private static final Function<Integer, String> getFile = i -> String.format(fileFormat, i);

    private List<Integer> getResult(short n, short k, short m, List<BitSequence> bitSequences) {
        AbstractZZZSolver solver = new ZZZSolver17(n, k, m, bitSequences);
        return solver.getResult();
    }

    @Test
    public void testAll() throws FileNotFoundException {
        // Test all provides inputs
        for (int i = 0; i <= 5; i++) {
            String current = getFile.apply(i);

            this.test(current);
        }
    }

    @Test
    public void test0() throws FileNotFoundException {
        this.test(getFile.apply(0));
    }

    @Test
    public void test1() throws FileNotFoundException {
        this.test(getFile.apply(1));
    }

    @Test
    public void test2() throws FileNotFoundException {
        this.test(getFile.apply(2));
    }

    @Test
    public void test3() throws FileNotFoundException {
        this.test(getFile.apply(3));
    }

    @Test
    public void test4() throws FileNotFoundException {
        this.test(getFile.apply(4));
    }

    @Test
    public void test5() throws FileNotFoundException {
        this.test(getFile.apply(5));
    }

    private void test(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        List<String> lines = JavaUtils.readFile(file);

        String firstLine = lines.get(0);

        String[] splitted = firstLine.split(" ");

        short n = Short.parseShort(splitted[0]);
        short k = Short.parseShort(splitted[1]);
        short m = Short.parseShort(splitted[2]);

        List<BitSequence> bits = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            bits.add(new BitSequence(lines.get(i)));
        }

        List<Integer> indexes = this.getResult(n, k, m, bits);
        Assert.assertNotNull("Result was null!", indexes);

        List<BitSequence> result = indexes.stream().map(bits::get).collect(Collectors.toList());
        BitSequence xor = BitSequenceUtils.xor(result);

        boolean[] boos = new boolean[m];
        Arrays.fill(boos, false);
        BitSequence expected = new BitSequence(boos);

        Assert.assertEquals("Result List did not have expected length!", k+1, result.size());
        Assert.assertEquals("XOR of list wasn't of form 0...0!", expected, xor);
    }
}
