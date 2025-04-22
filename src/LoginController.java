import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Course SWE2410-121
 * Fall 2024-2025
 * Class LoginController Purpose:
 * Allows user login or guest play, and mode selection (Normal / Hard).
 * <p>
 * Author: childressg
 * Version: created on 3/11/2025
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

    @FXML
    private RadioButton normalModeRadio;

    @FXML
    private RadioButton hardModeRadio;

    @FXML
    private ToggleGroup modeGroup;

    public void setWordleController(WordleController wordleController) {
        this.wordleController = wordleController;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void initialize() {
        modeGroup = new ToggleGroup();
        normalModeRadio.setToggleGroup(modeGroup);
        hardModeRadio.setToggleGroup(modeGroup);
        normalModeRadio.setSelected(true);

        guestButton.setOnAction(e -> switchToGame());

        createButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (WordleApp.createAccount(username, password)) {
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
                switchToGame();
            } else {
                loginPrompt.setText("Invalid Username or Password");
                loginPrompt.setTextFill(Color.RED);
            }
        });
    }

    private void switchToGame() {
        boolean isHardMode = hardModeRadio.isSelected();

        if (wordleController != null) {
            wordleController.setHardMode(isHardMode);
            wordleController.updateAdminUI();
        }
        mainStage.setScene(gameScene);
    }
}
