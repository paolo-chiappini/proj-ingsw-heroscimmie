package it.polimi.ingsw.client.cli.graphics.board;

import it.polimi.ingsw.client.cli.graphics.grids.GridCellElement;
import it.polimi.ingsw.client.cli.graphics.grids.TileGridElement;
import it.polimi.ingsw.client.cli.graphics.simple.CliTextElement;
import it.polimi.ingsw.client.cli.graphics.util.StringToCliTextConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board.
 */
public class BoardElement extends TileGridElement {
    private static final int SIZE = 9;

    public BoardElement() {
        this.contents = new GridCellElement[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            if (i == 0) this.representation.add(TileGridElement.generateTopRow(SIZE));
            else this.representation.add(TileGridElement.generateMiddleRow(SIZE));
            this.representation.add(TileGridElement.generateCellsRow(SIZE));
            this.representation.add(TileGridElement.generateCellsRow(SIZE));
            if (i == SIZE - 1) this.representation.add(TileGridElement.generateBottomRow(SIZE));
        }
    }
}
