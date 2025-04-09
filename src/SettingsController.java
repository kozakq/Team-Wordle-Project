import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SettingsController {

    public Label settingsPrompt;
    public Button unlimitedButton;
    public Button mediumButton;
    public Button hardButton;
    private Scene gameScene;
    private Stage mainStage;
    private CountdownTimer countdownTimer = new CountdownTimer();

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void initialize() {
        unlimitedButton.setOnAction(e -> switchToGame());

        mediumButton.setOnAction(e -> {

        });

        hardButton.setOnAction(e -> {

        });
    }
    private void switchToGame() {
        mainStage.setScene(gameScene);
    }

    public void unlimitedPressed(ActionEvent actionEvent) {
        switchToGame();
    }

    public void mediumPressed(ActionEvent actionEvent) {

    }

    public void hardPressed(ActionEvent actionEvent) {

    }
}
