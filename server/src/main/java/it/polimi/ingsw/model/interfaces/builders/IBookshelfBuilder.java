package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBag;
import it.polimi.ingsw.model.interfaces.IBookshelf;

public interface IBookshelfBuilder extends Builder<IBookshelf> {
    IBookshelf setTiles(int[][] tilesIds, IBag bag);
}
