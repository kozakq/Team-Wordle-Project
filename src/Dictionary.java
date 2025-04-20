import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Dictionary {

    private final HashMap<Integer, List<String>> wordDictionary;

    public Dictionary() {
        wordDictionary = new HashMap<>();
        loadToList(Dictionary.class.getResource("data/words.txt").getPath());
    }

    public Dictionary(String resourcePath) {
        wordDictionary = new HashMap<>();
        loadToList(resourcePath);
    }

    public String getRandomWord(int length) {
        List<String> wordList = wordDictionary.get(length);
        return wordList.get((int) (Math.random() * wordList.size()));
    }

    public boolean isValidWord(String word) {
        List<String> wordList = wordDictionary.get(word.length());
        return wordList.contains(word);
    }

    public boolean changeFile(File file) {
        loadToList(file.getPath());
        return true;
    }

    private void loadToList(String resourcePath) {
        wordDictionary.clear();
        try (InputStream input = new FileInputStream(resourcePath)) {
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

    public Set<Integer> getWordLengths() {
        return wordDictionary.keySet();
    }
}