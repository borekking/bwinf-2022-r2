package de.flo.zzz.util;

import java.util.Arrays;

public class SortedIntArray {

    private boolean hasHash;
    private int hash;

    private final int size;

    private final int[] array;

    public SortedIntArray(int e) {
        this.array = new int[] {e};
        this.size = 1;
    }

    public SortedIntArray() {
        this.array = new int[] {};
        this.size = 0;
    }

    // Expects sorted array!
    private SortedIntArray(int[] array) {
        this.array = array;
        this.size = this.array.length;
    }

    // O(log size) + O(size+1) = O(size)
    public SortedIntArray insert(int item) {
        int newSize = this.size + 1;
        int[] newArray = new int[newSize];

        int index = Arrays.binarySearch(this.array, item);
        int insertIndex = index < 0 ? -index - 1 : index + 1;

        if (insertIndex >= 0) System.arraycopy(this.array, 0, newArray, 0, insertIndex);

        newArray[insertIndex] = item;

        if (newSize - (insertIndex + 1) >= 0)
            System.arraycopy(this.array, insertIndex, newArray, insertIndex + 1, newSize - (insertIndex + 1));
//
        return new SortedIntArray(newArray);
    }

    public boolean doubledElements() {
        for (int i = 1; i < this.size; i++) {
            if (this.array[i-1] == this.array[i]) {
                return false;
            }
        }

        return true;
    }

    public boolean contains(int e) {
        int index = Arrays.binarySearch(this.array, e);
        return index >= 0;
    }

    public boolean containsAny(SortedIntArray that) {
        for (int e : that.array) {
            if (this.contains(e)) return true;
        }
        return false;
    }

    public boolean isSorted() {
        for (int i = 0; i < this.size-1; i++) {
            if (this.array[i] > this.array[i+1]) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortedIntArray that = (SortedIntArray) o;
        return Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        if (this.hasHash) return this.hash;

        this.hasHash = true;
        return this.hash = Arrays.hashCode(array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    public int[] getArray() {
        return array;
    }

    public int getSize() {
        return size;
    }
}
