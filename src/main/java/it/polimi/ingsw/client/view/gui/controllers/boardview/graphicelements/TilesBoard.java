package it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements;

import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


public class TilesBoard extends GraphicElement {
    private final GridPane boardGrid;

    public TilesBoard(BoardController controller, GridPane boardGrid) {
        super(controller);
        this.boardGrid = boardGrid;
    }

    public void addTile(TileElement tile, int i, int j){
        boardGrid.add(tile.getElementAsNode(), j, i);
    }

    @Override
    public Node getElementAsNode() {
        return boardGrid;
    }

    public ReadOnlyDoubleProperty getWidthProperty(){
        return boardGrid.widthProperty();
    }


    public void update(int[][] update) {
        boardGrid.getChildren().clear();

        for(int i = 0; i < update.length; i++) {
            for (int j = 0; j < update.length; j++) {
                if (update[i][j] >= 0) {
                    int tileIndexType = update[i][j];
                    var imageUrl = getClass().getResource("/sprites/item_tiles_small/" +tileIndexType+".png");
                    var tileImage = new ImageView(new Image(imageUrl.toString()));
                    TileElement itemTile = new TileElement(this.controller, tileImage, i, j);
                    addTile(itemTile, i, j);
                }
            }
        }
    }

}
