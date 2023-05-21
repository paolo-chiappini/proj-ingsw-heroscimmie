package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements.TileElement;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Random;

public class PickUpTilesState extends BoardViewState{
    public PickUpTilesState(BoardController controller) {
        super(controller);

        controller.confirmButton.disableProperty()
                .bind(Bindings.size(controller.getSelectedTilesList()).lessThan(1));

        controller.undoButton.visibleProperty()
                .bind(Bindings.size(controller.getSelectedTilesList()).greaterThan(0));
    }

    @Override
    public void clickTile(Node tile) {
        var selectedTilesList = controller.getSelectedTilesList();

        if(!selectedTilesList.contains(tile) && selectedTilesList.size() < 3) {
            selectedTilesList.add(tile);
        }else if(selectedTilesList.contains(tile)){
            undoTileSelection(tile);
        }
    }

    @Override
    public void clickColumn(VBox column) { /* Do nothing */}

    @Override
    public void clickConfirmButton() {
//        Random r = new Random();
//        boolean canPickUpSelectedTiles = r.nextDouble() > 0.6;
        boolean canPickUpSelectedTiles = true;

        if(!canPickUpSelectedTiles) {
            undoTilesSelectionWithError();
            return;
        }

        controller.setSelectedColumn(null);
        controller.setState(new SelectColumnState(controller));
        controller.playSwitchToBookshelfAnimation(-1);
    }

    @Override
    public void clickUndoButton() {
        var selectedTiles = new ArrayList<>(controller.getSelectedTilesList());
        for(var tile : selectedTiles){
            undoTileSelection(tile);
        }
    }

    private void undoTilesSelectionWithError() {
        var selectedTiles = new ArrayList<>(controller.getSelectedTilesList());
        for(var tile : selectedTiles){
            TileElement tileElement = TileElement.getInstanceFromNode(tile);
            undoTileSelection(tile);
            tileElement.playInvalidSelectionAnimation();
        }
    }



    private void undoTileSelection(Node tile){
        controller.getSelectedTilesList().remove(tile);
        var board = controller.getBoard();
        TileElement tileElement = TileElement.getInstanceFromNode(tile);
        board.addTile(tileElement, tileElement.getGridPositionX(), tileElement.getGridPositionY());
    }
}
