package it.polimi.ingsw.client.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MenuNewGameController {
    @FXML
    private Button bottoneACaso;

    public void setUp(ArrayList<Node> lastView, VBox daddy) {
        bottoneACaso.setOnMouseReleased(e->{
            daddy.setAlignment(Pos.TOP_CENTER);
            daddy.getChildren().clear();
            daddy.getChildren().addAll(lastView);
        });
    }
}