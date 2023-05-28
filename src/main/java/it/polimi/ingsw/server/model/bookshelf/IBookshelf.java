package it.polimi.ingsw.server.model.bookshelf;

import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.util.serialization.Serializable;

import java.util.List;

/**
 * Represents a generic Bookshelf used by the players to store tiles.
 */
public interface IBookshelf extends Serializable {
    /**
     * Get the tile at the specified coordinates.
     * @param row the row to take the tile from.
     * @param column the column to take the tile from.
     * @return the tile at the (row, column) coordinates.
     */
    GameTile getTileAt(int row, int column);

    /**
     * Get the width of the bookshelf.
     * @return the width of the bookshelf.
     */
    int getWidth();

    /**
     * Get the height of the bookshelf.
     * @return the height of the bookshelf.
     */
    int getHeight();

    /**
     * Checks if the bookshelf is full of tiles.
     * @return true if the bookshelf is full.
     */
    boolean isFull();

    /**
     * Inserts tiles inside the bookshelf by occupying the first available spaces from the bottom.
     * @param tilesToDrop list of tiles to drop in the bookshelf.
     * @param column column to drop the tiles in.
     */
    void dropTiles(List<GameTile> tilesToDrop, int column);

    /**
     * Checks if the specified quantity of tiles can be dropped in the chosen column.
     * @param numOfTiles number of tiles to drop.
     * @param column column to drop the tiles in.
     * @return true if there's enough space inside the column.
     */
    boolean canDropTiles(int numOfTiles, int column);

    /**
     * Checks if the bookshelf contains a tile at the specified coordinates.
     * @param row row to check.
     * @param column column to check.
     * @return true if the bookshelf contains a tile at the (row, column) coordinates.
     */
    boolean hasTile(int row, int column);

    /**
     * Compares two tiles given their coordinates.
     * @param row first row to check.
     * @param column first column to check.
     * @param row2 second row to check.
     * @param column2 second column to check.
     * @return true if the tiles at the specified coordinates are the same.
     */
    boolean compareTiles(int row, int column, int row2, int column2);

    /**
     * Chooses the order of the tiles
     * @param tilesToDrop are the chosen tiles to insert
     * @param position1 is the position of the first tile
     * @param position2 is the position of the second tile
     * @param position3 is the position of the third tile
     * @return sorted tiles to insert in the bookshelf
     */
     List<GameTile> decideTilesOrder(List<GameTile> tilesToDrop, int position1, int position2, int position3);
}
