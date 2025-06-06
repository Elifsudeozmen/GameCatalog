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

    // Şu an üzerinde çalışılan veya düzenlenen oyun
    Game currentGame = null;

    // Formu mevcut oyun verisi ile doldurur (düzenleme için)
    public void setGame(Game game) {
        this.currentGame = game;
        if (game != null) {
            titleField.setText(game.getTitle());
            releaseYearField.setText(String.valueOf(game.getReleaseYear()));
            genreField.setText(String.join(", ", game.getGenres()));
            playtimeField.setText(String.valueOf(game.getPlaytime()));
            developerField.setText(game.getDeveloper());
            formatField.setText(game.getFormat());
            publisherField.setText(game.getPublisher());
            languageField.setText(String.join(", ", game.getLanguage()));
            platformsField.setText(String.join(", ", game.getPlatforms()));
            ratingField.setText(String.valueOf(game.getRating()));
            translatorsField.setText(String.join(", ", game.getTranslators()));
            tagsField.setText(String.join(", ", game.getTags()));
            steamIdField.setText(game.getSteamId());
            coverImageField.setText(game.getCoverImagePath());
        }
    }

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
        String filePath = JSONHandler.getLastLoadedFilePath();

        if (filePath == null) {
            showErrorAlert("No file loaded", "Please load a JSON file first.");
            return;
        }

        // SteamId boş olmasın diye kontrol ekleyelim
        String steamId = getOrDefault(steamIdField.getText());
        if (steamId.equals("Not Specified")) {
            showErrorAlert("Validation Error", "Steam ID cannot be empty.");
            return;
        }

        Game newGame = new Game();
        newGame.setTitle(getOrDefault(titleField.getText()));
        newGame.setDeveloper(getOrDefault(developerField.getText()));
        newGame.setPublisher(getOrDefault(publisherField.getText()));
        newGame.setFormat(getOrDefault(formatField.getText()));
        newGame.setSteamId(steamId);
        newGame.setCoverImagePath(getOrDefault(coverImageField.getText()));
        newGame.setReleaseYear(parseIntegerOrDefault(releaseYearField.getText()));
        newGame.setPlaytime(parseDoubleOrDefault(playtimeField.getText()));
        newGame.setRating(parseDoubleOrDefault(ratingField.getText()));
        newGame.setGenres(parseListOrDefault(genreField.getText()));
        newGame.setPlatforms(parseListOrDefault(platformsField.getText()));
        newGame.setTranslators(parseListOrDefault(translatorsField.getText()));
        newGame.setLanguage(parseListOrDefault(languageField.getText()));
        newGame.setTags(parseListOrDefault(tagsField.getText()));

        List<Game> existingGames = JSONHandler.readGamesFromJson(filePath);
        if (existingGames == null) {
            existingGames = new ArrayList<>();
        }

        boolean updated = false;

        // Eğer düzenlenen oyun var ise steamId bazlı güncelleme yap
        if (currentGame != null) {
            for (int i = 0; i < existingGames.size(); i++) {
                if (existingGames.get(i).getSteamId().equals(currentGame.getSteamId())) {
                    existingGames.set(i, newGame);
                    updated = true;
                    break;
                }
            }
        }

        // Eğer güncelleme olmadıysa yeni ekle
        if (!updated) {
            existingGames.add(newGame);
        }

        JSONHandler.writeGamesToJson(filePath, existingGames);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText(updated ? "Game updated successfully!" : "Game saved successfully!");
        alert.showAndWait();

        HomePageController.refreshGameListStatic();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getOrDefault(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Not Specified";
        }
        return text.trim();
    }

    private int parseIntegerOrDefault(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleOrDefault(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
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
