import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;




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
    private final WordleApp app;
    private final GUIController guiController;
    private String goalWord;
    private String currentWord;
    private final List<String> guessedWords;
    private final Label[][] letterLabels;
    private final Label[][] keyLabels;
    private int guessCount;
    private static final int MAX_GUESSES = 6;

    @FXML
    private VBox words;

    @FXML
    private VBox keys;

    @FXML
    private Pane pane;

    public WordleController() {
        app = new WordleApp();
        goalWord = app.getGoalWord();
        letterLabels = new Label[MAX_GUESSES][goalWord.length()];
        keyLabels = new Label[][]{{null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null}};
        guiController = new GUIController(keyLabels);
        currentWord = "";
        guessedWords = new ArrayList<>();
    }


    @FXML
    public void initialize() {
        pane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Scene scene = pane.getScene();
                scene.setOnKeyPressed(this::keyPressed);
            }
        });
        pane.setFocusTraversable(true);
        pane.requestFocus();

        for (int i = 0; i < MAX_GUESSES; i++) { // creating letter labels
            HBox wordBox = new HBox();
            wordBox.setAlignment(Pos.CENTER);
            wordBox.setPrefHeight(55);
            wordBox.setSpacing(5);

            for (int j = 0; j < goalWord.length(); j++) {
                Label letterLabel = new Label();
                letterLabel.setAlignment(Pos.CENTER);
                letterLabel.setPrefSize(55, 55);
                letterLabel.setMaxSize(55, 55);
                letterLabel.setFont(new Font("System Bold", 32));
                letterLabel.setTextFill(Color.WHITE);
                letterLabel.setStyle("-fx-border-color: #323234; -fx-border-width: 2;");
                letterLabels[i][j] = letterLabel;
                wordBox.getChildren().add(letterLabel);
            }
            words.getChildren().add(wordBox);
        }

        String[][] keyTexts = {{"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"}, {"A", "S", "D", "F", "G", "H", "J", "K", "L"}, {"ENTER", "Z", "X", "C", "V", "B", "N", "M", "⌫"}};
        for (int i = 0; i < keyTexts.length; i++) { // creating key labels
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
                        keyLabel.setOnMousePressed((e) -> enter());
                    } else {
                        keyLabel.setOnMousePressed((e) -> backspace());
                    }
                } else {
                    keyLabel.setPrefSize(33, 50);
                    keyLabel.setMaxSize(33, 50);
                    keyLabel.setOnMousePressed((e) -> enterCharacter(key));
                }
                keyLabels[i][j] = keyLabel;
                keyBox.getChildren().add(keyLabel);
            }
            keys.getChildren().add(keyBox);
        }
        String username = "gosha";
        String password = "passwordd";
        if(!app.login(username, password)) {
            app.createAccount(username, password);
        }
    }

    private void keyPressed(KeyEvent e) {
        if (e.getCode().isLetterKey()) {
            enterCharacter(e.getCode().getChar());
        } else if (e.getCode().toString().equals("ENTER")) {
            enter();
        } else if (e.getCode().toString().equals("BACK_SPACE")) {
            backspace();
        }
    }

    private void enterCharacter(String key) {
        if (currentWord.length() < goalWord.length() && guessCount < MAX_GUESSES) {
            letterLabels[guessCount][currentWord.length()].setText(key);
            letterLabels[guessCount][currentWord.length()].setStyle("-fx-border-color: #545456; -fx-border-width: 2;");
            currentWord += key;
        }
    }

    private void enter() {
        if (currentWord.length() == goalWord.length()) {
            String info = app.checkWord(currentWord.toLowerCase());
            if (!info.isEmpty()) {
                for (int i = 0; i < info.length(); i++) {
                    switch (info.charAt(i)) {
                        case 'x' -> letterLabels[guessCount][i].setStyle("-fx-border-color: #323234; -fx-border-width: 2; -fx-background-color: #323234;");
                        case 'y' -> letterLabels[guessCount][i].setStyle("-fx-border-color: #b39f39; -fx-border-width: 2; -fx-background-color: #b39f39;");
                        case 'g' ->letterLabels[guessCount][i].setStyle("-fx-border-color: #538d4c; -fx-border-width: 2; -fx-background-color: #538d4c;");
                    }
                }
                guessedWords.add(currentWord.toLowerCase());
                guessCount++;
                isGameOver();
                guiController.updateView(currentWord, info);
                currentWord = "";
            }
        }
    }

    private void backspace() {
        if (!currentWord.isEmpty()) {
            letterLabels[guessCount][currentWord.length() - 1].setText("");
            letterLabels[guessCount][currentWord.length() - 1].setStyle("-fx-border-color: #323234; -fx-border-width: 2;");
            currentWord = currentWord.substring(0, currentWord.length() - 1);
        }
    }

    public void isGameOver() {
        if (guessedWords != null && ((guessedWords.contains(goalWord)) || guessCount == MAX_GUESSES)) {
            app.addGuessCount(guessCount);
            showEndGameWindow();
        }
    }

    public void showEndGameWindow() {
        Stage endGameStage = new Stage();
        endGameStage.initModality(Modality.APPLICATION_MODAL);
        endGameStage.setTitle("Game Over");

        Label message = new Label((guessedWords.contains(goalWord)) ? "You Win!" : "Game Over!");
        message.getStyleClass().add("game-over-text");

        Label guessInfo = new Label("It took you " + guessCount + " guesses!");
        guessInfo.getStyleClass().add("guess-info");

        Button restartButton = new Button("Restart Game");
        restartButton.getStyleClass().add("restart-button");
        restartButton.setOnAction(e -> restartGame(endGameStage));

        Button closeButton = new Button("Exit Game");
        closeButton.getStyleClass().add("exit-button");
        closeButton.setOnAction(e -> {
            closeGame();
            endGameStage.close();
            Platform.exit();
        });

        VBox layout = new VBox(20, message, guessInfo, new Label("Word was: " + goalWord),restartButton, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("end-game-layout");

        Scene scene = new Scene(layout, 350, 250);

        scene.getStylesheets().add(getClass().getResource("test.css").toExternalForm());

        endGameStage.setScene(scene);
        endGameStage.show();
    }

    public void restartGame(Stage stage) {
        guessCount = 0;
        if (guessedWords != null) {
            guessedWords.clear();
        }

        for (int i = 0; i < MAX_GUESSES; i++) {
            for (int j = 0; j < goalWord.length(); j++) {
                letterLabels[i][j].setText("");
                letterLabels[i][j].setStyle("-fx-border-color: #323234; -fx-border-width: 2;");
            }
        }

        goalWord = app.changeGoalWord();
        guiController.reset();
        stage.close();
    }

    public EventHandler<WindowEvent> closeGame() {
        System.out.println("Closing!");
        app.save();
        return null;
    }
}
