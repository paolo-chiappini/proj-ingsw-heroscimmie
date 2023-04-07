package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.IBag;

public interface IBagBuilder extends Builder<IBag> {
    void setRemainingTilesCount(TileType type, int count);
}
