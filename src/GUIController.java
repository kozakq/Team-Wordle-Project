import javafx.scene.control.Label;

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
public class GUIController {
    private final Label[][] keyLabels;

    public GUIController(Label[][] keyLabels) {
        this.keyLabels = keyLabels;
    }

    public void updateView(String word, String info) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Label l = getKeyLabel(c);
            if (l != null) {
                String color = getBackgroundColorFromStyle(l.getStyle());
                if (color != null) {
                    switch (info.charAt(i)) {
                        case 'x' -> {
                            if (!color.equals("#b39f39")) {
                                l.setStyle("-fx-background-color: #323234; -fx-border-radius: 5; -fx-background-radius: 5;");
                            }
                        }
                        case 'y' -> {
                            if (!color.equals("#538d4c")) {
                                l.setStyle("-fx-background-color: #b39f39; -fx-border-radius: 5; -fx-background-radius: 5;");
                            }
                        }
                        case 'g' -> l.setStyle("-fx-background-color: #538d4c; -fx-border-radius: 5; -fx-background-radius: 5;");
                    }
                }

            }
        }
    }

    public void reset() {
        for (int i = 0; i < keyLabels.length; i++) {
            for (int j = 0; j < keyLabels[i].length; j++) {
                keyLabels[i][j].setStyle("-fx-background-color: #808586; -fx-border-radius: 5; -fx-background-radius: 5;");
            }
        }
    }

    private Label getKeyLabel(char key) {
        for (int i = 0; i < keyLabels.length; i++) {
            for (int j = 0; j < keyLabels[i].length; j++) {
                if (keyLabels[i][j].getText().equals(String.valueOf(key))) {
                    return keyLabels[i][j];
                }
            }
        }
        return null;
    }

    private String getBackgroundColorFromStyle(String style) {
        String[] styles = style.split(";");
        for (String s : styles) {
            if (s.trim().startsWith("-fx-background-color")) {
                return s.split(":")[1].trim();
            }
        }
        return null;
    }
}