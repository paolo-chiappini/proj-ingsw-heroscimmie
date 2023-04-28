package it.polimi.ingsw.client.cli.graphics.bookshelf;

import it.polimi.ingsw.client.cli.graphics.grids.CellElement;
import it.polimi.ingsw.client.cli.graphics.grids.GridElement;
import it.polimi.ingsw.client.cli.graphics.tiles.TileElement;
import it.polimi.ingsw.server.model.tile.TileType;

/**
 * Represents a player's bookshelf.
 */
public class BookshelfElement extends GridElement {
    public static final int WIDTH = 5;
    public static final int HEIGHT = 6;

    public BookshelfElement() {
        this.contents = new CellElement[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            if (i == 0) this.representation.add(GridElement.generateTopRow(WIDTH));
            else this.representation.add(GridElement.generateMiddleRow(WIDTH));
            this.representation.add(GridElement.generateCellsRow(WIDTH));
            this.representation.add(GridElement.generateCellsRow(WIDTH));
            if (i == HEIGHT - 1) this.representation.add(GridElement.generateBottomRow(WIDTH));
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
