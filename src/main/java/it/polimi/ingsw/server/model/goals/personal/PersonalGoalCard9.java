package it.polimi.ingsw.server.model.goals.personal;

import it.polimi.ingsw.server.model.tile.TileType;

public class PersonalGoalCard9 extends PersonalGoalCard{
    public PersonalGoalCard9() {
        id = 9;
        pattern[4][4] = TileType.PLANT;
        pattern[5][0] = TileType.FRAME;
        pattern[2][2] = TileType.CAT;
        pattern[3][4] = TileType.BOOK;
        pattern[0][2] = TileType.TOY;
        pattern[4][1] = TileType.TROPHY;
    }
}
