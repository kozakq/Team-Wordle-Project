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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Course SWE2410-121
 * Fall 2024-2025
 * Class StatsController Purpose:
 *
 * @author childressg
 * @version created on 3/11/2025 10:25 PM
 */
public class StatsController {
    private Stage mainStage;
    private Scene gameScene;

    @FXML
    private Label stats;

    @FXML
    private Button back;

    public void initialize() {
        back.setOnAction(event -> mainStage.setScene(gameScene));
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void updateStats() {
        if (WordleApp.isLoggedIn()) {
            stats.setText(String.format("Average Guesses: %.2f",WordleApp.getAverageGuessess()));
        }
    }
}
