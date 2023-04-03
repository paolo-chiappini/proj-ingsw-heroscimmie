package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;

import java.util.HashMap;

public class CommonGoalCard4 extends CommonGoalCard {
    public CommonGoalCard4(int numPlayer) {
        super(4,numPlayer);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: Four lines each formed by 5 tiles of maximum three different types.
     * One line can show the same or a different combination of another line.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        HashMap<TileType,Integer> countTileType = new HashMap<>();  //counts the number of each type of tile
        int countLines=0;   //counts the number of lines with 5 tiles of maximum three different types.
        int countTiles;     //counts the number of tiles in a line
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
            //if you need to have a maximum of three different types, I must have at least 3 types of tile with no elements
            if((countTileType.values().stream().filter(value->value==0).count()>=3)&&(countTiles==5))
            {
                countLines++;
            }
            countTileType.replaceAll((key, value)->0);
        }
        return countLines>=4;
    }
}