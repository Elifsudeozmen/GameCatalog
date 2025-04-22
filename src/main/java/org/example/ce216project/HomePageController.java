package org.example.ce216project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        // entera basınca search yapılcak

    }
    public void onAddButton(){
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
    public void onHelpButton(){


    }
    public void onExportButton(){
        //yeni json dosyası updatelenecek / elif

    }
    public void openGame(){

    }
}
