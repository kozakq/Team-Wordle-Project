import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

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
public class Launcher extends Application {

    private static PlayersStatsController playersStatsController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize PlayersStatsController before loading any scenes
        playersStatsController = new PlayersStatsController();
        Launcher.setPlayersStatsController(playersStatsController);
        WordleApp.setPlayersStatsController(playersStatsController);
        System.out.println("PlayersStatsController has been initialized and set.");

        // Load the Login scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setPlayersStatsController(playersStatsController);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        WordleDriver.main(args);
    }

    public static PlayersStatsController getPlayersStatsController() {
        if (playersStatsController == null) {
            System.out.println("Error: PlayersStatsController is not initialized. Returning null.");
        }
        return playersStatsController;
    }

    public static void setPlayersStatsController(PlayersStatsController controller) {
        if (controller == null) {
            System.out.println("Warning: Attempt to set PlayersStatsController to null.");
            return;
        }
        playersStatsController = controller;
        System.out.println("PlayersStatsController has been initialized in Launcher.");
    }
}