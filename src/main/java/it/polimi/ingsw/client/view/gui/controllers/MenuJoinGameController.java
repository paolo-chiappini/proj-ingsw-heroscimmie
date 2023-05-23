package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.List;

public class MenuJoinGameController extends SubMenuController {
    public Button confirmButton;
    public void start(MenuController menuController, List<Node> previousView, Pane root) {
        super.start(menuController, previousView, root);
        EventHandlers.set(this);
    }

    public void setUsername(MouseEvent mouseEvent) {
        GuiController.getView().notifyNameChange(nameTextField.getText());
    }

    @Override
    public void joinGame(){
        GuiController.getView().notifyJoinGameCommand();
        SceneManager.waitGameScene(root);
    }
}