/*
 * Course: SWE2410-121
 * Fall 2024-2025
 * File header contains class WordleApp
 * Name: childressg
 * Created 2/19/2025
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Course SWE2410-121
 * Fall 2024-2025
 * Class WordleApp Purpose: main wordle app handler
 *
 * @author childressg
 * @version created on 2/19/2025 11:27 AM
 */
public class WordleApp {
    private final List<Account> accountList;
    private Account currentAccount;
    private final Dictionary dictionary;
    private String goalWord;

    public WordleApp() {
        dictionary = new Dictionary();
        accountList = new ArrayList<>();
        loadAccounts();
        changeGoalWord();
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
            if (currentAccount != null) {
                currentAccount.addGuess(word);
            }
            return sb.toString();
        } else {
            return "";
        }
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

    public String changeGoalWord() {
        goalWord = "allow";
//        goalWord = dictionary.getRandomWord();
        return goalWord;
    }

    public boolean createAccount(String username, String password) {
        if (isValidUsername(username)) {
            currentAccount = new Account(username, password);
            accountList.add(currentAccount);
            return true;
        }
        return false;
    }

    public Double getAverageGuessess() {
        return currentAccount.getAverageGuesses();
    }

    public String getGoalWord() {
        return goalWord;
    }

    public List<String> getMostCommonGuesses() {
        return currentAccount.getMostCommonGuesses();
    }

    public boolean login(String username, String password) {
        Account account = validateLogin(username, password);
        if (account != null) {
            currentAccount = account;
            return true;
        } else {
            System.out.println("Incorrect login!");
            return false;
        }
    }

    public Account validateLogin(String username, String password) {
        for (Account account : accountList) {
            if(account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }

    public boolean isValidUsername(String username) {
        for (Account account : accountList) {
            if(account.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    private void loadAccounts() {
        int maxID = -1;
        try {
            File directory = new File(Account.ACCOUNT_DIRECTORY);
            File[] accountFiles = directory.listFiles();
            for (File accountFile : accountFiles) {
                Account account = new Account(accountFile);
                if (account.getAccountID() > maxID) {
                    maxID = account.getAccountID();
                }
                accountList.add(account);
            }
            AccountID.setNextID(maxID + 1);
        } catch (NullPointerException e) {
            System.out.println("Directory does not exist");
        }
    }

    public void addGuessCount(int count) {
        if (currentAccount != null) {
            currentAccount.addGuessCount(count);
        }
    }

    public void save() {
        if (currentAccount != null) {
            currentAccount.saveToFile();
        }
    }
}
