import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Dictionary {

    private final HashMap<Integer, List<String>> wordDictionary;

    /**
     * no argument constructor for a dictionary object
     */

    public Dictionary() {
        wordDictionary = new HashMap<>();
        loadToList("/data/words.txt"); // Make sure this file is in resources/data
    }

    /**
     * constructor for dictionary taking the txt file
     *
     * @param resourcePath file a path of txt file
     */
    public Dictionary(String resourcePath) {
        wordDictionary = new HashMap<>();
        loadToList(resourcePath);
    }

    /**
     * method to get a random word
     */
    public String getRandomWord(int length) {
        List<String> wordList = wordDictionary.get(length);
        return wordList.get((int) (Math.random() * wordList.size()));
    }

    /**
     * method to check if a word is contained inside the dictionary
     *
     * @param word word to check
     */
    public boolean isValidWord(String word) {
        List<String> wordList = wordDictionary.get(word.length());
        return wordList.contains(word);
    }

    private void loadToList(String resourcePath) {
        try (InputStream input = Dictionary.class.getResourceAsStream(resourcePath)) {
            assert input != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String word = line.trim().toLowerCase();
                    if (wordDictionary.containsKey(word.length())) {
                        wordDictionary.get(word.length()).add(word);
                    } else {
                        List<String> wordList = new ArrayList<>();
                        wordList.add(word);
                        wordDictionary.put(word.length(), wordList);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dictionary: " + resourcePath, e);
        }
    }
}