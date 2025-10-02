import java.io.*;
import java.util.*;

public class Main_clean {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Main_clean <input_file>");
            System.exit(1);
        }
        
        String inputFile = args[0];
        String stopWordsFile = "../stop_words.txt";
        
        try {
            Set<String> stopWords = readStopWords(stopWordsFile);
            Map<String, Integer> wordFreq = processFile(inputFile, stopWords);
            printTopWords(wordFreq, 25);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static Set<String> readStopWords(String filename) throws IOException {
        Set<String> stopWords = new HashSet<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(",");
                for (String word : words) {
                    stopWords.add(word.trim().toLowerCase());
                }
            }
        }
        
        for (char c = 'a'; c <= 'z'; c++) {
            stopWords.add(String.valueOf(c));
        }
        
        return stopWords;
    }
    
    private static Map<String, Integer> processFile(String filename, Set<String> stopWords) throws IOException {
        Map<String, Integer> wordFreq = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();
                String[] words = line.replaceAll("[^a-zA-Z]", " ").split("\\s+");
                
                for (String word : words) {
                    word = word.trim();
                    if (!word.isEmpty() && !stopWords.contains(word) && word.length() >= 2) {
                        Integer count = wordFreq.get(word);
                        if (count == null) {
                            wordFreq.put(word, 1);
                        } else {
                            wordFreq.put(word, count + 1);
                        }
                    }
                }
            }
        }
        
        return wordFreq;
    }
    
    private static void printTopWords(Map<String, Integer> wordFreq, int topN) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordFreq.entrySet());
        sortedEntries.sort((a, b) -> {
            int freqCompare = Integer.compare(b.getValue(), a.getValue());
            if (freqCompare != 0) {
                return freqCompare;
            }
            return a.getKey().compareTo(b.getKey());
        });
        
        for (int i = 0; i < Math.min(topN, sortedEntries.size()); i++) {
            Map.Entry<String, Integer> entry = sortedEntries.get(i);
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
        }
    }
}
