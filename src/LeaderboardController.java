import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LeaderboardController {


    private final Leaderboard leaderboard = new Leaderboard();
    public Button back;
    public VBox vBox;
    private Scene gameScene;
    private Stage mainStage;


    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public void initialize() {
        updateLeaderboard();
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
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void updateLeaderboard() {
        vBox.getChildren().clear();
        for (int i = 0; i < 10; i++) {
            if (i < leaderboard.getListLength()) {
                Label label = new Label("#" + (i + 1) + " " + leaderboard.getListValue(i) + " seconds");
                label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
                vBox.getChildren().add(label);
            }
        }
    }
}
