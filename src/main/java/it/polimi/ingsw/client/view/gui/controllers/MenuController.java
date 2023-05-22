package it.polimi.ingsw.client.view.gui.controllers;


import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MenuController extends GuiController {
    public VBox menuBox;
    public ImageView publisherLogo;
    public StackPane stackPane;
    public StackPane innerStackPane;
    public Button joinGameButton;
    public Button newGameButton;
    public ImageView backgroundImage;

    public void startStage(Stage stage) {
        backgroundImage.fitHeightProperty().bind(stage.heightProperty().multiply(1.1));
        stage.show();
        startBackgroundAnimation();
        EventHandlers.set(this);
    }

    public void startBackgroundAnimation(){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(backgroundImage);
        transition.setDuration(Duration.seconds(120));
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        var distance = backgroundImage.getFitWidth() - getRootStage().getWidth();
        transition.setByX(distance);
        transition.play();
    }

    public Stage getRootStage() {
        return (Stage)backgroundImage.getScene().getWindow();
    }

}