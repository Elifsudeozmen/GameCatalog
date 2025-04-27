package org.example.ce216project;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JSONHandler {
    public static List<Game> readGamesFromJson(String filePath){
        Gson gson= new Gson();
        try(FileReader reader= new FileReader(filePath)){
            Type gameListType = new TypeToken<List<Game>>(){}.getType();
            return gson.fromJson(reader,gameListType);
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public static String lastLoadedFilePath;

    public static String getLastLoadedFilePath() {
        return lastLoadedFilePath;
    }

    public static void setLastLoadedFilePath(String path) {
        lastLoadedFilePath = path;
    }

    public static void writeGamesToJson(String filePath, List<Game> games){
        Gson gson = new Gson();
        try(FileWriter writer = new FileWriter(filePath)){
            gson.toJson(games, writer);
        } catch(IOException e){
            e.printStackTrace();
        }
    }



}
