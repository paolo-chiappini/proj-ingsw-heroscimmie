package it.polimi.ingsw.server.model.goals.personal;

import it.polimi.ingsw.server.model.tile.TileType;

public class PersonalGoalCard6 extends PersonalGoalCard{
    public PersonalGoalCard6() {
        id = 6;
        pattern[5][0] = TileType.PLANT;
        pattern[4][3] = TileType.FRAME;
        pattern[0][4] = TileType.CAT;
        pattern[2][3] = TileType.BOOK;
        pattern[4][1] = TileType.TOY;
        pattern[0][2] = TileType.TROPHY;
    }
}
