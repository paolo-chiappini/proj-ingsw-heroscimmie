package it.polimi.ingsw.model.interfaces.builders;

import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.model.interfaces.IPlayer;

public interface IPlayerBuilder extends Builder<IPlayer> {
    void setBookshelf(IBookshelf bookshelf);
}
