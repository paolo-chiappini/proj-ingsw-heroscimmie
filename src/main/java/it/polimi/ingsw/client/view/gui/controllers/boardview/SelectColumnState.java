package it.polimi.ingsw.client.view.gui.controllers.boardview;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class SelectColumnState extends BoardViewState{
    public SelectColumnState(BoardController controller) {
        super(controller);
        controller.undoButton.visibleProperty().unbind();
        controller.undoButton.setVisible(false);

        controller.confirmButton.disableProperty()
                .bind(Bindings.size(controller.getSelectedTilesList()).greaterThan(0));

    }

    @Override
    public void clickTile(Node tile) {
        var selectedTilesList = controller.getSelectedTilesList();
        var selectedVBoxColumn = controller.getSelectedColumn();

        if(selectedVBoxColumn == null || !selectedTilesList.contains(tile)) return;

        controller.setState(new PlaceTilesState(controller));
        controller.getState().clickTile(tile);
    }

    @Override
    public void clickColumn(VBox column) {
        controller.setSelectedColumn(null);
        var bookshelf = controller.getBookshelf();
        var tilesInColumns = column.getChildren().size();
        var numberOfSelectedTiles = controller.getSelectedTilesList().size();
        if(tilesInColumns + numberOfSelectedTiles > 6){
            bookshelf.playInvalidColumnAnimation(column);
            return;
        }

        bookshelf.selectColumn(column);
    }

    @Override
    public void clickConfirmButton() {

    }

    @Override
    public void clickUndoButton() {

    }
}
