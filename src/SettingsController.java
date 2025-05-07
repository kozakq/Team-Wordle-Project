import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SettingsController {

    private static final int ONE_MINUTE = 60;
    private static final int THREE_MINUTES = 180;
    public Label settingsPrompt;
    public Button unlimitedButton;
    public Button mediumButton;
    public Button hardButton;
    private Scene gameScene;
    private Stage mainStage;
    private ProgressBar progressBar;
    private WordleController wordleController;
    private int timeLeft = 100;
    private int time;
    private int gamemode = 3;
    private Timeline timeline = new Timeline();

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setWordleController(WordleController wordleController) {
        this.wordleController = wordleController;
    }

    public void initialize() {

    }

    private void switchToGame() {
        mainStage.setScene(gameScene);
    }

    public void setCountdownBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setupButtons() {
        unlimitedButton.setOnAction(e -> {
            gamemode = 3;
            wordleController.restartGame();
        });

        mediumButton.setOnAction(e -> {
            gamemode = 0;
            wordleController.restartGame();
        });

        hardButton.setOnAction(e -> {
            gamemode = 1;
            wordleController.restartGame();
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
        this.time = time;
        timeLeft = time;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), y -> {
            timeLeft--;
            progressBar.setProgress((double) timeLeft / time);
            if (timeLeft <= 0) {
                wordleController.isGameOver();
            }
        }));
        timeline.setCycleCount(time);
        timeline.play();
        switchToGame();
    }

    public int recordTime() {
        return time - timeLeft;
    }

    public int getGamemode() {
        return gamemode;
    }
    public void stopTime() {
        timeline.stop();
        if (progressBar != null) {
            progressBar.setVisible(false);
        }
    }

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }
}
