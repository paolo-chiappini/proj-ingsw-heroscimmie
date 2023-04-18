package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.util.Builder;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.bag.IBag;

/**
 * Represents a generic builder for IBoard objects.
 */
public interface IBoardBuilder extends Builder<IBoard> {
    /**
     * Sets the state of the board.
     * @param tileTypes 2D array containing the types of the tiles for each cell.
     * @param bag bag used to draw the required tiles.
     * @return the builder's instance.
     */
    IBoardBuilder setTiles(TileType[][] tileTypes, IBag bag);
}
