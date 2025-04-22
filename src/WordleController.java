import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;

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
public class WordleController {
    private static final int MAX_GUESSES = 6;
    private final GUIController guiController;
    private final List<String> guessedWords;
    private final List<List<Label>> letterLabels;
    private final Label[][] keyLabels;
    private final HBox[] wordBoxes;
    private final ImageView lightbulb = new ImageView(new Image("gui/lightbulb.png"));
    @FXML
    private final ImageView settings = new ImageView(new Image("gui/settings.png"));

    public Button hintButton;
    public ProgressBar countdownBar;
    private Stage mainStage;
    private Scene statsScene;
    private String goalWord;
    private String currentWord;
    private int guessCount;
    private Scene adminStatsScene;
    private Scene playerStatsScene;
    private Scene adminSettingsScene;
    private Scene settingsScene;

    private AdminStatsController adminStatsController;
    private StatsController playerStatsController;
    private AdminSettingsController adminSettingsController;
    public SettingsController settingsController;
    private LeaderboardController leaderboardController;

    private boolean isGameWon;
    private int remainingHints = 3;
    Stage endGameStage = new Stage();
    private boolean isFlipping = false;

    @FXML
    private VBox words;
    @FXML
    private VBox keys;
    @FXML
    private Pane pane;
    @FXML
    private ImageView stats;
    @FXML
    private Label adminLabel;


    public WordleController() {
        goalWord = WordleApp.getGoalWord();
        letterLabels = new ArrayList<>();
        keyLabels = new Label[][]{{null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null}};
        guiController = new GUIController(keyLabels);
        currentWord = "";
        guessedWords = new ArrayList<>();
        mainStage = null;
        statsScene = null;
        adminStatsController = null;
        playerStatsController = null;
        adminSettingsController = null;
        isGameWon = false;
        wordBoxes = new HBox[MAX_GUESSES];


    }

