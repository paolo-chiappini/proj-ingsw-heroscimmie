package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;

import java.util.HashMap;

public class CommonGoalCard9 extends CommonGoalCard {
    public CommonGoalCard9(int numPlayers) {
        super(9,numPlayers);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: Three columns each formed by 6 tiles of maximum three different types.
     * One column can show the same or a different combination of another column.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        HashMap<TileType,Integer> countTileType = new HashMap<>();  //counts the number of each type of tile
        int countColumns=0;  //counts the number of columns with 6 tiles of maximum three different types.
        int countTiles;     ////counts the number of tiles in a column
        for(TileType type : TileType.values())
        {
            countTileType.put(type,0);
        }
        for (int i = 0; i < bookShelf.getWidth(); i++)
        {
            countTiles=0;
            for (int j = 0; j < bookShelf.getHeight(); j++)
            {
                if(bookShelf.hasTile(j,i))
                {
                    countTiles++;
                    countTileType.computeIfPresent(bookShelf.getTileAt(j,i).getType(),(key, value) -> value + 1);
                }
            }
            //if you need to have a maximum of three different types,I must have at least 3 types of tile with no elements
            if((countTileType.values().stream().filter(value->value==0).count()>=3)&&(countTiles==6))
            {
                countColumns++;
            }
            countTileType.replaceAll((key, value)->0);
        }
        return countColumns>=3;
    }
}