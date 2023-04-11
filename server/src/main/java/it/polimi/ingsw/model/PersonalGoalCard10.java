package it.polimi.ingsw.model;

public class PersonalGoalCard10 extends PersonalGoalCard{
    public PersonalGoalCard10(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[5][3] = TileType.PLANT;
        pattern[4][1] = TileType.FRAME;
        pattern[3][3] = TileType.CAT;
        pattern[2][0] = TileType.BOOK;
        pattern[1][1] = TileType.TOY;
        pattern[0][4] = TileType.TROPHY;
    }
}
