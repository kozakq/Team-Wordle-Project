/*
 * Course: SWE2410-121
 * Fall 2024-2025
 * File header contains class AdminSettingsController
 * Name: childressg
 * Created 4/16/2025
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;
import java.util.Set;

/**
 * Course SWE2410-121
 * Fall 2024-2025
 * Class AdminSettingsController Purpose:
 *
 * @author childressg
 * @version created on 4/16/2025 11:03 AM
 */
public class AdminSettingsController {
    private Stage mainStage;
    private Scene gameScene;
    private WordleController wordleController;
    private int wordLength;

    @FXML
    private ChoiceBox<Integer> lengthSelector;

    @FXML
    private Button loadDictionary;

    @FXML
    private Label loadFeedback;

    @FXML
    private Button back;
    
    // Exclusion list UI elements
    @FXML
    private TextField exclusionField;
    
    @FXML
    private Button addExclusionBtn;
    
    @FXML
    private Button removeExclusionBtn;
    
    @FXML
    private ListView<String> exclusionListView;
    
    @FXML
    private Label exclusionFeedback;

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setWordleController(WordleController wordleController) { this.wordleController = wordleController; }

    public void initialize() {
        if (back != null) {
            back.setOnAction(event -> mainStage.setScene(gameScene));
        }

        if (lengthSelector != null) {
            if (lengthSelector.getItems().isEmpty()) {
                Set<Integer> lengths = WordleApp.getDictionary().getWordLengths();
                lengthSelector.getItems().addAll(lengths);
                lengthSelector.getSelectionModel().selectFirst();
            }

            lengthSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
                @Override
                public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                    if (t1 != null && t1 != wordLength) {
                        WordleApp.setWordLength(t1);
                        wordleController.restartGame();
                        wordLength = t1;
                    }
                }
            });
        }

        if (loadDictionary != null) {
            loadDictionary.setOnAction(event -> {
                // Create the FileChooser
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Dictionary");

                // Optional: set an initial directory
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

                // Optional: add file extension filters
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                );

                // Show the open file dialog
                File selectedFile = fileChooser.showOpenDialog(mainStage);

                if (selectedFile != null) {
                    if (WordleApp.getDictionary().changeFile(selectedFile)) {
                        loadFeedback.setTextFill(Color.GREEN);
                        loadFeedback.setText("Dictionary Loaded!");
                        updateBox();
                        wordleController.restartGame();
                    } else {
                        loadFeedback.setTextFill(Color.RED);
                        loadFeedback.setText("Error Loading Dictionary!");
                    }
                } else {
                    System.out.println("File selection cancelled.");
                }
            });
        }
        
        // Initialize exclusion list functionality
        initializeExclusionControls();
    }
    
    private void initializeExclusionControls() {
        // Update the list view with current exclusions
        updateExclusionListView();
        
        // Add word to exclusion list
        if (addExclusionBtn != null) {
            addExclusionBtn.setOnAction(event -> {
                String word = exclusionField.getText().trim();
                if (!word.isEmpty()) {
                    ExclusionManager.get().add(word);
                    exclusionField.clear();
                    updateExclusionListView();
                    exclusionFeedback.setTextFill(Color.GREEN);
                    exclusionFeedback.setText("'" + word + "' added to exclusion list");
                } else {
                    exclusionFeedback.setTextFill(Color.RED);
                    exclusionFeedback.setText("Please enter a word");
                }
            });
        }
        
        if (removeExclusionBtn != null) {
            removeExclusionBtn.setOnAction(event -> {
                String selected = exclusionListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    ExclusionManager.get().remove(selected);
                    updateExclusionListView();
                    exclusionFeedback.setTextFill(Color.GREEN);
                    exclusionFeedback.setText("'" + selected + "' removed from exclusion list");
                } else {
                    String word = exclusionField.getText().trim();
                    if (!word.isEmpty()) {
                        ExclusionManager.get().remove(word);
                        exclusionField.clear();
                        updateExclusionListView();
                        exclusionFeedback.setTextFill(Color.GREEN);
                        exclusionFeedback.setText("'" + word + "' removed from exclusion list");
                    } else {
                        exclusionFeedback.setTextFill(Color.RED);
                        exclusionFeedback.setText("Select a word from the list or enter a word to remove");
                    }
                }
            });
        }
    }
    
    private void updateExclusionListView() {
        if (exclusionListView != null) {
            Set<String> exclusions = ExclusionManager.get().list();
            exclusionListView.setItems(FXCollections.observableArrayList(exclusions));
        }
    }

    public void updateBox() {
        lengthSelector.getItems().clear();
        Set<Integer> lengths = WordleApp.getDictionary().getWordLengths();
        lengthSelector.getItems().addAll(lengths);
        lengthSelector.getSelectionModel().select(Integer.valueOf(WordleApp.getWordLength()));
    }
}


