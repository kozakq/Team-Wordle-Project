/*
 * Course: SWE2410-121
 * Fall 2024-2025
 * File header contains class StatsController
 * Name: childressg
 * Created 3/11/2025
 */

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminStatsController {
    private Stage mainStage;
    private Scene gameScene;
    private boolean cssLoaded = false;

    @FXML
    private Label stats;

    @FXML
    private Button back;

    @FXML
    private ComboBox<String> accountsComboBox;

    @FXML
    private Button switchButton;

    @FXML
    private TableView<StatEntry> statsTable;

    @FXML
    private TableColumn<StatEntry, String> itemColumn;

    @FXML
    private TableColumn<StatEntry, Integer> countColumn;

    @FXML
    private TabPane tabPane;
    
    @FXML
    private TextArea logTextArea;
    
    @FXML
    private Button viewLogsButton;

    private boolean isDisplayingWords = true;

    private int displayLimit = 5;
    private boolean showAllEntries = false;

    @FXML
    private Button toggleViewButton;

    @FXML
    private Label gamesPlayedLabel;

    @FXML
    private Label gamesWonLabel;

    @FXML
    private Label winRateLabel;

    public void initialize() {
        back.setOnAction(event -> mainStage.setScene(gameScene));
        setupTableColumns();
        loadGlobalStats();
        setupButton();
        populateAccounts();
        setupToggleViewButton();
        setupViewLogsButton();
        loadStylesheet();
        updateGameStats("Global Stats");
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
        if (mainStage.getScene() != null) {
            String cssPath = getClass().getResource("/gui/adminstatspanel.css").toExternalForm();
            mainStage.getScene().getStylesheets().add(cssPath);
        }
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    private void loadStylesheet() {
        if (!cssLoaded && mainStage != null && mainStage.getScene() != null) {
            try {
                String cssPath = getClass().getResource("/adminStats.css").toExternalForm();
                mainStage.getScene().getStylesheets().add(cssPath);
                cssLoaded = true;
            } catch (Exception e) {
                System.err.println("Failed to load CSS file: " + e.getMessage());
            }
        }
    }

    private void setupTableColumns() {
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        // Add sorting capability
        itemColumn.setSortable(true);
        countColumn.setSortable(true);
    }

    private void setupButton() {
        switchButton.setOnAction(e -> {
            isDisplayingWords = !isDisplayingWords;
            if (isDisplayingWords) {
                switchButton.setText("SWITCH TO LETTERS");
            } else {
                switchButton.setText("SWITCH TO WORDS");
            }
            refreshCurrentView(accountsComboBox.getValue());
        });
    }

    private void setupViewLogsButton() {
        if (viewLogsButton != null) {
            viewLogsButton.setOnAction(e -> {
                String selected = accountsComboBox.getValue();
                if (!"Global Stats".equals(selected)) {
                    loadLogsForAccount(selected);
                    if (tabPane != null) {
                        tabPane.getSelectionModel().select(1); // Switch to logs tab
                    }
                }
            });
        }
        
        // Also handle tab selection
        if (tabPane != null) {
            tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() == 1) { // Logs tab selected
                    String selected = accountsComboBox.getValue();
                    if (!"Global Stats".equals(selected)) {
                        loadLogsForAccount(selected);
                    }
                }
            });
        }
    }

    private void loadLogsForAccount(String username) {
        if (logTextArea == null) return;
        
        logTextArea.clear();
        
        // Find the account
        Account selectedAccount = WordleApp.accountList.stream()
                .filter(account -> account.getUsername().equals(username))
                .findFirst()
                .orElse(null);
                
        if (selectedAccount == null) {
            logTextArea.setText("No account found with username: " + username);
            return;
        }
        
        int accountId = selectedAccount.getAccountID();
        String logFilePath = String.format("%s/user%07d_keylog.txt", AdminLogging.LOG_DIRECTORY, accountId);
        File logFile = new File(logFilePath);
        
        System.out.println("Looking for log file: " + logFile.getAbsolutePath());
        
        if (!logFile.exists()) {
            logTextArea.setText("No log file found at: " + logFile.getAbsolutePath() + 
                               "\nMake sure logs are being created when using this account.");
            
            // Check if logs directory exists
            File logsDir = new File(AdminLogging.LOG_DIRECTORY);
            if (!logsDir.exists()) {
                System.out.println("Logs directory doesn't exist. Creating: " + logsDir.getAbsolutePath());
                logsDir.mkdirs();
            } else {
                System.out.println("Logs directory exists at: " + logsDir.getAbsolutePath());
                System.out.println("Contents: " + String.join(", ", logsDir.list() != null ? logsDir.list() : new String[]{"<empty>"}));
            }
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            StringBuilder logContent = new StringBuilder();
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                logContent.append(line).append("\n");
                lineCount++;
            }
            
            if (lineCount > 0) {
                logTextArea.setText(logContent.toString());
                System.out.println("Loaded " + lineCount + " lines from log file");
            } else {
                logTextArea.setText("Log file exists but is empty: " + logFile.getAbsolutePath());
            }
        } catch (IOException e) {
            logTextArea.setText("Error reading log file: " + e.getMessage());
            System.err.println("Error reading log file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateAccounts() {
        accountsComboBox.getItems().clear();
        accountsComboBox.getItems().add("Global Stats");
        for (Account account : WordleApp.accountList) {
            accountsComboBox.getItems().add(account.getUsername());
        }
        accountsComboBox.setValue("Global Stats");
        accountsComboBox.setOnAction(e -> {
            showAllEntries = false;
            toggleViewButton.setText("Show All");
            String selected = accountsComboBox.getValue();
            refreshCurrentView(selected);
            updateGameStats(selected);
            
            // Enable or disable log button based on selection
            if (viewLogsButton != null) {
                viewLogsButton.setDisable("Global Stats".equals(selected));
            }
        });
    }

    private void refreshCurrentView(String selected) {
        statsTable.getItems().clear(); // Always clear the table first
        if ("Global Stats".equals(selected)) {
            if (isDisplayingWords) {
                loadGlobalStats();
            } else {
                loadLetterStats();
            }
        } else {
            loadAccountStats(selected);
        }
    }

    private void loadGlobalStats() {
        statsTable.getItems().clear();
        Map<String, Integer> wordCounts = WordleApp.wordStorage.getWordCounts();
        ArrayList<StatEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            entries.add(new StatEntry(entry.getKey(), entry.getValue()));
        }

        entries.sort((a, b) -> b.getCount().compareTo(a.getCount()));
        int limit = showAllEntries ? entries.size() : Math.min(displayLimit, entries.size());
        for (int i = 0; i < limit; i++) {
            statsTable.getItems().add(entries.get(i));
        }
    }

    private void loadLetterStats() {
        statsTable.getItems().clear();
        Map<Character, Integer> letterCounts = PlayerStats.letterFreq(
                new ArrayList<>(WordleApp.wordStorage.getWordCounts().keySet())
        );
        ArrayList<StatEntry> entries = new ArrayList<>();

        for (Map.Entry<Character, Integer> entry : letterCounts.entrySet()) {
            entries.add(new StatEntry(entry.getKey().toString(), entry.getValue()));
        }

        entries.sort((a, b) -> b.getCount().compareTo(a.getCount()));

        int limit = showAllEntries ? entries.size() : Math.min(displayLimit, entries.size());
        for (int i = 0; i < limit; i++) {
            statsTable.getItems().add(entries.get(i));
        }
    }

    private void loadAccountStats(String username) {
        statsTable.getItems().clear();
        Account selected = WordleApp.accountList.stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (selected == null || selected.guesses == null || selected.guesses.isEmpty()) {
            return;
        }

        if (isDisplayingWords) {
            ArrayList<StatEntry> entries = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : selected.guesses.entrySet()) {
                entries.add(new StatEntry(entry.getKey(), entry.getValue()));
            }
            entries.sort((a, b) -> b.getCount().compareTo(a.getCount()));

            int limit = showAllEntries ? entries.size() : Math.min(displayLimit, entries.size());
            for (int i = 0; i < limit && i < entries.size(); i++) {
                statsTable.getItems().add(entries.get(i));
            }
        } else {
            Map<Character, Integer> letterCounts = PlayerStats.letterFreq(
                    new ArrayList<>(selected.guesses.keySet())
            );
            ArrayList<StatEntry> entries = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : letterCounts.entrySet()) {
                entries.add(new StatEntry(entry.getKey().toString(), entry.getValue()));
            }
            entries.sort((a, b) -> b.getCount().compareTo(a.getCount()));

            int limit = showAllEntries ? entries.size() : Math.min(displayLimit, entries.size());
            for (int i = 0; i < limit && i < entries.size(); i++) {
                statsTable.getItems().add(entries.get(i));
            }
        }
    }

    private void setupToggleViewButton() {
        toggleViewButton.setOnAction(e -> {
            showAllEntries = !showAllEntries;
            toggleViewButton.setText(showAllEntries ? "Show Top " + displayLimit : "Show All");
            refreshCurrentView(accountsComboBox.getValue());
        });
    }

    private void updateGameStats(String selected) {
        if ("Global Stats".equals(selected)) {
            int totalGames = 0;
            int totalWins = 0;
            int totalLosses = 0;

            for (Account account : WordleApp.accountList) {
                totalGames += account.getTotalGames();
                totalWins += account.getGamesWon();
                totalLosses += account.getGamesLost();
            }

            updateStatsLabels(totalGames, totalWins, totalLosses);
        } else {
            Account selectedAccount = WordleApp.accountList.stream()
                    .filter(a -> a.getUsername().equals(selected))
                    .findFirst()
                    .orElse(null);

            if (selectedAccount != null) {
                updateStatsLabels(
                        selectedAccount.getTotalGames(),
                        selectedAccount.getGamesWon(),
                        selectedAccount.getGamesLost()
                );
            } else {
                updateStatsLabels(0, 0, 0);
            }
        }
    }

    private void updateStatsLabels(int totalGames, int gamesWon, int gamesLost) {
        gamesPlayedLabel.setText(String.valueOf(totalGames));
        gamesWonLabel.setText(String.valueOf(gamesWon));

        // Calculate and format win rate
        double winRate = totalGames > 0 ? (gamesWon * 100.0 / totalGames) : 0.0;
        winRateLabel.setText(String.format("%.1f%%", winRate));
    }

    public void refreshAllStats() {
        if (accountsComboBox != null) {
            String selected = accountsComboBox.getValue();
            refreshCurrentView(selected);
            updateGameStats(selected);
            
            // Also refresh logs if a specific user is selected
            if (!"Global Stats".equals(selected)) {
                loadLogsForAccount(selected);
                if (viewLogsButton != null) {
                    viewLogsButton.setDisable(false);
                }
            } else if (viewLogsButton != null) {
                viewLogsButton.setDisable(true);
            }
            
            System.out.println("Refreshed stats for: " + selected); // Debug print
        }
    }

    public static class StatEntry {
        private final String item;
        private final Integer count;

        public StatEntry(String item, Integer count) {
            this.item = item;
            this.count = count;
        }

        public String getItem() {
            return item;
        }

        public Integer getCount() {
            return count;
        }
    }
}