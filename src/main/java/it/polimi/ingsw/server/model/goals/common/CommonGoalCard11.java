package it.polimi.ingsw.server.model.goals.common;

import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.HashMap;

public class CommonGoalCard11 extends CommonGoalCard {
    public CommonGoalCard11(int numPlayers) {
        super(11, numPlayers);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: Eight tiles of the same type.
     * There’s no restriction about the position of these tiles.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        HashMap<TileType,Integer> countTileType = new HashMap<>();
        for(TileType type : TileType.values())
        {
            countTileType.put(type,0);
        }
        for (int i = 0; i < bookShelf.getHeight(); i++)
        {
            for (int j = 0; j < bookShelf.getWidth(); j++)
            {
                if(bookShelf.hasTile(i,j))
                {
                    countTileType.computeIfPresent(bookShelf.getTileAt(i,j).getType(),(key, val) -> val + 1);
                }
            }
        }
        return countTileType.values().stream().anyMatch(value->value>7);
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}