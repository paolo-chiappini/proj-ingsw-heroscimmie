package it.polimi.ingsw.model.interfaces;

public interface BoardSpace {
    /**
     * Get the tile that is placed in the space.
     * @return tile saved in the space.
     */
    GameTile getTile();

    /**
     * Set a single tile to occupy the space.
     * The tile is only placed if the space is active and no other tile is
     * already in place.
     * @param tile tile to be placed inside the space of the board.
     */
    void setTile(GameTile tile);

    /**
     * Check if the current space can be used.
     * @return true if the space is active and empty (does not contain other tiles).
     */
    boolean canPlaceTile();

    /**
     * Remove the tile in the space.
     * @return the tile contained in the space.
     */
    GameTile removeTile();
}
