import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Time;

public class SettingsController {

    public Label settingsPrompt;
    public Button unlimitedButton;
    public Button mediumButton;
    public Button hardButton;
    private Scene gameScene;
    private Stage mainStage;
    private ProgressBar progressBar;
    private WordleController wordleController;
    private static final int ONE_MINUTE = 60;
    private static final int THREE_MINUTES = 180;
    private int timeLeft = 100;
    private int gamemode = 3;
    private Timeline timeline = new Timeline();
    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void initialize() {
//
    }
    private void switchToGame() {
        mainStage.setScene(gameScene);
    }
    public void setCountdownBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    public void setWordleController(WordleController wordleController) {
        this.wordleController = wordleController;
    }
    public void setupButtons() {
        unlimitedButton.setOnAction(e -> {
            gamemode = 3;
            wordleController.restartGame(null);
        });

        mediumButton.setOnAction(e -> {
            gamemode = 0;
            wordleController.restartGame(null);
        });

        hardButton.setOnAction(e -> {
            gamemode = 1;
            wordleController.restartGame(null);
        });
    }

    public boolean gameOver() {
        return timeLeft == 0;
    }
    public void restartBar(int gamemode) {
        switch (gamemode) {
            case 0 -> updateProgress(THREE_MINUTES);
            case 1 -> updateProgress(ONE_MINUTE);
            case 3 -> {
                progressBar.setVisible(false);
                timeline.stop();
                switchToGame();
            }
        }
    }

    private void updateProgress(int time) {
        timeline.stop();
        progressBar.setVisible(true);
        timeLeft = time;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), y -> {
            timeLeft--;
            progressBar.setProgress((double) timeLeft / time);
            if (timeLeft <= 0) {
                //((Timeline)y.getSource()).stop();
                wordleController.isGameOver();
            }
        }));
        timeline.setCycleCount(time);
        timeline.play();
        switchToGame();
    }

    public int getGamemode() {
        return gamemode;
    }
}
