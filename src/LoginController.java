/*
 * Course: SWE2410-121
 * Fall 2024-2025
 * File header contains class LoginController
 * Name: childressg
 * Created 3/11/2025
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Course SWE2410-121
 * Fall 2024-2025
 * Class LoginController Purpose:
 *
 * @author childressg
 * @version created on 3/11/2025 3:30 PM
 */
public class LoginController {
    private Scene gameScene;
    private Stage mainStage;

    @FXML
    private Pane pane;

    @FXML
    private Label loginPrompt;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createButton;

    @FXML
    private Button guestButton;
    private PlayersStatsController playersStatsController;

    public void setPlayersStatsController(PlayersStatsController playersStatsController) {
        this.playersStatsController = playersStatsController;
        System.out.println("PlayersStatsController has been set in LoginController.");
    }


    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void initialize() {
        guestButton.setOnAction(e -> switchToGame());

        createButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if(WordleApp.createAccount(username, password)) {
                switchToGame();
            } else {
                loginPrompt.setText("Invalid Username or Password");
                loginPrompt.setTextFill(Color.RED);
            }
        });

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (WordleApp.login(username, password)) {
                System.out.println("Login successful. Switching to game scene.");
                mainStage.setScene(gameScene);

                FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("gui/stats.fxml"));
                try {
                    Scene statsScene = new Scene(statsLoader.load());
                    StatsController statsController = statsLoader.getController();
                    statsController.setPlayersStatsController(Launcher.getPlayersStatsController());
                    statsController.updateStats();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                loginPrompt.setText("Invalid Username or Password");
                loginPrompt.setTextFill(Color.RED);
            }
        });


    }

    private void switchToGame() {
        mainStage.setScene(gameScene);
    }
}
