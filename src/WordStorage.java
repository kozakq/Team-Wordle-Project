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

    public WordStorage() {
        wordCounts = new HashMap<>();
        loadFromFile("/data/word-storage");
    }


    public WordStorage(String resourcePath) {
        wordCounts = new HashMap<>();
        loadFromFile(resourcePath);
    }


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

    public void addWord(String word) {
        String lowerWord = word.toLowerCase();
        int count = wordCounts.getOrDefault(lowerWord, 0);
        wordCounts.put(lowerWord, count + 1);
    }


    public Map<String, Integer> getWordCounts() {
        return wordCounts;
    }


    public void saveToFile() {
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
