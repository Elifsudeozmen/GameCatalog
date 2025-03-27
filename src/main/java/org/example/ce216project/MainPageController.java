package org.example.ce216project;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class MainPageController {
    public void onEnterFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a JSon File");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(jsonFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            if (selectedFile.getName().endsWith(".json")) {
                List<Game> games = JSONHandler.readGamesFromJson(selectedFile.getAbsolutePath());
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                System.out.println("Name: " + games.get(0).getTitle());
                System.out.println("Languages: " + games.get(0).getLanguage());
            } else {
                showAlert("Invalid File", "Please select a valid JSON file!");
            }
        }
        else{
                showAlert("No File Selected", "You must select a JSON file.");
        }
    }

    public void onHelpButton(){
        showHelpDialog();

    }

    private void showAlert(String title,String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showHelpDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📌 Need Help?");
        alert.setHeaderText("Here’s how you can contact us:");
        alert.setContentText(
                "📧 Email: support@gameapp.com\n" +
                        "📞 Phone: " +
                        "🌍 Website: " +
                        "We’re here to assist you 24/7! 😊"
        );
        alert.showAndWait();
    }
}
