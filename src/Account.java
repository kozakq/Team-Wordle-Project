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

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Account {
    public static final String ACCOUNT_DIRECTORY = "accounts";
    private int accountID;
    Map<String, Integer> guesses;
    private Map<Integer, Integer> guessCounts;
    private Map<String, Integer> gameStats;
    private String password;
    private String username;
    private UserType userType;
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
        if(accountID == 0){
            userType = UserType.ADMIN;
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

    public double getAverageGuesses() {
        double sum = 0;
        int count = 0;
        for(int i = 1; i <= 6; i++) {
            sum += i * guessCounts.get(i);
            count += guessCounts.get(i);
        }
        return sum / count;
    }

    public List<String> getMostCommonGuesses() {
        List<String> keys = new ArrayList<>(guesses.keySet());
        keys.sort(Comparator.comparingInt(guesses::get));
        return keys.subList(0, Math.min(keys.size(), 5));
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
    public UserType getUserType(){
        return userType;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void recordGameResult(boolean won) {
        totalGames++;
        gameStats.put("total", gameStats.getOrDefault("total", 0) + 1);
        
        if (won) {
            gamesWon++;
            gameStats.put("wins", gameStats.getOrDefault("wins", 0) + 1);
        } else {
            gamesLost++;
            gameStats.put("losses", gameStats.getOrDefault("losses", 0) + 1);
        }
    }
}