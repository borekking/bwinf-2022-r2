package de.flo.zzz;

import de.flo.zzz.util.BitSequence;
import de.flo.zzz.util.BitSequenceUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractZZZSolver {

    /*
     * n = size of bitSequences
     * m = amount if bits per bitSequence
     * k = amount of added bitSequences
     *
     */
    protected final int n, k, m, size;

    protected final List<BitSequence> bitSequences;

    private final BitSequence zeros;

    public AbstractZZZSolver(int n, int k, int m, List<BitSequence> bitSequences) {
        this.n = n;
        this.k = k;
        this.m = m;
        this.size = this.k + 1;
        this.bitSequences = bitSequences;
        this.zeros = new BitSequence(new boolean[m]);

    }

    protected boolean validResult(int[] indexes) {
        return this.validResult(0, this.m, indexes, this.size);
    }

    protected boolean validResult(int[]... indexes) {
        Set<BitSequence> set = new HashSet<>();

        for (int[] arr : indexes) {
            for (int e : arr)
                set.add(this.bitSequences.get(e));
        }

        if (set.size() != this.size) return false;

        return this.zeros.equals(BitSequenceUtils.xor(new ArrayList<>(set)));
    }

    protected boolean validResult(int from, int to, int[] indexes, int expectedSize) {
        int bits = to - from;

        Set<BitSequence> set = new HashSet<>();
        for (int index : indexes) {
            set.add(this.bitSequences.get(index));
        }

        if (set.size() != expectedSize) return false;

        BitSequence zeros = new BitSequence(new boolean[bits]);
        return zeros.equals(BitSequenceUtils.xor(new ArrayList<>(set)));
    }

    // No size ->
    protected boolean validResult(int from, int to, int[] indexes) {
        int bits = to - from;

        Set<BitSequence> set = new HashSet<>();
        for (int index : indexes) {
            set.add(this.bitSequences.get(index));
        }

//        if (set.size() != expectedSize) return false;

        BitSequence zeros = new BitSequence(new boolean[bits]);
        return zeros.equals(BitSequenceUtils.xor(new ArrayList<>(set)));
    }

    public abstract List<Integer> getResult();
}
