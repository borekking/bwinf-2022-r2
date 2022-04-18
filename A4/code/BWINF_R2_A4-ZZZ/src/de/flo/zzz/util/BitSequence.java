package de.flo.zzz.util;

import java.util.Arrays;

public class BitSequence implements Comparable<BitSequence> {

    private final int value;

    private final int size;

    private final boolean[] bits;

    public BitSequence(boolean[] bits) {
        this.size = bits.length;
        this.bits = bits;
        this.value = this.getValueImp();
    }

    // Constructor taking a binary number
    public BitSequence(String binaryNumber) {
        this.size = binaryNumber.length();
        this.bits = BitSequenceUtils.getBooleanArray(binaryNumber);
        this.value = this.getValueImp();
    }

    private void check() {
        if (this.size > 20) {
            throw new RuntimeException("Too big BS!");
        }
    }

    // XOR Operation
    // -> O(size)
    public BitSequence xor(BitSequence that) {
        // Make sure sequences have same length
        if (this.size != that.size) {
            throw new IllegalArgumentException("BitSequences have to have the same amount of bits to use the xor-operation!");
        }

        // Create and fill new boolean-array with XORs
        boolean[] result = new boolean[this.size];

        for (int i = 0; i < this.size; i++) {
            result[i] = this.bits[i] ^ that.bits[i];
        }

        // Return NEW BitSequence
        return new BitSequence(result);
    }

    public BitSequence subSequence(int fromInclusive, int toExclusive) {
        boolean[] subBits = Arrays.copyOfRange(this.bits, fromInclusive, toExclusive);
        return new BitSequence(subBits);
    }

    private int getValueImp() {
        int exp = this.size - 1;
        int value = 0;

        for (boolean b : this.bits) {
            if (b)
                value += Math.pow(2, exp);

            exp--;
        }

        return value;
    }

    @Override
    public String toString() {
        return BitSequenceUtils.getBinaryString(this.bits);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitSequence that = (BitSequence) o;
        return Arrays.equals(this.bits, that.bits);
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public int compareTo(BitSequence that) {
        return Integer.compare(that.value, this.value);

//        if (this.size != that.size) {
//            throw new RuntimeException("Can not compare BitSequences, which have not the same size!");
//        }
//
//        // Compare bit arrays (this.bits, that.bits) using BitSequence#getBit
//        for (int i = 0; i < this.size; i++) {
//            boolean bitThis = this.getBit(i);
//            boolean bitThat = that.getBit(i);
//
//            if (bitThis && !bitThat) return 1; // Current bit in this is true, current bit in that isn't -> this > that
//            if (bitThat && !bitThis) return -1; // Current bit in that is true, current bit in this isn't -> this < that
//        }
//
//        // Arrays are equal
//        return 0;
    }

    public int getValue() {
        return value;
    }

    // Returns bit on given index
    public boolean getBit(int index) {
        return this.bits[index];
    }

    public boolean[] getBits() {
        return this.bits.clone();
    }

    public int getSize() {
        return size;
    }
}
