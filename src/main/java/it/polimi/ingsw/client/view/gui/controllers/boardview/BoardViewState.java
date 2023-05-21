package it.polimi.ingsw.client.view.gui.controllers.boardview;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public abstract class BoardViewState {
    public final BoardController controller;

    public BoardViewState(BoardController controller) {
        this.controller = controller;
        var stateName = this.getClass().getSimpleName();
        System.out.println(stateName);
    }

    public abstract void clickTile(Node tile);
    public abstract void clickColumn(VBox column);

    public abstract void clickConfirmButton();

    public abstract void clickUndoButton();
}
