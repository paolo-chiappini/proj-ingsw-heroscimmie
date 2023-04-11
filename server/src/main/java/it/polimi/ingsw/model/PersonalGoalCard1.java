package it.polimi.ingsw.model;

public class PersonalGoalCard1 extends PersonalGoalCard{

    public PersonalGoalCard1(int id, TileType[][] pattern) {
        super(id, pattern);
        pattern[0][0] = TileType.PLANT;
        pattern[0][2] = TileType.FRAME;
        pattern[1][4] = TileType.CAT;
        pattern[2][3] = TileType.BOOK;
        pattern[3][1] = TileType.TOY;
        pattern[5][2] = TileType.TROPHY;
    }
}
