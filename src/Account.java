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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Account {
    public static final String ACCOUNT_DIRECTORY = "accounts";
    Map<String, Integer> guesses;
    private int accountID;
    private Map<Integer, Integer> guessCounts;
    private Map<String, Integer> gameStats;
    private String password;
    private String username;
    private UserType userType;
    private int time = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int totalGames = 0;

    public Account() {
        username = "Null";
        password = "Null";
        accountID = 0;
        guesses = new HashMap<>();
        guessCounts = new HashMap<>(Map.of(1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0));
        gameStats = new HashMap<>(Map.of("wins", 0, "losses", 0, "total", 0));
        userType = UserType.USER;
    }

    public Account(String username, String password) {
        this.username = username.toLowerCase();
        this.password = password;
        accountID = AccountID.getNextID();
        guesses = new HashMap<>();
        guessCounts = new HashMap<>(Map.of(1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0));
        gameStats = new HashMap<>(Map.of("wins", 0, "losses", 0, "total", 0));
        if (accountID == 0) {
            userType = UserType.ADMIN;
        } else {
            userType = UserType.USER;
        }
    }

    public Account(File accountFile) {
        try {
            List<String> lines = Files.readAllLines(accountFile.toPath());
            username = lines.get(0).substring(lines.get(0).indexOf(':') + 1);
            password = lines.get(1).substring(lines.get(1).indexOf(':') + 1);
            accountID = Integer.parseInt(lines.get(2).substring(lines.get(2).indexOf(':') + 1));
            userType = UserType.typeFromString(lines.get(3).substring(lines.get(3).indexOf(':') + 1));

            guessCounts = new HashMap<>();
            for (int i = 5; i <= 10; i++) {
                String[] nums = lines.get(i).split(":");
                guessCounts.put(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]));
            }
            int lineIndex = 12;
            gameStats = new HashMap<>();
            while (lineIndex < 15) {
                String[] splits = lines.get(lineIndex).split(":");
                gameStats.put(splits[0], Integer.parseInt(splits[1]));
                lineIndex++;
            }
            lineIndex++;
            guesses = new HashMap<>();
            while (lineIndex < lines.size()) {
                String[] splits = lines.get(lineIndex).split(":");
                guesses.put(splits[0], Integer.parseInt(splits[1]));
                lineIndex++;
            }

        } catch (IOException e) {
            System.out.println("Error reading from account file " + accountFile.getPath() + ".");
        } catch (Exception e) {
            System.out.println("Error parsing from account file " + accountFile.getPath() + ".");
        }
    }

    public List<Character> getMostCommonLetters() {
        Map<Character, Integer> letterCounts = new HashMap<>();
        for (String word : guesses.keySet()) {
            for (char c : word.toCharArray()) {
                letterCounts.put(c, letterCounts.getOrDefault(c, 0) + guesses.get(word));
            }
        }

        System.out.println("Letter Counts in Account: " + letterCounts);

        List<Map.Entry<Character, Integer>> entries = new ArrayList<>(letterCounts.entrySet());
        entries.sort(Map.Entry.<Character, Integer>comparingByValue(Comparator.reverseOrder()));

        List<Character> topLetters = new ArrayList<>();
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            topLetters.add(entries.get(i).getKey());
        }

        System.out.println("Top Letters in Account: " + topLetters);
        return topLetters;
    }

    public double getAverageGuesses() {
        double sum = 0;
        int count = 0;
        for (int i = 1; i <= 6; i++) {
            sum += i * guessCounts.get(i);
            count += guessCounts.get(i);
        }
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }

    public List<String> getMostCommonGuesses() {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(guesses.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()));

        List<String> topGuesses = new ArrayList<>();
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            topGuesses.add(entries.get(i).getKey());
        }

        return topGuesses;
    }


    public void saveToFile() {
        try (FileWriter writer = new FileWriter(String.format("%s/user%07d.txt", ACCOUNT_DIRECTORY, accountID), false)) {
            writer.write("username:" + username + "\n");
            writer.write("password:" + password + "\n");
            writer.write("accountID:" + accountID + "\n");
            writer.write("usertype:" + userType + "\n");

            writer.write("\n");

            for (int i = 1; i <= 6; i++) {
                writer.write(String.format("%d:%d\n", i, guessCounts.get(i)));
            }
            writer.write("\n");

            for (String key : gameStats.keySet()) {
                writer.write(String.format("%s:%d\n", key, gameStats.get(key)));
            }

            writer.write("\n");

            for (String key : guesses.keySet()) {
                writer.write(String.format("%s:%d\n", key, guesses.get(key)));
            }


            System.out.println("Data has been written to the file for user " + accountID + ".");
        } catch (IOException e) {
            System.out.println("Error saving to account file for user " + accountID + ".");
        }
    }

    public void addGuess(String guess) {
        guesses.put(guess, guesses.getOrDefault(guess, 0) + 1);
    }

    public void addGuessCount(int count) {
        guessCounts.put(count, guessCounts.getOrDefault(count, 0) + 1);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAccountID() {
        return accountID;
    }

    public UserType getUserType() {
        return userType;
    }

    public int getGamesWon() {
        if (gameStats != null && gameStats.containsKey("wins")) {
            return gameStats.get("wins");
        }
        return gamesWon;
    }

    public int getGamesLost() {
        if (gameStats != null && gameStats.containsKey("losses")) {
            return gameStats.get("losses");
        }
        return gamesLost;
    }

    public int getTotalGames() {
        if (gameStats != null && gameStats.containsKey("total")) {
            return gameStats.get("total");
        }
        return totalGames;
    }

    public int getTime() {
        if (gameStats != null && gameStats.containsKey("time")) {
            return gameStats.get("time");
        }
        return time;
    }

    public void recordGameResult(boolean won) {
        totalGames++;
        if (won) {
            gamesWon++;
        } else {
            gamesLost++;
        }
        gameStats.put("time", time);
        gameStats.put("total", totalGames);
        gameStats.put("wins", gamesWon);
        gameStats.put("losses", gamesLost);

        saveToFile();
    }
    public void reportTime(int time) {
        this.time = time;
    }


    public Map<String, Integer> getGuesses() {
        System.out.println("Account Guesses: " + guesses); // Debug print
        return guesses;
    }


}