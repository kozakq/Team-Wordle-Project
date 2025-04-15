import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private Map<String, Integer> guessList;

    public GlobalStats() {

    }

    public static void main(String[] args) {
        InputStream input = GlobalStats.class
                .getResourceAsStream("/data/wordle-official-1.txt");
        if (input == null) {
            System.err.println("File not found in resources!");
            return;
        }
        ArrayList<String> wordStorage = readFile(input);
        Map<String, Integer> wordFreqMap = wordFreq(wordStorage);
        Map<Character, Integer> letterFreqMap = letterFreq(wordStorage);
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

    public static Map<String, Integer> wordFreq(ArrayList<String> wordStorage) {
        List<String> words = wordStorage;
        Map<String, Integer> wordfreq = new HashMap<>();
        for (String word : words) {
            wordfreq.put(word, wordfreq.getOrDefault(word, 0) + 1);
        }
        return wordfreq;
    }

    public static Map<Character, Integer> letterFreq(ArrayList<String> wordStorage) {
        List<String> words = wordStorage;
        Map<Character, Integer> letterfreq = new HashMap<>();
        for (String word : words) {
            for (Character letter : word.toCharArray()) {
                letterfreq.put(letter, letterfreq.getOrDefault(letter, 0) + 1);
            }

        }
        return letterfreq;
    }

    public static ArrayList<String> readFile(InputStream file) {
        ArrayList<String> wordStorage = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordStorage.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("File Read");

        }
        return wordStorage;
    }

    public Map<String, Integer> getAllWordsGuessed() {
        return null;
    }

    public void saveToFile() {

    }
}