import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Map;

public class StatsController {

    private Stage mainStage;
    private Scene gameScene;
    @FXML
    private Label stats;

    @FXML
    private Button back;

    @FXML
    private GridPane guessedWordsTable;

    @FXML
    private GridPane mostGuessedLettersTable;

    @FXML
    private Label gamesPlayedLabel;

    @FXML
    private Label gamesWonLabel;

    @FXML
    private Label winRateLabel;

    @FXML
    public void initialize() {
        System.out.println("StatsController initialized. Instance: " + this.hashCode());

        if (guessedWordsTable == null) {
            System.out.println("Error: guessedWordsTable is null. Check FXML fx:id.");
        } else {
            System.out.println("guessedWordsTable initialized successfully.");
        }

        if (mostGuessedLettersTable == null) {
            System.out.println("Error: mostGuessedLettersTable is null. Check FXML fx:id.");
        } else {
            System.out.println("mostGuessedLettersTable initialized successfully.");
        }

        if (back != null) {
            back.setOnAction(event -> {
                if (mainStage != null && gameScene != null) {
                    System.out.println("Returning to game scene.");
                    mainStage.setScene(gameScene);
                } else {
                    System.out.println("Error: MainStage or GameScene not set.");
                }
            });
        }

        System.out.println("Calling updateStats() from initialize.");
        updateStats();
    }

    public void updateStats() {
        if (WordleApp.currentAccount == null) {
            System.out.println("Error: No account logged in. Cannot update stats.");
            stats.setText("No account logged in. Please log in to view stats.");
            return;
        }

        System.out.println("PlayersStatsController is set. Proceeding to update stats.");
        stats.setText(String.format("Average Guesses: %.2f", WordleApp.getAverageGuessess()));

        displayTopGuessesTable();
        displayMostGuessedLettersTable();
        updateGameStatistics();
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void displayTopGuessesTable() {
        Map<String, Integer> commonGuesses = WordleApp.getMostCommonGuessesWithCounts();
        System.out.println("Common Guesses Data: " + commonGuesses);

        guessedWordsTable.getChildren().clear();

        if (commonGuesses != null && !commonGuesses.isEmpty()) {
            Label rankHeader = new Label("Rank");
            Label wordHeader = new Label("Word");
            Label countHeader = new Label("Count");

            rankHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;");
            wordHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;");
            countHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;");

            guessedWordsTable.add(rankHeader, 0, 0);
            guessedWordsTable.add(wordHeader, 1, 0);
            guessedWordsTable.add(countHeader, 2, 0);

            int rank = 1;
            for (Map.Entry<String, Integer> entry : commonGuesses.entrySet()) {
                Label rankLabel = new Label(String.valueOf(rank));
                Label wordLabel = new Label(entry.getKey());
                Label countLabel = new Label(String.valueOf(entry.getValue()));

                rankLabel.setStyle("-fx-text-fill: #ffffff;");
                wordLabel.setStyle("-fx-text-fill: #ffffff;");
                countLabel.setStyle("-fx-text-fill: #ffffff;");

                guessedWordsTable.add(rankLabel, 0, rank);
                guessedWordsTable.add(wordLabel, 1, rank);
                guessedWordsTable.add(countLabel, 2, rank);
                rank++;
            }
        } else {
            System.out.println("No common guesses found.");
            Label noGuessesLabel = new Label("No common guesses found.");
            noGuessesLabel.setStyle("-fx-text-fill: #ffffff;");
            guessedWordsTable.add(noGuessesLabel, 0, 0, 3, 1);
        }
        guessedWordsTable.requestLayout();
    }

    private void displayMostGuessedLettersTable() {
        Map<Character, Integer> commonLetters = WordleApp.getMostCommonLettersWithCounts();
        System.out.println("Common Letters Data: " + commonLetters);

        mostGuessedLettersTable.getChildren().clear();

        if (commonLetters != null && !commonLetters.isEmpty()) {
            Label rankHeader = new Label("Rank");
            Label letterHeader = new Label("Letter");
            Label countHeader = new Label("Count");

            rankHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;");
            letterHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;");
            countHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;");

            mostGuessedLettersTable.add(rankHeader, 0, 0);
            mostGuessedLettersTable.add(letterHeader, 1, 0);
            mostGuessedLettersTable.add(countHeader, 2, 0);

            int rank = 1;
            for (Map.Entry<Character, Integer> entry : commonLetters.entrySet()) {
                Label rankLabel = new Label(String.valueOf(rank));
                Label letterLabel = new Label(String.valueOf(entry.getKey()));
                Label countLabel = new Label(String.valueOf(entry.getValue()));

                rankLabel.setStyle("-fx-text-fill: #ffffff;");
                letterLabel.setStyle("-fx-text-fill: #ffffff;");
                countLabel.setStyle("-fx-text-fill: #ffffff;");

                mostGuessedLettersTable.add(rankLabel, 0, rank);
                mostGuessedLettersTable.add(letterLabel, 1, rank);
                mostGuessedLettersTable.add(countLabel, 2, rank);
                rank++;
            }
        } else {
            System.out.println("No guessed letters found.");
            Label noLettersLabel = new Label("No guessed letters found.");
            noLettersLabel.setStyle("-fx-text-fill: #ffffff;");
            mostGuessedLettersTable.add(noLettersLabel, 0, 0, 3, 1);
        }
        mostGuessedLettersTable.requestLayout();
    }

    private void updateGameStatistics() {
        if (gamesPlayedLabel == null || gamesWonLabel == null || winRateLabel == null) {
            System.out.println("Game statistics labels not found in FXML. Skipping update.");
            return;
        }

        if (WordleApp.currentAccount != null) {
            int totalGames = WordleApp.currentAccount.getTotalGames();
            int gamesWon = WordleApp.currentAccount.getGamesWon();
            int gamesLost = WordleApp.currentAccount.getGamesLost();

            System.out.println("Account stats: Total=" + totalGames + ", Won=" + gamesWon + ", Lost=" + gamesLost);

            gamesPlayedLabel.setText(String.valueOf(totalGames));
            gamesWonLabel.setText(String.valueOf(gamesWon));

            double winRate = totalGames > 0 ? (gamesWon * 100.0 / totalGames) : 0.0;
            winRateLabel.setText(String.format("%.1f%%", winRate));
        } else {
            System.out.println("No current account found for stats update");
        }
    }
}
