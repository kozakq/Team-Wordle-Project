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
    private final WordleDictionary dictionary;
    private final String goalWord;
    private WordleDictionary dictionary;
    private String goalWord;
    private WordleDictionary wordleDictionary;
    private Dictionary dictionary;
    private final Dictionary dictionary;
    private Dictionary dictionary;
    private String goalWord;
    private WordleDictionary wordleDictionary;

    public WordleApp() {
        wordleDictionary = new WordleDictionary();
        dictionary = new WordleDictionary();
        goalWord = dictionary.getRandomWord();
        goalWord = dictionary.getRandomWord();
        wordleDictionary = new WordleDictionary();
        dictionary = new Dictionary();
        goalWord = "allow";
        dictionary = new Dictionary();
        goalWord = "soars";
        goalWord = "trade";
                //dictionary.getRandomWord();
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
                    if (numberCorrectCharacter(goalWord, word, guess_letter) == characterCount(goalWord, guess_letter)) {
                        sb.append('x');
                    } else if (characterCount(word.substring(0, i), guess_letter) < characterCount(goalWord, guess_letter)) {
                        sb.append('y');
                    } else {
                        sb.append('x');
                    }
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
        return wordleDictionary.isValidWord(word);
    }

    private int characterCount (String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) count++;
        }
        return count;
    }

    private int numberCorrectCharacter (String goal, String guess, char character) {
        int count = 0;
        for (int i = 0; i < guess.length(); i++) {
            if (goal.charAt(i) == guess.charAt(i) && guess.charAt(i) == character) {
                count++;
            }
        }
        return count;
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
        return null;
        return goalWord;
        return dictionary.getRandomWord();
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

    public boolean isValidUsername(String username) {
        for (Account account : accountList) {
            if (account.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
}
