package it.polimi.ingsw.model;

public class PersonalGoalCard12 extends PersonalGoalCard{
    TileType[][] pattern = new TileType[5][6];
    public PersonalGoalCard12(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[1][1] = TileType.PLANT;
        pattern[2][2] = TileType.FRAME;
        pattern[5][0] = TileType.CAT;
        pattern[0][2] = TileType.BOOK;
        pattern[4][4] = TileType.TOY;
        pattern[3][3] = TileType.TROPHY;
    }
}