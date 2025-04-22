import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ExclusionManager {
    private static final String FILE = "src/data/exclusion.txt";
    private static final ExclusionManager INSTANCE = new ExclusionManager();
    private final Set<String> excluded = ConcurrentHashMap.newKeySet();

    private ExclusionManager() {
        load();
    }

    public static ExclusionManager get() {
        return INSTANCE;
    }

    public boolean isExcluded(String guess, String goalWord) {
        return !guess.equals(goalWord) && excluded.contains(guess);
    }

    public void add(String w) {
        excluded.add(w.toLowerCase());
        save();
    }

    public void remove(String w) {
        excluded.remove(w.toLowerCase());
        save();
    }

    public Set<String> list() {
        return Set.copyOf(excluded);
    }

    private void load() {
        try {
            Files.lines(Path.of(FILE)).forEach(this::add);
        } catch (IOException ignored) { /* first run – file absent */ }
    }

    private synchronized void save() {
        try {
            Files.write(Path.of(FILE), excluded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
