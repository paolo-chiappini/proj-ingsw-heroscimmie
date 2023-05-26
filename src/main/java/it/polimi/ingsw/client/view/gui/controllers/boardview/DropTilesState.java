package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements.TileElement;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DropTilesState extends BoardViewState{
    private final List<Integer> droppedTiles = new ArrayList<>();
    private final List<Node> originalTiles;
    public DropTilesState(BoardController controller) {
        super(controller);
        originalTiles = new ArrayList<>(controller.getSelectedTilesList());
        controller.undoButton.setVisible(true);
    }

    @Override
    public void clickTile(Node tile) {
        if(!controller.getSelectedTilesList().contains(tile)) return;

        TileElement tileElement = TileElement.getInstanceFromNode(tile);

        var selectedTilesList = controller.getSelectedTilesList();

        var bookshelf = controller.getBookshelf();

        int tileIndex = originalTiles.indexOf(tile)+1;
        droppedTiles.add(tileIndex);

        selectedTilesList.remove(tile);
        bookshelf.dropTile(tileElement);
    }

    @Override
    public void clickConfirmButton() {
        if(controller.getSelectedTilesList().isEmpty()){
            int[] order = new int[]{0, 0, 0};
            for(int i = 0; i < droppedTiles.size(); i++){
                order[i] = droppedTiles.get(i);
            }
            GuiController.getView().notifyOrderCommand(order[0], order[1], order[2]);
            var bookshelf = controller.getBookshelf();
            int selectedColumnIndex = bookshelf.getColumns().indexOf(controller.getSelectedColumn());
            GuiController.getView().notifyDropCommand(selectedColumnIndex);

            controller.setState(new PickUpTilesState(controller));
            controller.playSwitchToBookshelfAnimation(1);
        }
    }

    @Override
    public void clickUndoButton() {
        droppedTiles.clear();
        var placedTiles = controller.getBookshelf().returnTiles();

        for(TileElement tile : placedTiles){
            controller.getSelectedTilesList().add(tile.getElementAsNode());
        }

        controller.getBookshelf().selectColumn(controller.getSelectedColumn());
        controller.setState(new SelectColumnState(controller));
    }

    @Override
    public void notifyValidMove() {

    }

    @Override
    public void notifyInvalidMove() {

    }

    @Override
    public void clickColumn(VBox column) {

    }
}
