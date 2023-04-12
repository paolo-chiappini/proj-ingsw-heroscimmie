package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBookshelf;

public interface IBookshelfBuilder extends Builder<IBookshelf> {
    IBookshelf setTileAt(int row, int col, GameTile tile);
}
