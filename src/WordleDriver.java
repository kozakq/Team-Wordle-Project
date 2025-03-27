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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WordleDriver extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        WordleApp.initialize();

        FXMLLoader gameFxmlLoader = new FXMLLoader(getClass().getResource("gui/wordle.fxml"));
        Scene gameScene = new Scene(gameFxmlLoader.load());
        WordleController gameController = gameFxmlLoader.getController();

        FXMLLoader loginFxmlLoader = new FXMLLoader(getClass().getResource("gui/login.fxml"));
        Scene loginScene = new Scene(loginFxmlLoader.load());
        LoginController loginController = loginFxmlLoader.getController();
        loginController.setWordleController(gameController);
        loginController.setGameScene(gameScene);
        loginController.setMainStage(stage);

        try {
            FXMLLoader statsFxmlLoader = new FXMLLoader(getClass().getResource("/gui/stats.fxml"));
            Scene statsScene = new Scene(statsFxmlLoader.load());
            StatsController statsController = statsFxmlLoader.getController();

            PlayersStatsController playersStatsController = new PlayersStatsController();
            Launcher.setPlayersStatsController(playersStatsController);
            WordleApp.setPlayersStatsController(playersStatsController);

            loginController.setPlayersStatsController(playersStatsController);
            statsController.setPlayersStatsController(playersStatsController);

            statsController.setGameScene(gameScene);
            statsController.setMainStage(stage);
            gameController.setStatsController(statsController);
            gameController.setStageScene(stage, statsScene);

            statsController.updateStats();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setScene(loginScene);
        stage.setOnCloseRequest(e -> gameController.closeGame());
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}