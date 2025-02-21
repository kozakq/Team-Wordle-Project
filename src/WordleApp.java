/*
 * Course: SWE2410-121
 * Fall 2024-2025
 * File header contains class WordleApp
 * Name: childressg
 * Created 2/19/2025
 */

import java.util.List;
import java.util.Map;

/**
 * Course SWE2410-121
 * Fall 2024-2025
 * Class WordleApp Purpose: main wordle app handler
 *
 * @author childressg
 * @version created on 2/19/2025 11:27 AM
 */
public class WordleApp {
    private List<Account> accountList;
    private Account currentAccount;
    private Dictionary dictionary;
    private String goalWord;
    private WordleDictionary wordleDictionary;
    private Dictionary dictionary;
    private final Dictionary dictionary;
    private Dictionary dictionary;
    private String goalWord;

    public WordleApp() {
        wordleDictionary = new WordleDictionary();
        dictionary = new Dictionary();
        goalWord = "allow";
        dictionary = new Dictionary();
        goalWord = "soars";
    }

    public boolean changeDictionary(String filePath) {
        return false;
    }

    public String checkWord(String word) {
        if (dictionary.isValidWord(word)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                char guess_letter = word.charAt(i);
                if (guess_letter == goalWord.charAt(i)) {
                    sb.append('g');
                } else if (goalWord.indexOf(guess_letter) != -1) {
                    sb.append('y');
                } else {
                    sb.append('x');
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    public boolean checkWord(String word) {
        return wordleDictionary.isValidWord(word);
    }

    public boolean createAccount(String username, String password) {
        return false;
    }

    public Map<String, Integer> getAllWordsGuesses() {
        return null;
    }

    public Double getAverageGuessess(int userId) {
        return null;
    }

    public String getGoalWord() {
        return null;
        return goalWord;
        return dictionary.getRandomWord();
    }

    public int getMostCommonGuesses() {
        return 0;
    }

    public boolean validateLogin(String username, String password) {
        return false;
    }

    public boolean isValidUsername(String username) {
        for (Account account : accountList) {
            if (account.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
}
