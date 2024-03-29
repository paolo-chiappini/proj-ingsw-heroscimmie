package it.polimi.ingsw.client.view.cli.graphics.board;

import it.polimi.ingsw.client.view.cli.graphics.grids.CellElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliBackColors;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliForeColors;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliTextElement;

import java.util.List;

/**
 * Represents a single space on the game board.
 */
public class BoardSpaceElement extends CellElement {
    /**
     * Creates a new tile space for the board.
     * @param type type of board tile.
     */
    public BoardSpaceElement(BoardTileType type) {
        super();

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
                this.setCell(0, 0, singleCell);
                this.setCell(2, 0, singleCell);
                this.setCell(2, 1, singleCell);
            }
            case FOUR_PLAYERS -> {
                singleCell = new CliTextElement('°', CliForeColors.DEFAULT, CliBackColors.DEFAULT);
                this.setCell(0, 0, singleCell);
                this.setCell(2, 0, singleCell);
                this.setCell(0, 1, singleCell);
                this.setCell(2, 1, singleCell);
            }
        }
    }
}
