import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Leaderboard {

    File file;

    public Leaderboard() {
        file = new File("src/data/leaderboard.txt");
        leaderboard = readFile(file);
    }
    private List<Integer> leaderboard;

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
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file", e);
        }
        List<Integer> toAppend = new ArrayList<>();
        for (Integer val : leaderboard) {
            if (!existing.contains(val)) {
                toAppend.add(val);
                existing.add(val);
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
        leaderboard.add(time);
        saveToFile(file);
    }
    public int getListValue(int index) {
        return leaderboard.get(index);
    }
    public int getListLength() {
        return leaderboard.size();
    }
}
