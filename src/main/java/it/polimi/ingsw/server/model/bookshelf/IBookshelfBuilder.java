package it.polimi.ingsw.server.model.bookshelf;

import it.polimi.ingsw.util.Builder;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.bag.IBag;

/**
 * Represents a generic builder for IBookshelf objects.
 */
public interface IBookshelfBuilder extends Builder<IBookshelf> {
    /**
     * Sets the state of the bookshelf.
     * @param tileTypes 2D array containing the types of the tiles for each cell.
     * @param bag bag used to draw the required tiles.
     * @return the builder's instance.
     */
    IBookshelfBuilder setTiles(TileType[][] tileTypes, IBag bag);
}
