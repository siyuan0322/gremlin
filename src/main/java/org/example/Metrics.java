package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Metrics {
    public static void main(String[] args) {
        long[] array = {347, 28, 28, 29, 29, 28, 29, 28, 29, 28};

        printStatistics(array);
    }

    public static void printStatistics(long[] array) {
        System.out.println(Arrays.toString(array));
        List<String> metrics = new ArrayList<>();
        long max = Arrays.stream(array).max().orElse(0);
        long min = Arrays.stream(array).min().orElse(0);
        double average = Arrays.stream(array).average().orElse(0);

        Arrays.sort(array);
        double median;
        int length = array.length;

        if (length % 2 == 0) {
            median = (array[length / 2 - 1] + array[length / 2]) / 2.0;
        } else {
            median = array[length / 2];
        }
        metrics.add(String.valueOf(max));
        metrics.add(String.valueOf(min));
        metrics.add(String.valueOf(average));
        metrics.add(String.valueOf(median));

        // System.out.println("| Max | Min | Average | Median|");
        // System.out.println("|-----|-----|---------|-------|");
        String metricsStr = String.join(" | ", metrics);
        System.out.println("| " + metricsStr + " | ");
    }

}
