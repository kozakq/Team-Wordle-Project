import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Dictionary {

    private final List<String> wordList;

    /**
     * no argument constructor for a dictionary object
     */

    public Dictionary() {
        wordList = new ArrayList<>();
        loadToList("/data/wordle-full-1.txt"); // Make sure this file is in resources/data
    }

    /**
     * constructor for dictionary taking the txt file
     *
     * @param resourcePath file a path of txt file
     */
    public Dictionary(String resourcePath) {
        wordList = new ArrayList<>();
        loadToList(resourcePath);
    }

    /**
     * method to get a random word
     */
    public String getRandomWord() {
        return wordList.get((int) (Math.random() * wordList.size()));
    }

    /**
     * method to check if a word is contained inside the dictionary
     *
     * @param word word to check
     */
    public boolean isValidWord(String word) {
        return wordList.contains(word);
    }

    private void loadToList(String resourcePath) {
        try (InputStream input = Dictionary.class.getResourceAsStream(resourcePath)) {
            assert input != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    wordList.add(line.trim().toLowerCase());
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dictionary: " + resourcePath, e);
        }
    }
}