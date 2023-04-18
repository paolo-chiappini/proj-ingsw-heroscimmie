package it.polimi.ingsw.server.model.goals.common;

import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

public class CommonGoalCard2 extends CommonGoalCard {
    public CommonGoalCard2(int numPlayer) {
        super(2, numPlayer);
    }

    /**
     * @param bookshelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: five tiles of the same type forming a diagonal.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookshelf) {
        for (int i = 0; i < bookshelf.getHeight() - 4; i++) {
            int matches = 0;
            int matches2 = 0;
            for (int j = 1; j < 5; j++) {
                // compare the tile on the previous row and previous column with the current tile.
                // i indicate a row offset from where the diagonal begins.
                if (bookshelf.compareTiles(i + j-1, j-1, i+j, j)) matches++;
                if (bookshelf.compareTiles(i + j-1, bookshelf.getWidth()-j, i+j, bookshelf.getWidth()-j-1)) matches2++;
            }
            //(if it has 5 tiles, you make 4 comparisons)
            if ((matches >= 4) || (matches2 >= 4)) return true;
        }
        return false;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}