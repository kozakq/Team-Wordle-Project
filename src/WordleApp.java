/*
 * Course: SWE2410-121
 * Fall 2024-2025
 * File header contains class WordleApp
 * Name: childressg
 * Created 2/19/2025
 */

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Course SWE2410-121
 * Fall 2024-2025
 * Class WordleApp Purpose: main wordle app handler
 *
 * @author childressg
 * @version created on 2/19/2025 11:27 AM
 */
public class WordleApp {
    public static boolean isAdmin;
    protected static Account currentAccount;
    static List<Account> accountList;
    static WordStorage wordStorage;
    private static Dictionary dictionary;
    private static String goalWord;

    public static void initialize() {
        dictionary = new Dictionary();
        accountList = new ArrayList<>();
        wordStorage = new WordStorage();
        loadAccounts();
        changeGoalWord();
    }

    public static boolean changeDictionary(String filePath) {
        return false;
    }

    public static String checkWord(String word) {
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
                wordStorage.addWord(word);
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    private static int characterCount(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) count++;
        }
        return count;
    }

    private static int numberCorrectCharacter(String goal, String guess, char character) {
        int count = 0;
        for (int i = 0; i < guess.length(); i++) {
            if (goal.charAt(i) == guess.charAt(i) && guess.charAt(i) == character) {
                count++;
            }
        }
        return count;
    }

    public static String changeGoalWord() {
        goalWord = dictionary.getRandomWord();
        System.out.println(goalWord);
        return goalWord;
    }

    public static boolean createAccount(String username, String password) {
        if (isValidUsername(username) && !password.isEmpty()) {
            currentAccount = new Account(username, password);
            accountList.add(currentAccount);
            return true;
        }
        return false;
    }

    public static Double getAverageGuessess() {
        return currentAccount.getAverageGuesses();
    }

    public static String getGoalWord() {
        return goalWord;
    }

    public static List<String> getMostCommonGuesses() {
        return currentAccount.getMostCommonGuesses();
    }

    public static boolean login(String username, String password) {
        Account account = validateLogin(username, password);
        if (account != null) {
            currentAccount = account;
            return true;
        } else {
            return false;
        }
    }

    public static Account validateLogin(String username, String password) {
        for (Account account : accountList) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }

    public static boolean isValidUsername(String username) {
        for (Account account : accountList) {
            if (account.getUsername().equals(username)) {
                return false;
            }
        }
        return !username.isEmpty();
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

    private static void loadAccounts() {
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

    public static void addGuessCount(int count) {
        if (currentAccount != null) {
            currentAccount.addGuessCount(count);
        }
    }

    public static void save() {
        if (currentAccount != null) {
            currentAccount.saveToFile();
        }
        if (wordStorage != null) {
            wordStorage.saveToFile();
        }
    }

    public static boolean isAdmin() {
        return currentAccount != null && currentAccount.getUserType() == UserType.ADMIN;
    }


    public static Map<String, Integer> getMostCommonGuessesWithCounts() {
        if (currentAccount == null) {
            System.out.println("Error: Current account is null. Returning an empty map.");
            return new LinkedHashMap<>();
        }

        Map<String, Integer> guessesMap = currentAccount.getGuesses();
        if (guessesMap == null) {
            System.out.println("Error: Guesses map is null. Returning an empty map.");
            return new LinkedHashMap<>();
        }

        return guessesMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


    public static Map<Character, Integer> getMostCommonLettersWithCounts() {
        if (currentAccount == null) {
            System.out.println("CurrentAccount is null.");
            return new HashMap<>();
        }

        Map<Character, Integer> letterCounts = new HashMap<>();
        Map<String, Integer> guessedWords = currentAccount.getGuesses();

        for (Map.Entry<String, Integer> entry : guessedWords.entrySet()) {
            String word = entry.getKey();
            int wordCount = entry.getValue();

            for (char c : word.toCharArray()) {
                letterCounts.put(c, letterCounts.getOrDefault(c, 0) + wordCount);
            }
        }

        Object LinkedHashMap;
        return letterCounts.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static boolean logout() {
        if (currentAccount != null) {
            currentAccount = null;
            return true;
        }
        return false;
    }

    public static void reloadAccounts() {
        accountList = new ArrayList<>();
        loadAccounts();


    }
}
