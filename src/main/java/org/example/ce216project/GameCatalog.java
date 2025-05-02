package org.example.ce216project;

import java.util.ArrayList;
import java.util.List;

public class GameCatalog {
    private List<Game> games;

    public GameCatalog(){
        games=new ArrayList<>();
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }


    public List<Game> getGames() {
        return games;
    }
}
