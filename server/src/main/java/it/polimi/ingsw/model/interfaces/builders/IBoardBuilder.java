package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBag;
import it.polimi.ingsw.model.interfaces.IBoard;

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
