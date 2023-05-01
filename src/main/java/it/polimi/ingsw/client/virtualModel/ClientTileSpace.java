package it.polimi.ingsw.client.virtualModel;

/**
 * This class represents a space of the board in the virtual model.
 */
public class ClientTileSpace {
    private final boolean active;
    private int tileRef;

    public ClientTileSpace(int playersNeeded, int playersPlaying) {
        active = playersPlaying >= playersNeeded;
        tileRef = -1;
    }

    public void setTile(int tile) {
        if(active)
            tileRef = tile;
        else tileRef = -1;
    }
    public int getTile() {
        return tileRef;
    }
}