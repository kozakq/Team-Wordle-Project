/*
 * Course: Software Tools & Process
 * Spring 2025
 * Group
 ** @author childressg
 ** @author OrugPeli
 ** @author Griffithjr
 ** @author kozakq
 * @version 1.0
 */

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WordStorage {
    private final Map<String, Integer> wordCounts;

    /**
     * No-argument constructor loads the word storage from the default resource path.
     */
    public WordStorage() {
        wordCounts = new HashMap<>();
        loadFromFile("/data/word-storage");
    }

    /**
     * Constructor that allows specifying a resource path.
     *
     * @param resourcePath the path of the word storage file in resources.
     */
    public WordStorage(String resourcePath) {
        wordCounts = new HashMap<>();
        loadFromFile(resourcePath);
    }

    /**
     * Loads word counts from a file in the resources using the same style as Dictionary.
     *
     * @param resourcePath the resource file path (e.g., "/data/wordStorage.txt")
     */
    private void loadFromFile(String resourcePath) {
        try (InputStream input = WordStorage.class.getResourceAsStream(resourcePath)) {
            assert input != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String word = parts[0].trim().toLowerCase();
                        int count = Integer.parseInt(parts[1].trim());
                        wordCounts.put(word, count);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load word storage from " + resourcePath, e);
        }
    }

    /**
     * Adds a word to the in-memory storage. If the word already exists,
     * its count is incremented.
     *
     * @param word the word to add
     */
    public void addWord(String word) {
        String lowerWord = word.toLowerCase();
        int count = wordCounts.getOrDefault(lowerWord, 0);
        wordCounts.put(lowerWord, count + 1);
    }

    /**
     * Returns the current word counts.
     *
     * @return a Map of words to their frequency counts
     */
    public Map<String, Integer> getWordCounts() {
        return wordCounts;
    }

    /**
     * Writes the current word frequencies to a file.
     * Note: Writing is done to a local file path (not to a resource stream).
     */
    public void saveToFile() {
        // Writing to a file on disk (e.g., in src/data)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/word-storage"))) {
            for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Word storage saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving word storage: " + e.getMessage());
        }
    }
}
