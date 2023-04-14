package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.util.serialization.Serializable;

import java.util.List;

/**
 * Represents ad generic Board object.
 */
public interface IBoard extends Serializable {
    /**
     * Checks if the board needs to be restored (refilled) with tiles.
     * @return true if each of the tiles left on the board have no adjacent tiles.
     */
    boolean needsRefill();

    /**
     * Restores the board with tiles.
     * @param bag bag to draw the tiles from.
     */
    void refill(IBag bag);

    /**
     * Returns the list of tiles picked up from the board in a row (or column) from the specified coordinates.
     * The given coordinates represent the start and end of the interval of tiles to pick up.
     * @param row1 start row.
     * @param col1 start column.
     * @param row2 end row.
     * @param col2 end column.
     * @throws IllegalActionException when trying to pick up tiles that have no freed sides.
     * @return the list of tiles.
     */
    List<GameTile> pickUpTiles(int row1, int col1, int row2, int col2);

    /**
     * Checks if the selected interval of tiles can be picked up.
     * @param row1 start row.
     * @param col1 start column.
     * @param row2 end row.
     * @param col2 end column.
     * @return true if the selected range violates the rules: the tiles must have a free side to be picked up.
     */
    boolean canPickUpTiles(int row1, int col1, int row2, int col2);

    /**
     * Get the tile at the specified coordinates.
     * @param row selected row.
     * @param col selected column.
     * @return the tile at the (row, column) coordinates.
     */
    GameTile getTileAt(int row, int col);

    /**
     * Get the size of the side of the board (the board is a square).
     * @return the size of the board.
     */
    int getSize();
}
