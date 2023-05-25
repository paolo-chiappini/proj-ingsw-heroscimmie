package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.ViewGui;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.List;

public class MenuLoadGameController extends SubMenuController {
    public Button confirmButton;
    public ListView<String> gamesList;

    public void start(MenuController menuController, List<Node> previousView, Pane root) {
        super.start(menuController, previousView, root);
        EventHandlers.set(this);
        getView().notifyListCommand();
    }

    public void setUsername(MouseEvent mouseEvent) {
//        System.out.println(gamesList.getItems().get(gamesList.getSelectionModel().getSelectedIndex()));
        GuiController.getView().notifyNameChange(nameTextField.getText());
    }

    @Override
    public void joinGame(){
        GuiController.getView().notifyLoadCommand(gamesList.getSelectionModel().getSelectedIndex());
        SceneManager.waitGameScene(root);
    }
    
    public void populateList(String[] savedGames){
        gamesList.getItems().addAll(savedGames);
    }
}