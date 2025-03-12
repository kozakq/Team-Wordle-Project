import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
public class GlobalStats {
    public static void main(String[] args) {
        File file = new File("src/data/wordle-full-1.txt");
        Map<String, Integer> wordFreqMap = wordFreq(file);
        Map<Character, Integer> letterFreqMap = letterFreq(file);
        printFreq(letterFreqMap);
        printFreq(wordFreqMap);
    }

    private static <K, V extends Comparable<V>> void printFreq(Map<K, V> frequencyMap) {
        sortedMap(frequencyMap).forEach(entry ->
                System.out.println(entry.getKey() + " -> " + entry.getValue())
        );
    }

    private static <K, V extends Comparable<V>> Stream<Map.Entry<K, V>> sortedMap(Map<K, V> map) {
        return map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed());
    }

    private Map<String, Integer> guessList;

    public GlobalStats() {

    }

    public Map<String, Integer> getAllWordsGuessed() {
        return null;
    }

    public void saveToFIle() {

    }

    public static Map<String, Integer> wordFreq(File wordStorage) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(wordStorage))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Integer> wordfreq = new HashMap<>();
        for (String word : words) {
            wordfreq.put(word, wordfreq.getOrDefault(word, 0) + 1);
        }
        return wordfreq;
    }

    public static Map<Character, Integer> letterFreq(File wordStorage) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(wordStorage))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<Character, Integer> letterfreq = new HashMap<>();
        for (String word : words) {
            for (Character letter : word.toCharArray()) {
                letterfreq.put(letter, letterfreq.getOrDefault(letter, 0) + 1);
            }
            
        }
        return letterfreq;
    }
}