package it.polimi.ingsw.client.view.cli.graphics.tiles;

import it.polimi.ingsw.client.view.cli.graphics.grids.CellElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliTextElement;
import it.polimi.ingsw.server.model.tile.TileType;

/**
 * Represents a simple game tile element.
 * The tile element can be inserted in grid containers.
 */
public class TileElement extends CellElement {
    public TileElement(TileType type) {
        CliTextElement singleCell = TileTextElementBuilder.createTileTextElement(type);
        for (int y = 0; y < this.getHeight(); y++)
            for (int x = 0; x < this.getWidth(); x++) this.setCell(x, y, singleCell);
    }
}
