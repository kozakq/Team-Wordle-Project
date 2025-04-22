import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Leaderboard {

    File file;
    private List<Integer> leaderboard;

    public Leaderboard() {
        file = new File("src/data/leaderboard.txt");
        leaderboard = readFile(file);
    }

    public static ArrayList<Integer> readFile(File file) {
        ArrayList<Integer> leaderboard = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                leaderboard.add(Integer.valueOf(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("File Read");

        }
        Collections.sort(leaderboard);
        return leaderboard;
    }

    public void saveToFile(File file) {
        Set<Integer> existing = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    existing.add(Integer.parseInt(line.trim()));
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file", e);
        }
        List<Integer> toAppend = new ArrayList<>();
        for (Integer val : leaderboard) {
            if (!existing.contains(val) && val > 0) {
                toAppend.add(val);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (Integer val : toAppend) {
                writer.write(String.valueOf(val));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to the file", e);
        }

    }

    public void addToLeaderboard(int time) {
        if (time > 0) {
            leaderboard.add(time);
            saveToFile(file);
        } else {
            System.out.println("Invalid time value: " + time + " (must be positive)");
        }

    }

    public int getListValue(int index) {
        return leaderboard.get(index);
    }

    public int getListLength() {
        return leaderboard.size();
    }
}
