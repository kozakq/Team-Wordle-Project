import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import players.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.SystemColor.text;

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
	@FXML
	public GridPane topKeyboardPane;
	@FXML
	public GridPane middleKeyboardPane;
	@FXML
	public GridPane bottomKeyboardPane;
	private WordleApp app;
	private String goalWord;
	private List<Character> guessedLetters;
	private List<String> guessedWords;
	private Person person;
	private int guessCount;
	private static final int MAX_GUESSES = 6;

	@FXML
	GridPane guess1;
	@FXML
	GridPane guess2;
	@FXML
	GridPane guess3;
	@FXML
	GridPane guess4;
	@FXML
	GridPane guess5;
	VBox box;
	@FXML
	Label text;
	@FXML
	Label GuessLabel;


	GridPane guess6;
	@FXML
	TextField guessTextField;
	private GridPane[] guessRows;
	private int currentRow;
	public WordleController(){
		app = new WordleApp();
		goalWord = app.getGoalWord();
		this.guessedWords = new ArrayList<>();
	}

	@FXML
	public void initialize(){
		guessRows = new GridPane[]{guess1, guess2, guess3, guess4, guess5,guess6};
		for (GridPane row : guessRows) {
			for (int col = 0; col < 5; col++) {
				Label letterLabel = new Label();
				letterLabel.getStyleClass().add("letter-cell");
				row.add(letterLabel, col, 0);
			}
		}
		guessTextField.textProperty().addListener((observable, oldText, newText) -> {
			updateActiveRow(newText);
		});
	}
	public void isGameOver() {
		if (guessedWords != null && (guessedWords.contains(goalWord)) || guessCount == MAX_GUESSES) {
			showEndGameWindow();
		}
	}

	public void updateguessLabel() {
		GuessLabel.setText(String.valueOf(guessCount) + "/6");
		System.out.println(guessedWords);
		isGameOver();
	}


	public void changeDictionary(){

    }

	public void changeWordLength(){

	}

	public void endGame(){
		System.out.println("Game Over!");
		Platform.exit();
	}

	/**
	 *
	 * @param word
	 */
	public void enterWord(String word) {
		if (word.length() != 5) {
			return;
		}

		if (currentRow >= guessRows.length) {
			return;
		}
		GridPane activeRow = guessRows[currentRow];
		ObservableList<Node> children = activeRow.getChildren();

//		for (int col = 0; col < word.length(); col++) {
//			char guessedChar = word.charAt(col);
//			Label label = (Label) children.get(col);
//			label.setText(String.valueOf(guessedChar));
//		}
		highlightLabel(topKeyboardPane, word);
		highlightLabel(middleKeyboardPane, word);
		highlightLabel(bottomKeyboardPane, word);
		currentRow++;
		guessTextField.clear();
	}

	private void updateActiveRow(String text) {
		if (currentRow >= guessRows.length) {
			return;
		}
		GridPane activeRow = guessRows[currentRow];
		ObservableList<Node> children = activeRow.getChildren();
		for (int i = 0; i < 5; i++) {
			Label label = (Label) children.get(i);
			if (i < text.length()) {
				label.setText(String.valueOf(text.charAt(i)));
			} else {
				label.setText("");
			}
	public void enterWord(String word){
		if (word.length() == 5) {
			boolean isValid = app.checkWord(word);
			text.setText(isValid ? "Word!" : "Not word!");
			if (isValid) {
				guessedWords.add(word);
				guessCount++;
				updateguessLabel();
			}
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
	@FXML
	public void show(String words) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Mystery Word Letters");
		alert.setContentText(words);
		alert.showAndWait();
	}
	private Label getLabelFromGrid(GridPane gridPane, char letter) {
		for (Node node : gridPane.getChildren()) {
			if (node instanceof Label label) {
				if (label.getText().equalsIgnoreCase(String.valueOf(letter))) {
					return label;
				}
			}
		}
		return null;
	}
	public void showEndGameWindow() {
		Stage endGameStage = new Stage();
		endGameStage.initModality(Modality.WINDOW_MODAL);
		endGameStage.setTitle("Game Over");

		Label message = new Label("Game Over!");
		message.getStyleClass().add("game-over-text");

		Label guessInfo = new Label("It took you " + guessCount + " guesses!");
		guessInfo.getStyleClass().add("guess-info");

		Button restartButton = new Button("Restart Game");
		restartButton.getStyleClass().add("restart-button");
		restartButton.setOnAction(e -> restartGame(endGameStage));

	public void highlightLabel(GridPane gridPane, String guessedWord) {
		for (char letter: guessedWord.toCharArray()) {
			Label label = getLabelFromGrid(gridPane, letter);
			if (label != null) {
				label.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
			}
		}
	}

	public void guessEntered(ActionEvent actionEvent) {
		String guess = guessTextField.getText();
		enterWord(guess);
	}
		Button closeButton = new Button("Exit Game");
		closeButton.getStyleClass().add("exit-button");
		closeButton.setOnAction(e -> {
			endGameStage.close();
			Platform.exit();
		});

		VBox layout = new VBox(20, message, guessInfo, restartButton, closeButton);
		layout.setAlignment(Pos.CENTER);
		layout.getStyleClass().add("end-game-layout");

		Scene scene = new Scene(layout, 350, 250);

		scene.getStylesheets().add(getClass().getResource("test.css").toExternalForm());

		endGameStage.setScene(scene);
		endGameStage.show();
	}
	public void restartGame(Stage stage) {

		guessCount = 0;
		if (guessedWords != null) {
			guessedWords.clear();
		}
		GuessLabel.setText("0/6");
		stage.close();
	}
}