package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.IBag;

public interface IBagBuilder extends Builder<IBag> {
    IBagBuilder setRemainingTilesCount(TileType type, int count);
}
