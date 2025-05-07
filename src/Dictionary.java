import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Dictionary {

    private final HashMap<Integer, List<String>> wordDictionary;
    private List<String> evilDictionary;
    private boolean evilMode;

    public Dictionary() {
        wordDictionary = new HashMap<>();
        evilDictionary = new ArrayList<>();
        loadToList("src/data/wordle-full-1.txt"); // Make sure this file is in resources/data
        evilMode = false;
    }

    public Dictionary(String resourcePath) {
        wordDictionary = new HashMap<>();
        evilDictionary = new ArrayList<>();
        loadToList(resourcePath);
        evilMode = false;
    }

    public void changeToEvilMode() {
        evilMode = true;
        resetEvil();
    }

    public void resetEvil() {
        evilDictionary = new ArrayList<>(wordDictionary.get(WordleApp.getWordLength()));
    }

    public String getRandomWord(int length) {
        if (evilMode) {
            return evilDictionary.get((int) (Math.random() * evilDictionary.size()));
        } else {
            List<String> wordList = wordDictionary.get(length);
            return wordList.get((int) (Math.random() * wordList.size()));
        }
    }

    private boolean sharesLetters(String word1, String word2) {
        for (char c : word1.toCharArray()) {
            if (word2.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private boolean sharesLettersWithLocation(String word1, String word2) {
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    public void minimizeResponse(String word) {
        List<String> remaining = new ArrayList<>(evilDictionary.size());
        for (String w : evilDictionary) {
            if (!sharesLetters(w, word)) {
                remaining.add(w);
            }
        }
        if (!remaining.isEmpty()) {
            evilDictionary = remaining;
        } else {
            List<String> remaining_again = new ArrayList<>(evilDictionary.size());
            for (String w : evilDictionary) {
                if (!sharesLettersWithLocation(w, word)) {
                    remaining_again.add(w);
                }
            }
            if (!remaining_again.isEmpty()) {
                evilDictionary = remaining_again;
            }
        }
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
                if (evilMode) {
                    resetEvil();
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