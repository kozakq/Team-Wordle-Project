import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminLogging {
    private static final String LOG_DIRECTORY = "logs";
    private final int accountID;
    private File logFile;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private boolean isClosed = false;
    private List<String> logEntries = new ArrayList<>();

    public AdminLogging(int accountID) {
        this.accountID = accountID;
        initializeLogger();
    }

    private void initializeLogger() {
        try {
            File dir = new File(LOG_DIRECTORY);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filename = String.format("%s/user%07d_keylog.txt", LOG_DIRECTORY, accountID);
            logFile = new File(filename);
            String startMessage = "=== Logging session started at " + LocalDateTime.now().format(formatter) + " ===";
            logEntries = new ArrayList<>();
            logEntries.add(startMessage);
            isClosed = false;
        } catch (Exception e) {
            System.err.println("Error initializing logger for user " + accountID + ": " + e.getMessage());
        }
    }

    public void log(String keyInput) {
        if (isClosed) {
            System.err.println("Logger already closed for user " + accountID);
            return;
        }

        String timeStamp = LocalDateTime.now().format(formatter);
        String logEntry = timeStamp + " - " + keyInput;
        logEntries.add(logEntry);
    }

    private void writeToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            for (String entry : logEntries) {
                writer.write(entry);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to key log file for user " + accountID + ": " + e.getMessage());
        }
    }


    public void close() {
        if (!isClosed) {
            String endMessage = "=== Logging session ended at " + LocalDateTime.now().format(formatter) + " ===";
            logEntries.add(endMessage);
            writeToFile();
            isClosed = true;
        }
    }
}