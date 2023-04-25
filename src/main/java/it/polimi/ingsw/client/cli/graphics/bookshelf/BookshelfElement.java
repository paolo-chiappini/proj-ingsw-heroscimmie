package it.polimi.ingsw.client.cli.graphics.bookshelf;

import it.polimi.ingsw.client.cli.graphics.grids.GridCellElement;
import it.polimi.ingsw.client.cli.graphics.grids.TileGridElement;
import it.polimi.ingsw.client.cli.graphics.tiles.TileElement;
import it.polimi.ingsw.server.model.tile.TileType;

/**
 * Represents a player's bookshelf.
 */
public class BookshelfElement extends TileGridElement {
    public static final int WIDTH = 5;
    public static final int HEIGHT = 6;

    public BookshelfElement() {
        this.contents = new GridCellElement[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            if (i == 0) this.representation.add(TileGridElement.generateTopRow(WIDTH));
            else this.representation.add(TileGridElement.generateMiddleRow(WIDTH));
            this.representation.add(TileGridElement.generateCellsRow(WIDTH));
            this.representation.add(TileGridElement.generateCellsRow(WIDTH));
            if (i == HEIGHT - 1) this.representation.add(TileGridElement.generateBottomRow(WIDTH));
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
