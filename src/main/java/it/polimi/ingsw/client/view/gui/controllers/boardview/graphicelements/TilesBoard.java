package it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements;

import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.List;
import java.util.Random;

public class TilesBoard extends GraphicElement {
    private final int[][] VALID_TILES={
            {0, 0, 0, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0}
    };
    private final int WIDTH = 9;
    private final int HEIGHT = 9;
    private final GridPane boardGrid;

    public TilesBoard(BoardController controller, GridPane boardGrid) {
        super(controller);
        this.boardGrid = boardGrid;
    }

    public void addTile(TileElement tile, int i, int j){
        if (isTileInvalid(i, j)){
            System.out.println("WARNING! Tried to put a tile in an invalid space");
            return;
        }
        boardGrid.add(tile.getElementAsNode(), j, i);
    }

    @Override
    public Node getElementAsNode() {
        return boardGrid;
    }

    public ReadOnlyDoubleProperty getWidthProperty(){
        return boardGrid.widthProperty();
    }

    public ReadOnlyDoubleProperty getHeightProperty(){
        return boardGrid.heightProperty();
    }

    public void randomFillWith(List<File> images) {
        Random rand = new Random();

        for(int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (isTileInvalid(i, j)) continue;

                File randomFile = images.get(rand.nextInt(images.size()));
                var tileImage = new ImageView(new Image(randomFile.getPath()));
                TileElement itemTile = new TileElement(this.controller, tileImage, i, j);

                addTile(itemTile, i, j);
            }
        }
    }

    private boolean isTileInvalid(int i, int j) {
        return VALID_TILES[i][j] != 1;
    }

}
