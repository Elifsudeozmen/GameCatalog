package org.example.ce216project;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.stage.Stage;
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
    @FXML
    private void doCancelOperation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel??");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("All data that isn't saved will be lost");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
    public void onCancelButton(ActionEvent event){
        doCancelOperation(event);
    }

}
