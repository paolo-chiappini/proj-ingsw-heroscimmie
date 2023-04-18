package it.polimi.ingsw.server.model.goals.personal;

import it.polimi.ingsw.server.model.tile.TileType;

public class PersonalGoalCard12 extends PersonalGoalCard{
    public PersonalGoalCard12() {
        id = 12;
        pattern[1][1] = TileType.PLANT;
        pattern[2][2] = TileType.FRAME;
        pattern[5][0] = TileType.CAT;
        pattern[0][2] = TileType.BOOK;
        pattern[4][4] = TileType.TOY;
        pattern[3][3] = TileType.TROPHY;
    }
}
