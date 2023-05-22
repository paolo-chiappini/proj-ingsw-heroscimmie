package it.polimi.ingsw.client.view.gui;

import javafx.scene.control.Alert;

public abstract class GuiController {
    private static ViewGui view;
    protected static void setView(ViewGui view){
        GuiController.view = view;
    }

    protected static ViewGui getView(){
        return GuiController.view;
    }

    public void showServerConnectionError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Server error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
