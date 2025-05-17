package org.example.ce216project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONHandler {

    // JSON dosyasının en son kullanılan yolu
    private static String lastLoadedFilePath;

    // Varsayılan dosya yolu (örnek: masaüstünde "games.json")
    public static String getDefaultJsonPath() {
        return System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "games.json";
    }

    // JSON dosyasını okur
    public static List<Game> readGamesFromJson(String path) {
        try (FileReader reader = new FileReader(path)) {
            Type gameListType = new TypeToken<List<Game>>() {}.getType();
            List<Game> games = new Gson().fromJson(reader, gameListType);
            return games != null ? games : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadı: " + path);
            return new ArrayList<>(); // Dosya yoksa boş liste döner
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // JSON dosyasına yazar
    public static void writeGamesToJson(String path, List<Game> games) {
        try (FileWriter writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(games, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Dosya yolu ayarlanır
    public static void setLastLoadedFilePath(String path) {
        lastLoadedFilePath = path;
    }

    public static String getLastLoadedFilePath() {
        return lastLoadedFilePath;
    }

    // Varsayılan dosya yoksa oluşturur
    public static void ensureJsonFileExists() {
        File file = new File(getDefaultJsonPath());

        // 1. Klasörü oluştur (varsa sorun olmaz)
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();  // Klasörleri oluştur
        }

        // 2. Dosya yoksa oluştur
        try {
            if (!file.exists()) {
                file.createNewFile();
                // Boş JSON dizisi yaz (yoksa JSON parse hatası alırsın)
                writeGamesToJson(file.getPath(), new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
