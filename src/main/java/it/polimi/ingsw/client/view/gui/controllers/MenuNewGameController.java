package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class MenuNewGameController extends SubMenuController {
    public Button twoPlayerButton;
    public Button threePlayerButton;
    public Button fourPlayerButton;

    private Button pressedButton;

    public void start(ArrayList<Node> lastView, Pane root) {
        this.root = root;
        this.lastView = lastView;
        EventHandlers.set(this);
    }

    public void setUsername(MouseEvent mouseEvent) {
        this.pressedButton = (Button) mouseEvent.getSource();

        GuiController.getView().notifyNameChange(nameTextField.getText());
    }

    @Override
    public void joinGame(){
        int lobbySize = 0;

        if (pressedButton == twoPlayerButton) {
            lobbySize = 2;
        } else if (pressedButton == threePlayerButton) {
            lobbySize = 3;
        } else if (pressedButton == fourPlayerButton){
            lobbySize = 4;
        }

        GuiController.getView().notifyNewGameCommand(lobbySize);
        SceneManager.waitGameScene(this, root);
    }
}