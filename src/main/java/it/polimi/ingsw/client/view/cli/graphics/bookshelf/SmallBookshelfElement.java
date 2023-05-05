package it.polimi.ingsw.client.view.cli.graphics.bookshelf;

import it.polimi.ingsw.client.view.cli.graphics.grids.SmallCellElement;
import it.polimi.ingsw.client.view.cli.graphics.grids.SmallGridElement;
import it.polimi.ingsw.client.view.cli.graphics.tiles.SmallTileElement;
import it.polimi.ingsw.server.model.tile.TileType;

/**
 * Represents a smaller version of the player's bookshelf.
 */
public class SmallBookshelfElement extends SmallGridElement {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 6;
    public SmallBookshelfElement() {
        this.contents = new SmallCellElement[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            this.representation.add(SmallGridElement.generateRow(WIDTH));
        }
    }

    /**
     * Sets a game tile at the given coordinates.
     * @param x x coordinate where to put the tile.
     * @param y y coordinate where to put the tile.
     * @param type type of the tile to set.
     */
    public void setGameTile(int x, int y, TileType type) {
        setElement(new SmallTileElement(type), x, y);
    }
}
