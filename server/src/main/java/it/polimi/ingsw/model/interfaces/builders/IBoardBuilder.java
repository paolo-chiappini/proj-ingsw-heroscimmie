package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBoard;

public interface IBoardBuilder extends Builder<IBoard> {
    void setTileAt(int row, int col, GameTile tile);
}
