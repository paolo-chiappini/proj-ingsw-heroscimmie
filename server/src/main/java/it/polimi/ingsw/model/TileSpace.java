package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.GameTile;

public class TileSpace {
    private final boolean active;
    private GameTile tileRef;

    public TileSpace(int playersNeeded, int playersPlaying) {
        active = playersPlaying >= playersNeeded;
        tileRef = null;
    }

    public GameTile getTile() {
        return tileRef;
    }

    public void setTile(GameTile tile) {
        if(!isActive()) return;
        tileRef = tile;
    }

    public boolean isActive() {
        return active;
    }

    public GameTile removeTile() {
        GameTile tempTileRef = tileRef;
        tileRef = null;
        return tempTileRef;
    }
}
