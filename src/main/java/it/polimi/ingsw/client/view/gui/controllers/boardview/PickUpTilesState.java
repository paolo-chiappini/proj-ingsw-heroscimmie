package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements.TileElement;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
public class PickUpTilesState extends BoardViewState{
    private final List<TileElement> pickedUpTiles = new ArrayList<>();
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
            pickedUpTiles.add(TileElement.getInstanceFromNode(tile));
            selectedTilesList.add(tile);
        }else if(selectedTilesList.contains(tile)){
            undoTileSelection(tile);
        }
    }

    @Override
    public void clickColumn(VBox column) { /* Do nothing */}

    @Override
    public void clickConfirmButton() {
        var x1 = pickedUpTiles.get(0).getGridPositionX();
        var y1 = pickedUpTiles.get(0).getGridPositionY();
        var x2 = pickedUpTiles.get(pickedUpTiles.size()-1).getGridPositionX();
        var y2 = pickedUpTiles.get(pickedUpTiles.size()-1).getGridPositionY();

        if(pickedUpTiles.size() == 2){
            if(Math.abs(x2-x1+y2-y1) != 1){
                notifyInvalidMove();
                return;
            }
        }

        GuiController.getView().notifyPickCommand(x1, y1, x2, y2);
    }

    @Override
    public void clickUndoButton() {
        var selectedTiles = new ArrayList<>(controller.getSelectedTilesList());
        for(var tile : selectedTiles){
            undoTileSelection(tile);
        }
    }

    @Override
    public void notifyInvalidMove() {
        undoTilesSelectionWithError();
    }

    @Override
    public void notifyValidMove() {
        controller.setState(new SelectColumnState(controller));
        controller.playSwitchToBookshelfAnimation(-1);
    }

    private void undoTilesSelectionWithError() {
        pickedUpTiles.clear();
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
