
package com.atziluth.sephirah.chesed.sorting;

import com.atziluth.sephirah.chesed.model.Umamusume;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates various sorting algorithms with Umamusume data.
 * Each method shows time/space complexity.
 */
public class UmamusumeSorter {
    private static final Logger logger = LoggerFactory.getLogger(UmamusumeSorter.class);
    
    /**
     * Bubble Sort - O(n²) time, O(1) space
     * Simple but inefficient. Good for small datasets.
     */
    public List<Umamusume> bubbleSort(List<Umamusume> list, Comparator<Umamusume> comparator) {
        logger.info("🌀 Bubble Sort - O(n²) comparisons");
        List<Umamusume> sorted = new ArrayList<>(list);
        int n = sorted.size();
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(sorted.get(j), sorted.get(j + 1)) > 0) {
                    // Swap
                    Collections.swap(sorted, j, j + 1);
                }
            }
        }
        return sorted;
    }
    
    /**
     * Selection Sort - O(n²) time, O(1) space
     * Finds minimum element each iteration.
     */
    public List<Umamusume> selectionSort(List<Umamusume> list, Comparator<Umamusume> comparator) {
        logger.info("🎯 Selection Sort - O(n²) comparisons");
        List<Umamusume> sorted = new ArrayList<>(list);
        int n = sorted.size();
        
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(sorted.get(j), sorted.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                Collections.swap(sorted, i, minIdx);
            }
        }
        return sorted;
    }
    
    /**
     * Insertion Sort - O(n²) time, O(1) space
     * Efficient for small or nearly sorted lists.
     */
    public List<Umamusume> insertionSort(List<Umamusume> list, Comparator<Umamusume> comparator) {
        logger.info("📝 Insertion Sort - O(n²), good for small n");
        List<Umamusume> sorted = new ArrayList<>(list);
        
        for (int i = 1; i < sorted.size(); i++) {
            Umamusume key = sorted.get(i);
            int j = i - 1;
            
            while (j >= 0 && comparator.compare(sorted.get(j), key) > 0) {
                sorted.set(j + 1, sorted.get(j));
                j--;
            }
            sorted.set(j + 1, key);
        }
        return sorted;
    }
    
    /**
     * Merge Sort - O(n log n) time, O(n) space
     * Divide and conquer algorithm. Stable sort.
     */
    public List<Umamusume> mergeSort(List<Umamusume> list, Comparator<Umamusume> comparator) {
        logger.info("🔄 Merge Sort - O(n log n), stable");
        if (list.size() <= 1) return new ArrayList<>(list);
        
        int mid = list.size() / 2;
        List<Umamusume> left = mergeSort(list.subList(0, mid), comparator);
        List<Umamusume> right = mergeSort(list.subList(mid, list.size()), comparator);
        
        return merge(left, right, comparator);
    }
    
    private List<Umamusume> merge(List<Umamusume> left, List<Umamusume> right, 
                                 Comparator<Umamusume> comparator) {
        List<Umamusume> merged = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }
        
        while (i < left.size()) merged.add(left.get(i++));
        while (j < right.size()) merged.add(right.get(j++));
        
        return merged;
    }
    
    /**
     * Quick Sort - O(n log n) average, O(n²) worst case
     * In-place, uses pivot element.
     */
    public List<Umamusume> quickSort(List<Umamusume> list, Comparator<Umamusume> comparator) {
        logger.info("⚡ Quick Sort - O(n log n) average");
        List<Umamusume> sorted = new ArrayList<>(list);
        quickSortRecursive(sorted, 0, sorted.size() - 1, comparator);
        return sorted;
    }
    
    private void quickSortRecursive(List<Umamusume> list, int low, int high,
                                   Comparator<Umamusume> comparator) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, comparator);
            quickSortRecursive(list, low, pivotIndex - 1, comparator);
            quickSortRecursive(list, pivotIndex + 1, high, comparator);
        }
    }
    
    private int partition(List<Umamusume> list, int low, int high,
                         Comparator<Umamusume> comparator) {
        Umamusume pivot = list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
    
    /**
     * Counting Sort for Rarity - O(n + k) where k = number of rarities
     * Efficient when range is limited.
     */
    public List<Umamusume> countingSortByRarity(List<Umamusume> list) {
        logger.info("🔢 Counting Sort - O(n + k), k = 5 rarities");
        
        // Count occurrences of each rarity
        int[] count = new int[6]; // Index 0 unused, 1-5 for rarities
        
        for (Umamusume uma : list) {
            count[uma.getRarity().getValue()]++;
        }
        
        // Calculate positions
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }
        
        // Build output array
        Umamusume[] output = new Umamusume[list.size()];
        for (int i = list.size() - 1; i >= 0; i--) {
            Umamusume uma = list.get(i);
            int pos = --count[uma.getRarity().getValue()];
            output[pos] = uma;
        }
        
        return Arrays.asList(output);
    }
    
    /**
     * Benchmark all sorting algorithms.
     */
    public void benchmarkAll(List<Umamusume> data) {
        logger.info("\n📊 SORTING ALGORITHM BENCHMARK");
        logger.info("Dataset size: " + data.size());
        logger.info("-".repeat(50));
        
        Comparator<Umamusume> comparator = Comparator.comparingInt(Umamusume::getTotalStats);
        
        Map<String, Runnable> algorithms = new LinkedHashMap<>();
        algorithms.put("Java Collections.sort()", () -> {
            List<Umamusume> copy = new ArrayList<>(data);
            copy.sort(comparator);
        });
        
        algorithms.put("Bubble Sort", () -> bubbleSort(data, comparator));
        algorithms.put("Selection Sort", () -> selectionSort(data, comparator));
        algorithms.put("Insertion Sort", () -> insertionSort(data, comparator));
        algorithms.put("Merge Sort", () -> mergeSort(data, comparator));
        algorithms.put("Quick Sort", () -> quickSort(data, comparator));
        algorithms.put("Counting Sort (Rarity)", () -> countingSortByRarity(data));
        
        for (Map.Entry<String, Runnable> entry : algorithms.entrySet()) {
            long start = System.nanoTime();
            entry.getValue().run();
            long end = System.nanoTime();
            
            double ms = (end - start) / 1_000_000.0;
            logger.info(String.format("%-25s: %8.3f ms", entry.getKey(), ms));
        }
    }
    
    /**
     * Comparators for different sorting criteria.
     */
    public static class Comparators {
        public static Comparator<Umamusume> byRarity() {
            return Comparator.comparingInt(u -> u.getRarity().getValue());
        }
        
        public static Comparator<Umamusume> byTotalStats() {
            return Comparator.comparingInt(Umamusume::getTotalStats);
        }
        
        public static Comparator<Umamusume> bySpeed() {
            return Comparator.comparingInt(u -> u.getStats().getSpeed());
        }
        
        public static Comparator<Umamusume> byName() {
            return Comparator.comparing(Umamusume::getName);
        }
    }
    
    // Private constructor for utility class
    public UmamusumeSorter() {
        // Utility class with static methods
    }
}
