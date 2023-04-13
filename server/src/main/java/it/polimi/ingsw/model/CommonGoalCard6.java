package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.HashMap;

public class CommonGoalCard6 extends CommonGoalCard {
    public CommonGoalCard6(int numPlayer) {
        super(6,numPlayer);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: Two columns each formed by 6 different types of tiles.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        HashMap<TileType,Integer> countTileType = new HashMap<>();  //counts the number of each type of tile
        int countColumns=0; //counts the number of columns formed by 6 different types of tiles.
        int countTile;      //counts the number of tiles in a column
        for(TileType type : TileType.values())
        {
            countTileType.put(type,0);
        }
        for (int i = 0; i < bookShelf.getWidth(); i++)
        {
            countTile=0;
            for (int j = 0; j < bookShelf.getHeight(); j++)
            {
                if(bookShelf.hasTile(j,i))
                {
                    countTile++;
                    countTileType.computeIfPresent(bookShelf.getTileAt(j,i).getType(),(key, value) -> value + 1);
                }
            }
            //it has all types of tiles and has 6 tiles
            if((countTileType.values().stream().noneMatch(value -> value == 0))&&(countTile==6))
            {
                countColumns++;
            }
            countTileType.replaceAll((key, value)->0);
        }
        return countColumns>=2;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}