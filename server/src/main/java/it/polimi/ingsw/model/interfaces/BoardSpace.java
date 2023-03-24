package it.polimi.ingsw.model.interfaces;

public interface BoardSpace {
    /** Get the tile that is placed in the space.
     * @return tile saved in the space.
     */
    GameTile getTile();

    /** Set a single tile to occupy the space.
     * @param tile tile to be placed inside the space of the board.
     */
    void setTile(GameTile tile);

    /** Check if the current space can be used in the current game.
     * @return true if the space can be used during the game for placing tiles,
     *         false otherwise.
     */
    boolean isActive();

    /** Remove the tile in the space.
     * @return the tile contained in the space.
     */
    GameTile removeTile();
}
