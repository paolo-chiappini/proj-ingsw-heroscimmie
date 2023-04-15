package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

public class CommonGoalCard5 extends CommonGoalCard {
    public CommonGoalCard5(int numPlayer) {
        super(5, numPlayer);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: Four tiles of the same type in the four corners of the bookshelf.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        return ((bookShelf.compareTiles(0,0,0, bookShelf.getWidth()-1))
                &&(bookShelf.compareTiles(0,bookShelf.getWidth()-1,bookShelf.getHeight()-1,bookShelf.getWidth()-1))
                &&(bookShelf.compareTiles(bookShelf.getHeight()-1,bookShelf.getWidth()-1,bookShelf.getHeight()-1,0))
                && bookShelf.hasTile(bookShelf.getHeight()-1,0));
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}