package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements.TileElement;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class PlaceTilesState extends BoardViewState{
    public PlaceTilesState(BoardController controller) {
        super(controller);
        controller.undoButton.setVisible(true);
    }

    @Override
    public void clickTile(Node tile) {
        if(!controller.getSelectedTilesList().contains(tile)) return;

        TileElement tileElement = TileElement.getInstanceFromNode(tile);

        var selectedTilesList = controller.getSelectedTilesList();

        var bookshelf = controller.getBookshelf();

        selectedTilesList.remove(tile);
        bookshelf.dropTile(tileElement);
    }

    @Override
    public void clickColumn(VBox column) {

    }

    @Override
    public void clickConfirmButton() {
        if(controller.getSelectedTilesList().isEmpty()){
            controller.setState(new PickUpTilesState(controller));
            controller.playSwitchToBookshelfAnimation(1);
        }
    }

    @Override
    public void clickUndoButton() {
        var placedTiles = controller.getBookshelf().returnTiles();

        for(TileElement tile : placedTiles){
            controller.getSelectedTilesList().add(tile.getElementAsNode());
        }

        controller.getBookshelf().selectColumn(controller.getSelectedColumn());
        controller.setState(new SelectColumnState(controller));
    }
}
