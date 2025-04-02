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
    public static void main(String[] args) {
        launch(args);
    }

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
        loginController.initialize();

        FXMLLoader adminStatsFxmlLoader = new FXMLLoader(getClass().getResource("gui/adminstatspanel.fxml"));
        Scene adminStatsScene = new Scene(adminStatsFxmlLoader.load());
        AdminStatsController adminStatsController = adminStatsFxmlLoader.getController();

        FXMLLoader playerStatsFxmlLoader = new FXMLLoader(getClass().getResource("gui/stats.fxml"));
        Scene playerStatsScene = new Scene(playerStatsFxmlLoader.load());
        StatsController playerStatsController = playerStatsFxmlLoader.getController();

        adminStatsController.setGameScene(gameScene);
        adminStatsController.setMainStage(stage);
        adminStatsController.initialize();

        playerStatsController.setGameScene(gameScene);
        playerStatsController.setMainStage(stage);
        playerStatsController.initialize();

        gameController.setAdminStatsController(adminStatsController);
        gameController.setPlayerStatsController(playerStatsController);
        gameController.setStageScene(stage, adminStatsScene, playerStatsScene);
        stage.setScene(loginScene);
        stage.setOnCloseRequest(e -> gameController.closeGame());
        stage.show();
    }
}