package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBag;
import it.polimi.ingsw.model.interfaces.IBookshelf;

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
