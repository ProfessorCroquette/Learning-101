package com.atziluth.core;

/**
 * Base interface for all Sephirah modules.
 */
public interface Sephirah {
    String getName();
    String getConcept();
    String getDescription();
    void initialize();
    void demonstrate();
    void shutdown();
    
    // Helper class for complexity analysis
    class Complexity {
        private final String time;
        private final String space;
        
        public Complexity(String time, String space) {
            this.time = time;
            this.space = space;
        }
        
        @Override
        public String toString() {
            return String.format("Time: %s | Space: %s", time, space);
        }
    }
}