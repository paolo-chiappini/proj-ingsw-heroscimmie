package it.polimi.ingsw.client.view.gui.controllers;
import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuWaitGameController extends GuiController {

    @FXML
    private Label waitingForLabel;

    public void startGame() {
        SceneManager.mainGameScene(getRootStage());
    }

    public Stage getRootStage() {
        return (Stage) waitingForLabel.getScene().getWindow();
    }

}