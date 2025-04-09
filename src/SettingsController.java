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
    private int timeLeft = 0;
    private int gamemode;
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
            timeline.stop();
            progressBar.setVisible(false);
            switchToGame();
        });

        mediumButton.setOnAction(e -> {
            gamemode = 0;
            restartBar(gamemode);
        });

        hardButton.setOnAction(e -> {
            gamemode = 1;
            restartBar(gamemode);
        });
    }

    public boolean gameOver() {
        return timeLeft == 0;
    }
    public void restartBar(int gamemode) {
        if (gamemode == 0) {
            timeline.stop();
            progressBar.setVisible(true);
            timeLeft = THREE_MINUTES;
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), y -> {
                timeLeft--;
                progressBar.setProgress((double) timeLeft / THREE_MINUTES);
                if (timeLeft <= 0) {
                    //((Timeline)y.getSource()).stop();
                    wordleController.isGameOver();
                }
            }));
            timeline.setCycleCount(THREE_MINUTES);
            timeline.play();
            switchToGame();
        } else if (gamemode == 1) {
            timeline.stop();
            progressBar.setVisible(true);
            timeLeft = ONE_MINUTE;
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), y -> {
                timeLeft--;
                progressBar.setProgress((double) timeLeft / ONE_MINUTE);
                if (timeLeft <= 0) {
                    //((Timeline)y.getSource()).stop();
                    wordleController.isGameOver();
                }
            }));
            timeline.setCycleCount(ONE_MINUTE);
            timeline.play();
            switchToGame();
        }
    }

    public int getGamemode() {
        return gamemode;
    }
}
