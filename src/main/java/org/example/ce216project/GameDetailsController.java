package org.example.ce216project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
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

    public void setGame(Game game) throws IOException {
        this.currentGame = game;
        displayGameDetails(game);
    }

    private void displayGameDetails(Game game) throws IOException {
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
                    gameImageView.setImage(null);
                    Image image = new Image(imageStream);
                    System.out.println("Resim genişliği: " + image.getWidth() + ", yüksekliği: " + image.getHeight());
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addGame.fxml"));
            Parent root = loader.load();

            AddGameController controller = loader.getController();
            controller.setGame(currentGame); // Düzenlemek istediğin oyun objesini gönder

            Stage stage = new Stage();
            stage.setTitle("Edit Game");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Düzenleme sonrası detayları güncelle
            displayGameDetails(controller.currentGame);
            HomePageController.refreshGameListStatic();
        } catch (IOException e) {
            e.printStackTrace();
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
