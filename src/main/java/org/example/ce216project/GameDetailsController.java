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
            File imageFile = new File(game.getCoverImagePath());
            if (imageFile.exists()) {
                gameImageView.setImage(new Image(imageFile.toURI().toString()));
            }
        }
    }

    @FXML
    private void onEditButton() {
        if (!isEditMode) {
            isEditMode = true;

            TextInputDialog dialog = new TextInputDialog(currentGame.getTitle());
            dialog.setHeaderText("Edit Game Title");
            dialog.setContentText("Title:");
            dialog.showAndWait().ifPresent(newTitle -> {
                currentGame.setTitle(newTitle);
                titleLabel.setText(newTitle);
            });

            // You can expand this to add dialogs for other fields similarly
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
                // Logic to delete game from main list can be implemented through a shared state or callback
                ((Stage) deleteButton.getScene().getWindow()).close();
            }
        });
    }

    @FXML
    private void onReturnButton() {
        ((Stage) returnButton.getScene().getWindow()).close();
    }
}
