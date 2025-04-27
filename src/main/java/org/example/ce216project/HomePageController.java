package org.example.ce216project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.*;

public class HomePageController {

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<String> gameListView;

    @FXML
    private Button addButton, helpButton, exportButton;

    @FXML
    private MenuButton moreMenuButton;

    private List<Game> gameList = new ArrayList<>();

    public void setGameList(List<Game> games) {
        this.gameList = games;
        updateGameListView(games);
        populateGenreButtons();
        setupGameClickListener();

    }

    @FXML
    private void onEnterSearch(KeyEvent event) {
        String query = searchBar.getText().trim().toLowerCase();

        List<String> filtered = gameList.stream()
                .map(Game::getTitle)
                .filter(title -> title.toLowerCase().contains(query))
                .toList();

        gameListView.setItems(FXCollections.observableArrayList(filtered));
    }

    private void populateGenreButtons() {
        if (gameList == null || gameList.isEmpty()) return;

        Set<String> allGenres = new TreeSet<>();
        for (Game game : gameList) {
            List<String> genres = game.getGenres();
            if (genres != null) {
                allGenres.addAll(genres);
            }
        }

        moreMenuButton.getItems().clear();

        for (String genre : allGenres) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(e -> filterGamesByGenre(genre));
            moreMenuButton.getItems().add(item);
        }
    }

    private void filterGamesByGenre(String genre) {
        List<String> filteredTitles = gameList.stream()
                .filter(g -> g.getGenres() != null && g.getGenres().contains(genre))
                .map(Game::getTitle)
                .toList();

        gameListView.setItems(FXCollections.observableArrayList(filteredTitles));
    }

    private void updateGameListView(List<Game> games) {
        List<String> titles = games.stream()
                .map(Game::getTitle)
                .toList();
        gameListView.setItems(FXCollections.observableArrayList(titles));
    }

    // Stub methods for other button actions (implement as needed)
    @FXML
    private void onAddButton() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ce216project/addGame.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Game");
            stage.setScene(new Scene(root));
            stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🕹️ How to Use the Game Library");
        alert.setHeaderText("Need Help? Here’s what you can do:");
        alert.setContentText(
                "🔍 To search for a game, use the search bar.\n" +
                        "🏷️ To filter games according to their tags, use the filters.\n" +
                        "➕ To add more games, use the 'Add Game' button.\n" +
                        "📄 To display game info, click on a game.\n" +
                        "📤 If you're done, use the 'Export' button."
        );
        alert.showAndWait();
    }

    public void onHelpButton() {
        showHelpDialog();
    }

    @FXML
    private void onExportButton() {

    }
    private void setupGameClickListener() {
        gameListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) { // double click
                    String selectedTitle = gameListView.getSelectionModel().getSelectedItem();
                    if (selectedTitle != null) {
                        Game selectedGame = findGameByTitle(selectedTitle);
                        if (selectedGame != null) {
                            openGameDetailsPage(selectedGame);
                        }
                    }
                }
            }
        });
    }

    private Game findGameByTitle(String title) {
        for (Game game : gameList) {
            if (game.getTitle().equals(title)) {
                return game;
            }
        }
        return null;
    }

    private void openGameDetailsPage(Game game) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ce216project/gameDetails.fxml"));
            Parent root = loader.load();

            GameDetailsController controller = loader.getController();
            controller.setGame(game);

            Stage stage = new Stage();
            stage.setTitle(game.getTitle() + " Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
