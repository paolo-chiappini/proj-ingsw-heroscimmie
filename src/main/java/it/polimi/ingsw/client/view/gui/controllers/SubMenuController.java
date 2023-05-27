package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.List;

public abstract class SubMenuController extends GuiController {
    public TextField nameTextField;
    public Label nameAlreadyTakenLabel;
    public List<Node> previousView;
    public MenuController menuController;
    public Button undo;
    public Pane root;

    public abstract void confirmUsername();

    public void start(MenuController menuController, List<Node> previousView, Pane root){
        this.root = root;
        this.previousView = previousView;
        this.menuController = menuController;
        nameAlreadyTakenLabel.setVisible(false);

        undo = new Button();
        undo.setText("👈Menu");
        undo.getStyleClass().add("undo");
        StackPane.setAlignment(undo, Pos.TOP_LEFT);
        StackPane.setMargin(undo, new Insets(10));

        root.getChildren().add(undo);

        EventHandlers.set(this);
    }

    public void returnToMenu(MouseEvent ignoredE){
        root.getChildren().clear();
        root.getChildren().addAll(previousView);
        SceneManager.setCurrentController(menuController);
    }

//    public Stage getRootStage() {
//        return (Stage)root.getScene().getWindow();
//    }

    public void notifyNameAlreadyTaken() {
        nameAlreadyTakenLabel.setVisible(true);
    }

    public void successfullyJoined(){
        SceneManager.waitGameScene(root);
    }

    public void notifyNotWhitelisted() {
        showErrorMessageDialog("Your nickname is not whitelisted");
    }
}