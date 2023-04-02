package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;

public class CommonGoalCard2 extends CommonGoalCard {
    public CommonGoalCard2(int numPlayer) {
        super(2, numPlayer);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve: five tiles of the same type forming a diagonal.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        int countDiagonal1=0;
        int countDiagonal2=0;
        int countDiagonal3=0;
        int countDiagonal4=0;
        for(int i=0;i<bookShelf.getHeight()-2;i++)
        {
            if((bookShelf.compareTiles(i,i,i+1,i+1)))
            {
                countDiagonal1++;
            }
            if((bookShelf.compareTiles(i+1,i,i+2,i+1)))
            {
                countDiagonal2++;
            }
            if((bookShelf.compareTiles(i, bookShelf.getHeight()-2-i,i+1,bookShelf.getHeight()-3-i)))
            {
                countDiagonal3++;
            }
            if((bookShelf.compareTiles(i+1, bookShelf.getHeight()-2-i,i+2, bookShelf.getHeight()-3-i)))
            {
                countDiagonal4++;
            }
        }
        return (countDiagonal1== bookShelf.getWidth()-1 || countDiagonal2== bookShelf.getWidth()-1 ||
                countDiagonal3== bookShelf.getWidth()-1 || countDiagonal4== bookShelf.getWidth()-1);
    }
}