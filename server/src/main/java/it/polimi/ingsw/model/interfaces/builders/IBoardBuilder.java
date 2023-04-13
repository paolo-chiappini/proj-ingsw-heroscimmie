package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBag;
import it.polimi.ingsw.model.interfaces.IBoard;

public interface IBoardBuilder extends Builder<IBoard> {
    IBoardBuilder setTiles(TileType[][] tileTypes, IBag bag);
}