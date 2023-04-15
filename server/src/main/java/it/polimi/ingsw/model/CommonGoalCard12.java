package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

public class CommonGoalCard12 extends CommonGoalCard {
    public CommonGoalCard12(int numPlayer) {
        super(12, numPlayer);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: five columns of increasing or decreasing height.
     * Starting from the first column on the left or on the right,
     * each next column must be made of exactly one more tile.
     * Tiles can be of any type.
     * @return true if the goal is achieved
     **/
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
       int diagonal1 = 0;   //in the first column it has 6 tiles (decreasing height)
       int diagonal2 = 0;   //in the first column it has 5 tiles (decreasing height)
       int diagonal3 = 0;   //in the first column it has 1 tiles (increasing height)
       int diagonal4 = 0;   //in the first column it has 2 tiles (increasing height)
       int countTiles;      //counts the number of tiles in a column
       for (int i = 0; i < bookShelf.getWidth(); i++)
       {
           countTiles = 0;
           for (int j = 0; j < bookShelf.getHeight(); j++)
           {
               if (bookShelf.hasTile(j,i)) countTiles++;
           }
           if (countTiles == (bookShelf.getHeight()-i)) diagonal1++;

           if(countTiles == (bookShelf.getHeight()-1-i)) diagonal2++;

           if (countTiles == (i + 1)) diagonal3++;

           if(countTiles == (i + 2)) diagonal4++;
       }
       return diagonal1==bookShelf.getWidth() || diagonal2==bookShelf.getWidth()
               || diagonal3==bookShelf.getWidth() || diagonal4==bookShelf.getWidth();
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}