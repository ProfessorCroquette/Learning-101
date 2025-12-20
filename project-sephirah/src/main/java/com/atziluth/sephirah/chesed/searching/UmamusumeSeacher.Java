package com.atziluth.sephirah.chesed.searching;

import com.atziluth.sephirah.chesed.model.Umamusume;
import java.util.*;

/**
 * Demonstrates various search algorithms.
 */
public class UmamusumeSearcher {
    
    /**
     * Linear Search - O(n) time
     * Works on unsorted data.
     */
    public Umamusume linearSearch(List<Umamusume> list, String name) {
        System.out.println("🔍 Linear Search - O(n), works on unsorted data");
        for (Umamusume uma : list) {
            if (uma.getName().equalsIgnoreCase(name)) {
                return uma;
            }
        }
        return null;
    }
    
    /**
     * Binary Search - O(log n) time
     * Requires sorted data.
     */
    public Umamusume binarySearch(List<Umamusume> sortedList, String name) {
        System.out.println("🎯 Binary Search - O(log n), requires sorted data");
        int left = 0;
        int right = sortedList.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Umamusume midUma = sortedList.get(mid);
            int comparison = midUma.getName().compareToIgnoreCase(name);
            
            if (comparison == 0) {
                return midUma;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    
    /**
     * Jump Search - O(√n) time
     * Better than linear for large sorted arrays.
     */
    public Umamusume jumpSearch(List<Umamusume> sortedList, String name) {
        System.out.println("🐸 Jump Search - O(√n), better than linear for large n");
        int n = sortedList.size();
        int step = (int) Math.sqrt(n);
        int prev = 0;
        
        // Find block where element is present
        while (sortedList.get(Math.min(step, n) - 1).getName()
               .compareToIgnoreCase(name) < 0) {
            prev = step;
            step += (int) Math.sqrt(n);
            if (prev >= n) return null;
        }
        
        // Linear search within block
        while (prev < Math.min(step, n)) {
            if (sortedList.get(prev).getName().equalsIgnoreCase(name)) {
                return sortedList.get(prev);
            }
            prev++;
        }
        return null;
    }
    
    /**
     * Interpolation Search - O(log log n) average
     * For uniformly distributed sorted data.
     */
    public Umamusume interpolationSearch(List<Umamusume> sortedList, String name) {
        System.out.println("📈 Interpolation Search - O(log log n) average");
        int low = 0;
        int high = sortedList.size() - 1;
        
        while (low <= high && 
               name.compareTo(sortedList.get(low).getName()) >= 0 &&
               name.compareTo(sortedList.get(high).getName()) <= 0) {
            
            // Estimate position
            int pos = low + ((name.charAt(0) - sortedList.get(low).getName().charAt(0)) * 
                    (high - low)) / 
                    (sortedList.get(high).getName().charAt(0) - 
                     sortedList.get(low).getName().charAt(0));
            
            Umamusume posUma = sortedList.get(pos);
            int comparison = posUma.getName().compareToIgnoreCase(name);
            
            if (comparison == 0) {
                return posUma;
            } else if (comparison < 0) {
                low = pos + 1;
            } else {
                high = pos - 1;
            }
        }
        return null;
    }
    
    /**
     * Hash Map Search - O(1) average
     * Using HashMap for instant lookup.
     */
    public Umamusume hashSearch(Map<String, Umamusume> map, String name) {
        System.out.println("🗺️  Hash Map Search - O(1) average");
        return map.get(name.toLowerCase());
    }
    
    /**
     * Build search index for faster lookups.
     */
    public Map<String, Umamusume> buildSearchIndex(List<Umamusume> list) {
        Map<String, Umamusume> index = new HashMap<>();
        for (Umamusume uma : list) {
            index.put(uma.getName().toLowerCase(), uma);
        }
        return index;
    }
    
    /**
     * Benchmark all search algorithms.
     */
    public void benchmarkAll(List<Umamusume> data, String searchName) {
        System.out.println("\n🔎 SEARCH ALGORITHM BENCHMARK");
        System.out.println("Searching for: " + searchName);
        System.out.println("Dataset size: " + data.size());
        System.out.println("-".repeat(50));
        
        // Prepare data structures
        List<Umamusume> sortedByName = new ArrayList<>(data);
        sortedByName.sort(Comparator.comparing(Umamusume::getName));
        Map<String, Umamusume> hashIndex = buildSearchIndex(data);
        
        Map<String, Runnable> algorithms = new LinkedHashMap<>();
        algorithms.put("Linear Search", () -> linearSearch(data, searchName));
        algorithms.put("Binary Search", () -> binarySearch(sortedByName, searchName));
        algorithms.put("Jump Search", () -> jumpSearch(sortedByName, searchName));
        algorithms.put("Interpolation Search", () -> interpolationSearch(sortedByName, searchName));
        algorithms.put("Hash Map Search", () -> hashSearch(hashIndex, searchName));
        
        for (Map.Entry<String, Runnable> entry : algorithms.entrySet()) {
            long start = System.nanoTime();
            Umamusume result = null;
            try {
                // We need to capture result for some algorithms
                if (entry.getKey().equals("Hash Map Search")) {
                    result = hashSearch(hashIndex, searchName);
                } else if (entry.getKey().equals("Linear Search")) {
                    result = linearSearch(data, searchName);
                } else {
                    entry.getValue().run();
                }
            } catch (Exception e) {
                // Continue benchmarking
            }
            long end = System.nanoTime();
            
            double ms = (end - start) / 1_000_000.0;
            System.out.printf("%-25s: %8.3f ms\n", entry.getKey(), ms);
        }
    }
}