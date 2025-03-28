import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Map;

public class FrequencyReportSwitch extends Application {

    private TableView tableView;
    private ComboBox<String> reportTypeComboBox;
    private Map<String, Integer> wordFreqMap;
    private Map<Character, Integer> letterFreqMap;

    @Override
    public void start(Stage primaryStage) {
        // Load the file from resources using GlobalStats
        InputStream input = GlobalStats.class.getResourceAsStream("/data/wordle-official-1.txt");
        if (input == null) {
            System.err.println("Resource file not found!");
            return;
        }
        // Read file into a list of words using GlobalStats
        var wordStorage = GlobalStats.readFile(input);
        // Generate frequency maps
        wordFreqMap = GlobalStats.wordFreq(wordStorage);
        letterFreqMap = GlobalStats.letterFreq(wordStorage);

        // Set up ComboBox for switching report types
        reportTypeComboBox = new ComboBox<>();
        reportTypeComboBox.getItems().addAll("Word Frequency", "Letter Frequency");
        reportTypeComboBox.setValue("Word Frequency");
        reportTypeComboBox.setOnAction(e -> updateTable());

        // Create TableView to display frequency data
        tableView = new TableView<>();
        updateTable();  // initial table setup

        VBox root = new VBox(10, reportTypeComboBox, tableView);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Frequency Report Switcher");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateTable() {
        tableView.getColumns().clear();
        String selected = reportTypeComboBox.getValue();

        if ("Word Frequency".equals(selected)) {
            // Configure columns for word frequency report
            TableColumn<Map.Entry<String, Integer>, String> wordCol = new TableColumn<>("Word");
            wordCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getKey()));

            TableColumn<Map.Entry<String, Integer>, String> freqCol = new TableColumn<>("Frequency");
            freqCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getValue().toString()));

            tableView.getColumns().addAll(wordCol, freqCol);

            ObservableList<Map.Entry<String, Integer>> data =
                    FXCollections.observableArrayList(wordFreqMap.entrySet());
            tableView.setItems(data);

        } else if ("Letter Frequency".equals(selected)) {
            // Configure columns for letter frequency report
            TableColumn<Map.Entry<Character, Integer>, String> letterCol = new TableColumn<>("Letter");
            letterCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getKey().toString()));

            TableColumn<Map.Entry<Character, Integer>, String> freqCol = new TableColumn<>("Frequency");
            freqCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getValue().toString()));

            tableView.getColumns().addAll(letterCol, freqCol);

            ObservableList<Map.Entry<Character, Integer>> data =
                    FXCollections.observableArrayList(letterFreqMap.entrySet());
            tableView.setItems(data);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
