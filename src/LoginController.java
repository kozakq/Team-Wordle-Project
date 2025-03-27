/*
 * Course: SWE2410-121
 * Fall 2024-2025
 * File header contains class LoginController
 * Name: childressg
 * Created 3/11/2025
 */

import javafx.fxml.FXML;
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
    private WordleController wordleController;
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
    public void setWordleController(WordleController wordleController){
        this.wordleController = wordleController;
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
            if(WordleApp.login(username, password)) {
                switchToGame();
            } else {
                loginPrompt.setText("Invalid Username or Password");
                loginPrompt.setTextFill(Color.RED);
            }
        });
    }

    private void switchToGame() {
        if (wordleController != null){
        wordleController.updateAdminUI();
    }
        mainStage.setScene(gameScene);

    }
}
