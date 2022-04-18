package de.flo.zzz.test;

import de.flo.zzz.util.BitSequence;
import de.flo.zzz.util.BitSequenceUtils;
import de.flo.zzz.util.XORReverser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class XORTests {

    @Test
    public void testReverseXOR() {
        // - Test all results actually creating given xor.
        BitSequence bitSequence = new BitSequence("101010001");
        BitSequence[][] xors = XORReverser.reverseXOR(bitSequence);

        for (BitSequence[] pair : xors) {
            BitSequence b1 = pair[0];
            BitSequence b2 = pair[1];

            Assert.assertEquals("XOR did not equal expected!", bitSequence, b1.xor(b2));
        }
    }

    @Test
    public void testXOR() {
        // Test b^b = 0...0
        BitSequence b0_0 = new BitSequence("00110101");
        BitSequence expected0 = new BitSequence("00000000");
        this.testXor(b0_0, b0_0, expected0);

        // Test normal
        BitSequence b1_0 = new BitSequence("110110111000");
        BitSequence b1_1 = new BitSequence("110010100010");
        BitSequence expected1 = new BitSequence("000100011010");
        /*
        110110111000
      ^ 110010100010
      = 000100011010
         */
        this.testXor(b1_0, b1_1, expected1);

        // Test List
        List<BitSequence> l2 = new ArrayList<>(Arrays.asList(
                new BitSequence("010001100"),
                new BitSequence("110100101"),
                new BitSequence("001001010"),
                new BitSequence("100101011"),
                new BitSequence("110110100")
        ));
        /*
        010001100
      ^ 110100101
      ^ 001001010
      ^ 100101011
      ^ 110110100
      = 111111100
         */
        BitSequence expected2 = new BitSequence("111111100");
        Assert.assertEquals("List XOR went wrong!", expected2, BitSequenceUtils.xor(l2));

        // Test commutativity
        BitSequence b3_0 = new BitSequence("110010101");
        BitSequence b3_1 = new BitSequence("101000110");
        Assert.assertEquals("Commutativity failed!", b3_1.xor(b3_0), b3_0.xor(b3_1));
    }

    private void testXor(BitSequence b1, BitSequence b2, BitSequence expected) {
        BitSequence actual = b1.xor(b2);

        Assert.assertEquals("XOR Test went wrong!", expected, actual);
    }
}
