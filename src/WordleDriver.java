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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class WordleDriver extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
				"gui/wordle.fxml")));
		stage.setTitle("");
		stage.setScene(new Scene(root));
		stage.show();
	}
}