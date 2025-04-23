package org.example.ce216project;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class AddGameController {
    @FXML
    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🕹️ How to Add a Game");
        alert.setHeaderText("Need Help? Here’s what you can do:");
        alert.setContentText(
                "To enter a game fill in the blank boxes with the correct information.\n" +
                        "To cancel press the cancel button.\n" +
                        "To clear the boxes press the clear button.\n" +
                        "To save the information press the save button.\n"
        );
        alert.showAndWait();
    }

    public void onHelpButton() {
        showHelpDialog();
    }
}
