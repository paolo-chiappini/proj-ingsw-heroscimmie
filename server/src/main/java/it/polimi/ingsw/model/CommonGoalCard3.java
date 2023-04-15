package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.util.BookshelfFloodFill;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.List;

public class CommonGoalCard3 extends CommonGoalCard {
    public CommonGoalCard3(int numPlayer) {
        super(3, numPlayer);
    }

    /**
     * @param bookshelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: four groups each containing at least 4 tiles of the same type (not necessarily
     * in the depicted shape). The tiles of one group can be different from those of another group.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookshelf) {
        List<Integer> groupsAdjacency;
        groupsAdjacency= BookshelfFloodFill.getTileGroupsSizes(bookshelf);
        return groupsAdjacency.stream().filter(size -> size >= 4).count()>= 4;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}