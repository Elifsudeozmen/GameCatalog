package org.example.ce216project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class GameDetailsController {

    @FXML
    private Label titleLabel, developerLabel, publisherLabel, releaseYearLabel, playtimeLabel,
            formatLabel, languageLabel, ratingLabel, platformsLabel, translatorsLabel, steamIdLabel;

    @FXML
    private Label genresLabel, tagsLabel;

    @FXML
    private ImageView gameImageView;

    @FXML
    private Button editButton, deleteButton, returnButton;

    private Game currentGame;
    private boolean isEditMode = false;

    public void setGame(Game game) {
        this.currentGame = game;
        displayGameDetails(game);
    }

    private void displayGameDetails(Game game) {
        titleLabel.setText(game.getTitle());
        developerLabel.setText("Developer: " + game.getDeveloper());
        publisherLabel.setText("Publisher: " + game.getPublisher());
        releaseYearLabel.setText("Release Year: " + game.getReleaseYear());
        playtimeLabel.setText("Playtime: " + game.getPlaytime() + " hrs");
        formatLabel.setText("Format: " + game.getFormat());
        ratingLabel.setText("Rating: " + game.getRating());
        steamIdLabel.setText("Steam ID: " + game.getSteamId());

        languageLabel.setText("Languages: " + String.join(", ", game.getLanguage()));
        platformsLabel.setText("Platforms: " + String.join(", ", game.getPlatforms()));
        translatorsLabel.setText("Translators: " + String.join(", ", game.getTranslators()));
        genresLabel.setText("Genres: " + String.join(", ", game.getGenres()));
        tagsLabel.setText("Tags: " + String.join(", ", game.getTags()));

        // Set image if available
        if (game.getCoverImagePath() != null && !game.getCoverImagePath().isEmpty()) {
            String path = "/" + game.getCoverImagePath(); // örn: "/covers/rdr2.jpg"
            InputStream imageStream = getClass().getResourceAsStream(path);

            if (imageStream != null) {
                try {
                    Image image = new Image(imageStream);
                    gameImageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("HATA: Resim yüklenirken sorun oluştu -> " + path);
                    e.printStackTrace();
                }
            } else {
                System.err.println("UYARI: Resim dosyası bulunamadı -> " + path);
            }
        } else {
            System.err.println("UYARI: Oyun için geçerli bir kapak görseli yolu belirtilmemiş.");
        }
    }

    @FXML
    private void onEditButton() {
        if (!isEditMode) {
            isEditMode = true;

            // Her alan için bir dialog gösterelim
            TextInputDialog titleDialog = new TextInputDialog(currentGame.getTitle());
            titleDialog.setHeaderText("Edit Game Title");
            titleDialog.setContentText("Title:");
            titleDialog.showAndWait().ifPresent(currentGame::setTitle);

            TextInputDialog developerDialog = new TextInputDialog(currentGame.getDeveloper());
            developerDialog.setHeaderText("Edit Developer");
            developerDialog.setContentText("Developer:");
            developerDialog.showAndWait().ifPresent(currentGame::setDeveloper);

            TextInputDialog publisherDialog = new TextInputDialog(currentGame.getPublisher());
            publisherDialog.setHeaderText("Edit Publisher");
            publisherDialog.setContentText("Publisher:");
            publisherDialog.showAndWait().ifPresent(currentGame::setPublisher);

            TextInputDialog yearDialog = new TextInputDialog(String.valueOf(currentGame.getReleaseYear()));
            yearDialog.setHeaderText("Edit Release Year");
            yearDialog.setContentText("Year:");
            yearDialog.showAndWait().ifPresent(year -> currentGame.setReleaseYear(Integer.parseInt(year)));

            TextInputDialog playtimeDialog = new TextInputDialog(String.valueOf(currentGame.getPlaytime()));
            playtimeDialog.setHeaderText("Edit Playtime");
            playtimeDialog.setContentText("Hours:");
            playtimeDialog.showAndWait().ifPresent(pt -> currentGame.setPlaytime(Double.parseDouble(pt)));

            TextInputDialog formatDialog = new TextInputDialog(currentGame.getFormat());
            formatDialog.setHeaderText("Edit Format");
            formatDialog.setContentText("Format:");
            formatDialog.showAndWait().ifPresent(currentGame::setFormat);

            TextInputDialog languageDialog = new TextInputDialog(String.join(", ", currentGame.getLanguage()));
            languageDialog.setHeaderText("Edit Languages");
            languageDialog.setContentText("Comma-separated:");
            languageDialog.showAndWait().ifPresent(input -> currentGame.setLanguage(List.of(input.split("\\s*,\\s*"))));

            TextInputDialog steamDialog = new TextInputDialog(currentGame.getSteamId());
            steamDialog.setHeaderText("Edit Steam ID");
            steamDialog.setContentText("Steam ID:");
            steamDialog.showAndWait().ifPresent(currentGame::setSteamId);

            TextInputDialog platformsDialog = new TextInputDialog(String.join(", ", currentGame.getPlatforms()));
            platformsDialog.setHeaderText("Edit Platforms");
            platformsDialog.setContentText("Comma-separated:");
            platformsDialog.showAndWait().ifPresent(input -> currentGame.setPlatforms(List.of(input.split("\\s*,\\s*"))));

            TextInputDialog translatorsDialog = new TextInputDialog(String.join(", ", currentGame.getTranslators()));
            translatorsDialog.setHeaderText("Edit Translators");
            translatorsDialog.setContentText("Comma-separated:");
            translatorsDialog.showAndWait().ifPresent(input -> currentGame.setTranslators(List.of(input.split("\\s*,\\s*"))));

            TextInputDialog genresDialog = new TextInputDialog(String.join(", ", currentGame.getGenres()));
            genresDialog.setHeaderText("Edit Genres");
            genresDialog.setContentText("Comma-separated:");
            genresDialog.showAndWait().ifPresent(input -> currentGame.setGenres(List.of(input.split("\\s*,\\s*"))));

            TextInputDialog tagsDialog = new TextInputDialog(String.join(", ", currentGame.getTags()));
            tagsDialog.setHeaderText("Edit Tags");
            tagsDialog.setContentText("Comma-separated:");
            tagsDialog.showAndWait().ifPresent(input -> currentGame.setTags(List.of(input.split("\\s*,\\s*"))));

            // JSON güncelleme
            String filePath = JSONHandler.getLastLoadedFilePath();
            if (filePath != null) {
                List<Game> games = JSONHandler.readGamesFromJson(filePath);
                for (int i = 0; i < games.size(); i++) {
                    if (games.get(i).getTitle().equals(currentGame.getTitle())) {
                        games.set(i, currentGame);
                        break;
                    }
                }
                JSONHandler.writeGamesToJson(filePath, games);
            }

            // Ana sayfa güncelle
            HomePageController.refreshGameListStatic();

            // Görsel bilgileri yenile
            displayGameDetails(currentGame);

            Alert updated = new Alert(Alert.AlertType.INFORMATION);
            updated.setTitle("Game Updated");
            updated.setHeaderText(null);
            updated.setContentText("Game updated successfully.");
            updated.showAndWait();

            isEditMode = false;
        }
    }


    @FXML
    private void onDeleteButton() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Are you sure you want to delete this game?");
        confirm.setContentText("Game: " + currentGame.getTitle());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // 1. JSON dosyasından sil
                String filePath = JSONHandler.getLastLoadedFilePath();
                if (filePath != null) {
                    java.util.List<Game> games = JSONHandler.readGamesFromJson(filePath);
                    games.removeIf(g -> g.getTitle().equals(currentGame.getTitle()));
                    JSONHandler.writeGamesToJson(filePath, games);
                }

                // 2. Ana sayfayı güncelle
                HomePageController.refreshGameListStatic();

                // 3. Detay sayfasını kapat
                ((Stage) deleteButton.getScene().getWindow()).close();
            }
        });
    }

    @FXML
    private void onReturnButton() {
        ((Stage) returnButton.getScene().getWindow()).close();
    }
}
