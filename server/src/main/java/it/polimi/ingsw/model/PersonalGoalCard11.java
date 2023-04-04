package it.polimi.ingsw.model;

public class PersonalGoalCard11 extends PersonalGoalCard{
    TileType[][] pattern = new TileType[5][6];
    public PersonalGoalCard11(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[0][2] = TileType.PLANT;
        pattern[3][2] = TileType.FRAME;
        pattern[4][4] = TileType.CAT;
        pattern[1][1] = TileType.BOOK;
        pattern[2][0] = TileType.TOY;
        pattern[5][3] = TileType.TROPHY;
    }
}