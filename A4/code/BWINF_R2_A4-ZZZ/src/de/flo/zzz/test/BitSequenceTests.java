package de.flo.zzz.test;

import de.flo.zzz.util.BitSequence;
import de.flo.zzz.util.BitSequenceUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class BitSequenceTests {

    @Test
    public void testConstructor() {
        boolean[] bits = new boolean[] {true, true, false, false, true, false, true, false, false};
        String str = "110010100";
        Assert.assertEquals("Constructors do not mathc!", new BitSequence(bits), new BitSequence(str));
    }

    @Test
    public void testEquals() {
        BitSequence b0_0 = new BitSequence("0001101001100");
        BitSequence b0_1 = new BitSequence("0001101001100");
        Assert.assertEquals("Equals went wrong!", b0_0, b0_1);
    }

    @Test
    public void testComparing() {
        // Case 1
        BitSequence b0_0 = new BitSequence("000111101011");
        BitSequence b0_1 = new BitSequence("000101101011");
        Assert.assertEquals("Comparing BS went wrong", 1, b0_0.compareTo(b0_1));

        // Case 0
        BitSequence b1_0 = new BitSequence("0001101001100");
        BitSequence b1_1 = new BitSequence("0001101001100");
        Assert.assertEquals("Comparing BS went wrong", 0, b1_0.compareTo(b1_1));
        Assert.assertTrue("compare is 0 but equals is false", b1_0.compareTo(b1_1) == 0 && b1_0.equals(b1_1));

        // Case -1
        BitSequence b2_0 = new BitSequence("00100100100001");
        BitSequence b2_1 = new BitSequence("00111001100011");
        Assert.assertEquals("Comparing BS went wrong", -1, b2_0.compareTo(b2_1));
    }

    @Test
    public void testComparingList() {
        List<BitSequence> list = new ArrayList<>(Arrays.asList(
                new BitSequence("1001000101101010"),
                new BitSequence("0010110011100101"),
                new BitSequence("1100100111001010"),
                new BitSequence("0000000000000100"),
                new BitSequence("1111111111111111")
        ));

        List<BitSequence> expected = new ArrayList<>(Arrays.asList(
                new BitSequence("0000000000000100"),
                new BitSequence("0010110011100101"),
                new BitSequence("1001000101101010"),
                new BitSequence("1100100111001010"),
                new BitSequence("1111111111111111")
        ));

        BitSequenceUtils.sortBitSequences(list);
        Assert.assertEquals("Sorting BS-List went wrong!", expected, list);
    }
}
