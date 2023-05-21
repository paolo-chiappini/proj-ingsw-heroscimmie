package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneSelector;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController extends GuiController implements Initializable {
    @FXML
    private ImageView myShelfieLogo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event -> SceneSelector.nextScene(this, getRootStage()));
        delay.play();
    }

    private Stage getRootStage() {
        return (Stage)myShelfieLogo.getScene().getWindow();
    }

}