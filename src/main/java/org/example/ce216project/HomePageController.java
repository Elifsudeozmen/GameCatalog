package org.example.ce216project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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


    @FXML
    private void onAddButton() {
        System.out.println("Add button clicked");
    }

    @FXML
    private void onHelpButton() {
        System.out.println("Help button clicked");
    }

    @FXML
    private void onExportButton() {
        System.out.println("Export button clicked");
    }
}
