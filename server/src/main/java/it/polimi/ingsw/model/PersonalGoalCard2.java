package it.polimi.ingsw.model;

public class PersonalGoalCard2 extends PersonalGoalCard{
    TileType[][] pattern = new TileType[5][6];

    public PersonalGoalCard2(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[1][1] = TileType.PLANT;
        pattern[4][5] = TileType.FRAME;
        pattern[2][0] = TileType.CAT;
        pattern[3][5] = TileType.BOOK;
        pattern[2][2] = TileType.TOY;
        pattern[4][3] = TileType.TROPHY;
    }
}
