package it.polimi.ingsw.client.cli.graphics.board;

import it.polimi.ingsw.client.cli.graphics.grids.GridCellElement;
import it.polimi.ingsw.client.cli.graphics.simple.CliBackColors;
import it.polimi.ingsw.client.cli.graphics.simple.CliForeColors;
import it.polimi.ingsw.client.cli.graphics.simple.CliTextElement;

import java.util.List;

/**
 * Represents a single space on the game board.
 */
public class BoardSpaceElement extends GridCellElement {
    /**
     * Creates a new tile space for the board.
     * @param type type of board tile.
     */
    public BoardSpaceElement(BoardTileType type) {
        CliTextElement singleCell;
        switch (type) {
            case BLOCKED -> {
                singleCell = new CliTextElement('X', CliForeColors.DEFAULT, CliBackColors.DEFAULT);
                representation = List.of(
                        List.of(singleCell, singleCell, singleCell),
                        List.of(singleCell, singleCell, singleCell)
                );
            }
            case THREE_PLAYERS -> {
                singleCell = new CliTextElement('°', CliForeColors.DEFAULT, CliBackColors.DEFAULT);
                setCell(0, 0, singleCell);
                setCell(0, 2, singleCell);
                setCell(1, 2, singleCell);
            }
            case FOUR_PLAYERS -> {
                singleCell = new CliTextElement('°', CliForeColors.DEFAULT, CliBackColors.DEFAULT);
                setCell(0, 0, singleCell);
                setCell(0, 2, singleCell);
                setCell(1, 0, singleCell);
                setCell(1, 2, singleCell);
            }
        }
    }
}
