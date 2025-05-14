package org.example.ce216project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;
import javafx.scene.Node;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private static HomePageController instance;


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
        clearFilterButton.setVisible(true); // Show clear button
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
                        "➕ To add more " +
                        "games, use the 'Add Game' button.\n" +
                        "📄 To display game info, click on a game.\n" +
                        "📤 If you're done, use the 'Export' button."
        );
        alert.showAndWait();
    }

    public void onHelpButton() {
        showHelpDialog();
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
    public HomePageController() {
        instance = this;
    }

    public static void refreshGameListStatic() {
        if (instance != null) {
            instance.refreshGameList();
        }
    }
    public void refreshGameList() {
        String filePath = JSONHandler.getLastLoadedFilePath();
        if (filePath != null) {
            List<Game> games = JSONHandler.readGamesFromJson(filePath);
            if (games != null) {
                setGameList(games);
            }
        }
    }


  /*  @FXML
    public void initialize() {
        refreshGameList();
    }
    public void deleteGame(Game game) {
        gameList.remove(game);
        updateGameListView(gameList);
        populateGenreButtons();
    }*/
    @FXML
    private void initialize() {
        gameListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // double click to open
                String selectedTitle = gameListView.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {
                    Game selectedGame = gameList.stream()
                            .filter(g -> g.getTitle().equals(selectedTitle))
                            .findFirst()
                            .orElse(null);
                    if (selectedGame != null) {
                        openGameDetailsPage(selectedGame);
                    }
                }
            }
        });
    }

    @FXML
    private Button clearFilterButton;
    @FXML
    private void onClearFilter() {
        updateGameListView(gameList);
        clearFilterButton.setVisible(false); // Hide the button again
    }
    @FXML
    private void onExportButton() {

        if(gameList==null|| gameList.isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Export Error","There are no games to export.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game List");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"));
        fileChooser.setInitialFileName("games.json");

        Stage stage= (Stage) exportButton.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if(file !=null){
            try(FileWriter writer = new FileWriter(file)){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(gameList,writer);
                showAlert(Alert.AlertType.INFORMATION,"Success","Games exported");
            }catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR,"Export Failed","An error occured");
            }
        }

    }
    private void showAlert(Alert.AlertType alertType, String title,String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void importFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a JSon File");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(jsonFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            if (selectedFile.getName().endsWith(".json")) {
                JSONHandler.setLastLoadedFilePath(selectedFile.getAbsolutePath());
                List<Game> games = JSONHandler.readGamesFromJson(selectedFile.getAbsolutePath());
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                System.out.println("Name: " + games.get(0).getTitle());
                System.out.println("Languages: " + games.get(0).getLanguage());
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("homePage.fxml"));
                    Parent homePageRoot = loader.load();

                    HomePageController controller = loader.getController();
                    controller.setGameList(games);

                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(homePageRoot));
                    stage.show();
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            } else {
                showAlert("Invalid File", "Please select a valid JSON file!");
            }

        }
        else{
            showAlert("No File Selected", "You must select a JSON file.");
        }
    }
    private void showAlert(String title,String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void onImportFileButton(ActionEvent event){
        importFileAction(event);
    }
}
