package it.polimi.ingsw.client.cli.graphics.tiles;

import it.polimi.ingsw.client.cli.graphics.grids.SmallCellElement;
import it.polimi.ingsw.server.model.tile.TileType;

/**
 * Represents a simple small game tile element.
 * The tile element can be inserted in small grid containers.
 */
public class SmallTileElement extends SmallCellElement {
    public SmallTileElement(TileType type) {
        var singleCell = TileTextElementBuilder.createTileTextElement(type);
        for (int y = 0; y < this.getHeight(); y++)
            for (int x = 0; x < this.getWidth(); x++) this.setCell(x, y, singleCell);
    }
}
