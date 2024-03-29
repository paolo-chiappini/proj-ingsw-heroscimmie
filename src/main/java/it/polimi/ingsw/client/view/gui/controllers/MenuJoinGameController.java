package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
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

    public void setUsername(MouseEvent ignoredMouseEvent) {
        GuiController.getView().notifyNameChange(nameTextField.getText());
    }

    @Override
    public void confirmUsername(){
        GuiController.getView().notifyJoinGameCommand();
    }


}