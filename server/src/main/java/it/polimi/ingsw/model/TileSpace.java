package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.BoardSpace;
import it.polimi.ingsw.model.interfaces.GameTile;

/**
 * This class represents a single space on the game board and is responsible
 * for containing the tile placed on that space.
 *
 * <p>The class has three states:
 * <ul>
 * <li>Active empty: the space allows for the placement of new tiles in the space;</li>
 * <li>Active full: the space does not allow the placement of new tiles and
 *  continues storing the current one;</li>
 * <li>Inactive empty: the space does not allow for the placement of any tile.</li>
 * </ul>
 * The state "Inactive full" is not possible since an inactive space cannot
 * be used to store tiles.
 */
public class TileSpace implements BoardSpace {
    private final boolean active;
    private GameTile tileRef;

    /**
     * @param playersNeeded players needed for the space to be used on the board.
     * @param playersPlaying players actually playing at the beginning of the game.
     */
    public TileSpace(int playersNeeded, int playersPlaying) {
        active = playersPlaying >= playersNeeded;
        tileRef = null;
    }

    @Override
    public GameTile getTile() {
        return tileRef;
    }

    @Override
    public void setTile(GameTile tile) {
        if(!canPlaceTile()) return;
        tileRef = tile;
    }

    @Override
    public boolean canPlaceTile() {
        boolean empty = tileRef == null;
        return active && empty;
    }

    @Override
    public GameTile removeTile() {
        GameTile tempTileRef = tileRef;
        tileRef = null;
        return tempTileRef;
    }
}
