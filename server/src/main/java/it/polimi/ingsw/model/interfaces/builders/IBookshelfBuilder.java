package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBag;
import it.polimi.ingsw.model.interfaces.IBookshelf;

public interface IBookshelfBuilder extends Builder<IBookshelf> {
    IBookshelfBuilder setTiles(TileType[][] tileTypes, IBag bag);
}
