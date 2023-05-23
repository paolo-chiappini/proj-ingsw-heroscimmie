package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiController;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public abstract class SubMenuController extends GuiController {
    public TextField nameTextField;
    public Pane root;
    public ArrayList<Node> lastView;

    public abstract void joinGame();

}