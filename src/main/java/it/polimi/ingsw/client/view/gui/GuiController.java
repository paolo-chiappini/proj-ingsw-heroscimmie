package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public abstract class GuiController {
    private static ViewGui view;
    public static void setView(ViewGui view){
        GuiController.view = view;
    }

    public static ViewGui getView(){
        return GuiController.view;
    }

    public void showConnectionErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Connection error");
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        //noinspection OptionalGetWithoutIsPresent
        if (result.get() == ButtonType.OK){
            view.connectToServer();
        } else {
            view.shutdown();
        }
    }

    public void showErrorMessageDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Error");
        alert.setContentText(message);

        alert.showAndWait();
    }

    public void shutdown() {
        Platform.exit();
    }
}
