package org.example.ce216project;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;

import java.util.Collections;
import java.util.Optional;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.util.List;
import java.util.ArrayList;

public class AddGameController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField releaseYearField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField playtimeField;
    @FXML
    private TextField developerField;
    @FXML
    private TextField formatField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField languageField;
    @FXML
    private TextField platformsField;
    @FXML
    private TextField ratingField;
    @FXML
    private TextField translatorsField;
    @FXML
    private TextField tagsField;
    @FXML
    private TextField steamIdField;
    @FXML
    private TextField coverImageField;

    @FXML
    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🕹️ How to Add a Game");
        alert.setHeaderText("Need Help? Here’s what you can do:");
        alert.setContentText(
                "To enter a game fill in the blank boxes with the correct information.\n" +
                        "To cancel press the cancel button.\n" +
                        "To clear the boxes press the clear button.\n" +
                        "To save the information press the save button.\n"
        );
        alert.showAndWait();
    }

    public void onHelpButton() {
        showHelpDialog();
    }
    @FXML
    private void doCancelOperation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel??");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("All data that isn't saved will be lost");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
    public void onCancelButton(ActionEvent event){
        doCancelOperation(event);
    }
    @FXML
    private void doClearOperation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear??");
        alert.setHeaderText("Are you sure you want to clear?");
        alert.setContentText("All progress will be lost");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            titleField.clear();
            releaseYearField.clear();
            genreField.clear();
            playtimeField.clear();
            developerField.clear();
            formatField.clear();
            publisherField.clear();
            languageField.clear();
            platformsField.clear();
            ratingField.clear();
            translatorsField.clear();
            tagsField.clear();
            steamIdField.clear();
            coverImageField.clear();
        }
    }

    public void onClearButton(ActionEvent event) {
        doClearOperation(event);
    }
    @FXML
    private void doSaveOperation(ActionEvent event) {

        Game newGame = new Game();
        newGame.setTitle(getOrDefault(titleField.getText()));
        newGame.setDeveloper(getOrDefault(developerField.getText()));
        newGame.setPublisher(getOrDefault(publisherField.getText()));
        newGame.setFormat(getOrDefault(formatField.getText()));
        newGame.setSteamId(getOrDefault(steamIdField.getText()));
        newGame.setCoverImagePath(getOrDefault(coverImageField.getText()));
        newGame.setReleaseYear(parseIntegerOrDefault(releaseYearField.getText()));
        newGame.setPlaytime(parseDoubleOrDefault(playtimeField.getText()));
        newGame.setRating(parseDoubleOrDefault(ratingField.getText()));
        newGame.setGenres(parseListOrDefault(genreField.getText()));
        newGame.setPlatforms(parseListOrDefault(platformsField.getText()));
        newGame.setTranslators(parseListOrDefault(translatorsField.getText()));
        newGame.setLanguage(parseListOrDefault(languageField.getText()));
        newGame.setTags(parseListOrDefault(tagsField.getText()));

        if (JSONHandler.getLastLoadedFilePath() != null) {
            List<Game> existingGames = JSONHandler.readGamesFromJson(JSONHandler.getLastLoadedFilePath());
            if (existingGames != null) {
                existingGames.add(newGame);
                JSONHandler.writeGamesToJson(JSONHandler.getLastLoadedFilePath(), existingGames);
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText("Game saved successfully!");
        alert.showAndWait();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private String getOrDefault(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Not Specified";
        }
        return text.trim();
    }

    private int parseIntegerOrDefault(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleOrDefault(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private List<String> parseListOrDefault(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.singletonList("Not Specified");
        }
        String[] parts = text.split(",");
        List<String> list = new ArrayList<>();
        for (String part : parts) {
            list.add(part.trim());
        }
        return list;
    }
    public void onSaveButton(ActionEvent event){
        doSaveOperation(event);
    }

}
