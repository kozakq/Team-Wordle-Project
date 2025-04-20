import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ExclusionManager {
    private static final String FILE = "data/exclusion.txt";
    private final Set<String> excluded = ConcurrentHashMap.newKeySet();

    private ExclusionManager() { load(); }
    private static final ExclusionManager INSTANCE = new ExclusionManager();
    public static ExclusionManager get() { return INSTANCE; }

    public boolean isExcluded(String guess, String goalWord) {
        return !guess.equals(goalWord) && excluded.contains(guess);
    }
    public void add(String w)    { excluded.add(w.toLowerCase()); save(); }
    public void remove(String w) { excluded.remove(w.toLowerCase()); save(); }
    public Set<String> list()    { return Set.copyOf(excluded); }

    private void load() {
        try { Files.lines(Path.of(FILE)).forEach(this::add); }
        catch (IOException ignored) { /* first run – file absent */ }
    }
    private synchronized void save() {
        try { Files.write(Path.of(FILE), excluded); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        ExclusionManager manager = ExclusionManager.get();

        System.out.println("=== ExclusionManager Test ===");
        System.out.println("Current exclusion list: " + manager.list());
        
        try {
            List<String> wordlist = new ArrayList<>();
            // Load wordlist
            try (Scanner fileScanner = new Scanner(new File("src/data/wordlist.txt"))) {
                while (fileScanner.hasNextLine()) {
                    wordlist.add(fileScanner.nextLine().trim());
                }
            }
            System.out.println("Loaded " + wordlist.size() + " words from wordlist");
            
            try (Scanner in = new Scanner(System.in)) {
                // Phase 1: Add words to exclusion list
                System.out.println("\n--- Phase 1: Manage Exclusion List ---");
                boolean managingExclusions = true;
                
                while (managingExclusions) {
                    System.out.println("\nExclusion List Options:");
                    System.out.println("1. Add a word to exclusion list");
                    System.out.println("2. Remove a word from exclusion list");
                    System.out.println("3. Show current exclusion list");
                    System.out.println("4. Proceed to testing phase");
                    System.out.print("Enter your choice: ");
                    
                    int choice;
                    try {
                        choice = Integer.parseInt(in.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number");
                        continue;
                    }
                    
                    switch (choice) {
                        case 1:
                            System.out.print("Enter word to add to exclusion list: ");
                            String wordToAdd = in.nextLine().toLowerCase();
                            manager.add(wordToAdd);
                            System.out.println("Added '" + wordToAdd + "' to exclusion list");
                            break;
                            
                        case 2:
                            System.out.print("Enter word to remove from exclusion list: ");
                            String wordToRemove = in.nextLine().toLowerCase();
                            manager.remove(wordToRemove);
                            System.out.println("Removed '" + wordToRemove + "' from exclusion list");
                            break;
                            
                        case 3:
                            System.out.println("Current exclusion list: " + manager.list());
                            break;
                            
                        case 4:
                            managingExclusions = false;
                            break;
                            
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
                
                // Phase 2: Test against wordlist
                System.out.println("\n--- Phase 2: Testing Wordle Game ---");
                
                while (true) {
                    System.out.println("\nTesting Options:");
                    System.out.println("1. Play simulated Wordle game");
                    System.out.println("2. Check specific word against exclusions");
                    System.out.println("3. Exit");
                    System.out.print("Enter your choice: ");
                    
                    int choice;
                    try {
                        choice = Integer.parseInt(in.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number");
                        continue;
                    }
                    
                    switch (choice) {
                        case 1:
                            // Simulate Wordle game
                            if (wordlist.isEmpty()) {
                                System.out.println("Error: Wordlist is empty");
                                break;
                            }
                            
                            // Select random goal word
                            String goalWord = wordlist.get(new Random().nextInt(wordlist.size()));
                            System.out.println("Goal word selected (hidden). Start guessing!");
                            
                            int attempts = 0;
                            final int MAX_ATTEMPTS = 6;
                            
                            while (attempts < MAX_ATTEMPTS) {
                                System.out.print("Enter guess #" + (attempts+1) + ": ");
                                String guess = in.nextLine().toLowerCase();
                                
                                if (guess.length() != 5) {
                                    System.out.println("Please enter a 5-letter word");
                                    continue;
                                }
                                
                                if (manager.isExcluded(guess, goalWord)) {
                                    System.out.println("That word is excluded! Try another word.");
                                    continue;
                                }
                                
                                attempts++;
                                
                                if (guess.equals(goalWord)) {
                                    System.out.println("Correct! You won in " + attempts + " attempts.");
                                    break;
                                } else if (attempts == MAX_ATTEMPTS) {
                                    System.out.println("Game over! The word was: " + goalWord);
                                } else {
                                    System.out.println("Incorrect. " + (MAX_ATTEMPTS - attempts) + " attempts remaining.");
                                }
                            }
                            break;
                            
                        case 2:
                            System.out.print("Enter word to check: ");
                            String wordToCheck = in.nextLine().toLowerCase();
                            System.out.print("Enter goal word: ");
                            String goalWord2 = in.nextLine().toLowerCase();
                            
                            boolean excluded = manager.isExcluded(wordToCheck, goalWord2);
                            System.out.println("'" + wordToCheck + "' is " + (excluded ? "excluded" : "not excluded") + 
                                               " when goal word is '" + goalWord2 + "'");
                            break;
                            
                        case 3:
                            System.out.println("Exiting...");
                            return;
                            
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Wordlist file not found at src/data/wordlist.txt");
        }
    }
}
