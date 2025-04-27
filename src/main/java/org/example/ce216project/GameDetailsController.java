package org.example.ce216project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class GameDetailsController {

    @FXML
    private Label titleLabel, developerLabel, publisherLabel, genreLabel, platformLabel, yearLabel, ratingLabel, playtimeLabel;

    private Game currentGame;

    public void setGame(Game game) {
        this.currentGame = game;
        updateGameDetails();
    }

    private void updateGameDetails() {
        if (currentGame != null) {
            titleLabel.setText(currentGame.getTitle());
            developerLabel.setText("Developer: " + currentGame.getDeveloper());
            publisherLabel.setText("Publisher: " + currentGame.getPublisher());
            genreLabel.setText("Genres: " + String.join(", ", currentGame.getGenres()));
            platformLabel.setText("Platforms: " + String.join(", ", currentGame.getPlatforms()));
            yearLabel.setText("Release Year: " + currentGame.getReleaseYear());
            ratingLabel.setText("Rating: " + currentGame.getRating());
            playtimeLabel.setText("Playtime: " + currentGame.getPlaytime() + " hours");
        }
    }

    @FXML
    private void onReturnButton() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onEditButton() {
        // You can implement editing logic here
        System.out.println("Edit clicked for: " + currentGame.getTitle());
    }

    @FXML
    private void onDeleteButton() {
        // You can implement delete logic here
        System.out.println("Delete clicked for: " + currentGame.getTitle());
    }
}
