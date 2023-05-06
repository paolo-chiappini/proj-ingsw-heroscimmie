package it.polimi.ingsw.client.cli.graphics.tiles;


import it.polimi.ingsw.client.cli.graphics.simple.CliBackColors;
import it.polimi.ingsw.client.cli.graphics.simple.CliForeColors;
import it.polimi.ingsw.client.cli.graphics.simple.CliTextElement;
import it.polimi.ingsw.server.model.tile.TileType;

/**
 * Builder for game tile elements.
 */
public abstract class TileTextElementBuilder {
    /**
     * Creates a single text element representing a tile.
     * @param type type of tile to create.
     * @return the element representing the tile.
     */
    public static CliTextElement createTileTextElement(TileType type) {
        CliTextElement singleCell = null;
        switch (type) {
            case CAT -> singleCell = new CliTextElement('C', CliForeColors.DEFAULT, CliBackColors.GREEN);
            case BOOK -> singleCell = new CliTextElement('B', CliForeColors.BRIGHT_BLACK, CliBackColors.WHITE);
            case PLANT -> singleCell = new CliTextElement('P', CliForeColors.DEFAULT, CliBackColors.PURPLE);
            case TROPHY -> singleCell = new CliTextElement('T', CliForeColors.DEFAULT, CliBackColors.CYAN);
            case FRAME -> singleCell = new CliTextElement('F', CliForeColors.DEFAULT, CliBackColors.BLUE);
            case TOY -> singleCell = new CliTextElement('G', CliForeColors.DEFAULT, CliBackColors.YELLOW);
        }
        return singleCell;
    }
}