    @FXML
    public void initialize() {
        createHintButton();
        countdownBar.setVisible(false);


        pane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Scene scene = pane.getScene();
                scene.setOnKeyPressed(this::keyPressed);
            }
        });
        pane.setFocusTraversable(true);
        pane.requestFocus();

        for (int i = 0; i < MAX_GUESSES; i++) {
            HBox wordBox = new HBox();
            wordBox.setAlignment(Pos.CENTER);
            wordBox.setPrefHeight(55);
            wordBox.setSpacing(5);
            List<Label> row = new ArrayList<>();
            for (int j = 0; j < goalWord.length(); j++) {
                Label letterLabel = new Label();
                letterLabel.setAlignment(Pos.CENTER);
                letterLabel.setPrefSize(55, 55);
                letterLabel.setMaxSize(55, 55);
                letterLabel.setFont(new Font("System Bold", 32));
                letterLabel.setTextFill(Color.WHITE);
                letterLabel.setStyle("-fx-border-color: #323234; -fx-border-width: 2;");
                row.add(letterLabel);
                wordBox.getChildren().add(letterLabel);
            }
            letterLabels.add(row);
            words.getChildren().add(wordBox);
            wordBoxes[i] = wordBox;
        }

        String[][] keyTexts = {{"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"}, {"A", "S", "D", "F", "G", "H", "J", "K", "L"}, {"ENTER", "Z", "X", "C", "V", "B", "N", "M", "⌫"}};
        for (int i = 0; i < keyTexts.length; i++) {
            HBox keyBox = new HBox();
            keyBox.setAlignment(Pos.CENTER);
            keyBox.setPrefHeight(50);
            keyBox.setSpacing(4);

            for (int j = 0; j < keyTexts[i].length; j++) {
                String key = keyTexts[i][j];
                Label keyLabel = new Label();
                keyLabel.setAlignment(Pos.CENTER);
                keyLabel.setText(key);
                keyLabel.setFont(new Font("System Bold", 10));
                keyLabel.setTextFill(Color.WHITE);
                keyLabel.setStyle("-fx-background-color: #808586; -fx-border-radius: 5; -fx-background-radius: 5;");
                if (i == 2 && (j == 0 || j == 8)) {
                    keyLabel.setPrefSize(50, 50);
                    keyLabel.setMaxSize(50, 50);
                    if (j == 0) {
                        keyLabel.setOnMousePressed((e) -> {
                            enter();
                            logKeyPress("ENTER");
                        });
                    } else {
                        keyLabel.setOnMousePressed((e) -> {
                            backspace();
                            logKeyPress("BACKSPACE");
                        });
                    }
                } else {
                    keyLabel.setPrefSize(33, 50);
                    keyLabel.setMaxSize(33, 50);
                    keyLabel.setOnMousePressed((e) -> {
                        enterCharacter(key);
                        logKeyPress(key);
                    });
                }
                keyLabels[i][j] = keyLabel;
                keyBox.getChildren().add(keyLabel);
            }
            keys.getChildren().add(keyBox);
        }
        hintButton.setVisible(true);
        updateHintButton();
    }


    private void keyPressed(KeyEvent e) {
        if (e.getCode().isLetterKey()) {
            enterCharacter(e.getCode().getChar());
            logKeyPress(e.getCode().getChar());
        } else if (e.getCode().toString().equals("ENTER")) {
            enter();
            logKeyPress("ENTER");
        } else if (e.getCode().toString().equals("BACK_SPACE")) {
            backspace();
            logKeyPress("BACKSPACE");
        }
    }

    private void logKeyPress(String keyInput) {
        if (WordleApp.adminLogging != null) {
            WordleApp.adminLogging.log(keyInput);
        }
    }

    private void enterCharacter(String key) {
        if (currentWord.length() < goalWord.length() && guessCount < MAX_GUESSES) {
            letterLabels.get(guessCount).get(currentWord.length()).setText(key);
            letterLabels.get(guessCount).get(currentWord.length()).setStyle("-fx-border-color: #545456; -fx-border-width: 2;");
            currentWord += key;
        }
    }

    private void enter() {
        if (currentWord.length() == goalWord.length()) {
            String info = WordleApp.checkWord(currentWord.toLowerCase());
            if (!info.isEmpty()) {
                if (info.equals("excluded")) {
                    exclusionPopUp();
                } else {
                    isFlipping = true;
                    flipLabel(letterLabels.get(guessCount), info, 0);
                    guessedWords.add(currentWord.toLowerCase());
                    guessCount++;
                    guiController.updateView(currentWord, info);
                    currentWord = "";
                }
            } else {
                shakeNode(wordBoxes[guessCount]);
            }
        }
    }

    private void exclusionPopUp() {
        shakeNode(wordBoxes[guessCount]);
        Label messageLabel = new Label("Word is excluded!");
        messageLabel.setTextFill(Color.RED);
        messageLabel.setStyle("-fx-font-weight: bold;");
        Popup popup = new Popup();
        VBox content = new VBox(messageLabel);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #3d3d3d; -fx-background-radius: 5;");
        popup.getContent().add(content);

        // Show popup and auto-hide after 2 seconds
        popup.show(pane.getScene().getWindow());

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }

    private void backspace() {
        if (!currentWord.isEmpty()) {
            letterLabels.get(guessCount).get(currentWord.length() - 1).setText("");
            letterLabels.get(guessCount).get(currentWord.length() - 1).setStyle("-fx-border-color: #323234; -fx-border-width: 2;");
            currentWord = currentWord.substring(0, currentWord.length() - 1);
        }
    }

    public void isGameOver() {
        if (guessedWords != null && ((guessedWords.contains(goalWord)) || guessCount == MAX_GUESSES) || settingsController.gameOver()) {
            boolean won = guessedWords.contains(goalWord);
            isGameWon = won;

            WordleApp.addGuessCount(guessCount);
            WordleApp.adminLogging.log("Game Over, Game Won? : " + isGameWon);
            if (WordleApp.isLoggedIn() && WordleApp.currentAccount != null) {
                Account currentAccount = WordleApp.currentAccount;
                currentAccount.reportTime(settingsController.recordTime());
                currentAccount.recordGameResult(won);

                WordleApp.save();

                int time = currentAccount.getTime();
                leaderboardController.getLeaderboard().addToLeaderboard(time);
                leaderboardController.updateLeaderboard();

                System.out.println("Game ended: " + (won ? "WON" : "LOST") +
                        " - Total games: " + currentAccount.getTotalGames() +
                        ", Wins: " + currentAccount.getGamesWon() +
                        ", Losses: " + currentAccount.getGamesLost());
            }

            if (playerStatsController != null) {
                playerStatsController.updateStats();
            }
            if (adminStatsController != null) {
                adminStatsController.refreshAllStats();
            }

            showEndGameWindow();
        }
    }
    public void showEndGameWindow() {
        endGameStage.setWidth(400);
        endGameStage.setHeight(400);
        endGameStage.setTitle("Game Over");

        Label message = new Label((guessedWords.contains(goalWord)) ? "You Win!" : "You Lost!");
        message.getStyleClass().add("game-over-text");

        Label guessInfo = new Label("You used " + guessCount + " guesses!");
        Label timeTaken = new Label((guessedWords.contains(goalWord)) ? "It took you " +
                settingsController.recordTime() + " seconds!" : "You used all the time!");
        timeTaken.setVisible(false);

        guessInfo.getStyleClass().add("guess-info");

        Button restartButton = new Button("Restart Game");
        restartButton.getStyleClass().add("restart-button");
        restartButton.setOnAction(e -> {
            System.out.println("Restarting");
            endGameStage.close();
            if (!isFlipping) restartGame();
        });

        Button closeButton = new Button("Exit Game");
        closeButton.getStyleClass().add("exit-button");
        closeButton.setOnAction(e -> {
            closeGame();
            endGameStage.close();
            Platform.exit();
        });

        Button statsButton = new Button("Player Stats");
        statsButton.getStyleClass().add("stats-button");
        statsButton.setOnAction(e -> {
            if (!isFlipping) showPlayerStats();
        });

        VBox layout = new VBox(20, message, guessInfo, timeTaken, new Label("Word was: " + goalWord), restartButton, statsButton, closeButton);
        if (WordleApp.isLoggedIn() && settingsController.getGamemode() != 3) {
            timeTaken.setVisible(true);
        }
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("end-game-layout");

        Scene scene = new Scene(layout, 350, 250);
        scene.getStylesheets().add(getClass().getResource("test.css").toExternalForm());

        endGameStage.setScene(scene);
        endGameStage.show();
    }
    private void showPlayerStats() {
        if (WordleApp.isAdmin()) {
            if (adminStatsController != null) {
                this.mainStage.setScene(this.adminStatsScene);
                adminStatsController.refreshAllStats();
            }
            this.mainStage.setScene(this.adminStatsScene);
        } else {
            if (playerStatsController != null) {
                this.mainStage.setScene(this.playerStatsScene);
                playerStatsController.updateStats();
            }
        }
        endGameStage.close();
        restartGame();
    }

    public void restartGame() {
        guessCount = 0;
        currentWord = "";
        if (guessedWords != null) {
            guessedWords.clear();
        }

        for (int i = 0; i < MAX_GUESSES; i++) {
            for (int j = 0; j < goalWord.length(); j++) {
                letterLabels.get(i).get(j).setText("");
                letterLabels.get(i).get(j).setStyle("-fx-border-color: #323234; -fx-border-width: 2;");
            }
        }

        goalWord = WordleApp.changeGoalWord();

        updateLabelLength();

        guiController.reset();

        remainingHints = 3;
        hintButton.setDisable(false);
        updateHintButton();
        logKeyPress("Restart");
        runSettings();
        settingsController.restartBar(settingsController.getGamemode());

    }

    private void updateLabelLength() {
        int difference = letterLabels.getFirst().size() - goalWord.length();
        if (difference < 0) {
            for (int i = 0; i < MAX_GUESSES; i++) {
                for (int j = 0; j < Math.abs(difference); j++) {
                    Label letterLabel = new Label();
                    letterLabel.setAlignment(Pos.CENTER);
                    letterLabel.setPrefSize(55, 55);
                    letterLabel.setMaxSize(55, 55);
                    letterLabel.setFont(new Font("System Bold", 32));
                    letterLabel.setTextFill(Color.WHITE);
                    letterLabel.setStyle("-fx-border-color: #323234; -fx-border-width: 2;");
                    letterLabels.get(i).add(letterLabel);
                    wordBoxes[i].getChildren().add(letterLabel);
                }
            }
        } else if (difference > 0) {
            for (int i = 0; i < MAX_GUESSES; i++) {
                for (int j = 0; j < difference; j++) {
                    wordBoxes[i].getChildren().remove(letterLabels.get(i).removeLast());
                }
            }
        }
    }

    public EventHandler<WindowEvent> closeGame() {
        System.out.println("Closing!");
        logKeyPress("Closed Game");

        WordleApp.save();
        if (WordleApp.adminLogging != null) {
            WordleApp.adminLogging.close();
        }
        return null;
    }

    public void updateAdminUI() {
        if (WordleApp.isAdmin()) {
            adminLabel.setVisible(true);
            adminLabel.setManaged(true);
        } else {
            adminLabel.setVisible(false);
            adminLabel.setManaged(false);
        }
    }

    private void getHint() {
        if (currentWord.length() != goalWord.length() && remainingHints > 0) {
            String hintLetter = "";
            hintLetter = goalWord.substring(currentWord.length(), currentWord.length() + 1);
            enterCharacter(hintLetter.toUpperCase());

            remainingHints--;

            updateHintButton();
        }
    }

    public void hintPressed(ActionEvent actionEvent) {
        getHint();
        logKeyPress("Hint used");

        if (remainingHints <= 0) {
            hintButton.setDisable(true);
        }
    }

    private void createHintButton() {
        lightbulb.setStyle("-fx-opacity: 1.0");
        lightbulb.setFitWidth(30);
        lightbulb.setFitHeight(30);

        updateHintButton();
    }



    private void updateHintButton() {
        if (remainingHints > 0) {
            lightbulb.setStyle("-fx-opacity: 1.0");
            hintButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

            Label countLabel = new Label(String.valueOf(remainingHints));
            countLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: #538d4e; -fx-padding: 2px 6px; -fx-background-radius: 10;");

            StackPane buttonContent = new StackPane();
            buttonContent.getChildren().addAll(lightbulb, countLabel);
            StackPane.setAlignment(countLabel, Pos.TOP_RIGHT);

            hintButton.setGraphic(buttonContent);
        } else {
            lightbulb.setStyle("-fx-opacity: 0.3");
            hintButton.setGraphic(lightbulb);
        }
    }


    public void setAdminStatsController(AdminStatsController controller) {
        this.adminStatsController = controller;
    }

    public void setPlayerStatsController(StatsController controller) {
        this.playerStatsController = controller;
    }

    public void setSettingsController(SettingsController controller) {this.settingsController = controller;}

    public void setAdminSettingsController(AdminSettingsController controller) {
        this.adminSettingsController = controller;
    }

    public void setStageScene(Stage stage, Scene adminScene, Scene playerScene, Scene adminSettingsScene, Scene SettingScene) {
        this.mainStage = stage;
        this.adminStatsScene = adminScene;
        this.playerStatsScene = playerScene;
        this.settingsScene = settingsScene;
        this.adminSettingsScene = adminSettingsScene;

        this.stats.setOnMouseClicked(e -> {
            if (WordleApp.isAdmin()) {
                if (adminStatsController != null) {
                    adminStatsController.refreshAllStats();
                }
                this.mainStage.setScene(this.adminStatsScene);
            } else {
                if (playerStatsController != null) {
                    playerStatsController.updateStats();
                }
                this.mainStage.setScene(this.playerStatsScene);
            }
        });

        this.settings.setOnMouseClicked(e -> {
            if (WordleApp.isAdmin()) {
                if (adminSettingsController != null) {
                    adminSettingsController.updateBox();
                }
                this.mainStage.setScene(this.adminSettingsScene);
            }
        });
    }

    public void shakeNode(Node node) {
        double originalX = words.getTranslateX();

        TranslateTransition moveLeft = new TranslateTransition(Duration.millis(50), node);
        moveLeft.setToX(originalX - 10);
        moveLeft.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition moveRight = new TranslateTransition(Duration.millis(50), node);
        moveRight.setToX(originalX + 10);
        moveRight.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition moveCenter = new TranslateTransition(Duration.millis(50), node);
        moveCenter.setToX(originalX);
        moveCenter.setInterpolator(Interpolator.EASE_BOTH);

        SequentialTransition shake = new SequentialTransition(moveLeft, moveRight, moveCenter);
        shake.setCycleCount(3);
        shake.play();
    }

    private void flipLabel(List<Label> labels, String info, int index) {
        Label label = labels.get(index);
        ScaleTransition shrink = new ScaleTransition(Duration.millis(750 / goalWord.length()), label);
        shrink.setToY(0);
        shrink.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        shrink.setOnFinished(e -> {
            if (index < labels.size() - 1) {
                flipLabel(labels, info, index + 1);
            }
            if (index < labels.size()) {
                switch (info.charAt(index)) {
                    case 'x' ->
                            labels.get(index).setStyle("-fx-border-color: #323234; -fx-border-width: 2; -fx-background-color: #323234;");
                    case 'y' ->
                            labels.get(index).setStyle("-fx-border-color: #b39f39; -fx-border-width: 2; -fx-background-color: #b39f39;");
                    case 'g' ->
                            labels.get(index).setStyle("-fx-border-color: #538d4c; -fx-border-width: 2; -fx-background-color: #538d4c;");
                }
            }
        });

        ScaleTransition expand = new ScaleTransition(Duration.millis(750 / goalWord.length()), label);
        expand.setToY(1);
        expand.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        if (index == labels.size() - 1) {
            expand.setOnFinished(e -> {
                isFlipping = false;
                isGameOver();
            });
        }

        SequentialTransition flip = new SequentialTransition(shrink, expand);
        flip.play();
    }

    public void settingPressed(ActionEvent actionEvent) {
        openGamemodeWindow();
    }
    private void openGamemodeWindow() {
            this.mainStage.setScene(this.settingsScene);
    }

    public void runSettings() {
        settingsController.setWordleController(this);
        settingsController.setCountdownBar(countdownBar);
    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }
}
