package it.polimi.ingsw.model;

public class PersonalGoalCard3  extends PersonalGoalCard{
    public PersonalGoalCard3(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[2][2] = TileType.PLANT;
        pattern[1][0] = TileType.FRAME;
        pattern[3][1] = TileType.CAT;
        pattern[5][0] = TileType.BOOK;
        pattern[1][3] = TileType.TOY;
        pattern[3][4] = TileType.TROPHY;
    }
}
