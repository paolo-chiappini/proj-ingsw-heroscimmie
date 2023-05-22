package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public abstract class GuiController {
    private static ViewGui view;
    protected static void setView(ViewGui view){
        GuiController.view = view;
    }

    protected static ViewGui getView(){
        return GuiController.view;
    }

    public void showServerConnectionError(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Connection error");
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            view.connectToServer();
        } else {
            view.shutdown();
        }
    }

    public void shutdown() {
        Platform.exit();
    }
}
