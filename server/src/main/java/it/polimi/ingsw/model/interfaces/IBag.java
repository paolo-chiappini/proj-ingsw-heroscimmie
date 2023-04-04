package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.model.TileType;

import java.util.HashMap;

public interface IBag {
    HashMap<TileType, Integer> getTilesBag();
    GameTile drawTile();
    void addTile(GameTile tile);
    void setRemainingTilesCount(TileType type, int count);
    GameTile getTileByType(TileType type);
}
