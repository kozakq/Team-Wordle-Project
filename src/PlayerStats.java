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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStats {

    private int playerID;


    public PlayerStats() {

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

    public double getAverageGuesses() {
        return 0;
    }

    public List<String> getMostCommonGuesses() {
        return null;
    }

}