package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MenuJoinGameController extends SubMenuController {
    public Button confirmButton;
    public void start(ArrayList<Node> lastView, Pane root) {
        this.root = root;
        this.lastView = lastView;
        EventHandlers.set(this);
    }

    public void setUsername(MouseEvent mouseEvent) {
        GuiController.getView().notifyNameChange(nameTextField.getText());
    }

    @Override
    public void joinGame(){
        GuiController.getView().notifyJoinGameCommand();
        SceneManager.waitGameScene(this, root);
    }
}