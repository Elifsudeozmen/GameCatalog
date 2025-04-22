package org.example.ce216project;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
public class HomePageController {
    @FXML
    private TextField searchBar;
    @FXML
    private ListView<String> gameListView;
    private List<Game> gameList;

    public void setGameList(List<Game> games){
        this.gameList=games;
        List<String> titles = games.stream().map(Game::getTitle).toList();
        gameListView.getItems().setAll(titles);
    }

    public void onEnterSearch(){

    }
    public void onAddButton(){

    }
    public void onHelpButton(){

    }
    public void onExportButton(){

    }
    public void openGame(){

    }
}
