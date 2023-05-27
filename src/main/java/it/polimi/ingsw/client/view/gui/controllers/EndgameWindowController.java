package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EndgameWindowController extends GuiController {
    public Button exitButton;
    public Label winnerNameLabel;
    public Label winnerPointsLabel;
    public VBox playersList;

    public void setWinner(String winner, int points) {
        winnerNameLabel.setText(winner.toUpperCase()+"!");
        winnerPointsLabel.setText(points+" points");
    }

    public void setOtherPlayer(String player, int points, int position) {
        Label playerLabel = new Label(position+"°: "+player+" ("+points+" points)");
        playerLabel.getStyleClass().add("player-name");
        playersList.getChildren().add(playerLabel);
    }

    public void backToMenu(MouseEvent ignoredMouseEvent) {
        SceneManager.menuScene(getRootStage());
    }

    public Stage getRootStage() {
        return (Stage)exitButton.getScene().getWindow();
    }

}