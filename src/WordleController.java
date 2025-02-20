import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import players.Person;

import java.io.IOException;
import java.util.List;

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
public class WordleController {

	private WordleApp app;
	private String goalWord;
	private List<Character> guessedLetters;
	private List<String> guessedWords;
	private Person person;

	@FXML
	VBox box;

	@FXML
	Label text;

	public WordleController(){
		app = new WordleApp();
		goalWord = app.getGoalWord();
	}

	@FXML
	public void initialize(){
		TextField tf = new TextField();
		tf.setOnAction(event -> {
			enterWord(tf.getText());
		});
		box.getChildren().add(tf);
	}

	public void changeDictionary(){

    }

	public void changeWordLength(){

	}

	public void endGame(){

	}

	/**
	 * 
	 * @param word
	 */
	public void enterWord(String word){
		if (word.length() == 5) {
			text.setText(app.checkWord(word) ? "Word!" : "Not word!");
		} else {
			text.setText("Not 5 letters!");
		}
	}

	public void getAllLettersGuessed(){

	}

	public void getAllWordsGuessed(){

	}

	public void getAverageGuesses(){

	}

	public void getMostCommonGuess(){

	}

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public void login(String username, String password){

	}

	public void showHint(){

	}

	/**
	 * 
	 * @param filePath
	 */
	public void testWithFile(String filePath){

	}

	private void openPlayerStatsController(ActionEvent event){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("KernelController.fxml"));
			Parent root = loader.load();
			PlayersStatsController playersStatsController = loader.getController();
			playersStatsController.setWordleController(this);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UTILITY);
			stage.setTitle("Stats");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			System.err.println("Error opening player stats controller" + e.getMessage());
		}
	}

}