package it.polimi.ingsw.client.view.cli.graphics.board;

import it.polimi.ingsw.client.view.cli.graphics.grids.CellElement;
import it.polimi.ingsw.client.view.cli.graphics.grids.GridElement;
import it.polimi.ingsw.client.view.cli.graphics.tiles.TileElement;
import it.polimi.ingsw.server.model.tile.TileType;

/**
 * Represents the game board.
 */
public class BoardElement extends GridElement {
    private static final int SIZE = 9;

    public BoardElement() {
        this.contents = new CellElement[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            if (i == 0) this.representation.add(GridElement.generateTopRow(SIZE));
            else this.representation.add(GridElement.generateMiddleRow(SIZE));
            this.representation.add(GridElement.generateCellsRow(SIZE));
            this.representation.add(GridElement.generateCellsRow(SIZE));
            if (i == SIZE - 1) this.representation.add(GridElement.generateBottomRow(SIZE));
        }
    }

    /**
     * Sets a game tile at the given coordinates.
     * @param x x coordinate where to put the tile.
     * @param y y coordinate where to put the tile.
     * @param type type of the tile to set.
     */
    public void setGameTile(int x, int y, TileType type) {
        setElement(new TileElement(type), x, y);
    }
}
