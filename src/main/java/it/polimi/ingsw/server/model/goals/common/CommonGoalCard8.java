package it.polimi.ingsw.server.model.goals.common;

import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.HashMap;

public class CommonGoalCard8 extends CommonGoalCard {
    public CommonGoalCard8(int numPlayer) {
        super(8, numPlayer);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: two lines each formed by 5 different types of tiles.
     * One line can show the same or a different combination of the other line.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        HashMap<TileType,Integer> countTileType = new HashMap<>();  //counts the number of each type of tile
        int countLines=0;   //counts the number of lines formed by 5 different types of tiles.
        int countTiles;      //counts the number of tiles in a line
        for(TileType type : TileType.values())
        {
            countTileType.put(type,0);
        }
        for (int i = 0; i < bookShelf.getHeight(); i++)
        {
            countTiles=0;
            for (int j = 0; j < bookShelf.getWidth(); j++)
            {
                if(bookShelf.hasTile(i,j))
                {
                    countTiles++;
                    countTileType.computeIfPresent(bookShelf.getTileAt(i,j).getType(),(key, value) -> value + 1);
                }
            }
            //the number of tile types without elements must be 1 and has 5 tiles
            if((countTileType.values().stream().filter(value->value==0).count()==1)&&(countTiles==5))
            {
                countLines++;
            }
            countTileType.replaceAll((key, value)->0);
        }
        return countLines>=2;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}