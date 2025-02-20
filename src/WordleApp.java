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
    private final Dictionary dictionary;
    private Dictionary dictionary;
    private String goalWord;

    public WordleApp() {
        dictionary = new Dictionary();
        goalWord = "allow";
    }

    public boolean changeDictionary(String filePath) {
        return false;
    }

    public boolean checkWord(String word) {
        return dictionary.isValidWord(word);
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
            if(account.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
}
