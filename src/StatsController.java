/*
 * Course: SWE2410-121
 * Fall 2024-2025
 * File header contains class StatsController
 * Name: childressg
 * Created 3/11/2025
 */

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Map;

public class StatsController {
    private Stage mainStage;
    private Scene gameScene;

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

    private boolean isDisplayingWords = true;


    public void initialize() {
        back.setOnAction(event -> mainStage.setScene(gameScene));
        setupTableColumns();
        loadGlobalStats();
        setupButton();
        populateAccounts();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void updateStats() {
        if (WordleApp.isLoggedIn()) {
            stats.setText(String.format("Average Guesses: %.2f", WordleApp.getAverageGuessess()));
        }
    }

    private void setupTableColumns() {
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }

    private void setupButton() {
        switchButton.setOnAction(e -> {
            isDisplayingWords = !isDisplayingWords;
            if (isDisplayingWords) {
                switchButton.setText("SWITCH TO LETTERS");
                loadGlobalStats();
            } else {
                switchButton.setText("SWITCH TO WORDS");
                loadLetterStats();
            }
        });
    }

    private void loadGlobalStats() {
        statsTable.getItems().clear();
        Map<String, Integer> wordCounts = WordleApp.wordStorage.getWordCounts();
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            statsTable.getItems().add(new StatEntry(entry.getKey(), entry.getValue()));
        }
    }

    private void loadLetterStats() {
        statsTable.getItems().clear();
        Map<Character, Integer> letterCounts = PlayerStats.letterFreq(
                new ArrayList<>(WordleApp.wordStorage.getWordCounts().keySet())
        );
        for (Map.Entry<Character, Integer> entry : letterCounts.entrySet()) {
            statsTable.getItems().add(new StatEntry(entry.getKey().toString(), entry.getValue()));
        }
    }

    private void populateAccounts() {
        for (Account account : WordleApp.accountList) {
            accountsComboBox.getItems().add(account.getUsername());
        }
        accountsComboBox.setOnAction(e -> loadAccountStats(accountsComboBox.getValue()));
    }

    private void loadAccountStats(String username) {
        Account selected = WordleApp.accountList.stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        if (selected != null) {
            statsTable.getItems().clear();
            Map<String, Integer> guesses = selected.guesses;
            for (Map.Entry<String, Integer> entry : guesses.entrySet()) {
                statsTable.getItems().add(new StatEntry(entry.getKey(), entry.getValue()));
            }
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