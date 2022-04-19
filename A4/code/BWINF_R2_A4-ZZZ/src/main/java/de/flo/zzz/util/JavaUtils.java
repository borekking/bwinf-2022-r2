package de.flo.zzz.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JavaUtils {

    // Function for reading a file and returning it as List of String
    public static List<String> readFile(File file) throws FileNotFoundException {
        List<String> list = new ArrayList<>();

        Scanner sc = new Scanner(file);
        while (sc.hasNext()) list.add(sc.nextLine());

        return list;
    }

    public static boolean[] toPrimitiveArray(List<Boolean> list) {
        int n = list.size();
        boolean[] bools = new boolean[n];

        for (int i = 0; i < n; i++)
            bools[i] = list.get(i);

        return bools;
    }

    public static <T> List<T> getAsList(T[] arr) {
        return Arrays.stream(arr).collect(Collectors.toList());
    }
}
