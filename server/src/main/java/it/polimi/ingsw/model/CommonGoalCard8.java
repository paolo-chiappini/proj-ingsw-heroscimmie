package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;

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
        HashMap<TileType,Integer> countTileType = new HashMap<>();
        int countLines=0;
        int countTile;
        for(TileType type : TileType.values())
        {
            countTileType.put(type,0);
        }
        for (int i = 0; i < bookShelf.getHeight(); i++)
        {
            countTile=0;
            for (int j = 0; j < bookShelf.getWidth(); j++)
            {
                if(bookShelf.hasTile(i,j))
                {
                    countTile++;
                    countTileType.computeIfPresent(bookShelf.getTileAt(i,j).getType(),(key, value) -> value + 1);
                }
            }
            if((countTileType.values().stream().filter(value->value==0).count()==1)&&(countTile==5))
            {
                countLines++;
            }
            countTileType.replaceAll((key, value)->0);
        }
        return countLines>=2;
    }
}