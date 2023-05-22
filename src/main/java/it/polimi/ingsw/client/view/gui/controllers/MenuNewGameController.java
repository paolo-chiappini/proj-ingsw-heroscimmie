package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MenuNewGameController extends GuiController {
    public Button confirmButton;
    public Button backButton;
    public TextField nameTextField;
    public Pane root;
    public ArrayList<Node> lastView;

    public void start(ArrayList<Node> lastView, Pane root) {
        this.root = root;
        this.lastView = lastView;
        EventHandlers.set(this);
    }

    public void setUsername(MouseEvent mouseEvent) {
        GuiController.getView().notifyNameChange(nameTextField.getText());
    }
}