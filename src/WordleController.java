import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.geometry.Pos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import players.Person;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
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
    @FXML
    private GridPane guess1;
    @FXML
    private GridPane guess2;
    @FXML
    private GridPane guess3;
    @FXML
    private GridPane guess4;
    @FXML
    private GridPane guess5;
    @FXML
    private GridPane guess6;
    @FXML
    private TextField guessTextField;
    private final WordleApp app;
    private final WordleDictionary dictionary;
    private final String goalWord;
    private List<Character> guessedLetters;
    private List<String> guessedWords;
    private Person person;
    private GridPane[] guessRows;
    private Label[][] letterLabels;
    private int currentRow;

    public WordleController() {
        app = new WordleApp();
        dictionary = new WordleDictionary();
        goalWord = app.getGoalWord();
    }
	@FXML
	public GridPane topKeyboardPane;
	@FXML
	public GridPane middleKeyboardPane;
	@FXML
	public GridPane bottomKeyboardPane;
	private WordleApp app;
	private List<Character> guessedLetters;
	private List<String> guessedWords;
	private Person person;
	private List<Label> labels;
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
		labels = new ArrayList<>();
	}

    @FXML
    public void initialize() {
        guessRows = new GridPane[] {guess1, guess2, guess3, guess4, guess5, guess6};
        handleTyping();
    }

    private void handleTyping() {
        letterLabels = new Label[guessRows.length][5];
        for (int rowIndex = 0; rowIndex < guessRows.length; rowIndex++) {
            GridPane row = guessRows[rowIndex];
            for (int col = 0; col < 5; col++) {
                final int currentRowIndex = rowIndex;
                final int[] currentCol = {col};
                Label letterLabel = new Label();
                letterLabel.getStyleClass().add("letter-cell");
                letterLabel.setMinSize(78, 67);
                letterLabel.setFont(new Font("Wingding", 40));
                letterLabel.setAlignment(Pos.CENTER);
                letterLabel.setFocusTraversable(true);

                letterLabel.setOnKeyPressed(event -> {
                    // Handle Backspace
                    if (event.getCode() == KeyCode.BACK_SPACE) {
                        if (!letterLabel.getText().isEmpty()) {
                            // Clear the current cell if it has text
                            letterLabel.setText("");
                        } else if (currentCol[0] > 0) {
                            // If already empty and not the first cell, move focus back
                            Label prevLabel = letterLabels[currentRowIndex][currentCol[0] - 1];
                            prevLabel.setText(""); // clear previous cell
                            prevLabel.requestFocus();
                        }
                        event.consume();
                    } else if (event.getCode() == KeyCode.ENTER) {
                        // When ENTER is pressed, gather the word from the entire row.
                        String enteredWord = getWordFromRow(currentRowIndex);
                        if (enteredWord.length() == 5) {
                            // Validate the word
                            if (dictionary.isValidWord(enteredWord.toLowerCase())) {
                                show("Valid word: " + enteredWord);

                                // Optionally, compare against the mystery word here...
                                // Lock the current row and move to the next row:
                                Feedback(enteredWord, currentRowIndex);
                            } else {
                                show("Invalid word: " + enteredWord);
                                // Optionally clear the row or provide feedback so the user can try again.
                            }
                        } else {
                            show("Incomplete word. Please fill all cells.");
                        }
                        event.consume();
                    } else {
                        String text = event.getText().toUpperCase();
                        if (text.matches("[A-Z]")) {
                            letterLabel.setText(text);
                            if (currentCol[0] < 4) {
                                letterLabels[currentRowIndex][currentCol[0] + 1].requestFocus();
                            }
                            event.consume();
                        } else {
                            show("WE ONLY TAKE LETTERS BITCH");
                        }
                    }
                });
                row.add(letterLabel, col, 0);
                letterLabels[rowIndex][col] = letterLabel;
            }
        }
    }

    private void Feedback(String word, int rowIndex) {
        if (word.length() == 5) {
            String ret = app.checkWord(word.toLowerCase());
            if (!ret.isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    // Update the label's text in the given row
                    letterLabels[rowIndex][i].setText(word.substring(i, i + 1).toUpperCase());
                    // Update the style based on the checkWord return code.
                    switch (ret.charAt(i)) {
                        case 'x' -> letterLabels[rowIndex][i].setStyle("-fx-background-color: gray;");
                        case 'y' -> letterLabels[rowIndex][i].setStyle("-fx-background-color: yellow;");
                        case 'g' -> letterLabels[rowIndex][i].setStyle("-fx-background-color: green;");
                    }
                }
            }
        }
    }


    private String getWordFromRow(int rowIndex) {
        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < 5; col++) {
            sb.append(letterLabels[rowIndex][col].getText());
        }
        return sb.toString();
    }
	@FXML
	public void initialize(){
		TextField tf = new TextField();

		HBox hbox = new HBox();
		box.getChildren().addAll(hbox);
		for (int i = 0; i < 5; i++) {
			Label l = new Label();
			l.setPrefSize(40, 40);
			l.setAlignment(Pos.CENTER);
			l.setStyle("-fx-background-color: lightgray;");
			labels.add(l);
			hbox.getChildren().add(l);
		}

		tf.setOnAction(event -> {
			enterWord(tf.getText());
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


    public void changeDictionary() {

    }

    public void changeWordLength() {

    }

	public void endGame(){
		System.out.println("Game Over!");
		Platform.exit();
	}
    public void endGame() {

    }

    /**
     * @param word
     */
    public void enterWord(String word) {
        if (word.length() != 5) {
            return;
        }

        if (currentRow >= guessRows.length) {
            return;
        }
        for (int col = 0; col < 5; col++) {
            char guessedChar = word.charAt(col);
            Label label = letterLabels[currentRow][col];
            label.setText(String.valueOf(guessedChar));
        }
        currentRow++;
        guessTextField.clear();
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

//	private void updateActiveRow(String text) {
//		if (currentRow >= guessRows.length) {
//			return;
//		}
//		GridPane activeRow = guessRows[currentRow];
//		ObservableList<Node> children = activeRow.getChildren();
//		for (int i = 0; i < 5; i++) {
//			Label label = (Label) children.get(i);
//			if (i < text.length()) {
//				label.setText(String.valueOf(text.charAt(i)));
//			} else {
//				label.setText("");
//			}
//		}
//	}
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
			String ret = app.checkWord(word);
			text.setText(!ret.isEmpty()  ? "Word!" : "Not word!");
			if (!ret.isEmpty()) {
				for (int i = 0; i < 5; i++) {
					labels.get(i).setText(word.substring(i, i + 1).toUpperCase());
					switch (ret.charAt(i)) {
						case 'x' -> labels.get(i).setStyle("-fx-background-color: gray;");
						case 'y' -> labels.get(i).setStyle("-fx-background-color: yellow;");
						case 'g' -> labels.get(i).setStyle("-fx-background-color: green;");
					}
				}
			}
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


    public void getAllLettersGuessed() {

    }

    public void getAllWordsGuessed() {

    }

    public void getAverageGuesses() {

    }

    public void getMostCommonGuess() {

    }

    /**
     * @param username
     * @param password
     */
    public void login(String username, String password) {

    }

    public void showHint() {

    }

    /**
     * @param filePath
     */
    public void testWithFile(String filePath) {

    }

    private void openPlayerStatsController(ActionEvent event) {
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

    public void show(String words) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mystery Word Letters");
        alert.setContentText(words);
        alert.showAndWait();
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

    private void lockCurrentRowAndAdvance() {
        guessRows[currentRow].setDisable(true);
        currentRow++;
        if (currentRow < guessRows.length) {
            // Enable the new current row and set focus to its first cell.
            guessRows[currentRow].setDisable(false);
            letterLabels[currentRow][0].requestFocus();
        } else {
            show("Game over!");
        }
    }
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
