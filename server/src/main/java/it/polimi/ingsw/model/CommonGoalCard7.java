package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;

public class CommonGoalCard7 extends CommonGoalCard {
    public CommonGoalCard7(int numPlayer) {
        super(7, numPlayer);
    }

    /**
     * @param bookShelf is the bookshelf on which the achievements of a goal are checked
     * Pattern to achieve:Two groups each containing 4 tiles of the same type in a 2x2 square.
     * The tiles of one square can be different from those of the other square.
     * @return true if the goal is achieved
     */
    @Override
    public boolean canObtainPoints(IBookshelf bookShelf) {
        int countSquare=0;
        int countSquareInLine;
        int columnStart=0;
        int positionOccurrence=0;
        int columnEnd=bookShelf.getWidth()-1;
        for (int i = 0; i < bookShelf.getHeight()-1; i++)
        {
            countSquareInLine=0;
            for (int j =columnStart; j < columnEnd;j++)
            {
                if(bookShelf.compareTiles(i,j,i,j+1) &&
                        bookShelf.compareTiles(i,j,i+1,j) &&
                            bookShelf.compareTiles(i,j,i+1,j+1))
                {
                    countSquare++;
                    countSquareInLine++;
                    positionOccurrence=j;
                    j++;
                }
            }
            if(countSquareInLine==2)
            {
                i++;
            }
            else if (countSquareInLine==1)
            {
                if(positionOccurrence<(bookShelf.getWidth()-2))
                {
                    columnStart=positionOccurrence+2;
                    columnEnd=bookShelf.getWidth()-1;
                }
                else
                {
                    columnEnd=positionOccurrence;
                    columnStart=0;
                }
            }
            else
            {
                columnStart=0;
                columnEnd=bookShelf.getWidth()-1;
            }
        }
        return countSquare>=2;
    }
}
