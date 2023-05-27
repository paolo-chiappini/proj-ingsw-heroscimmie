package it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements;

import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.scene.Node;

public abstract class GraphicElement {
    public final BoardController controller;
    public GraphicElement(BoardController controller){
        this.controller = controller;
    }
    public abstract Node getElementAsNode();
}
