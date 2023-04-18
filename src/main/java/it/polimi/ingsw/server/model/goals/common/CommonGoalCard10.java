package it.polimi.ingsw.server.model.goals.common;

import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

public class CommonGoalCard10 extends CommonGoalCard {
    public CommonGoalCard10(int numPlayers) {
        super(10, numPlayers);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: five tiles of the same type forming an X.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        for (int i = 1; i < bookShelf.getHeight()-1; i++)
        {
            for (int j = 1; j < bookShelf.getWidth()-1; j++)
            {
                if(bookShelf.compareTiles(i,j,i-1,j-1) &&
                        bookShelf.compareTiles(i,j,i+1,j+1) &&
                            bookShelf.compareTiles(i,j,i-1,j+1) &&
                                bookShelf.compareTiles(i,j,i+1,j-1))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}